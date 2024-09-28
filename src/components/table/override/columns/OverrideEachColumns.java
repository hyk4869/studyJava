package src.components.table.override.columns;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.swing.JCheckBox;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import src.components.table.override.checkbox.CheckBoxEditor;
import src.components.table.override.checkbox.CheckBoxRenderer;

/** カラムの上書きとoverrideしたカラムの読み込み */
public class OverrideEachColumns {

  private Function<String, Integer> getColumnIndexFunction;
  private Supplier<TableColumnModel> getColumnModelFunction;
  private ColumnsName columnsName;

  public OverrideEachColumns(Function<String, Integer> getColumnIndexFunction,
      Supplier<TableColumnModel> getColumnModelFunction, ColumnsName columnsName) {

    this.getColumnIndexFunction = getColumnIndexFunction;
    this.getColumnModelFunction = getColumnModelFunction;
    this.columnsName = columnsName;
  }

  /**
   * カラムの上書きとoverrideしたカラムの読み込み
   *
   * @param columnList ここで上書きしたいものを選択
   * @param hiddenList 非表示にしたいものを選択
   */
  public void reloadOverridedColumn(List<String> columnList, List<String> hiddenList) {
    columnsName.overrideColumnLabel();

    for (String columnName : columnList) {
      int columnIndex = getColumnIndexFunction.apply(columnName);

      if (columnIndex != 1 && columnsName.getColumnNames().containsKey(columnName)) {
        String type = columnsName.getColumnNames().get(columnName);

        // 必要に応じてここで独自のコンポーネントを呼ぶ
        if (type.equals("Boolean")) {
          TableColumn checkBoxColumn = getColumnModelFunction.get().getColumn(columnIndex);
          checkBoxColumn.setCellRenderer(new CheckBoxRenderer());
          checkBoxColumn.setCellEditor(new CheckBoxEditor(new JCheckBox()));

        }

      }
    }

    for (String hiddenName : hiddenList) {
      int columnIndex = getColumnIndexFunction.apply(hiddenName);

      if (columnIndex != -1) {
        TableColumn column = getColumnModelFunction.get().getColumn(columnIndex);

        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setWidth(0);
      }
    }
  }
}
