package SQL.migration.execution;

import src.dataBase.PostgreSQLConnection;

public class Migration {

  public PostgreSQLConnection dbConnection;
  public Drop drop;
  public ReadFile readFile;

  public Migration() {
    dbConnection = new PostgreSQLConnection();
    this.drop = new Drop(dbConnection);
    this.readFile = new ReadFile(dbConnection);
  }

}
