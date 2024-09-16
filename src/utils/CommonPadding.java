package src.utils;

import javax.swing.border.EmptyBorder;

/** 共通のpaddingを設定 */
public class CommonPadding {

  /** テキストフィールドのpadding */
  public EmptyBorder paddingBorder() {
    return new EmptyBorder(5, 10, 5, 10);
  }
}
