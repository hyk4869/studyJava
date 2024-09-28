package src.components.table.override.numeric;

import javax.swing.*;
import javax.swing.text.AbstractDocument;

import java.awt.Component;

public class NumericEditor extends DefaultCellEditor {

  private JTextField textField;

  public NumericEditor(JTextField textField) {
    super(textField);
    this.textField = new JTextField();
    this.textField.setHorizontalAlignment(JTextField.RIGHT);
    ((AbstractDocument) this.textField.getDocument()).setDocumentFilter(new NumericDocumentFilter());

  }

  @Override
  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
    if (value != null) {
      textField.setText(value.toString()); // 値を文字列に変換して表示
    } else {
      textField.setText("");
    }
    return textField;
  }

  @Override
  public Object getCellEditorValue() {
    try {
      return Integer.parseInt(textField.getText());
    } catch (NumberFormatException e) {
      JOptionPane.showMessageDialog(null, "数字のみ入力してください", "入力エラー", JOptionPane.ERROR_MESSAGE);
      return 0;
    }
  }
}
