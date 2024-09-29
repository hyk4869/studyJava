package SQL.migration.execution;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import src.dataBase.PostgreSQLConnection;

public class Drop {
  private PostgreSQLConnection dbConnection;

  public Drop(PostgreSQLConnection dbConnection) {
    this.dbConnection = dbConnection;
  }

  /**
   * 全てのテーブルを削除する
   */
  public void dropAllTables() throws SQLException {
    Connection connection = dbConnection.getConnection();
    try (Statement statement = connection.createStatement()) {
      // テーブル削除の順番を考慮（依存関係に基づいて削除）
      statement.executeUpdate("DROP TABLE IF EXISTS \"T_TodoCategory\" CASCADE;");
      statement.executeUpdate("DROP TABLE IF EXISTS \"T_TodoList\" CASCADE;");
      statement.executeUpdate("DROP TABLE IF EXISTS \"T_Category\" CASCADE;");
      statement.executeUpdate("DROP TABLE IF EXISTS \"User\" CASCADE;");

      System.out.println("🗑️ テーブルの削除に成功しました 🗑️");
      System.out.println("★ ★ ★ ★ ★ ★ ★ ★ ★ ★ ★ ");

      dbConnection.commit();
    } catch (SQLException e) {
      e.printStackTrace();
      dbConnection.rollback();
      throw e;
    }
  }
}
