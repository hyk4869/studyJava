package src.components.table;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

/** 共通のテーブルを生成 */
public class CommonTable {

  private DefaultTableModel tableModel;
  private JTable table;

  public CommonTable(Object[] columnNames) {
    tableModel = new DefaultTableModel(columnNames, 0);
    table = new JTable(tableModel);
  }

  /** テーブルパネルの作成 */
  public JPanel createTablePanel(String panelTitle) {
    JPanel tablePanel = new JPanel(new java.awt.BorderLayout());
    tablePanel.setBorder(new TitledBorder(panelTitle));

    JScrollPane scrollPane = new JScrollPane(table);
    tablePanel.add(scrollPane, java.awt.BorderLayout.CENTER);
    return tablePanel;
  }

  /** テーブルモデルを取得 */
  public DefaultTableModel getTableModel() {
    return tableModel;
  }

  /** テーブルに行を追加 */
  public void addRow(Object[] rowData) {
    tableModel.addRow(rowData);
  }

  /** テーブルの全データをクリア */
  public void clearTable() {
    tableModel.setRowCount(0);
  }
}
