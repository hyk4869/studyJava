package src.components.parts;

import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public final class CustomButton extends JButton {

  // private JButton button;

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

    this.setText(buttonText);
    this.addActionListener(listener);

    GridBagConstraints innerGbc = new GridBagConstraints();

    // button = new JButton(buttonText);
    // button.addActionListener(listener);

    innerGbc.gridx = gridX; // 配置する列
    innerGbc.gridy = gridY; // 配置する行
    innerGbc.gridwidth = gridWidth; // 列幅を設定
    innerGbc.fill = GridBagConstraints.NONE; // ボタンの横幅を自動調整
    innerGbc.anchor = GridBagConstraints.EAST; // 右寄せ
    innerPanel.add(this, innerGbc);
  }

  public JButton getButton() {
    return this;
  }

}
