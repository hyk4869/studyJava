package src.components.table;

import java.awt.BorderLayout;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import src.components.enums.DefaultSortDateType;
import src.components.table.override.checkbox.CheckBoxEditor;
import src.components.table.override.checkbox.CheckBoxRenderer;

public class CommonTable extends JTable {

  private DefaultTableModel tableModel;
  private boolean isEditable;
  private boolean checkBoxColumnAdded = false;

  public CommonTable(List<String> columnNames, boolean isEditable) {
    super(new DefaultTableModel(columnNames.toArray(), 0));

    this.tableModel = (DefaultTableModel) this.getModel();
    this.isEditable = isEditable;
    this.setRowSorter(new TableRowSorter<>(tableModel));
    this.setRowHeight(30);
  }

  /** テーブルモデルを取得 */
  public DefaultTableModel getTableModel() {
    return tableModel;
  }

  /** JTableを取得 */
  public JTable getTable() {
    return this;
  }

  /** テーブルの編集状態を切り替える */
  public void setEditable(boolean isEditable) {
    this.isEditable = isEditable;

    // 現在のデータを保存
    ArrayList<Object[]> currentData = new ArrayList<>();
    for (int i = 0; i < tableModel.getRowCount(); i++) {
      Object[] rowData = new Object[tableModel.getColumnCount()];
      for (int j = 0; j < tableModel.getColumnCount(); j++) {
        rowData[j] = tableModel.getValueAt(i, j);
      }
      currentData.add(rowData);
    }

    // モデルを再作成して編集可否を反映
    tableModel.setRowCount(0);
    for (Object[] rowData : currentData) {
      tableModel.addRow(rowData);
    }

    // 編集モード時にチェックボックス列を追加
    if (isEditable && !checkBoxColumnAdded) {
      addCheckBoxColumn();
    }
    // 編集モードが終了したらチェックボックス列を削除
    else if (!isEditable && checkBoxColumnAdded) {
      removeCheckBoxColumn();
    }

    this.revalidate();
    this.repaint();
  }

  /** テーブルモデルの編集可否を定義 */
  @Override
  public boolean isCellEditable(int row, int column) {
    return isEditable;
  }

  /** チェックボックス列を追加 */
  private void addCheckBoxColumn() {
    tableModel.addColumn("Delete");
    this.getColumnModel().getColumn(tableModel.getColumnCount() - 1)
        .setCellRenderer(new CheckBoxRenderer());
    this.getColumnModel().getColumn(tableModel.getColumnCount() - 1)
        .setCellEditor(new CheckBoxEditor(new JCheckBox()));
    checkBoxColumnAdded = true;
  }

  /** チェックボックス列を削除 */
  private void removeCheckBoxColumn() {
    if (checkBoxColumnAdded) {
      this.getColumnModel().removeColumn(this.getColumnModel().getColumn(tableModel.getColumnCount() - 1));
      checkBoxColumnAdded = false;
    }
  }

  /** 行を追加 */
  public void addRow(ArrayList<Object> rowData) {
    tableModel.addRow(rowData.toArray());
  }

  /** 全データをクリア */
  public void clearTable() {
    tableModel.setRowCount(0);
  }

  /** テーブルパネルの作成 */
  public JPanel createTablePanel(String panelTitle) {
    JPanel tablePanel = new JPanel(new BorderLayout());
    tablePanel.setBorder(new TitledBorder(panelTitle));

    JScrollPane scrollPane = new JScrollPane(this);
    tablePanel.add(scrollPane, BorderLayout.CENTER);
    return tablePanel;
  }

  /** 選択された行の削除 */
  public List<String> deleteSelectedRows() {
    if (getCellEditor() != null) {
      getCellEditor().stopCellEditing();
    }

    List<String> idsToDelete = new ArrayList<>();
    int deleteColumnIndex = getColumnCount() - 1; // "Delete" 列のインデックス

    for (int i = getRowCount() - 1; i >= 0; i--) {
      Object value = getValueAt(i, deleteColumnIndex);
      if (value instanceof Boolean && (Boolean) value) {
        String id = (String) getValueAt(i, 0);
        idsToDelete.add(id);
        getTableModel().removeRow(i);
      }
    }
    return idsToDelete;
  }

  /** 列名からインデックスを取得する */
  private int getColumnIndexByName(String columnName) {
    for (int i = 0; i < getColumnCount(); i++) {
      if (getColumnName(i).equals(columnName)) {
        return i;
      }
    }
    return -1; // 列名が見つからなかった場合
  }

  /** データをリロードするメソッド */
  public void reloadTableData(List<ArrayList<Object>> rows, DefaultSortDateType sortColumnName) {
    int sortColumnIndex = getColumnIndexByName(sortColumnName.name());

    if (sortColumnIndex == -1) {
      throw new IllegalArgumentException("Invalid column name: " + sortColumnName);
    }

    clearTable();

    // rowsをソートしてテーブルに追加
    rows.stream()
        .sorted(Comparator.comparing(row -> (Timestamp) row.get(sortColumnIndex), Comparator.reverseOrder()))
        .forEach(d -> addRow(d));
  }
}
