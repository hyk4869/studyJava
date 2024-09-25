package src.components.labels;

import java.util.LinkedHashMap;
import java.util.Map;

public class FieldLabel {
  /** 英語フィールド名と日本語ラベルのマッピング */
  public final Map<String, String> TODO_FIELD_LABELS = new LinkedHashMap<>() {
    {
      put("title", "タイトル");
      put("description", "説明");
      put("isCompleted", "完了");
    }
  };

  /** フィールドで使うものを宣言 */
  public final Map<String, String> TODO_FIELD_CONFIGS = new LinkedHashMap<>() {
    {
      put("title", "text");
      put("description", "textArea");
      put("isCompleted", "check");
    }
  };

}
