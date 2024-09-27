package src.components.table.override.columns;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JTable;

public class ColumnsName {

  private Map<String, String> columnNames;
  private Map<String, String> columnLabels;
  private JTable commonTable;

  public ColumnsName(JTable commonTable, Map<String, String> columnNames, Map<String, String> columnLabels) {
    this.commonTable = commonTable;
    this.columnNames = columnNames;
    this.columnLabels = columnLabels;
  }

  /**
   * テーブルのカラムを日本語対応できるように
   */
  public void overrideColumnLabel() {
    List<String> columnKeys = new ArrayList<>(columnNames.keySet());
    for (int i = 0; i < columnKeys.size(); i++) {
      String englishColumnName = columnKeys.get(i);
      String japaneseLabel = columnLabels.getOrDefault(englishColumnName, englishColumnName);
      commonTable.getColumnModel().getColumn(i).setHeaderValue(japaneseLabel);
    }
  }

  /** TODO_LIST_COLUMNSのような各フィールドを生成するものを保持している */
  public Map<String, String> getColumnNames() {
    return this.columnNames;
  }

  /** TODO_LIST_COLUMN_LABELSのような各カラムの日本語タイトルを保持している */
  public Map<String, String> getColumnLabels() {
    return this.columnLabels;
  }
}
