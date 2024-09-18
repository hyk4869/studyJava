package src.components;

import src.components.enums.TextFieldStyle;
import src.components.styles.CustomStyledContents;

/** 文字のinput */
public class CustomTextField extends CustomStyledContents {

  // コンストラクタ
  public CustomTextField(int columns, TextFieldStyle style, int fontSize) {
    super(style, fontSize);
    this.setColumns(columns);
  }
}
