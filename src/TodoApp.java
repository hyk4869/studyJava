package src;

import java.awt.Font;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import java.util.Arrays;
import java.util.Locale;

import src.tab.CommonTab;
import src.utils.CommonFont;
import src.views.SearchTodoPannel;
import src.views.TodoListPannel;

public class TodoApp {
  public static void main(String[] args) {

    Locale.setDefault(Locale.JAPAN);

    CommonFont commonFont = new CommonFont();

    setUIFont(commonFont.commonNotoSansCJKJP(14));

    CommonTab.initializeUI();

    JFrame frame = new JFrame("Todo Application（日本語）");
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

  public static void setUIFont(Font font) {
    FontUIResource fontUIResource = new FontUIResource(font);
    UIManager.put("Label.font", fontUIResource);
    UIManager.put("Button.font", fontUIResource);
    UIManager.put("TextField.font", fontUIResource);
    UIManager.put("TextArea.font", fontUIResource);
    UIManager.put("TabbedPane.font", fontUIResource);
    UIManager.put("Panel.font", fontUIResource);
    UIManager.put("Frame.titleFont", fontUIResource);
    UIManager.put("OptionPane.messageFont", fontUIResource);
  }

}
