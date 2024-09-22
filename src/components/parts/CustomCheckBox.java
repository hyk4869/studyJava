package src.components.parts;

import javax.swing.JCheckBox;

public final class CustomCheckBox extends JCheckBox {

  // コンストラクタ
  public CustomCheckBox(String label) {
    super(label);
    setSelected(false);
  }

  // チェックが入っているかを返す
  public boolean isChecked() {
    return isSelected();
  }

  // チェックの状態を設定
  public void setChecked(boolean isChecked) {
    setSelected(isChecked);
  }
}
