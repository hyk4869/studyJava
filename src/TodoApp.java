package src;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.Locale;

import src.dataBase.PostgreSQLConnection;
import src.tab.CommonTab;
import src.views.SearchTodoPannel;
import src.views.TodoListPannel;

public class TodoApp {
  public static void main(String[] args) {

    Locale.setDefault(Locale.JAPAN);

    CommonTab.initializeUI();

    JFrame frame = new JFrame("Todo Application");
    frame.setSize(1000, 800);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLocationRelativeTo(null);

    JTabbedPane tabbedPane = new JTabbedPane();

    PostgreSQLConnection connection = new PostgreSQLConnection();

    TodoListPannel todoListPannel = new TodoListPannel(connection);
    todoListPannel.GenerateTodoListTab(tabbedPane);

    SearchTodoPannel searchTodoPannel = new SearchTodoPannel(connection);
    searchTodoPannel.GenerateSearchTodoTab(tabbedPane);

    frame.add(tabbedPane);

    CommonTab.addFocusListener(frame, Arrays.asList());

    // DBとの接続を切る
    frame.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        connection.closeConnection();
        System.out.println("Connection closed successfully.");
      }
    });

    frame.setVisible(true);
  }

}
