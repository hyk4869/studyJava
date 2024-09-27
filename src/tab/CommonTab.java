package src.tab;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.InsetsUIResource;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import src.components.enums.TextFieldStyle;
import src.components.labels.CustomLabeledComponent;
import src.components.parts.CustomCheckBox;
import src.components.parts.CustomDateField;
import src.components.parts.CustomNumericField;
import src.components.parts.interfaces.FormattedField;
import src.tab.inputFields.GenerateInputFields;
import src.tab.pannels.CreatePannel;
import src.utils.CommonFont;

public class CommonTab {
  /** テキストフィールドのリスト */
  private Map<String, Object> fieldMap = new HashMap<>();
  private List<CustomLabeledComponent> components = new ArrayList<>();

  private final CreatePannel createPannel = new CreatePannel();
  private final static CommonFont commonFont = new CommonFont();

  /** 初期化 */
  public static void initializeUI() {
    // タブの高さを変更
    UIManager.put("TabbedPane.tabInsets", new InsetsUIResource(10, 20, 10, 20));
    // フォントの設定
    UIManager.put("TabbedPane.font", new FontUIResource(commonFont.commonNotoSansCJKJP(14)));
  }

  /** タブパネルを作成して返す */
  public JPanel createInnerPanel(String title, Map<String, String> fieldConfigs, Map<String, String> customLabel,
      TextFieldStyle style, int columns) {
    JPanel innerPanel = createPannel.createInnerPanel(title);
    addFields(innerPanel, fieldConfigs, customLabel, style, columns);
    return innerPanel;
  }

  /** 指定した各テキストフィールドの追加 */
  private void addFields(JPanel innerPanel, Map<String, String> fieldConfigs, Map<String, String> customLabel,
      TextFieldStyle style, int columns) {

    GridBagConstraints innerGbc = new GridBagConstraints();

    innerGbc.insets = new Insets(10, 10, 10, 10);

    GenerateInputFields generateInputFields = new GenerateInputFields();

    int index = 0;

    for (Map.Entry<String, String> entry : fieldConfigs.entrySet()) {
      String labelText = entry.getKey();
      String fieldType = entry.getValue();

      String japaneseLabel = customLabel.getOrDefault(labelText, labelText);

      CustomLabeledComponent labeledComponent;
      Object field = null;

      generateInputFields.generateFileds(fieldType, style, japaneseLabel);
      labeledComponent = generateInputFields.getLabeledComponent();
      field = generateInputFields.getField();

      innerGbc.gridx = index % columns; // 指定された列数に基づいて配置
      innerGbc.gridy = index / columns; // 行
      innerGbc.fill = GridBagConstraints.HORIZONTAL; // 横方向に広げる
      innerGbc.weightx = 1.0; // 余分なスペースがあれば均等に配分
      innerGbc.anchor = GridBagConstraints.WEST; // 左寄せ
      innerPanel.add(labeledComponent, innerGbc);

      components.add(labeledComponent);

      fieldMap.put(labelText, field);

      index++;
    }
  }

  /** フィールドの値を取得する */
  public Object getFieldValue(String fieldName) {
    Object field = fieldMap.get(fieldName);

    if (field == null) {
      throw new IllegalArgumentException("No field found for name: " + fieldName);
    }

    if (field instanceof FormattedField<?>) {
      return ((FormattedField<?>) field).getFormattedValue();
    } else if (field instanceof JTextField) {
      return ((JTextField) field).getText();
    } else if (field instanceof CustomCheckBox) {
      return ((CustomCheckBox) field).isChecked();
    } else if (field instanceof JTextArea) {
      return ((JTextArea) field).getText();
    }

    return null;
  }

  /** フィールドの値を設定する */
  public void setFieldValue(String fieldName, Object value) {
    Object field = fieldMap.get(fieldName);

    // フィールドが FormattedField を実装している場合、型に応じた設定を行う
    if (field instanceof FormattedField<?>) {
      // FormattedField の場合、キャストして適切な型の値を設定する
      if (field instanceof CustomNumericField && value instanceof Integer) {
        ((CustomNumericField) field).setText(String.valueOf(value));
      } else if (field instanceof CustomDateField && value instanceof Timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ((CustomDateField) field).setText(dateFormat.format((Timestamp) value));
      } else if (field instanceof CustomDateField && value instanceof String) {
        ((CustomDateField) field).setText((String) value);
      }
    } else if (field instanceof JTextField) {
      ((JTextField) field).setText((String) value);
    } else if (field instanceof CustomCheckBox) {
      ((CustomCheckBox) field).setChecked((Boolean) value);
    } else if (field instanceof JTextArea) {
      ((JTextArea) field).setText((String) value);
    }
  }

  public static void addFocusListener(JFrame frame, List<JTextField> textFields) {
    frame.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        boolean anyFieldHasFocus = textFields.stream().anyMatch(JTextField::hasFocus);
        if (!anyFieldHasFocus) {
          for (JTextField textField : textFields) {
            textField.setFocusable(false);
          }
          frame.requestFocusInWindow(); // フレームにフォーカスを移す
          for (JTextField textField : textFields) {
            textField.setFocusable(true);
          }
        }
      }
    });
  }
}
