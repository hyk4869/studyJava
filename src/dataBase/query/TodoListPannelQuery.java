package src.dataBase.query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import src.dataBase.PostgreSQLConnection;

public class TodoListPannelQuery {
  private PostgreSQLConnection connection;

  public TodoListPannelQuery(PostgreSQLConnection connection) {
    this.connection = connection;
  }

  /** 共通のSELECTカラム部分をメソッド化 */
  private String getAllColumns() {
    return "\"id\", \"title\", \"description\", \"createdByName\", \"updatedByName\", \"createdAt\", \"updatedAt\", \"isCompleted\", \"sort\"";
  }

  /** INSERT用のカラム部分をSELECT用に足りないカラムを追加して返すメソッド */
  private String getInsertColumns() {
    return "\"createdById\", \"updatedById\", \"deletedAt\", " + getAllColumns();
  }

  /** INSERT用のプレースホルダー部分をメソッド化 */
  private String getInsertValuesPlaceholders() {
    return "?, ?, NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?";
  }

  /** タイトルの重複をチェックするクエリ */
  public boolean isTitleDuplicated(String title, String id) throws SQLException {
    String query = "SELECT 1 FROM \"T_TodoList\" WHERE \"title\" = ? AND \"id\" != ? AND \"deletedAt\" IS NULL";
    ResultSet resultSet = connection.executeQuery(query, title, id);
    boolean isDuplicated = resultSet.next();
    resultSet.close();
    return isDuplicated;
  }

  /** Todo項目を追加するクエリ */
  public void insertTodoItem(Map<String, Object> todoItemData) throws SQLException {
    // カラム名とプレースホルダーを動的に生成
    StringBuilder columns = new StringBuilder();
    StringBuilder placeholders = new StringBuilder();

    todoItemData.forEach((key, value) -> {
      columns.append("\"").append(key).append("\", ");
      placeholders.append("?, ");
    });

    // カラム名とプレースホルダーの末尾の余分な「, 」を削除
    if (columns.length() > 0) {
      columns.setLength(columns.length() - 2);
      placeholders.setLength(placeholders.length() - 2);
    }

    String query = "INSERT INTO \"T_TodoList\" (" + columns.toString() + ") " +
        "VALUES (" + placeholders.toString() + ")";

    connection.executeUpdate(query, todoItemData.values().toArray());
  }

  /** Todo項目を更新するクエリ */
  public void updateTodoItem(Map<String, Object> updatedValues) throws SQLException {
    StringBuilder setClause = new StringBuilder();
    List<Object> params = new ArrayList<>();

    // 更新するカラムと値を動的に生成
    updatedValues.forEach((key, value) -> {
      if (!key.equals("id")) { // ID は WHERE 句に使用するため SET 句には含めない
        setClause.append("\"").append(key).append("\" = ?, ");
        params.add(value);
      }
    });

    // 最後のカンマとスペースを削除
    if (setClause.length() > 0) {
      setClause.setLength(setClause.length() - 2);
    }

    String query = "UPDATE \"T_TodoList\" SET " + setClause.toString()
        + ", \"updatedAt\" = ? WHERE \"id\" = ? AND \"deletedAt\" IS NULL";

    Timestamp updatedAt = new Timestamp(System.currentTimeMillis());
    params.add(updatedAt);

    // ID は WHERE 句に使用するので最後に追加
    params.add(updatedValues.get("id"));

    connection.executeUpdate(query, params.toArray());
  }

  /** Todo項目を取得するクエリ */
  public ResultSet getTodoItemById(String id) throws SQLException {
    String query = "SELECT " + getAllColumns() + " FROM \"T_TodoList\" WHERE id = ? AND \"deletedAt\" IS NULL";
    return connection.executeQuery(query, id);
  }

  /** T_TodoListの全データを取得するクエリ */
  public ResultSet getAllTodoItems() throws SQLException {
    String query = "SELECT * FROM \"T_TodoList\" WHERE \"deletedAt\" IS NULL";
    return connection.executeQuery(query);
  }

  /**
   * Todo項目を削除するクエリ
   *
   * @param ids 削除したいTodo項目のIDリスト
   */
  public void deleteTodoItemsByIds(String[] ids) throws SQLException {
    String query = "UPDATE \"T_TodoList\" SET \"deletedAt\" = NOW() WHERE \"id\" = ANY (?)";
    connection.executeUpdate(query, (Object) ids);
  }

}
