package src.components.parts;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import src.utils.CommonColor;

public final class CustomButton extends JButton {

  private Color buttonBorderColor;
  private CommonColor commonColor = new CommonColor();
  private Insets insets = new Insets(5, 10, 5, 10);

  /**
   * ボタンを追加
   *
   * @param innerPanel
   * @param buttonText
   * @param gridX      配置する列
   * @param gridY      配置する行
   * @param gridWidth  列幅を設定
   * @param listener   ボタンのクリック時に実行するアクションリスナー
   */
  public void addButton(JPanel innerPanel, String buttonText, int gridX, int gridY, int gridWidth,
      ActionListener listener, Color borderColor) {

    this.buttonBorderColor = borderColor;

    this.setText(buttonText);
    this.addActionListener(listener);

    GridBagConstraints innerGbc = new GridBagConstraints();

    innerGbc.gridx = gridX; // 配置する列
    innerGbc.gridy = gridY; // 配置する行
    innerGbc.gridwidth = gridWidth; // 列幅を設定
    innerGbc.fill = GridBagConstraints.NONE; // ボタンの横幅を自動調整
    innerGbc.anchor = GridBagConstraints.EAST; // 右寄せ
    innerPanel.add(this, innerGbc);

    // ポインターを変更する
    this.setCursor(new Cursor(Cursor.HAND_CURSOR));

    // マウスホバー時の動作を設定する
    this.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseEntered(MouseEvent e) {
        setShadowBorder(true);
      }

      @Override
      public void mouseExited(MouseEvent e) {
        setShadowBorder(false);
      }
    });
    setOutlinedButton(1);
  }

  /**
   * ボタンの枠線の色を設定する
   *
   * @param borderColor 枠線の色
   * @param thickness   枠線の太さ
   */
  public void setOutlinedButton(int thickness) {
    /** 外枠の枠線 */
    LineBorder lineBorder = new LineBorder(new Color(0, 0, 0, 50), thickness);
    /** 内側の余白 */
    EmptyBorder emptyBorder = new EmptyBorder(this.insets);
    // CompoundBorderで外枠と内側の余白を組み合わせる
    this.setBorder(new CompoundBorder(lineBorder, emptyBorder));
    // 塗りつぶしを無効にする
    this.setContentAreaFilled(false);
    // 文字の色を変更
    this.setForeground(this.buttonBorderColor);
  }

  /**
   * ホバー時に影を追加するかどうかを切り替える
   *
   * @param isHovered ホバー状態かどうか
   */
  private void setShadowBorder(boolean isHovered) {
    if (isHovered) {
      this.setBorder(
          new CompoundBorder(new LineBorder(this.buttonBorderColor, 1), new EmptyBorder(this.insets)));

      this.setContentAreaFilled(true);
      this.setBackground(commonColor.commonWhite());

    } else {
      this.setBorder(new CompoundBorder(new LineBorder(new Color(0, 0, 0, 50), 1), new EmptyBorder(this.insets)));

      this.setContentAreaFilled(false);
    }
  }

  public JButton getButton() {
    return this;
  }

}
