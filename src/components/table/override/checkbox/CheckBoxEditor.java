package src.components.table.override.checkbox;

import javax.swing.*;
import java.awt.Component;

public class CheckBoxEditor extends DefaultCellEditor {

  private JCheckBox checkBox;

  public CheckBoxEditor(JCheckBox checkBox) {
    super(checkBox);
    this.checkBox = new JCheckBox();
    this.checkBox.setHorizontalAlignment(JLabel.CENTER);
  }

  @Override
  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
    if (value != null) {
      checkBox.setSelected((Boolean) value);
    }
    return checkBox;
  }

  @Override
  public Object getCellEditorValue() {
    return checkBox.isSelected();
  }
}
