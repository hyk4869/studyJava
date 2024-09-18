package src.components;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.SqlDateModel;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;
import src.components.enums.TextFieldStyle;
import src.components.styles.CustomStyledContents;

/** 日付のinput */
public class CustomDateField extends CustomStyledContents {

  private JDatePickerImpl datePicker; // 日付ピッカーのインスタンス

  // コンストラクタ
  public CustomDateField(int columns, TextFieldStyle style, int fontSize) {
    super(style, fontSize);
    setFormatter(createFormatter());

    // カラム数を設定
    this.setColumns(columns);

    // 日付ピッカーを作成
    createDatePicker();
  }

  // 日付ピッカーを作成し、クリック時に表示するように設定
  private void createDatePicker() {
    SqlDateModel model = new SqlDateModel();
    Properties p = new Properties();
    p.put("text.today", "Today");
    p.put("text.month", "Month");
    p.put("text.year", "Year");
    JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
    datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

    // 日付フィールドをクリックしたら独立したダイアログを表示
    this.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        SwingUtilities.invokeLater(() -> {
          JDialog dialog = new JDialog();
          dialog.setTitle("Select Date");
          dialog.setModal(true); // モーダルダイアログとして設定
          dialog.getContentPane().add(datePicker);
          dialog.pack();
          dialog.setLocationRelativeTo(null); // 画面中央に表示
          dialog.setVisible(true);

          // ダイアログが閉じられたら選択した日付を設定
          if (datePicker.getModel().getValue() != null) {
            setText(datePicker.getJFormattedTextField().getText());
          }
        });
      }
    });

  }

  // 日付フォーマッターを作成
  private static DateFormatter createFormatter() {
    DateFormat format = new SimpleDateFormat("yyyy-MM-dd"); // 日付フォーマットを設定
    DateFormatter formatter = new DateFormatter(format) {
      @Override
      public Object stringToValue(String text) throws ParseException {
        if (text == null || text.trim().isEmpty()) {
          return null; // テキストが空の場合はnullを返す
        }
        return super.stringToValue(text); // 有効な日付の場合は通常のパースを行う
      }
    };

    formatter.setAllowsInvalid(false); // 無効な入力を許可しない
    formatter.setCommitsOnValidEdit(true);
    return formatter;
  }

  // DateLabelFormatterの内部クラス
  private class DateLabelFormatter extends DateFormatter {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Object stringToValue(String text) throws ParseException {
      return dateFormat.parseObject(text);
    }

    @Override
    public String valueToString(Object value) {
      if (value != null) {
        return dateFormat.format(value);
      }
      return "";
    }
  }
}
