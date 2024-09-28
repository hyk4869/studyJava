package src.components.table;

import java.awt.BorderLayout;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import src.components.table.data.TableData;
import src.components.table.override.columns.ColumnsName;
import src.components.table.override.columns.DeleteColumn;
import src.components.table.override.columns.OverrideEachColumns;

/** 共通のテーブル */
public class CommonTable extends JTable {

  private DefaultTableModel tableModel;
  private ColumnsName columnsName;
  public DeleteColumn deleteColumn;
  public OverrideEachColumns overrideEachColumns;
  public TableData tableData;

  private boolean isEditable;
  private boolean checkBoxColumnAdded = false;

  public CommonTable(boolean isEditable, Map<String, String> columnNames, Map<String, String> columnLabels) {
    super(new DefaultTableModel(columnNames.keySet().toArray(), 0));

    columnsName = new ColumnsName(this, columnNames, columnLabels);

    this.tableModel = (DefaultTableModel) this.getModel();
    this.isEditable = isEditable;

    this.setRowSorter(new TableRowSorter<>(tableModel));
    this.setRowHeight(30);

    this.deleteColumn = new DeleteColumn(this, tableModel, false);

    this.overrideEachColumns = new OverrideEachColumns(
        this::getColumnIndexByName,
        this::getColumnModel,
        columnsName);

    this.tableData = new TableData(
        this::getTableModel,
        this::getColumnIndexByName);
  }

  /**
   * テーブルパネルの作成
   *
   * @param panelTitle
   * @return
   */
  public JPanel createTablePanel(String panelTitle) {
    JPanel tablePanel = new JPanel(new BorderLayout());
    tablePanel.setBorder(new TitledBorder(panelTitle));

    JScrollPane scrollPane = new JScrollPane(this);
    tablePanel.add(scrollPane, BorderLayout.CENTER);
    return tablePanel;
  }

  /**
   * テーブルモデルを取得
   *
   * @return
   */
  public DefaultTableModel getTableModel() {
    return tableModel;
  }

  /**
   * JTableを取得
   *
   * @return
   */
  public JTable getTable() {
    return this;
  }

  /**
   * テーブルの編集状態を切り替える
   *
   * @param isEditable
   */
  public void setEditable(boolean isEditable) {
    this.isEditable = isEditable;

    // 編集モード時にチェックボックス列を追加
    if (isEditable && !checkBoxColumnAdded) {
      deleteColumn.addCheckBoxColumn();
      checkBoxColumnAdded = deleteColumn.getCheckBoxColumnAdded();
    }
    // 編集モードが終了したらチェックボックス列を削除
    else if (!isEditable && checkBoxColumnAdded) {
      deleteColumn.removeCheckBoxColumn();
      checkBoxColumnAdded = deleteColumn.getCheckBoxColumnAdded();
    }

    this.revalidate();
    this.repaint();
  }

  /**
   * テーブルモデルの編集可否を定義
   */
  @Override
  public boolean isCellEditable(int row, int column) {
    return isEditable;
  }

  /**
   * 列名からインデックスを取得する
   *
   * @param columnName
   * @return
   */
  public int getColumnIndexByName(String columnName) {
    for (int i = 0; i < getColumnCount(); i++) {
      if (getColumnName(i).equals(columnName)) {
        return i;
      }
    }
    return -1;
  }

}
