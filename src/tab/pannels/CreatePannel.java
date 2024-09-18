package src.tab.pannels;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import src.utils.CommonColor;

import javax.swing.border.LineBorder;
import java.awt.GridBagLayout;

public class CreatePannel {
  private final CommonColor commonColor = new CommonColor();

  /** メインパネルを作成 */
  public JPanel createMainPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new GridBagLayout()); // GridBagLayoutで柔軟に配置
    return panel;
  }

  /** 内部パネルを作成 */
  public JPanel createInnerPanel(String borderTitle) {
    JPanel innerPanel = new JPanel(new GridBagLayout());
    innerPanel.setBorder(new TitledBorder(new LineBorder(commonColor.commonGray()), borderTitle)); // 四角い枠を追加
    return innerPanel;
  }
}
