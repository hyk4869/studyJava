package src;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import java.util.Arrays;
import src.tab.CommonTab;
import src.views.SearchTodoPannel;
import src.views.TodoListPannel;

public class TodoApp {
  public static void main(String[] args) {
    CommonTab.initializeUI();

    JFrame frame = new JFrame("Todo Application");
    frame.setSize(800, 600);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLocationRelativeTo(null);

    JTabbedPane tabbedPane = new JTabbedPane();

    TodoListPannel todoListPannel = new TodoListPannel();
    todoListPannel.GenerateTodoListTab(tabbedPane);

    SearchTodoPannel searchTodoPannel = new SearchTodoPannel();
    searchTodoPannel.GenerateSearchTodoTab(tabbedPane);

    frame.add(tabbedPane);

    CommonTab.addFocusListener(frame, Arrays.asList());

    frame.setVisible(true);
  }
}
