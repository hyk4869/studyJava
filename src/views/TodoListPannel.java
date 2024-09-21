package src.views;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;
import java.util.Comparator;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
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
  /** テーブルの編集 */
  private boolean isEditable = false;
  private HashSet<Integer> modifiedRows = new HashSet<>();

  private TableModelListener tableModelListener = new TableModelListener() {
    @Override
    public void tableChanged(TableModelEvent e) {
      if (e.getType() == TableModelEvent.UPDATE) {
        int row = e.getFirstRow();
        modifiedRows.add(row);
      }
    }
  };

  public TodoListPannel(PostgreSQLConnection connection) {
    this.todoQuery = new TodoListPannelQuery(connection);
    this.connection = connection;
  }

  /** isCompletedカラムの設定を行う */
  private void configureIsCompletedColumn(JTable table) {
    TableColumn isCompletedColumn = table.getColumnModel().getColumn(7);
    isCompletedColumn.setCellRenderer(new CheckBoxRenderer());
    isCompletedColumn.setCellEditor(new CheckBoxEditor(new JCheckBox()));

    commonTable.getTable().getColumnModel().getColumn(0).setMinWidth(0);
    commonTable.getTable().getColumnModel().getColumn(0).setMaxWidth(0);
    commonTable.getTable().getColumnModel().getColumn(0).setWidth(0);
  }

  /** mainメソッドにTodoListタブとその中身を生成 */
  public void GenerateTodoListTab(JTabbedPane tabbedPane) {
    Map<String, String> fieldConfigs = new LinkedHashMap<>();
    fieldConfigs.put("title", "text");
    fieldConfigs.put("description", "text");
    fieldConfigs.put("isCompleted", "check");

    commonTable = new CommonTable(new Object[] { "id", "title", "description", "createdByName", "updatedByName",
        "createdAt", "updatedAt", "isCompleted", "sort" }, isEditable);

    commonTable.getTableModel().addTableModelListener(tableModelListener);

    // innerPanelの作成
    JPanel innerPanel = commonTab.createInnerPanel("Add Your Todo", fieldConfigs, TextFieldStyle.STANDARD, 1);
    // formPanelの作成
    JPanel formPanel = new JPanel(new GridBagLayout());

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.gridwidth = 2;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1.0;
    gbc.insets = new Insets(10, 0, 0, 0);

    formPanel.add(innerPanel, gbc);

    // CustomButtonを使ってボタンを追加
    CustomButton addTodoButton = new CustomButton();
    addTodoButton.addButton(innerPanel, "Add Todo to List", gbc.gridx, gbc.gridy, gbc.gridwidth, this);

    // メインパネルの作成
    JPanel mainPanel = new JPanel(new BorderLayout());

    JPanel tablePanel = commonTable.createTablePanel("Todo List");

    JPanel buttonPanel = new JPanel();

    CustomButton saveButton = new CustomButton();
    saveButton.setVisible(false);
    saveButton.addButton(buttonPanel, "Save", gbc.gridx, gbc.gridy, gbc.gridwidth, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        saveModifiedRows();
      }
    });

    CustomButton reloadButton = new CustomButton();
    reloadButton.addButton(buttonPanel, "Reload", gbc.gridx, gbc.gridy, gbc.gridwidth, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        commonTable.clearTable();
        loadAllTodoItems();
      }
    });

    CustomButton editButton = new CustomButton();
    editButton.addButton(buttonPanel, "Edit", gbc.gridx, gbc.gridy, gbc.gridwidth, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        isEditable = !isEditable;
        commonTable.setEditable(isEditable);
        editButton.setText(isEditable ? "Cancel" : "Edit");
        saveButton.setVisible(isEditable);
        reloadButton.setEnabled(!isEditable);
        configureIsCompletedColumn(commonTable.getTable());

        commonTable.getTableModel().removeTableModelListener(tableModelListener);
        commonTable.getTableModel().addTableModelListener(tableModelListener);
      }
    });

    buttonPanel.add(saveButton);
    buttonPanel.add(editButton);
    buttonPanel.add(reloadButton);

    tablePanel.add(buttonPanel, BorderLayout.SOUTH);

    // メインパネルにフォームとテーブルを追加
    mainPanel.add(formPanel, BorderLayout.NORTH); // フォームを上部に配置
    mainPanel.add(tablePanel, BorderLayout.CENTER); // テーブルを枠で囲んで下部に配置

    configureIsCompletedColumn(commonTable.getTable());

    // メインパネルをタブに追加
    tabbedPane.addTab("Add Todo", mainPanel);
    loadAllTodoItems();
  }

  /** ResultSetから1行分のデータを取得 */
  private ArrayList<Object> getRowData(ResultSet resultSet) throws SQLException {
    ArrayList<Object> rowData = new ArrayList<>();
    for (int i = 0; i < commonTable.getTableModel().getColumnCount(); i++) {
      rowData.add(resultSet.getObject(commonTable.getTableModel().getColumnName(i)));
    }
    return rowData;
  }

  /** T_TodoListの全データをロードしてテーブルに追加 */
  private void loadAllTodoItems() {
    try (ResultSet resultSet = todoQuery.getAllTodoItems()) {
      List<ArrayList<Object>> rows = new ArrayList<>();

      while (resultSet.next()) {
        rows.add(getRowData(resultSet)); // データをリストに追加
      }

      // updatedAtで降順にソートし、テーブルに追加
      rows.stream()
          .sorted(Comparator.comparing(row -> (Timestamp) row.get(commonTable.getTableModel().findColumn("updatedAt")),
              Comparator.reverseOrder()))
          .forEach(d -> addRowToTable(d));

    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  /** テーブルに行を追加 */
  private void addRowToTable(ArrayList<Object> rowData) {
    commonTable.addRow(rowData);
  }

  private void saveModifiedRows() {
    if (commonTable.getTable().getCellEditor() != null) {
      commonTable.getTable().getCellEditor().stopCellEditing();
    }

    try {
      for (Integer rowIndex : modifiedRows) {
        String id = (String) commonTable.getTable().getValueAt(rowIndex, 0);
        String title = (String) commonTable.getTable().getValueAt(rowIndex, 1);
        String description = (String) commonTable.getTable().getValueAt(rowIndex, 2);
        Boolean isCompleted = (Boolean) commonTable.getTable().getValueAt(rowIndex, 7);
        Integer sort = (Integer) commonTable.getTable().getValueAt(rowIndex, 8);

        // タイトルの重複チェック
        if (todoQuery.isTitleDuplicated(title, id)) {
          throw new SQLException("Error: Title '" + title + "' is duplicated. Row " + rowIndex + " cannot be saved.");
        }

        // タイトルが重複していない場合のみ更新
        todoQuery.updateTodoItem(id, title, description, isCompleted, sort);
      }
      commonTable.clearTable();

      loadAllTodoItems();

      connection.commit();
    } catch (SQLException ex) {
      try {
        connection.rollback();
        System.err.println("Transaction rolled back due to: " + ex.getMessage());
      } catch (SQLException rollbackEx) {
        rollbackEx.printStackTrace();
      }
      ex.printStackTrace();
    } finally {
      modifiedRows.clear();
    }
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
      // タイトルの重複チェック
      if (todoQuery.isTitleDuplicated(title, id)) {
        throw new SQLException("Error: Title '" + title + "' is duplicated. Insertion aborted.");
      }

      // タイトルが重複していない場合のみ挿入
      todoQuery.insertTodoItem(id, createdById, createdByName, updatedById, updatedByName, currentTimestamp,
          currentTimestamp, title, description, isCompleted);

      // テーブルをクリアして全データを再取得
      commonTable.clearTable();

      loadAllTodoItems();

      // コミット
      connection.commit();
      System.out.println("Todo item inserted and committed successfully.");

    } catch (SQLException ex) {
      try {
        connection.rollback();
        System.err.println("Transaction rolled back due to: " + ex.getMessage());
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
