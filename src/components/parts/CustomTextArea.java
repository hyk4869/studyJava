package src.components.parts;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import src.components.enums.TextFieldStyle;
import src.utils.CommonColor;
import src.utils.CommonFont;
import src.utils.CommonPadding;

public class CustomTextArea extends JTextArea {

  /** OUTLINEDのスタイル保持 */
  private final Border outlinedBorder;
  /** STANDARDのスタイル保持 */
  private final Border standardBorder;
  /** STANDARDスタイル用のフォーカスボーダー */
  private final Border standardFocusBorder;
  /** STANDARDスタイル用のホバーボーダー */
  private final Border standardHoverBorder;
  /** OUTLINEDとFILLED用のフォーカスボーダー */
  private final Border otherFocusBorder;
  /** OUTLINEDとFILLED用のホバーボーダー */
  private final Border otherHoverBorder;
  /** スタイルを保持 */
  protected final TextFieldStyle style;

  protected final CommonPadding commonPadding = new CommonPadding();
  protected final CommonColor commonColor = new CommonColor();
  protected final CommonFont commonFont = new CommonFont();

  // コンストラクタ
  public CustomTextArea(int rows, int columns, TextFieldStyle style, int fontSize) {
    super(rows, columns);
    this.style = style;

    // ボーダーの初期化
    outlinedBorder = createBorder(commonColor.commonGray(), 1, true);
    standardBorder = createMatteBorder(commonColor.commonGray(), 1);
    standardFocusBorder = createMatteBorder(commonColor.commonBlue(), 2);
    standardHoverBorder = createMatteBorder(commonColor.commonBlack(), 2);

    otherFocusBorder = createBorder(commonColor.commonBlue(), 2, true);
    otherHoverBorder = createBorder(commonColor.commonBlack(), 2, true);

    customizeTextArea(fontSize);
  }

  /** ボーダーを作成する共通メソッド */
  private Border createBorder(Color color, int thickness, boolean roundedCorners) {
    return BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(color, thickness, roundedCorners),
        commonPadding.paddingBorder());
  }

  /** マットボーダーを作成する共通メソッド */
  private Border createMatteBorder(Color color, int thickness) {
    return BorderFactory.createCompoundBorder(
        BorderFactory.createMatteBorder(0, 0, thickness, 0, color),
        commonPadding.paddingBorder());
  }

  /** JTextArea内をカスタマイズ */
  private void customizeTextArea(int fontSize) {
    this.setFont(commonFont.commonNotoSansCJKJP(fontSize));
    this.setPreferredSize(new Dimension(200, 100)); // テキストエリアの大きさを設定

    // スタイルに応じた初期設定
    switch (style) {
      case OUTLINED:
        this.setBorder(outlinedBorder);
        this.setOpaque(false); // 背景を透明にする
        break;
      case STANDARD:
        this.setBorder(standardBorder);
        this.setOpaque(false); // 背景を透明にする
        break;
    }

    // フォーカス時の効果を追加
    this.addFocusListener(new FocusListener() {
      @Override
      public void focusGained(FocusEvent e) {
        if (style == TextFieldStyle.STANDARD) {
          setBorder(standardFocusBorder);
        } else {
          setBorder(otherFocusBorder);
        }
      }

      @Override
      public void focusLost(FocusEvent e) {
        switch (style) {
          case OUTLINED:
            setBorder(outlinedBorder);
            break;
          case STANDARD:
            setBorder(standardBorder);
            break;
        }
      }
    });

    // マウスホバー時の効果を追加
    this.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseEntered(MouseEvent e) {
        if (!hasFocus()) {
          if (style == TextFieldStyle.STANDARD) {
            setBorder(standardHoverBorder);
          } else {
            setBorder(otherHoverBorder);
          }
        }
      }

      @Override
      public void mouseExited(MouseEvent e) {
        if (!hasFocus()) {
          switch (style) {
            case OUTLINED:
              setBorder(outlinedBorder);
              break;
            case STANDARD:
              setBorder(standardBorder);
              break;
          }
        }
      }
    });
  }
}
