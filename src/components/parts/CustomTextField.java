package src.components.parts;

import src.components.enums.TextFieldStyle;
import src.components.styles.CustomStyledTextFields;

/** 文字のinput */
public final class CustomTextField extends CustomStyledTextFields {

  // コンストラクタ
  public CustomTextField(int columns, TextFieldStyle style, int fontSize) {
    super(style, fontSize);
    this.setColumns(columns);
  }
}
