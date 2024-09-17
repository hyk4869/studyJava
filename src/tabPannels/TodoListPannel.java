package src.tabPannels;

import javax.swing.JTabbedPane;

import src.components.CustomTextField.TextFieldStyle;
import src.tab.CommonTab;

public class TodoListPannel {

  public void GenerateTodoListTab(JTabbedPane tabbedPane) {

    CommonTab.addTab(tabbedPane, "Add Todo", "Title", TextFieldStyle.OUTLINED, "Add Todo");
  }
}
