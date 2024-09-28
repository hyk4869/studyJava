package src.components.table.override.columns;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import src.components.table.override.checkbox.CheckBoxEditor;
import src.components.table.override.checkbox.CheckBoxRenderer;

public class DeleteColumn {
  private JTable commonTable;
  private DefaultTableModel tableModel;
  private boolean checkBoxColumnAdded;

  public DeleteColumn(JTable commonTable, DefaultTableModel tableModel, boolean checkBoxColumnAdded) {
    this.commonTable = commonTable;
    this.tableModel = tableModel;
    this.checkBoxColumnAdded = checkBoxColumnAdded;
  }

  /**
   * DELETEチェックボックス列を追加
   */
  public void addCheckBoxColumn() {
    // モデルに "Delete" カラムを追加
    tableModel.addColumn("Delete");

    // テーブルの再描画
    commonTable.revalidate();
    commonTable.repaint();

    // カラムモデルが正しく反映された後にレンダラとエディタを設定
    int deleteColumnIndex = tableModel.getColumnCount() - 1;
    commonTable.getColumnModel().getColumn(deleteColumnIndex).setCellRenderer(new CheckBoxRenderer());
    commonTable.getColumnModel().getColumn(deleteColumnIndex).setCellEditor(new CheckBoxEditor(new JCheckBox()));

    checkBoxColumnAdded = true;
  }

  /**
   * DELETEチェックボックス列を削除
   */
  public void removeCheckBoxColumn() {
    if (checkBoxColumnAdded) {
      // "Delete" カラムのインデックスを取得
      int deleteColumnIndex = -1;
      for (int i = 0; i < commonTable.getColumnModel().getColumnCount(); i++) {
        if (commonTable.getColumnModel().getColumn(i).getHeaderValue().equals("Delete")) {
          deleteColumnIndex = i;
          break;
        }
      }

      // カラムが見つかった場合のみ削除
      if (deleteColumnIndex != -1) {
        commonTable.getColumnModel().removeColumn(commonTable.getColumnModel().getColumn(deleteColumnIndex));
        // TableModelのカラム情報を直接操作
        tableModel.setColumnCount(tableModel.getColumnCount() - 1);

        commonTable.revalidate();
        commonTable.repaint();

        checkBoxColumnAdded = false;
      } else {
        System.out.println("Deleteカラムが見つかりませんでした。");
      }
    }
  }

  public boolean getCheckBoxColumnAdded() {
    return this.checkBoxColumnAdded;
  }
}
