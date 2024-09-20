package src.views;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import src.components.CustomButton;
import src.components.enums.TextFieldStyle;
import src.components.table.CommonTable;
import src.tab.CommonTab;

public class TodoListPannel implements ActionListener {

  private CommonTable commonTable;
  private CommonTab commonTab = new CommonTab();

  /** mainメソッドにTodoListタブとその中身を生成 */
  public void GenerateTodoListTab(JTabbedPane tabbedPane) {
    Map<String, String> fieldConfigs = new LinkedHashMap<>();
    fieldConfigs.put("title", "text");
    fieldConfigs.put("description", "text");

    commonTable = new CommonTable(new Object[] { "title", "description", "createdByName", "updatedByName",
        "createdAt", "updatedAt", "isCompleted", "sort" });

    // innerPanelの作成
    JPanel innerPanel = commonTab.createInnerPanel("Add Your Todo", fieldConfigs, TextFieldStyle.STANDARD, 1);

    // formPanelの作成
    JPanel formPanel = new JPanel(new java.awt.GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1.0;

    formPanel.add(innerPanel, gbc);

    // CustomButtonを使ってボタンを追加
    CustomButton customButton = new CustomButton();
    customButton.addButton(formPanel, "Add Todo", 0, 1, 2, this);

    // メインパネルの作成
    JPanel mainPanel = new JPanel(new BorderLayout());

    JPanel tablePanel = commonTable.createTablePanel("Todo List");

    // メインパネルにフォームとテーブルを追加
    mainPanel.add(formPanel, BorderLayout.NORTH); // フォームを上部に配置
    mainPanel.add(tablePanel, BorderLayout.CENTER); // テーブルを枠で囲んで下部に配置

    // メインパネルをタブに追加
    tabbedPane.addTab("Add Todo", mainPanel);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String title = commonTab.getFieldValue("title");
    String description = commonTab.getFieldValue("description");

    // テーブルに追加
    commonTable.addRow(new Object[] { title, description });

    // テキストフィールドをクリア
    commonTab.setFieldValue("title", "");
    commonTab.setFieldValue("description", "");
  }
}
