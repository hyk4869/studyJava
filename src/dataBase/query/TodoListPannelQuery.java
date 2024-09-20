package src.dataBase.query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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
  public boolean isTitleDuplicated(String title) throws SQLException {
    String query = "SELECT 1 FROM \"T_TodoList\" WHERE \"title\" = ?";
    ResultSet resultSet = connection.executeQuery(query, title);
    boolean isDuplicated = resultSet.next();
    resultSet.close();
    return isDuplicated;
  }

  /** Todo項目を追加するクエリ */
  public void insertTodoItem(String id, String createdById, String createdByName, String updatedById,
      String updatedByName, Timestamp createdAt, Timestamp updatedAt, String title, String description,
      Boolean isCompleted)
      throws SQLException {
    String query = "INSERT INTO \"T_TodoList\" (" + getInsertColumns() + ") " +
        "VALUES (" + getInsertValuesPlaceholders() + ")";
    connection.executeUpdate(query, createdById, updatedById, id, title, description, createdByName, updatedByName,
        createdAt, updatedAt, isCompleted, 0);
  }

  /** Todo項目を取得するクエリ */
  public ResultSet getTodoItemById(String id) throws SQLException {
    String query = "SELECT " + getAllColumns() + " FROM \"T_TodoList\" WHERE id = ? AND \"deletedAt\" IS NULL";
    return connection.executeQuery(query, id);
  }

  /** T_TodoListの全データを取得するクエリ */
  public ResultSet getAllTodoItems() throws SQLException {
    String query = "SELECT " + getAllColumns() + " FROM \"T_TodoList\" WHERE \"deletedAt\" IS NULL";
    return connection.executeQuery(query);
  }

}
