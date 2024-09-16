package src;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import src.components.CustomTextField;
import src.components.CustomTextField.TextFieldStyle;

public class TodoApp {
  public static void main(String[] args) {
    // タブの高さを変更
    UIManager.put("TabbedPane.tabInsets", new javax.swing.plaf.InsetsUIResource(10, 20, 10, 20)); // マージンの設定
    UIManager.put("TabbedPane.font", new FontUIResource(new Font("Arial", Font.BOLD, 16))); // フォントの設定

    JFrame frame = new JFrame("ToDo Application");

    frame.setSize(800, 600);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLocationRelativeTo(null);

    JTabbedPane tabbedPane = new JTabbedPane();

    // タブ1の内容
    JPanel panel1 = new JPanel();
    JTextField todoInput = new CustomTextField(20, TextFieldStyle.OUTLINED);
    JButton addButton = new JButton("Add ToDo");
    panel1.add(todoInput);
    panel1.add(addButton);
    tabbedPane.addTab("Add ToDo", panel1); // タブを追加

    // タブ2の内容
    JPanel panel2 = new JPanel();
    JTextField searchField = new CustomTextField(20, TextFieldStyle.STANDARD);
    JButton searchButton = new JButton("Search ToDo");
    panel2.add(searchField);
    panel2.add(searchButton);
    tabbedPane.addTab("Search ToDo", panel2); // タブを追加

    // フレームにタブ付きペインを追加
    frame.add(tabbedPane);

    // フレーム全体にMouseListenerを追加
    frame.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        // テキストフィールド以外をクリックした場合にフォーカスを外す
        if (!todoInput.hasFocus()) {
          todoInput.setFocusable(false);
          frame.requestFocusInWindow(); // フレームにフォーカスを移す
          todoInput.setFocusable(true);
        }
      }
    });

    frame.setVisible(true);
  }
}
