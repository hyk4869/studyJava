package src.components;

import javax.swing.*;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

public class CustomRadioButton extends JPanel {

  private List<JRadioButton> buttons;
  private ButtonGroup group;

  public CustomRadioButton(String[] labels) {
    setLayout(new FlowLayout(FlowLayout.LEFT));

    // ラジオボタンを格納するリスト
    buttons = new ArrayList<>();
    group = new ButtonGroup();

    // 配列のラベルを使ってラジオボタンを作成
    for (String label : labels) {
      JRadioButton button = new JRadioButton(label);
      group.add(button);
      buttons.add(button);
      add(button);
    }

    // デフォルトで最初のボタンを選択
    if (!buttons.isEmpty()) {
      buttons.get(0).setSelected(true);
    }
  }

  // 選択されているボタンのラベルを取得
  public String getSelectedLabel() {
    for (JRadioButton button : buttons) {
      if (button.isSelected()) {
        return button.getText();
      }
    }
    return null;
  }

  // 指定したラベルのボタンを選択
  public void setSelectedLabel(String label) {
    for (JRadioButton button : buttons) {
      if (button.getText().equals(label)) {
        button.setSelected(true);
      }
    }
  }
}
