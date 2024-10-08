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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import src.components.enums.TextFieldStyle;
import src.components.parts.CustomButton;
import src.components.table.CommonTable;
import src.components.table.columns.TableColumnName;
import src.dataBase.PostgreSQLConnection;
import src.dataBase.query.TodoListPannelQuery;
import src.tab.footer.FooterButtons;
import src.views.common.CommonViewPannel;

public class TodoListPannel extends CommonViewPannel implements ActionListener {

  private PostgreSQLConnection connection;
  private TodoListPannelQuery todoQuery;
  /** テーブルの編集 */
  private boolean isEditable = false;
  private HashSet<Integer> modifiedRows = new HashSet<>();
  private TableColumnName tableColumns = new TableColumnName();
  private CustomButton addTodoButton = new CustomButton();
  public FooterButtons footerButtons = new FooterButtons();

  public CustomButton saveButton;
  public CustomButton reloadButton;
  public CustomButton deleteButton;
  public CustomButton editButton;

  private Runnable reloadColumns = () -> commonTable.overrideEachColumns.reloadOverridedColumn(
      Arrays.asList("isCompleted", "sort"), Arrays.asList("id"));

  /** テーブルのデータモデルに変更があった場合の変更をキャッチ */
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

  /** mainメソッドにTodoListタブとその中身を生成 */
  public void GenerateTodoListTab(JTabbedPane tabbedPane) {

    // innerPanelの作成
    JPanel innerPanel = commonTab.createInnerPanel("Add Your Todo", fieldLabel.TODO_FIELD_CONFIGS,
        fieldLabel.TODO_FIELD_LABELS,
        TextFieldStyle.STANDARD, 1);

    Map<String, String> newTodoListColumn = tableColumns.pickColumns(tableColumns.TODO_LIST_COLUMNS,
        Arrays.asList("id", "title", "description", "isCompleted", "updatedAt", "sort"));

    String[] notEditProperties = { "updatedAt" };

    commonTable = new CommonTable(isEditable, newTodoListColumn, tableColumns.TODO_LIST_COLUMN_LABELS,
        notEditProperties);

    commonTable.getTableModel().addTableModelListener(tableModelListener);

    /** メインパネルの作成 */
    JPanel mainPanel = new JPanel(new BorderLayout());
    /** 入力部分の作成 */
    JPanel formPanel = new JPanel(new GridBagLayout());
    /** テーブルのパネル作成 */
    JPanel tablePanel = commonTable.createTablePanel("Todo List");
    /** ボタンのパネル作成 */
    JPanel buttonPanel = new JPanel();

    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.gridwidth = 2;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1.0;
    gbc.insets = new Insets(10, 0, 0, 0);

    formPanel.add(innerPanel, gbc);

    addTodoButton.addButton("Add Todo to List", gbc.gridx, gbc.gridy, gbc.gridwidth, this,
        commonColor.commonMUIBlue());
    gbc.fill = GridBagConstraints.NONE;
    gbc.anchor = GridBagConstraints.EAST;
    innerPanel.add(addTodoButton, gbc);

    // FooterButton
    saveButton = footerButtons.generateFooterButton("Save", e -> saveModifiedRows(), false,
        commonColor.commonMUIBlue());

    reloadButton = footerButtons.generateFooterButton("Reload", e -> {
      try {
        ResultSet resultSet = todoQuery.getAllTodoItems();
        commonTable.tableData.loadItems(resultSet);
      } catch (SQLException ex) {
        ex.printStackTrace();
      }
    }, true, commonColor.commonMUIBlue());

    deleteButton = footerButtons.generateFooterButton("Delete", e -> {
      List<String> idsToDelete = commonTable.deleteColumn.deleteSelectedRows();
      if (!idsToDelete.isEmpty()) {
        deleteFromDatabase(idsToDelete);
      }
    }, false, commonColor.commonMUIRed());

    editButton = footerButtons.generateFooterButton("Edit", e -> changeEdit(), true, commonColor.commonMUIBlue());

    buttonPanel.add(saveButton);
    buttonPanel.add(editButton);
    buttonPanel.add(deleteButton);
    buttonPanel.add(reloadButton);

    tablePanel.add(buttonPanel, BorderLayout.SOUTH);

    // メインパネルにフォームとテーブルを追加
    mainPanel.add(formPanel, BorderLayout.NORTH);
    mainPanel.add(tablePanel, BorderLayout.CENTER);

    reloadColumns.run();

    // メインパネルをタブに追加
    tabbedPane.addTab("Add Todo", mainPanel);

    try {
      ResultSet resultSet = todoQuery.getAllTodoItems();
      commonTable.tableData.loadItems(resultSet);
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  /** Editの切り替え */
  public void changeEdit() {
    isEditable = !isEditable;
    editButton.setText(isEditable ? "Cancel" : "Edit");
    saveButton.setVisible(isEditable);
    deleteButton.setVisible(isEditable);
    reloadButton.setEnabled(!isEditable);

    commonTable.setEditable(isEditable);
    reloadColumns.run();
    commonTable.getTableModel().removeTableModelListener(tableModelListener);
    commonTable.getTableModel().addTableModelListener(tableModelListener);
  }

  /** DBから削除 */
  private void deleteFromDatabase(List<String> idsToDelete) {
    try {
      todoQuery.deleteTodoItemsByIds(idsToDelete.toArray(new String[0]));

      commonTable.tableData.loadItems(todoQuery.getAllTodoItems());

      connection.commit();
      System.out.println("Selected rows deleted successfully.");
    } catch (SQLException ex) {
      try {
        connection.rollback();
      } catch (SQLException rollbackEx) {
        rollbackEx.printStackTrace();
      }
      ex.printStackTrace();
    } finally {
      changeEdit();
    }
  }

  /** DB更新処理 */
  private void saveModifiedRows() {
    if (commonTable.getTable().getCellEditor() != null) {
      commonTable.getTable().getCellEditor().stopCellEditing();
    }

    try {
      ArrayList<Integer> validModifiedRows = new ArrayList<>();

      // 有効な行インデックスのみを収集
      for (Integer rowIndex : modifiedRows) {
        if (rowIndex >= 0 && rowIndex < commonTable.getRowCount()) {
          validModifiedRows.add(rowIndex);
        }
      }

      for (Integer rowIndex : validModifiedRows) {
        int modelRowIndex = commonTable.convertRowIndexToModel(rowIndex);

        Map<String, Object> updatedValues = new HashMap<>();
        String id = (String) commonTable.getTableModel().getValueAt(modelRowIndex,
            commonTable.getColumnIndexByName("id"));

        // 更新するカラムと値を動的にMapに追加
        updatedValues.put("id", id);
        updatedValues.put("title", commonTable.getTableModel().getValueAt(modelRowIndex,
            commonTable.getColumnIndexByName("title")));
        updatedValues.put("description", commonTable.getTableModel().getValueAt(modelRowIndex,
            commonTable.getColumnIndexByName("description")));
        updatedValues.put("isCompleted", commonTable.getTableModel().getValueAt(modelRowIndex,
            commonTable.getColumnIndexByName("isCompleted")));
        updatedValues.put("sort",
            commonTable.getTableModel().getValueAt(modelRowIndex,
                commonTable.getColumnIndexByName("sort")));

        // タイトルの重複チェック
        if (todoQuery.isTitleDuplicated((String) updatedValues.get("title"), id)) {
          throw new SQLException(
              "Error: Title '" + updatedValues.get("title") + "' is duplicated. Row " + rowIndex + " cannot be saved.");
        }

        // 更新処理を呼び出し
        todoQuery.updateTodoItem(updatedValues);
      }

      commonTable.tableData.loadItems(todoQuery.getAllTodoItems());
      connection.commit();
      modifiedRows.clear();

    } catch (SQLException ex) {
      try {
        connection.rollback();
        System.err.println("Transaction rolled back due to: " + ex.getMessage());
      } catch (SQLException rollbackEx) {
        rollbackEx.printStackTrace();
      }
      ex.printStackTrace();
    } finally {
      changeEdit();
    }
  }

  /** DBに追加 */
  @Override
  public void actionPerformed(ActionEvent e) {
    Map<String, Object> newTodoItem = new HashMap<>();

    Object title = commonTab.getFieldValue("title");
    Object description = commonTab.getFieldValue("description");
    Object isCompleted = commonTab.getFieldValue("isCompleted");

    if (title == null || title.toString().trim().isEmpty()) {
      throw new IllegalArgumentException("タイトルが空です");
      // javax.swing.JOptionPane.showMessageDialog(null, "Title cannot be empty",
      // "Input Error",
      // javax.swing.JOptionPane.ERROR_MESSAGE);
      // return;
    }

    int maxSort = 0;

    try {
      ResultSet resultSet = todoQuery.getAllTodoItems();
      List<Integer> sortValues = new ArrayList<>();

      while (resultSet.next()) {
        Integer sortValue = (Integer) resultSet.getObject("sort");
        if (sortValue != null) {
          sortValues.add(sortValue);
        }
      }

      if (!sortValues.isEmpty()) {
        maxSort = sortValues.stream().max(Integer::compareTo).orElse(0) + 1;
      } else {
        maxSort = 1;
      }

    } catch (SQLException ex) {
      ex.printStackTrace();
    }

    newTodoItem.put("id", java.util.UUID.randomUUID().toString());
    newTodoItem.put("createdById", "ce3b1d98-2ed8-4a02-a5c3-9e8f9e5c20f7");
    newTodoItem.put("updatedById", "ce3b1d98-2ed8-4a02-a5c3-9e8f9e5c20f7");
    newTodoItem.put("createdByName", "CreatedUser");
    newTodoItem.put("updatedByName", "CreatedUser");
    newTodoItem.put("createdAt", new Timestamp(System.currentTimeMillis()));
    newTodoItem.put("updatedAt", new Timestamp(System.currentTimeMillis()));
    newTodoItem.put("title", title);
    newTodoItem.put("description", description);
    newTodoItem.put("isCompleted", isCompleted);
    newTodoItem.put("sort", maxSort);

    try {
      // タイトルの重複チェック
      if (todoQuery.isTitleDuplicated((String) title, (String) newTodoItem.get("id"))) {
        throw new SQLException("Error: Title '" + title + "' is duplicated. Insertion aborted.");
      }

      // Todo項目を挿入
      todoQuery.insertTodoItem(newTodoItem);

      commonTable.tableData.loadItems(todoQuery.getAllTodoItems());

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
