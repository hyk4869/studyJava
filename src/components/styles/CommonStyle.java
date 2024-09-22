package src.components.styles;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;

import src.components.enums.TextFieldStyle;
import src.utils.CommonColor;
import src.utils.CommonFont;
import src.utils.CommonPadding;

public class CommonStyle<T extends JComponent> {

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
  private final TextFieldStyle style;

  private final int width;
  private final int height;

  private final CommonPadding commonPadding = new CommonPadding();
  private final CommonColor commonColor = new CommonColor();
  private final CommonFont commonFont = new CommonFont();

  private final T component;

  public CommonStyle(T component, TextFieldStyle style, int fontSize, int width, int height) {
    this.style = style;
    this.width = width;
    this.height = height;
    this.component = component;

    outlinedBorder = createBorder(commonColor.commonGray(), 1, true);
    standardBorder = createMatteBorder(commonColor.commonGray(), 1);
    standardFocusBorder = createMatteBorder(commonColor.commonBlue(), 2);
    standardHoverBorder = createMatteBorder(commonColor.commonBlack(), 2);

    otherFocusBorder = createBorder(commonColor.commonBlue(), 2, true);
    otherHoverBorder = createBorder(commonColor.commonBlack(), 2, true);

    customizeFields(fontSize);
  }

  /** ボーダーを作成する共通メソッド */
  public Border createBorder(Color color, int thickness, boolean roundedCorners) {
    return BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(color, thickness, roundedCorners),
        commonPadding.paddingBorder());
  }

  /** マットボーダーを作成する共通メソッド */
  public Border createMatteBorder(Color color, int thickness) {
    return BorderFactory.createCompoundBorder(
        BorderFactory.createMatteBorder(0, 0, thickness, 0, color),
        commonPadding.paddingBorder());
  }

  public void customizeFields(int fontSize) {
    // this.setPreferredSize(new Dimension(width, height));
    component.setPreferredSize(new Dimension(width, height));
    component.setFont(commonFont.commonNotoSansCJKJP(fontSize));

    // スタイルに応じた初期設定
    switch (style) {
      case OUTLINED:
        component.setBorder(outlinedBorder);
        component.setOpaque(false); // 背景を透明にする
        break;
      case STANDARD:
        component.setBorder(standardBorder);
        component.setOpaque(false); // 背景を透明にする
        break;
    }

    // フォーカス時の効果を追加
    component.addFocusListener(new FocusListener() {
      @Override
      public void focusGained(FocusEvent e) {
        if (style == TextFieldStyle.STANDARD) {
          component.setBorder(standardFocusBorder);
        } else {
          component.setBorder(otherFocusBorder);
        }
      }

      @Override
      public void focusLost(FocusEvent e) {
        switch (style) {
          case OUTLINED:
            component.setBorder(outlinedBorder);
            break;
          case STANDARD:
            component.setBorder(standardBorder);
            break;
        }
      }
    });

    // マウスホバー時の効果を追加
    component.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseEntered(MouseEvent e) {
        if (!component.hasFocus()) {
          if (style == TextFieldStyle.STANDARD) {
            component.setBorder(standardHoverBorder);
          } else {
            component.setBorder(otherHoverBorder);
          }
        }
      }

      @Override
      public void mouseExited(MouseEvent e) {
        if (!component.hasFocus()) {
          switch (style) {
            case OUTLINED:
              component.setBorder(outlinedBorder);
              break;
            case STANDARD:
              component.setBorder(standardBorder);
              break;
          }
        }
      }
    });
  }

}
