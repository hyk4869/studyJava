package src;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import java.util.Arrays;
import src.components.CustomTextField.TextFieldStyle;
import src.tab.CommonTab;

public class TodoApp {
  public static void main(String[] args) {
    CommonTab.initializeUI();

    JFrame frame = new JFrame("Todo Application");
    frame.setSize(800, 600);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLocationRelativeTo(null);

    // JTabbedPaneの生成
    JTabbedPane tabbedPane = new JTabbedPane();

    // CommonTabのaddTabメソッドを使ってタブを追加
    CommonTab.addTab(tabbedPane, "Add Todo", "ToDo Title:", TextFieldStyle.OUTLINED, "Add Todo");
    CommonTab.addTab(tabbedPane, "Search Todo", "Search:", TextFieldStyle.STANDARD, "Search Todo");

    // フレームにタブ付きペインを追加
    frame.add(tabbedPane);

    // テキストフィールドのリストを取得してフォーカスリスナーを追加
    // ここでは、CommonTabのaddTabで生成されたテキストフィールドを渡す必要があります。
    // 今回は仮のリストを作成しますが、実際の実装では生成されたテキストフィールドを取得してください。
    // 例えば、CommonTabにテキストフィールドのリストを返すメソッドを追加するなど
    CommonTab.addFocusListener(frame, Arrays.asList()); // ここに実際のテキストフィールドリストを渡します

    frame.setVisible(true);
  }
}
