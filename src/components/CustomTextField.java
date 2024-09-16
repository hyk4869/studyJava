package src.components;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.border.Border;

import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import src.utils.CommonColor;
import src.utils.CommonFont;
import src.utils.CommonPadding;

/** JTextFieldをカスタム */
public class CustomTextField extends JTextField {

  /** スタイルの種類を表すEnum */
  public enum TextFieldStyle {
    OUTLINED, STANDARD
  }

  /** outlinedのスタイル保持 */
  private final Border outlinedBorder;
  /** standardのスタイル保持 */
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

  private final CommonPadding commonPadding = new CommonPadding();

  private final CommonColor commonColor = new CommonColor();

  private final CommonFont commonFont = new CommonFont();

  // コンストラクタ
  public CustomTextField(int columns, TextFieldStyle style) {
    super(columns);
    this.style = style;

    outlinedBorder = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(commonColor.commonGray(), 1, true),
        commonPadding.paddingBorder());

    standardBorder = BorderFactory.createCompoundBorder(
        BorderFactory.createMatteBorder(0, 0, 1, 0, commonColor.commonGray()),
        commonPadding.paddingBorder());

    standardFocusBorder = BorderFactory.createCompoundBorder(
        BorderFactory.createMatteBorder(0, 0, 2, 0, commonColor.commonBlue()),
        commonPadding.paddingBorder());

    standardHoverBorder = BorderFactory.createCompoundBorder(
        BorderFactory.createMatteBorder(0, 0, 2, 0, commonColor.commonBlack()),
        commonPadding.paddingBorder());

    otherFocusBorder = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(commonColor.commonBlue(), 2, true),
        commonPadding.paddingBorder());

    otherHoverBorder = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(commonColor.commonBlack(), 2, true),
        commonPadding.paddingBorder());

    customizeTextField();
  }

  /** JTextField内をカスタマイズ */
  private void customizeTextField() {
    // プレースホルダーのような効果を追加
    this.setToolTipText("Enter your ToDo here...");

    // カスタムのフォントを設定
    this.setFont(commonFont.commonArialFont(14));

    // サイズを設定
    this.setPreferredSize(new Dimension(200, 40));

    // スタイルに応じた初期設定
    switch (style) {
      case OUTLINED:
        this.setBorder(outlinedBorder);
        this.setBackground(commonColor.commonWhite());
        break;
      case STANDARD:
        this.setBorder(standardBorder);
        this.setBackground(commonColor.commonWhite());
        break;
    }

    // フォーカス時の効果を追加
    this.addFocusListener(new FocusListener() {
      @Override
      public void focusGained(FocusEvent e) {
        // フォーカス時に枠線の色を変更
        if (style == TextFieldStyle.STANDARD) {
          setBorder(standardFocusBorder);
        } else {
          setBorder(otherFocusBorder);
        }
      }

      @Override
      public void focusLost(FocusEvent e) {
        // フォーカスが外れたら元のスタイルに戻す
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
        if (!hasFocus()) { // フォーカスされていないときのみ
          if (style == TextFieldStyle.STANDARD) {
            setBorder(standardHoverBorder);
          } else {
            setBorder(otherHoverBorder);
          }
        }
      }

      @Override
      public void mouseExited(MouseEvent e) {
        if (!hasFocus()) { // フォーカスされていないときのみ
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
