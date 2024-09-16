package src.components;

import javax.swing.*;
import java.awt.*;

public class CustomLabeledTextField extends JPanel {

  private final CustomTextField textField;
  private final JLabel label;

  // コンストラクタ
  public CustomLabeledTextField(String labelText, int columns, CustomTextField.TextFieldStyle style, int fontSize) {
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // 垂直に配置

    // ラベルを作成
    label = new JLabel(labelText);
    label.setAlignmentX(Component.LEFT_ALIGNMENT); // 左寄せ

    // カスタムテキストフィールドを作成
    textField = new CustomTextField(columns, style, fontSize);
    textField.setAlignmentX(Component.LEFT_ALIGNMENT); // 左寄せ

    // ラベルとテキストフィールドをこのパネルに追加
    this.add(label);
    this.add(textField);
  }

  // カスタムテキストフィールドを取得
  public CustomTextField getTextField() {
    return textField;
  }

  // ラベルを取得
  public JLabel getLabel() {
    return label;
  }
}
