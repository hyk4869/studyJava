package src.components.table.override.checkbox;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.Component;

public class CheckBoxRenderer extends JCheckBox implements TableCellRenderer {

  public CheckBoxRenderer() {
    setHorizontalAlignment(JLabel.CENTER);
  }

  @Override
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
      int row, int column) {
    if (value != null) {
      setSelected((Boolean) value);
    }
    return this;
  }
}
