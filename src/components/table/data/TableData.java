package src.components.table.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.swing.table.DefaultTableModel;

import src.components.enums.DefaultSortDateType;

/** テーブルのデータを扱う箇所 */
public class TableData {

  private Function<String, Integer> getColumnIndexFunction;
  private Supplier<DefaultTableModel> getTableModelFunction;

  public TableData(Supplier<DefaultTableModel> getTableModel, Function<String, Integer> getColumnIndexByName) {
    this.getTableModelFunction = getTableModel;
    this.getColumnIndexFunction = getColumnIndexByName;
  }

  /**
   * 行を追加
   *
   * @param rowData
   */
  private void addRow(ArrayList<Object> rowData) {
    getTableModelFunction.get().addRow(rowData.toArray());
  }

  /**
   * 全データをクリア
   */
  private void clearTable() {
    getTableModelFunction.get().setRowCount(0);
  }

  /**
   * データをリロードするメソッド
   *
   * @param rows
   * @param sortColumnName
   */
  private void reloadTableData(List<ArrayList<Object>> rows, DefaultSortDateType sortColumnName) {
    int sortColumnIndex = getColumnIndexFunction.apply(sortColumnName.name());

    clearTable();

    rows.stream()
        .sorted(Comparator.comparing(row -> (Timestamp) row.get(sortColumnIndex), Comparator.reverseOrder()))
        .forEach(d -> addRow(d));
  }

  /**
   * ResultSetから1行分のデータを取得
   *
   * @param resultSet
   * @return
   * @throws SQLException
   */
  private ArrayList<Object> getRowData(ResultSet resultSet) throws SQLException {
    ArrayList<Object> rowData = new ArrayList<>();
    int columnCount = getTableModelFunction.get().getColumnCount();

    for (int i = 0; i < columnCount; i++) {
      String columnName = getTableModelFunction.get().getColumnName(i);
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

  /**
   * データをロードしてテーブルに追加
   *
   * @param resultSet
   */
  public void loadItems(ResultSet resultSet) {
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

}
