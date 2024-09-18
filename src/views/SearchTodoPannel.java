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

public class SearchTodoPannel {

  public void GenerateSearchTodoTab(JTabbedPane tabbedPane) {
    // String[] labelTexts = { "Title", "Description", "createdByName",
    // "updatedByName", "createdAt", "updatedAt",
    // "isCompleted", "sort" };

    Map<String, String> fieldConfigs = new HashMap<>();
    fieldConfigs.put("Title", "text");
    fieldConfigs.put("Description", "text");
    fieldConfigs.put("createdByName", "text");
    fieldConfigs.put("updatedByName", "text");
    fieldConfigs.put("createdAt", "date");
    fieldConfigs.put("updatedAt", "date");
    fieldConfigs.put("isCompleted", "text");
    fieldConfigs.put("sort", "numeric");

    CommonTab commonTab = new CommonTab();

    // commonTab.addTab(tabbedPane, "Search Todo", labelTexts,
    // TextFieldStyle.STANDARD, "Search Todo");

    JPanel innerPanel = commonTab.addTab(tabbedPane, "Search Todo", fieldConfigs, TextFieldStyle.STANDARD, 3);

    // CustomButtonを使ってボタンを追加
    CustomButton customButton = new CustomButton();
    customButton.addButton(innerPanel, "Search Todo", 2, (fieldConfigs.size() + 2) / 3, 1, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // JOptionPane.showMessageDialog(null, "Search button clicked!");
        System.out.println("click search button");
      };
    });

  }
}
