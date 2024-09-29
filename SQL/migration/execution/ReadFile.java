package SQL.migration.execution;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import src.dataBase.PostgreSQLConnection;

public class ReadFile {
  public PostgreSQLConnection dbConnection;

  public ReadFile(PostgreSQLConnection dbConnection) {
    this.dbConnection = dbConnection;
  }

  /**
   * 指定された .sql ファイルを読み込み、SQL文を実行する
   */
  public void executeSQLFromFile(String filePath) throws SQLException, IOException {
    Connection connection = dbConnection.getConnection();
    try (Statement statement = connection.createStatement();
        BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

      StringBuilder sqlBuilder = new StringBuilder();
      String line;

      System.out.println(reader);

      while ((line = reader.readLine()) != null) {
        // コメント行や空行は無視
        line = line.trim();
        if (line.isEmpty() || line.startsWith("--")) {
          continue;
        }
        sqlBuilder.append(line);

        // セミコロン (;) でSQL文の終わりを検出
        if (line.endsWith(";")) {
          String sql = sqlBuilder.toString();
          System.out.println("⚡ Executing SQL: " + sql);
          statement.executeUpdate(sql);
          sqlBuilder.setLength(0);
        }
      }

      dbConnection.commit();
    } catch (SQLException | IOException e) {
      e.printStackTrace();
      dbConnection.rollback();
      throw e;
    }
  }
}
