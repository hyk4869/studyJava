package src.components.table.override.columns;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import src.components.table.override.checkbox.CheckBoxEditor;
import src.components.table.override.checkbox.CheckBoxRenderer;

public class DeleteColumn {
  private JTable commonTable;
  private DefaultTableModel tableModel;
  private boolean checkBoxColumnAdded;

  private CheckBoxRenderer checkBoxRenderer;
  private CheckBoxEditor checkBoxEditor;

  public DeleteColumn(JTable commonTable, DefaultTableModel tableModel, boolean checkBoxColumnAdded) {
    this.commonTable = commonTable;
    this.tableModel = tableModel;
    this.checkBoxColumnAdded = checkBoxColumnAdded;

    this.checkBoxRenderer = new CheckBoxRenderer();
    this.checkBoxEditor = new CheckBoxEditor(new JCheckBox());
  }

  /**
   * DELETEチェックボックスの追加
   */
  public void addCheckBoxColumn() {
    tableModel.addColumn("Delete");

    commonTable.revalidate();
    commonTable.repaint();

    int deleteColumnIndex = tableModel.getColumnCount() - 1;
    commonTable.getColumnModel().getColumn(deleteColumnIndex).setCellRenderer(checkBoxRenderer);
    commonTable.getColumnModel().getColumn(deleteColumnIndex).setCellEditor(checkBoxEditor);

    checkBoxColumnAdded = true;
  }

  /**
   * DELETEチェックボックスの削除
   */
  public void removeCheckBoxColumn() {
    if (checkBoxColumnAdded) {
      int deleteColumnIndex = -1;
      for (int i = 0; i < commonTable.getColumnModel().getColumnCount(); i++) {
        if (commonTable.getColumnModel().getColumn(i).getHeaderValue().equals("Delete")) {
          deleteColumnIndex = i;
          break;
        }
      }

      if (deleteColumnIndex != -1) {
        commonTable.getColumnModel().removeColumn(commonTable.getColumnModel().getColumn(deleteColumnIndex));
        tableModel.setColumnCount(tableModel.getColumnCount() - 1);

        commonTable.revalidate();
        commonTable.repaint();

        checkBoxColumnAdded = false;
      } else {
        System.out.println("Deleteカラムが見つかりませんでした。");
      }
    }
  }

  /**
   * 選択された行の削除
   *
   * @return
   */
  public List<String> deleteSelectedRows() {
    if (commonTable.getCellEditor() != null) {
      commonTable.getCellEditor().stopCellEditing();
    }

    List<String> idsToDelete = new ArrayList<>();
    int deleteColumnIndex = commonTable.getColumnCount() - 1;

    for (int i = commonTable.getRowCount() - 1; i >= 0; i--) {
      Object value = commonTable.getValueAt(i, deleteColumnIndex);
      if (value instanceof Boolean && (Boolean) value) {
        String id = (String) commonTable.getValueAt(i, 0);
        idsToDelete.add(id);
        tableModel.removeRow(i);
      }
    }
    return idsToDelete;
  }

  public boolean getCheckBoxColumnAdded() {
    return this.checkBoxColumnAdded;
  }
}
