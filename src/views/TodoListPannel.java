package src.views;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import src.tab.CommonTab;

public class TodoListPannel implements ActionListener {

  private CommonTable commonTable;
  private CommonTab commonTab = new CommonTab();

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
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String title = (String) commonTab.getFieldValue("title");
    String description = (String) commonTab.getFieldValue("description");
    Boolean isCompleted = (Boolean) commonTab.getFieldValue("isCompleted");

    // テーブルに追加
    ArrayList<Object> rowData = new ArrayList<>();
    rowData.add(title);
    rowData.add(description);
    rowData.add("");
    rowData.add("");
    rowData.add("");
    rowData.add("");
    rowData.add(isCompleted);
    rowData.add("");

    commonTable.addRow(rowData);

    // テキストフィールドをクリア
    commonTab.setFieldValue("title", "");
    commonTab.setFieldValue("description", "");
    commonTab.setFieldValue("isCompleted", false);

  }
}
