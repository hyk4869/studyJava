package src.components.parts;

import javax.swing.JTextArea;

import src.components.enums.TextFieldStyle;
import src.components.styles.CommonStyle;

/** テキストエリアのinput */
public final class CustomTextArea extends JTextArea {

  public CustomTextArea(int rows, int columns, TextFieldStyle style, int fontSize) {
    super(rows, columns);

    new CommonStyle<>(this, style, fontSize, 200, 100);
  }

}
