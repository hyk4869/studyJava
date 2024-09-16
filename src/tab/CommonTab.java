package src.tab;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import src.components.CustomLabeledTextField;
import src.components.CustomTextField;
import src.components.CustomTextField.TextFieldStyle;

public class CommonTab {
  public static void initializeUI() {
    // タブの高さを変更
    // マージンの設定
    UIManager.put("TabbedPane.tabInsets", new javax.swing.plaf.InsetsUIResource(10, 20, 10, 20));
    // フォントの設定
    UIManager.put("TabbedPane.font", new FontUIResource(new Font("Arial", Font.BOLD, 16)));
  }

  // タブ追加メソッド
  public static void addTab(JTabbedPane tabbedPane, String title, String labelText, TextFieldStyle style,
      String buttonText) {
    JPanel panel = new JPanel();

    CustomLabeledTextField labeledTextField = new CustomLabeledTextField(labelText, 20, style, 14);
    // JTextField textField = new CustomTextField(20, style, 14);
    JButton button = new JButton(buttonText);

    panel.add(labeledTextField);
    // panel.add(textField);
    panel.add(button);
    tabbedPane.addTab(title, panel);
  }

  // テキストフィールドを渡して、それらをクリックでフォーカス解除するリスナーを追加
  public static void addFocusListener(JFrame frame, List<JTextField> textFields) {
    frame.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        boolean anyFieldHasFocus = textFields.stream().anyMatch(JTextField::hasFocus);
        if (!anyFieldHasFocus) {
          for (JTextField textField : textFields) {
            textField.setFocusable(false);
          }
          frame.requestFocusInWindow(); // フレームにフォーカスを移す
          for (JTextField textField : textFields) {
            textField.setFocusable(true);
          }
        }
      }
    });
  }
}
