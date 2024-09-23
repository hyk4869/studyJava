package src.components.labels;

import javax.swing.*;
import java.awt.*;

/** 各フィールドのラベルを作っている */
public class CustomLabeledComponent extends JPanel {

  private final JLabel label;

  public CustomLabeledComponent(String labelText, JComponent component) {
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // 垂直に配置

    // ラベルを作成
    label = new JLabel(labelText);
    label.setAlignmentX(Component.LEFT_ALIGNMENT); // 左寄せ

    // 渡されたコンポーネントをこのパネルに追加
    component.setAlignmentX(Component.LEFT_ALIGNMENT); // 左寄せ

    // ラベルとコンポーネントをこのパネルに追加
    this.add(label);
    this.add(component);
  }

  public JLabel getLabel() {
    return label;
  }

  public JComponent getComponent() {
    return this;
  }
}
