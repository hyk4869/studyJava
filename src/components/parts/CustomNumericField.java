package src.components.parts;

import javax.swing.text.NumberFormatter;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import src.components.enums.TextFieldStyle;
import src.components.styles.CustomStyledTextFields;
import java.text.NumberFormat;
import java.text.ParseException;

/** 数字のinput */
public class CustomNumericField extends CustomStyledTextFields {

  // コンストラクタ
  public CustomNumericField(int columns, TextFieldStyle style, int fontSize) {
    super(style, fontSize);
    setFormatter(createFormatter());

    // 数字以外の入力を制限するためのリスナーを追加
    addKeyListener(new KeyAdapter() {
      @Override
      public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        // 数字でない場合はイベントを消費して入力を無効にする
        if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE) {
          e.consume();
        }
      }
    });
  }

  // 数値フォーマッターを作成
  private static NumberFormatter createFormatter() {
    NumberFormat format = NumberFormat.getIntegerInstance();
    format.setGroupingUsed(false);
    NumberFormatter formatter = new NumberFormatter(format) {
      @Override
      public Object stringToValue(String text) throws ParseException {
        if (text == null || text.trim().isEmpty()) {
          return null; // テキストが空の場合はnullを返す
        }
        return super.stringToValue(text);
      }
    };
    formatter.setValueClass(Integer.class);
    formatter.setAllowsInvalid(false);
    formatter.setCommitsOnValidEdit(true);
    return formatter;
  }
}
