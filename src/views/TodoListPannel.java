package src.views;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

import src.components.CustomButton;
import src.components.enums.TextFieldStyle;
import src.components.table.CommonTable;
import src.components.table.override.checkbox.CheckBoxEditor;
import src.components.table.override.checkbox.CheckBoxRenderer;
import src.dataBase.PostgreSQLConnection;
import src.dataBase.query.TodoListPannelQuery;
import src.tab.CommonTab;

public class TodoListPannel implements ActionListener {

  private CommonTable commonTable;
  private CommonTab commonTab = new CommonTab();
  private PostgreSQLConnection connection;
  private TodoListPannelQuery todoQuery;

  public TodoListPannel(PostgreSQLConnection connection) {
    this.todoQuery = new TodoListPannelQuery(connection);
    this.connection = connection;
  }

  /** mainメソッドにTodoListタブとその中身を生成 */
  public void GenerateTodoListTab(JTabbedPane tabbedPane) {
    Map<String, String> fieldConfigs = new LinkedHashMap<>();
    fieldConfigs.put("title", "text");
    fieldConfigs.put("description", "text");
    fieldConfigs.put("isCompleted", "check");

    commonTable = new CommonTable(new Object[] { "title", "description", "createdByName", "updatedByName",
        "createdAt", "updatedAt", "isCompleted", "sort" });

    // innerPanelの作成
    JPanel innerPanel = commonTab.createInnerPanel("Add Your Todo", fieldConfigs, TextFieldStyle.STANDARD, 1);

    // formPanelの作成
    JPanel formPanel = new JPanel(new java.awt.GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.gridwidth = 2;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1.0;
    gbc.insets = new java.awt.Insets(10, 0, 0, 0);

    formPanel.add(innerPanel, gbc);

    // CustomButtonを使ってボタンを追加
    CustomButton customButton = new CustomButton();
    customButton.addButton(innerPanel, "Add Todo", gbc.gridx, gbc.gridy, gbc.gridwidth, this);

    // メインパネルの作成
    JPanel mainPanel = new JPanel(new BorderLayout());

    JPanel tablePanel = commonTable.createTablePanel("Todo List");

    // メインパネルにフォームとテーブルを追加
    mainPanel.add(formPanel, BorderLayout.NORTH); // フォームを上部に配置
    mainPanel.add(tablePanel, BorderLayout.CENTER); // テーブルを枠で囲んで下部に配置

    JTable table = commonTable.getTable();
    TableColumn isCompletedColumn = table.getColumnModel().getColumn(6);
    isCompletedColumn.setCellRenderer(new CheckBoxRenderer());
    isCompletedColumn.setCellEditor(new CheckBoxEditor(new JCheckBox()));

    // メインパネルをタブに追加
    tabbedPane.addTab("Add Todo", mainPanel);
    loadAllTodoItems();
  }

  /** T_TodoListの全データをロードしてテーブルに追加 */
  private void loadAllTodoItems() {
    try {
      ResultSet resultSet = todoQuery.getAllTodoItems();

      while (resultSet.next()) {
        addRowToTable(resultSet);
      }

      // リソース解放
      resultSet.close();
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  /** ResultSetからデータを取得してテーブルに行を追加 */
  private void addRowToTable(ResultSet resultSet) throws SQLException {
    ArrayList<Object> rowData = new ArrayList<>();
    rowData.add(resultSet.getString("title"));
    rowData.add(resultSet.getString("description"));
    rowData.add(resultSet.getString("createdByName"));
    rowData.add(resultSet.getString("updatedByName"));
    rowData.add(resultSet.getTimestamp("createdAt"));
    rowData.add(resultSet.getTimestamp("updatedAt"));
    rowData.add(resultSet.getBoolean("isCompleted"));
    rowData.add(resultSet.getInt("sort"));

    // テーブルに追加
    commonTable.addRow(rowData);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String title = (String) commonTab.getFieldValue("title");
    String description = (String) commonTab.getFieldValue("description");
    Boolean isCompleted = (Boolean) commonTab.getFieldValue("isCompleted");

    // idやその他の必要なフィールドを生成
    String id = java.util.UUID.randomUUID().toString(); // 一意のIDを生成
    String createdById = "ce3b1d98-2ed8-4a02-a5c3-9e8f9e5c20f7"; // 実際のユーザーIDを設定
    String updatedById = createdById;
    String createdByName = "CreatedUser"; // 実際のユーザー名
    String updatedByName = createdByName;
    Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

    try {
      if (!todoQuery.isTitleDuplicated(title)) {
        todoQuery.insertTodoItem(id, createdById, createdByName, updatedById, updatedByName, currentTimestamp,
            currentTimestamp, title, description, isCompleted);

        // データを取得してテーブルに追加
        ResultSet resultSet = todoQuery.getTodoItemById(id);

        if (resultSet.next()) {
          addRowToTable(resultSet);
        }

        connection.commit();

        // リソース解放
        resultSet.close();
      } else {
        System.out.println("Title already exists, no insertion.");
        connection.rollback();
      }
    } catch (SQLException ex) {
      try {
        connection.rollback();
      } catch (SQLException rollbackEx) {
        rollbackEx.printStackTrace();
      }
      ex.printStackTrace();
    }

    // テキストフィールドをクリア
    commonTab.setFieldValue("title", "");
    commonTab.setFieldValue("description", "");
    commonTab.setFieldValue("isCompleted", false);
  }

}
