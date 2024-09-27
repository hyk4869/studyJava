package src.tab.inputFields;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

import src.components.enums.TextFieldStyle;
import src.components.labels.CustomLabeledComponent;
import src.components.parts.CustomCheckBox;
import src.components.parts.CustomDateField;
import src.components.parts.CustomNumericField;
import src.components.parts.CustomTextArea;
import src.components.parts.CustomTextField;

public class GenerateInputFields {
  private Object field = null;
  private CustomLabeledComponent labeledComponent;

  /** 各フィールドに対してコンポーネントを生成 */
  public Object generateFileds(String fieldType, TextFieldStyle style, String japaneseLabel) {
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

    } else if ("text".equalsIgnoreCase(fieldType)) {
      CustomTextField textFieldStandard = new CustomTextField(20, style, 14);
      labeledComponent = new CustomLabeledComponent(japaneseLabel, textFieldStandard);
      field = textFieldStandard;

    } else {
      throw new IllegalArgumentException("Unknown field type: " + fieldType);
    }
    return field;
  }

  /** 作成したfieldの返却 */
  public Object getField() {
    if (field == null) {
      throw new IllegalStateException(
          "フィールドが初期化されていません。generateFieldsが呼び出され、適切なfieldTypeが指定されていることを確認してください。");

    }
    return field;
  }

  /** 作成したラベルの返却 */
  public CustomLabeledComponent getLabeledComponent() {
    if (labeledComponent == null) {
      throw new IllegalStateException(
          "ラベルコンポーネントが初期化されていません。generateFieldsが正しく実行されていることを確認してください。");
    }
    return labeledComponent;
  }
}
