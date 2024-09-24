package src.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetLog {

  public static void printResultSetRow(ResultSet resultSet) throws SQLException {
    // メタデータを取得
    int columnCount = resultSet.getMetaData().getColumnCount();

    System.out.println("------ ResultSet Row ------");

    // 各カラム名とその値を見やすく表示
    for (int i = 1; i <= columnCount; i++) {
      String columnName = resultSet.getMetaData().getColumnName(i);
      Object value = resultSet.getObject(i);
      System.out.printf("%-20s : %s%n", columnName, value);
    }

    System.out.println("---------------------------");
  }
}
