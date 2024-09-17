package src.tabPannels;

import javax.swing.JTabbedPane;

import src.components.CustomTextField.TextFieldStyle;
import src.tab.CommonTab;

public class SearchTodoPannel {

  public void GenerateSearchTodoTab(JTabbedPane tabbedPane) {

    CommonTab.addTab(tabbedPane, "Search Todo", "Search", TextFieldStyle.STANDARD, "Search Todo");
  }
}
