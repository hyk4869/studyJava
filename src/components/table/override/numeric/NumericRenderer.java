package src.components.table.override.numeric;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.Component;

public class NumericRenderer extends JLabel implements TableCellRenderer {

  public NumericRenderer() {
    setHorizontalAlignment(JLabel.RIGHT);
  }

  @Override
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
      int row, int column) {

    if (value != null) {
      setText(value.toString()); // 数字を文字列に変換して表示
    } else {
      setText("");
    }

    return this;
  }
}
