package src.tab;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
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

  /** オーバーロードしたもの TODO:要らないからあとで削除 */
  private void addFields(JPanel innerPanel, Map<String, String> fieldConfigs,
      TextFieldStyle style, int columns) {
    GridBagConstraints innerGbc = new GridBagConstraints();
    innerGbc.insets = new Insets(10, 10, 10, 10);

    int index = 0;
    for (Map.Entry<String, String> entry : fieldConfigs.entrySet()) {
      String labelText = entry.getKey();
      String fieldType = entry.getValue();

      CustomLabeledComponent labeledComponent;
      Object field = null;

      // フィールドタイプに応じてコンポーネントを選択
      if ("numeric".equalsIgnoreCase(fieldType)) {
        CustomNumericField numericField = new CustomNumericField(20, style, 14);
        labeledComponent = new CustomLabeledComponent(labelText, numericField);
        field = numericField;

      } else if ("date".equalsIgnoreCase(fieldType)) {
        CustomDateField dateField = new CustomDateField(20, style, 14);
        labeledComponent = new CustomLabeledComponent(labelText, dateField);
        field = dateField;

      } else if ("check".equalsIgnoreCase(fieldType)) {
        CustomCheckBox checkBox = new CustomCheckBox("isCompleted");
        labeledComponent = new CustomLabeledComponent(labelText, checkBox);
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

        labeledComponent = new CustomLabeledComponent(labelText, scrollPane);
        field = customTextArea;

      } else {
        CustomTextField textFieldStandard = new CustomTextField(20, style, 14);
        labeledComponent = new CustomLabeledComponent(labelText, textFieldStandard);
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
    }
  }

  /** 内部パネルをメインパネルに追加 */
  private void addInnerPanelToMain(JPanel mainPanel, JPanel innerPanel, int position) {
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = position; // 位置を指定する
    gbc.gridwidth = GridBagConstraints.REMAINDER; // パネルの幅全体を使う
    gbc.insets = new Insets(0, 10, 0, 10); // 左右のマージンを10に設定
    gbc.fill = GridBagConstraints.HORIZONTAL; // 横方向に広げる
    gbc.weightx = 1.0; // 横方向に余分なスペースを与え、均等に広げる
    gbc.weighty = 0.0; // 縦方向のスペースを余分に与えない
    gbc.anchor = GridBagConstraints.NORTH; // 上に配置

    mainPanel.add(innerPanel, gbc);

    // メインパネルが上詰めになるように設定
    GridBagConstraints fillerGbc = new GridBagConstraints();
    fillerGbc.gridx = 0;
    fillerGbc.gridy = position + 1; // 次の行にスペースを追加
    fillerGbc.weightx = 1.0;
    fillerGbc.weighty = 1.0; // 縦方向に余分なスペースを与える
    fillerGbc.fill = GridBagConstraints.BOTH; // 余白を埋める
    mainPanel.add(new JPanel(), fillerGbc); // 余分なスペースを埋めるためのダミーパネルを追加
  }

  /**
   * タブ追加メソッド
   *
   * @param tabbedPane
   * @param title
   * @param fieldConfigs
   * @param style
   * @param columns
   *                     テキストフィールドを配置する列数
   * @return innerPanelを返すことで、外部からボタンを追加できるようにする
   */
  public JPanel addTab(JTabbedPane tabbedPane, String title, Map<String, String> fieldConfigs, TextFieldStyle style,
      int columns, int position) {
    JPanel mainPanel = createPannel.createMainPanel();
    JPanel innerPanel = createPannel.createInnerPanel(title);

    addFields(innerPanel, fieldConfigs, style, columns);
    addInnerPanelToMain(mainPanel, innerPanel, position);

    tabbedPane.addTab(title, mainPanel);

    return innerPanel;
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
