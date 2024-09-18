package src.views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import src.components.CustomButton;
import src.components.enums.TextFieldStyle;
import src.tab.CommonTab;

public class TodoListPannel implements ActionListener {

  @Override
  public void actionPerformed(ActionEvent e) {
    // ボタンがクリックされたときの処理
    System.out.println("click add todo button");
  }

  public void GenerateTodoListTab(JTabbedPane tabbedPane) {
    // String[] labelTexts = { "Title", "Description" };

    Map<String, String> fieldConfigs = new HashMap<>();
    fieldConfigs.put("Title", "text");
    fieldConfigs.put("Description", "text");

    CommonTab commonTab = new CommonTab();

    // 1列でテキストフィールドを配置
    JPanel innerPanel = commonTab.addTab(tabbedPane, "Add Todo", fieldConfigs, TextFieldStyle.STANDARD, 1);

    // CustomButtonを使ってボタンを追加
    CustomButton customButton = new CustomButton();
    customButton.addButton(innerPanel, "Add Todo", 0, fieldConfigs.size(), 1, this);
  }
}
