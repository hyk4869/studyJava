package src.components;

import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class CustomButton {

  private JButton button;

  /**
   * ボタンを追加
   *
   * @param innerPanel
   * @param buttonText
   * @param gridX
   *                   配置する列
   * @param gridY
   *                   配置する行
   * @param gridWidth
   *                   列幅を設定
   * @param listener
   *                   ボタンのクリック時に実行するアクションリスナー
   *
   */
  public void addButton(JPanel innerPanel, String buttonText, int gridX, int gridY, int gridWidth,
      ActionListener listener) {
    GridBagConstraints innerGbc = new GridBagConstraints();
    button = new JButton(buttonText);

    button.addActionListener(listener);

    innerGbc.gridx = gridX; // 配置する列
    innerGbc.gridy = gridY; // 配置する行
    innerGbc.gridwidth = gridWidth; // 列幅を設定
    innerGbc.fill = GridBagConstraints.NONE; // ボタンの横幅を自動調整
    innerGbc.anchor = GridBagConstraints.EAST; // 右寄せ
    innerPanel.add(button, innerGbc);
  }

  public JButton getButton() {
    return button;
  }

  /**
   * ボタンのテキストを変更
   *
   * @param text ボタンに設定する新しいテキスト
   */
  public void setText(String text) {
    if (button != null) {
      button.setText(text);
    }
  }
}
