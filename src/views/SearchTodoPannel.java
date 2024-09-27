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
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import src.components.enums.TextFieldStyle;
import src.components.labels.FieldLabel;
import src.components.parts.CustomButton;
import src.components.table.CommonTable;
import src.components.table.columns.TableColumns;
import src.dataBase.PostgreSQLConnection;
import src.dataBase.query.SearchTodoPannelQuery;
import src.tab.CommonTab;
import src.utils.CommonColor;

public class SearchTodoPannel {

  private PostgreSQLConnection connection;
  private SearchTodoPannelQuery searchTodoQuery;

  private CommonTable commonTable;
  private TableColumns tableColumns = new TableColumns();
  private CommonColor commonColor = new CommonColor();
  private FieldLabel fieldLabel = new FieldLabel();
  private CommonTab commonTab;

  public SearchTodoPannel(PostgreSQLConnection connection) {
    this.searchTodoQuery = new SearchTodoPannelQuery(connection);
    this.connection = connection;
  }

  /** mainメソッドにSearchTodoタブとその中身を生成 */
  public void GenerateSearchTodoTab(JTabbedPane tabbedPane) {
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 3;
    gbc.gridwidth = 1;
    gbc.fill = GridBagConstraints.NONE;
    gbc.anchor = GridBagConstraints.EAST;
    gbc.weightx = 1.0;
    gbc.insets = new Insets(10, 0, 0, 0);

    commonTab = new CommonTab();

    commonTable = new CommonTable(tableColumns.TODO_LIST_COLUMNS, false, tableColumns.TODO_LIST_COLUMN_LABELS);

    /** メインパネルの作成 */
    JPanel mainPanel = new JPanel(new BorderLayout());
    /** 入力部分の作成 */
    JPanel formPanel = new JPanel(new GridBagLayout());
    /** テーブルのパネル作成 */
    JPanel tablePanel = commonTable.createTablePanel("List");

    JPanel innerPanel = commonTab.createInnerPanel("Search Todo", fieldLabel.SEARCH_FIELD_CONFIG,
        fieldLabel.SEARCH_FIELD_LABELS,
        TextFieldStyle.STANDARD, 3);

    CustomButton customButton = new CustomButton();
    customButton.addButton("Search Todo", 2, (fieldLabel.SEARCH_FIELD_CONFIG.size() + 2) / 3, 1, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        getContent();
      };
    }, commonColor.commonMUIBlue());

    innerPanel.add(customButton, gbc);
    gbc.fill = GridBagConstraints.HORIZONTAL;
    formPanel.add(innerPanel, gbc);
    mainPanel.add(formPanel, BorderLayout.NORTH);
    mainPanel.add(tablePanel, BorderLayout.CENTER);

    tabbedPane.addTab("Search Todo", mainPanel);
  }

  public void getContent() {
    Map<String, Object> searchParams = new HashMap<>();

    Object title = commonTab.getFieldValue("title");
    Object description = commonTab.getFieldValue("description");
    Object createdByName = commonTab.getFieldValue("createdByName");
    Object updatedByName = commonTab.getFieldValue("updatedByName");
    Object createdAt = commonTab.getFieldValue("createdAt");
    Object updatedAt = commonTab.getFieldValue("updatedAt");
    Object deletedAt = commonTab.getFieldValue("deletedAt");
    Object isCompleted = commonTab.getFieldValue("isCompleted");
    Object sortValue = commonTab.getFieldValue("sort");

    // 条件に応じて検索パラメータを追加
    if (title instanceof String && !((String) title).isEmpty()) {
      searchParams.put("title", title);
    }
    if (description instanceof String && !((String) description).isEmpty()) {
      searchParams.put("description", description);
    }
    if (createdByName instanceof String && !((String) createdByName).isEmpty()) {
      searchParams.put("createdByName", createdByName);
    }
    if (updatedByName instanceof String && !((String) updatedByName).isEmpty()) {
      searchParams.put("updatedByName", updatedByName);
    }
    if (createdAt instanceof Timestamp) {
      searchParams.put("createdAt", createdAt);
    }
    if (updatedAt instanceof Timestamp) {
      searchParams.put("updatedAt", updatedAt);
    }
    if (deletedAt instanceof Timestamp) {
      searchParams.put("deletedAt", deletedAt);
    }
    if (isCompleted instanceof Boolean) {
      searchParams.put("isCompleted", isCompleted);
    }
    if (sortValue instanceof Integer) {
      searchParams.put("sort", sortValue);
    }

    try {
      ResultSet rs = searchTodoQuery.searchTodoList(searchParams);
      commonTable.loadAllTodoItems(rs);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}
