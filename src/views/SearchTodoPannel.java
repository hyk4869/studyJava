package src.views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import src.components.enums.TextFieldStyle;
import src.components.parts.CustomButton;
import src.dataBase.PostgreSQLConnection;
import src.tab.CommonTab;
import src.utils.CommonColor;

public class SearchTodoPannel {

  private PostgreSQLConnection connection;
  private CommonColor commonColor = new CommonColor();

  public SearchTodoPannel(PostgreSQLConnection connection) {
    this.connection = connection;
  }

  /** mainメソッドにSearchTodoタブとその中身を生成 */
  public void GenerateSearchTodoTab(JTabbedPane tabbedPane) {
    Map<String, String> fieldConfigs = new LinkedHashMap<>();
    fieldConfigs.put("title", "text");
    fieldConfigs.put("description", "text");
    fieldConfigs.put("createdByName", "text");
    fieldConfigs.put("updatedByName", "text");
    fieldConfigs.put("createdAt", "date");
    fieldConfigs.put("updatedAt", "date");
    fieldConfigs.put("isCompleted", "check");
    fieldConfigs.put("sort", "numeric");

    CommonTab commonTab = new CommonTab();

    JPanel innerPanel = commonTab.addTab(tabbedPane, "Search Todo", fieldConfigs, TextFieldStyle.STANDARD, 3, 1);

    // CustomButtonを使ってボタンを追加
    CustomButton customButton = new CustomButton();
    customButton.addButton("Search Todo", 2, (fieldConfigs.size() + 2) / 3, 1, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // JOptionPane.showMessageDialog(null, "Search button clicked!");
        System.out.println("click search button");
      };
    }, commonColor.commonBlue());

  }
}
