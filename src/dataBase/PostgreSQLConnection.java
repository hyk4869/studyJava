package src.dataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PostgreSQLConnection {
  private Connection connection;

  public PostgreSQLConnection() {
    String url = "jdbc:postgresql://postgres:5432/sampledb";
    String user = "user";
    String password = "password";

    try {
      connection = DriverManager.getConnection(url, user, password);
      System.out.println("Connection established successfully.");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /** SELECT文をPreparedStatementで実行 */
  public ResultSet executeQuery(String query, Object... params) throws SQLException {
    PreparedStatement preparedStatement = connection.prepareStatement(query);
    setParameters(preparedStatement, params);
    return preparedStatement.executeQuery();
  }

  /** INSERT, UPDATE, DELETE 文をPreparedStatementで実行 */
  public int executeUpdate(String query, Object... params) throws SQLException {
    PreparedStatement preparedStatement = connection.prepareStatement(query);
    setParameters(preparedStatement, params);
    int result = preparedStatement.executeUpdate();
    System.out.println("Update successful. Rows affected: " + result);
    return result;
  }

  /** PreparedStatement にパラメータをセットするヘルパーメソッド */
  private void setParameters(PreparedStatement preparedStatement, Object... params) throws SQLException {
    for (int i = 0; i < params.length; i++) {
      preparedStatement.setObject(i + 1, params[i]); // インデックスは1から始まる
    }
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
