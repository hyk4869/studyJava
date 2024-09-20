package src.components;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.SqlDateModel;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.Locale;

import src.components.enums.TextFieldStyle;
import src.components.styles.CustomStyledContents;

/** 日付のinput */
public class CustomDateField extends CustomStyledContents {

  // フィールド
  private JButton customEllipsisButton;

  /** コンストラクタ */
  public CustomDateField(int columns, TextFieldStyle style, int fontSize) {
    super(style, fontSize);
    setFormatter(createFormatter());

    // カラム数を設定
    this.setColumns(columns);

    // カスタムボタンを作成（ボタンをここで初期化）
    customEllipsisButton = new JButton("date");

    // ボタンのアクションリスナーをコンストラクタ内で設定
    customEllipsisButton.addActionListener(e -> {
      showDatePickerDialog();
    });

    // テキストフィールドをクリックしたら日付ピッカーをダイアログで表示
    this.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (getText() == null || getText().trim().isEmpty()) {
          showDatePickerDialog();
        }
      }
    });

    // フォーカスが外れたらボタンを非表示に
    this.addFocusListener(new FocusListener() {
      @Override
      public void focusGained(FocusEvent e) {
        customEllipsisButton.setVisible(true);
      }

      @Override
      public void focusLost(FocusEvent e) {
        customEllipsisButton.setVisible(false);
      }
    });
  }

  /** 日付ピッカーの表示 */
  private void showDatePickerDialog() {

    // 日付ピッカーを作成
    SqlDateModel model = new SqlDateModel();
    Properties p = new Properties();
    p.put("text.today", "今日");
    p.put("text.month", "月");
    p.put("text.year", "年");
    JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
    JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

    datePicker.setLocale(Locale.JAPAN);

    // JDatePickerImplのテキストフィールドを非表示にする
    JFormattedTextField textField = datePicker.getJFormattedTextField();
    textField.setVisible(false);

    // カスタムダイアログで日付ピッカーを表示
    JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "select date", true);
    dialog.setLayout(new BorderLayout());
    dialog.add(datePanel, BorderLayout.CENTER);
    dialog.pack();
    dialog.setLocationRelativeTo(this);

    // 日付選択時にCustomStyledContentsのテキストフィールドに反映し、ダイアログを閉じる
    datePanel.addActionListener(e -> {
      java.util.Date selectedDate = (java.util.Date) datePicker.getModel().getValue();
      if (selectedDate != null) {
        setText(new SimpleDateFormat("yyyy-MM-dd").format(selectedDate));
      }
      dialog.dispose();
    });

    dialog.setVisible(true);

    if (customEllipsisButton.getParent() == null) {
      this.setLayout(new BorderLayout());
      this.add(customEllipsisButton, BorderLayout.EAST);
    }

    customEllipsisButton.setVisible(true);
    this.revalidate();
    this.repaint();
  }

  /** 日付フォーマッターを作成 */
  private static DateFormatter createFormatter() {
    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    DateFormatter formatter = new DateFormatter(format) {
      @Override
      public Object stringToValue(String text) throws ParseException {
        if (text == null || text.trim().isEmpty()) {
          return null;
        }
        return super.stringToValue(text);
      }
    };

    formatter.setAllowsInvalid(false); // 無効な入力を許可しない
    formatter.setCommitsOnValidEdit(true);
    return formatter;
  }

  /** DateLabelFormatterの内部クラス */
  private class DateLabelFormatter extends DateFormatter {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Object stringToValue(String text) throws ParseException {
      return dateFormat.parseObject(text);
    }

    @Override
    public String valueToString(Object value) {
      if (value instanceof java.util.Date) {
        return dateFormat.format(value);
      }
      return "";
    }
  }
}
