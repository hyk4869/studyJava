package src.components.table.override.radiobutton;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.Component;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

public class RadioButtonRenderer extends JPanel implements TableCellRenderer {

  private List<JRadioButton> buttons;
  private ButtonGroup group;

  public RadioButtonRenderer(String[] labels) {
    setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));

    buttons = new ArrayList<>();
    group = new ButtonGroup();

    // ラベルの配列を使ってラジオボタンを作成
    for (String label : labels) {
      JRadioButton button = new JRadioButton(label);
      group.add(button);
      buttons.add(button);
      add(button);
    }
  }

  @Override
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
      int row, int column) {
    if (value != null) {
      String selectedLabel = (String) value;
      for (JRadioButton button : buttons) {
        if (button.getText().equals(selectedLabel)) {
          button.setSelected(true);
        }
      }
    }
    return this;
  }
}
