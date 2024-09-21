package src.components.table;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import src.components.table.override.checkbox.CheckBoxEditor;
import src.components.table.override.checkbox.CheckBoxRenderer;

/** 共通のテーブルを生成 */
public class CommonTable {

  private DefaultTableModel tableModel;
  private JTable table;
  private boolean isEditable;
  private Object[] columnNames;
  private boolean checkBoxColumnAdded = false;

  public CommonTable(Object[] columnNames, boolean isEditable) {
    this.columnNames = columnNames;
    this.isEditable = isEditable;
    createTableModel();
    table = new JTable(tableModel);

    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
    table.setRowSorter(sorter);

    table.setRowHeight(30);
  }

  /** テーブルモデルを作成 */
  private void createTableModel() {
    tableModel = new DefaultTableModel(columnNames, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return isEditable;
      }
    };
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

    // モデルを再作成し、編集状態を反映
    createTableModel();
    table.setModel(tableModel);

    // 保存したデータを新しいモデルに再追加
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

    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
    table.setRowSorter(sorter);

    // テーブルの再描画
    table.revalidate();
    table.repaint();
  }

  /** チェックボックス列を追加 */
  private void addCheckBoxColumn() {
    tableModel.addColumn("Delete");
    table.getColumnModel().getColumn(tableModel.getColumnCount() - 1).setCellRenderer(new CheckBoxRenderer());
    table.getColumnModel().getColumn(tableModel.getColumnCount() - 1)
        .setCellEditor(new CheckBoxEditor(new JCheckBox()));
    checkBoxColumnAdded = true;
  }

  /** チェックボックス列を削除 */
  private void removeCheckBoxColumn() {
    if (checkBoxColumnAdded) {
      table.getColumnModel().removeColumn(table.getColumnModel().getColumn(tableModel.getColumnCount() - 1));
      checkBoxColumnAdded = false;
    }
  }

  /** テーブルパネルの作成 */
  public JPanel createTablePanel(String panelTitle) {
    JPanel tablePanel = new JPanel(new BorderLayout());
    tablePanel.setBorder(new TitledBorder(panelTitle));

    JScrollPane scrollPane = new JScrollPane(table);
    tablePanel.add(scrollPane, BorderLayout.CENTER);
    return tablePanel;
  }

  /** JTableを返すメソッドを追加 */
  public JTable getTable() {
    return table;
  }

  /** テーブルモデルを取得 */
  public DefaultTableModel getTableModel() {
    return tableModel;
  }

  /** テーブルに行を追加 */
  public void addRow(ArrayList<Object> rowData) {
    tableModel.addRow(rowData.toArray());
  }

  /** テーブルの全データをクリア */
  public void clearTable() {
    tableModel.setRowCount(0);
  }
}
