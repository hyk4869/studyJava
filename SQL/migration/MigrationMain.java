package SQL.migration;

import java.io.IOException;
import java.sql.SQLException;

import SQL.migration.execution.Migration;

public class MigrationMain {

  public static void main(String[] args) {

    Migration migration = new Migration();

    try {
      // 全てのテーブルを削除
      migration.drop.dropAllTables();

      // テーブル作成用のSQLファイルを依存関係順に実行
      migration.readFile.executeSQLFromFile("SQL/createTable/user.sql");
      migration.readFile.executeSQLFromFile("SQL/createTable/category.sql");
      migration.readFile.executeSQLFromFile("SQL/createTable/todo.sql");
      migration.readFile.executeSQLFromFile("SQL/createTable/todoCategory.sql");

      System.out.println("✅ 全てのテーブルの作成に成功しました ✅");
      System.out.println("★ ★ ★ ★ ★ ★ ★ ★ ★ ★ ★ ");

      migration.readFile.executeSQLFromFile("SQL/insertData/user.sql");

      System.out.println("✅ データの作成に成功しました ✅");
      System.out.println("★ ★ ★ ★ ★ ★ ★ ★ ★ ★ ★ ");

    } catch (SQLException | IOException e) {
      e.printStackTrace();
      try {
        migration.dbConnection.rollback(); // エラーが発生した場合にロールバック
      } catch (SQLException rollbackEx) {
        rollbackEx.printStackTrace();
      }
    } finally {
      // データベース接続を閉じる
      migration.dbConnection.closeConnection();
    }
  }
}
