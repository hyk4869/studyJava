package src.utils;

import java.awt.Font;

/** 共通のフォント */
public class CommonFont {

  /** Arial */
  public Font commonArialFont(int fontSize) {
    return new Font("Arial", Font.PLAIN, fontSize);
  }

  public Font commonNotoSansCJKJP(int fontSize) {
    return new Font("Noto Serif CJK JP", Font.PLAIN, fontSize);
  }
}
