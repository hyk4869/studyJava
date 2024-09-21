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
  public boolean isTitleDuplicated(String title, String id) throws SQLException {
    String query = "SELECT 1 FROM \"T_TodoList\" WHERE \"title\" = ? AND \"id\" != ?";
    ResultSet resultSet = connection.executeQuery(query, title, id);
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

  /** Todo項目を更新するクエリ */
  public void updateTodoItem(String id, String title, String description, Boolean isCompleted, Integer sort)
      throws SQLException {
    String query = "UPDATE \"T_TodoList\" " +
        "SET \"title\" = ?, \"description\" = ?, \"isCompleted\" = ?, \"sort\" = ?, \"updatedAt\" = ? " +
        "WHERE \"id\" = ? AND \"deletedAt\" IS NULL";

    Timestamp updatedAt = new Timestamp(System.currentTimeMillis());

    connection.executeUpdate(query, title, description, isCompleted, sort, updatedAt, id);

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
