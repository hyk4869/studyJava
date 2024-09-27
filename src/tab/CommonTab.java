package src.tab;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.InsetsUIResource;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.text.ParseException;
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
import src.components.parts.CustomTextArea;
import src.components.parts.CustomTextField;
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

    int index = 0;
    for (Map.Entry<String, String> entry : fieldConfigs.entrySet()) {
      String labelText = entry.getKey();
      String fieldType = entry.getValue();

      String japaneseLabel = customLabel.getOrDefault(labelText, labelText);

      CustomLabeledComponent labeledComponent;
      Object field = null;

      // フィールドタイプに応じてコンポーネントを選択
      if ("numeric".equalsIgnoreCase(fieldType)) {
        CustomNumericField numericField = new CustomNumericField(20, style, 14);
        labeledComponent = new CustomLabeledComponent(japaneseLabel, numericField);
        field = numericField;

      } else if ("date".equalsIgnoreCase(fieldType)) {
        CustomDateField dateField = new CustomDateField(20, style, 14);
        labeledComponent = new CustomLabeledComponent(japaneseLabel, dateField);
        field = dateField;

      } else if ("check".equalsIgnoreCase(fieldType)) {
        CustomCheckBox checkBox = new CustomCheckBox("isCompleted");
        labeledComponent = new CustomLabeledComponent(japaneseLabel, checkBox);
        field = checkBox;

      } else if ("textArea".equalsIgnoreCase(fieldType)) {
        CustomTextArea customTextArea = new CustomTextArea(5, 20, style, 14);

        customTextArea.setLineWrap(true);
        customTextArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(customTextArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // 固定サイズを指定せずに、行数・列数に基づく
        customTextArea.setPreferredSize(null);

        labeledComponent = new CustomLabeledComponent(japaneseLabel, scrollPane);
        field = customTextArea;

      } else {
        CustomTextField textFieldStandard = new CustomTextField(20, style, 14);
        labeledComponent = new CustomLabeledComponent(japaneseLabel, textFieldStandard);
        field = textFieldStandard;

      }

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

    if (field instanceof JTextField) {
      return ((JTextField) field).getText();
    } else if (field instanceof CustomCheckBox) {
      return ((CustomCheckBox) field).isChecked();
    } else if (field instanceof JTextArea) {
      return ((JTextArea) field).getText();
    } else if (field instanceof CustomNumericField) {
      String text = ((CustomNumericField) field).getText();
      try {
        if (text != null && !text.isEmpty()) {
          return Integer.parseInt(text);
        }
      } catch (NumberFormatException e) {
        System.out.println("Invalid number format: " + text);
        return null;
      }
    } else if (field instanceof CustomDateField) {
      String text = ((CustomDateField) field).getText();
      if (text != null && !text.isEmpty()) {
        try {
          SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
          java.util.Date parsedDate = dateFormat.parse(text);
          return new Timestamp(parsedDate.getTime());
        } catch (ParseException e) {
          System.out.println("Invalid date format: " + text);
          return null;
        }
      }
    }

    return null;
  }

  /** フィールドの値を設定する */
  public void setFieldValue(String fieldName, Object value) {
    Object field = fieldMap.get(fieldName);

    if (field instanceof JTextField) {
      ((JTextField) field).setText((String) value);
    } else if (field instanceof CustomCheckBox) {
      ((CustomCheckBox) field).setChecked((Boolean) value);
    } else if (field instanceof JTextArea) {
      ((JTextArea) field).setText((String) value);
    } else if (field instanceof CustomNumericField) {
      if (value instanceof Integer) {
        ((CustomNumericField) field).setText(String.valueOf(value));
      }
    } else if (field instanceof CustomDateField) {
      if (value instanceof Timestamp) {
        Timestamp timestamp = (Timestamp) value;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ((CustomDateField) field).setText(dateFormat.format(timestamp));
      } else if (value instanceof String) {
        ((CustomDateField) field).setText((String) value);
      }
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
