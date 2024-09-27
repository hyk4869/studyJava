package src.dataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class PostgreSQLConnection {
  private Connection connection;

  public PostgreSQLConnection() {
    String url = "jdbc:postgresql://postgres:5432/sampledb";
    String user = "user";
    String password = "password";

    try {
      connection = DriverManager.getConnection(url, user, password);
      connection.setAutoCommit(false); // トランザクションを開始
      System.out.println("Connection established successfully.");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public Connection getConnection() {
    return connection;
  }

  /** SELECT文をPreparedStatementで実行 */
  public ResultSet executeQuery(String query, Object... params) throws SQLException {
    PreparedStatement preparedStatement = prepareStatement(query, params);
    return preparedStatement.executeQuery();
  }

  /** INSERT, UPDATE, DELETE 文をPreparedStatementで実行 */
  public int executeUpdate(String query, Object... params) throws SQLException {
    PreparedStatement preparedStatement = prepareStatement(query, params);
    return preparedStatement.executeUpdate();
  }

  /** 共通のPreparedStatement作成メソッド */
  public PreparedStatement prepareStatement(String query, Object... params) throws SQLException {
    PreparedStatement preparedStatement = connection.prepareStatement(query);
    setParameters(preparedStatement, params);
    return preparedStatement;
  }

  /** PreparedStatement にパラメータをセット */
  private void setParameters(PreparedStatement preparedStatement, Object... params) throws SQLException {
    System.out.println(preparedStatement.toString());
    System.out.println(Arrays.toString(params));
    System.out.println("★★★★★★★★★★★★★★★★★★★");
    for (int i = 0; i < params.length; i++) {
      Object value = params[i];
      if (value instanceof java.sql.Timestamp) {
        preparedStatement.setTimestamp(i + 1, (java.sql.Timestamp) value);
      } else if (value instanceof java.sql.Date) {
        preparedStatement.setDate(i + 1, (java.sql.Date) value);
      } else if (value instanceof Boolean) {
        preparedStatement.setBoolean(i + 1, (Boolean) value);
      } else if (value instanceof Integer) {
        preparedStatement.setInt(i + 1, (Integer) value);
      } else {
        preparedStatement.setObject(i + 1, value); // インデックスは1から始まる
      }
    }
  }

  /** トランザクションのコミット */
  public void commit() throws SQLException {
    connection.commit();
  }

  /** トランザクションのロールバック */
  public void rollback() throws SQLException {
    connection.rollback();
  }

  /** 接続を閉じるメソッド */
  public void closeConnection() {
    try {
      if (connection != null && !connection.isClosed()) {
        connection.close();
        System.out.println("Connection closed successfully.");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
