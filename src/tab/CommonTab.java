package src.tab;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import src.components.CustomDateField;
import src.components.CustomNumericField;
import src.components.CustomTextField;
import src.components.enums.TextFieldStyle;
import src.components.labels.CustomLabeledComponent;
import src.tab.pannels.CreatePannel;
import src.utils.CommonFont;

public class CommonTab {
  /** テキストフィールドのリスト */

  private List<CustomLabeledComponent> components = new ArrayList<>();

  private final CreatePannel createPannel = new CreatePannel();
  private final static CommonFont commonFont = new CommonFont();

  /** 初期化 */
  public static void initializeUI() {
    // タブの高さを変更
    UIManager.put("TabbedPane.tabInsets", new javax.swing.plaf.InsetsUIResource(10, 20, 10, 20));
    // フォントの設定
    UIManager.put("TabbedPane.font", new FontUIResource(commonFont.commonNotoSansCJKJP(14)));
  }

  /** テキストフィールドの追加 */
  private void addFields(JPanel innerPanel, Map<String, String> fieldConfigs, TextFieldStyle style, int columns) {
    GridBagConstraints innerGbc = new GridBagConstraints();
    innerGbc.insets = new Insets(10, 10, 10, 10);

    int index = 0;
    for (Map.Entry<String, String> entry : fieldConfigs.entrySet()) {
      String labelText = entry.getKey();
      String fieldType = entry.getValue();

      CustomLabeledComponent labeledComponent;

      // フィールドタイプに応じてコンポーネントを選択
      if ("numeric".equalsIgnoreCase(fieldType)) {
        CustomNumericField numericField = new CustomNumericField(20, style, 14);
        labeledComponent = new CustomLabeledComponent(labelText, numericField);
      } else if ("date".equalsIgnoreCase(fieldType)) {
        CustomDateField dateField = new CustomDateField(20, style, 14);
        labeledComponent = new CustomLabeledComponent(labelText, dateField);
      } else {
        CustomTextField textField = new CustomTextField(20, style, 14);
        labeledComponent = new CustomLabeledComponent(labelText, textField);
      }

      innerGbc.gridx = index % columns; // 指定された列数に基づいて配置
      innerGbc.gridy = index / columns; // 行
      innerGbc.fill = GridBagConstraints.HORIZONTAL; // 横方向に広げる
      innerGbc.weightx = 1.0; // 余分なスペースがあれば均等に配分
      innerGbc.anchor = GridBagConstraints.WEST; // 左寄せ
      innerPanel.add(labeledComponent, innerGbc);

      components.add(labeledComponent);
      index++;
    }
  }

  /** 内部パネルをメインパネルに追加 */
  private void addInnerPanelToMain(JPanel mainPanel, JPanel innerPanel) {
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2; // 内部パネルの幅に広げる
    gbc.insets = new Insets(0, 10, 0, 10); // 左右のマージンを10に設定
    gbc.fill = GridBagConstraints.HORIZONTAL; // 横方向に広げる
    gbc.weightx = 1.0; // 余分なスペースがあれば横方向に広げる
    mainPanel.add(innerPanel, gbc);
  }

  /**
   * タブ追加メソッド
   *
   * @param tabbedPane
   * @param title
   * @param fieldConfigs
   * @param style
   * @param columns
   *                     テキストフィールドを配置する列数
   * @return innerPanelを返すことで、外部からボタンを追加できるようにする
   */
  public JPanel addTab(JTabbedPane tabbedPane, String title, Map<String, String> fieldConfigs, TextFieldStyle style,
      int columns) {
    JPanel mainPanel = createPannel.createMainPanel();
    JPanel innerPanel = createPannel.createInnerPanel(title);

    addFields(innerPanel, fieldConfigs, style, columns);
    addInnerPanelToMain(mainPanel, innerPanel);

    tabbedPane.addTab(title, mainPanel);

    return innerPanel;
  }

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
