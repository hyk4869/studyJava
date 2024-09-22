package src.components.styles;

import javax.swing.JFormattedTextField;

import src.components.enums.TextFieldStyle;

public abstract class CustomStyledTextFields extends JFormattedTextField {

  // コンストラクタ
  public CustomStyledTextFields(TextFieldStyle style, int fontSize) {
    new CommonStyle<>(this, style, fontSize, 200, 40);
  }

}
