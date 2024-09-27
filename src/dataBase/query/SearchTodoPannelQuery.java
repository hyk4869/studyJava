package src.dataBase.query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import src.dataBase.PostgreSQLConnection;

public class SearchTodoPannelQuery {
  private PostgreSQLConnection connection;

  public SearchTodoPannelQuery(PostgreSQLConnection connection) {
    this.connection = connection;
  }

  public ResultSet searchTodoList(Map<String, Object> searchParams) throws SQLException {
    // 検索条件がなければ全データを取得
    if (searchParams.isEmpty()) {
      String query = "SELECT * FROM \"T_TodoList\"";
      return connection.executeQuery(query);
    }

    // クエリを動的に生成
    StringBuilder queryBuilder = new StringBuilder("SELECT * FROM \"T_TodoList\" WHERE ");
    int paramIndex = 0;

    // 各条件に応じたクエリを構築
    for (String key : searchParams.keySet()) {
      if (paramIndex > 0) {
        queryBuilder.append(" AND ");
      }

      Object value = searchParams.get(key);

      // 型に応じてクエリを変更
      if (value instanceof Boolean || value instanceof Integer) {
        queryBuilder.append("\"").append(key).append("\" = ?");
      } else if (value instanceof java.sql.Timestamp) {
        queryBuilder.append("DATE(\"").append(key).append("\") = ?");
      } else if (value instanceof String) {
        queryBuilder.append("\"").append(key).append("\" ILIKE ?");
      }

      paramIndex++;
    }

    String query = queryBuilder.toString();

    // パラメータをセットし、ILIKE用にはワイルドカードを追加
    Object[] params = searchParams.values().stream().map(value -> {
      if (value instanceof String) {
        return "%" + value + "%";
      }
      return value;
    }).toArray();

    return connection.executeQuery(query, params);
  }

}
