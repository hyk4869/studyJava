package src.views.common;

import java.awt.GridBagConstraints;

import src.components.labels.FieldLabel;
import src.components.table.CommonTable;
import src.tab.CommonTab;
import src.utils.CommonColor;

public abstract class CommonViewPannel {
  /** テーブル生成 */
  protected CommonTable commonTable;
  /** タブの生成 */
  protected CommonTab commonTab = new CommonTab();
  /** 共通の色 */
  protected CommonColor commonColor = new CommonColor();
  /** inputフィールドで何を生成するか */
  protected FieldLabel fieldLabel = new FieldLabel();
  /**
   * GridBagLayoutを使用してコンポーネントの配置やレイアウトを管理するための制約情報を持つオブジェクト。
   * 各コンポーネントの位置（行・列）、サイズ、間隔、配置方法などを指定するために使用される。
   * commonPannel内でUIコンポーネントの配置を柔軟に設定するための設定が含まれている。
   */
  protected GridBagConstraints gbc = new GridBagConstraints();

}
