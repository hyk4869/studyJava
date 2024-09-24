package src.components.table;

import java.awt.BorderLayout;
import java.sql.Timestamp;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

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
  private List<String> columnNames;
  private Map<String, String> columnLabels;

  public CommonTable(List<String> columnNames, boolean isEditable, Map<String, String> columnLabels) {
    super(new DefaultTableModel(columnNames.toArray(), 0));
    this.columnNames = columnNames;
    this.columnLabels = columnLabels;

    this.tableModel = (DefaultTableModel) this.getModel();
    this.isEditable = isEditable;
    this.setRowSorter(new TableRowSorter<>(tableModel));
    this.setRowHeight(30);
  }

  /** テーブルのカラムを日本語対応できるように */
  public void overrideColumnLabel() {
    for (int i = 0; i < columnNames.size(); i++) {
      String englishColumnName = columnNames.get(i);
      String japaneseLabel = columnLabels.getOrDefault(englishColumnName, englishColumnName);
      getColumnModel().getColumn(i).setHeaderValue(japaneseLabel);
    }
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
    for (int i = 0; i < getTableModel().getRowCount(); i++) {
      Object[] rowData = new Object[getTableModel().getColumnCount()];
      for (int j = 0; j < getTableModel().getColumnCount(); j++) {
        rowData[j] = getTableModel().getValueAt(i, j);
      }
      currentData.add(rowData);
    }

    // モデルを再作成して編集可否を反映
    getTableModel().setRowCount(0);
    for (Object[] rowData : currentData) {
      getTableModel().addRow(rowData);
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
    getTableModel().addColumn("Delete");
    this.getColumnModel().getColumn(getTableModel().getColumnCount() - 1)
        .setCellRenderer(new CheckBoxRenderer());
    this.getColumnModel().getColumn(getTableModel().getColumnCount() - 1)
        .setCellEditor(new CheckBoxEditor(new JCheckBox()));
    checkBoxColumnAdded = true;
  }

  /** チェックボックス列を削除 */
  private void removeCheckBoxColumn() {
    if (checkBoxColumnAdded) {
      // "Delete" カラムのインデックスを取得
      int deleteColumnIndex = getColumnModel().getColumnCount() - 1;

      // 表示から削除
      getColumnModel().removeColumn(getColumnModel().getColumn(deleteColumnIndex));

      // TableModelのカラム情報を直接操作
      tableModel.setColumnCount(tableModel.getColumnCount() - 1);

      this.revalidate();
      this.repaint();

      checkBoxColumnAdded = false;
    }
  }

  /** 行を追加 */
  public void addRow(ArrayList<Object> rowData) {
    getTableModel().addRow(rowData.toArray());
  }

  /** 全データをクリア */
  public void clearTable() {
    getTableModel().setRowCount(0);
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
  public int getColumnIndexByName(String columnName) {
    for (int i = 0; i < getColumnCount(); i++) {
      if (getColumnName(i).equals(columnName)) {
        return i;
      }
    }
    throw new IllegalArgumentException("Invalid column name: " + columnName);
  }

  /** データをリロードするメソッド */
  public void reloadTableData(List<ArrayList<Object>> rows, DefaultSortDateType sortColumnName) {
    int sortColumnIndex = getColumnIndexByName(sortColumnName.name());

    clearTable();

    rows.stream()
        .sorted(Comparator.comparing(row -> (Timestamp) row.get(sortColumnIndex), Comparator.reverseOrder()))
        .forEach(d -> addRow(d));
  }

  /** 全データをロードしてテーブルに追加 */
  public void loadAllTodoItems(ResultSet resultSet) {
    try (resultSet) {
      List<ArrayList<Object>> rows = new ArrayList<>();

      while (resultSet.next()) {
        rows.add(getRowData(resultSet)); // データをリストに追加
      }

      reloadTableData(rows, DefaultSortDateType.updatedAt);

    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  /** ResultSetから1行分のデータを取得 */
  private ArrayList<Object> getRowData(ResultSet resultSet) throws SQLException {
    ArrayList<Object> rowData = new ArrayList<>();
    int columnCount = getTableModel().getColumnCount();

    // 最後の列 "Select" を無視するため、columnCount - 1 にする
    for (int i = 0; i < columnCount - 1; i++) {
      String columnName = getTableModel().getColumnName(i);
      if (!columnName.equals("Delete")) {
        try {
          if (!columnName.equals("Delete")) {
            // ResultSet からカラム名に対応するデータを取得
            Object value = resultSet.getObject(columnName);
            rowData.add(value);
          }
        } catch (SQLException e) {
          System.out.println("カラムが一致しません: " + columnName);
        }
      }
    }
    return rowData;
  }
}
