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
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    String title = (String) commonTab.getFieldValue("title");
    String description = (String) commonTab.getFieldValue("description");
    String createdByName = (String) commonTab.getFieldValue("createdByName");
    String updatedByName = (String) commonTab.getFieldValue("updatedByName");
    Timestamp createdAt = parseTimestamp((String) commonTab.getFieldValue("createdAt"));
    Timestamp updatedAt = parseTimestamp((String) commonTab.getFieldValue("updatedAt"));
    Timestamp deletedAt = parseTimestamp((String) commonTab.getFieldValue("deletedAt"));
    Boolean isCompleted = (Boolean) commonTab.getFieldValue("isCompleted");
    // Integer sort = (Integer) commonTab.getFieldValue("sort");
    Object sortValue = commonTab.getFieldValue("sort");
    Integer sort = null;
    if (sortValue != null && sortValue instanceof Integer) {
      sort = (Integer) sortValue;
    }

    if (title != null && !title.isEmpty())
      searchParams.put("title", title);
    if (description != null && !description.isEmpty())
      searchParams.put("description", description);
    if (createdByName != null && !createdByName.isEmpty())
      searchParams.put("createdByName", createdByName);
    if (updatedByName != null && !updatedByName.isEmpty())
      searchParams.put("updatedByName", updatedByName);
    if (createdAt != null)
      searchParams.put("createdAt", createdAt);
    if (updatedAt != null)
      searchParams.put("updatedAt", updatedAt);
    if (deletedAt != null)
      searchParams.put("deletedAt", deletedAt);
    if (isCompleted != null)
      searchParams.put("isCompleted", isCompleted);
    if (sort != null)
      searchParams.put("sort", sort);

    try {
      System.out.println(searchParams);
      ResultSet rs = searchTodoQuery.searchTodoList(searchParams);
      commonTable.loadAllTodoItems(rs);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private Timestamp parseTimestamp(String dateString) {
    if (dateString == null || dateString.trim().isEmpty()) {
      return null;
    }

    try {
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      java.util.Date parsedDate = dateFormat.parse(dateString);
      return new Timestamp(parsedDate.getTime());
    } catch (ParseException e) {
      e.printStackTrace();
      return null;
    }
  }

}
