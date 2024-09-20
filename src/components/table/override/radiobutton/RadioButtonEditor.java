package src.components.table.override.radiobutton;

import javax.swing.*;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class RadioButtonEditor extends DefaultCellEditor {

  private JPanel panel;
  private List<JRadioButton> buttons;
  private ButtonGroup group;

  public RadioButtonEditor(String[] labels) {
    super(new JCheckBox()); // ダミーのチェックボックス
    panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));

    buttons = new ArrayList<>();
    group = new ButtonGroup();

    // ラベルの配列を使ってラジオボタンを作成
    for (String label : labels) {
      JRadioButton button = new JRadioButton(label);
      group.add(button);
      buttons.add(button);
      panel.add(button);
    }

    // ボタンが押された時の処理
    for (JRadioButton button : buttons) {
      button.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          fireEditingStopped();
        }
      });
    }
  }

  @Override
  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
    if (value != null) {
      String selectedLabel = (String) value;
      for (JRadioButton button : buttons) {
        if (button.getText().equals(selectedLabel)) {
          button.setSelected(true);
        }
      }
    }
    return panel;
  }

  @Override
  public Object getCellEditorValue() {
    for (JRadioButton button : buttons) {
      if (button.isSelected()) {
        return button.getText();
      }
    }
    return null;
  }
}
