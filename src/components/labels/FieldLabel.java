package src.components.labels;

import java.util.LinkedHashMap;
import java.util.Map;

public class FieldLabel {
  /** 英語フィールド名と日本語ラベルのマッピング */
  public final Map<String, String> TODO_FIELD_LABELS = new LinkedHashMap<>();

  public FieldLabel() {
    TODO_FIELD_LABELS.put("title", "タイトル");
    TODO_FIELD_LABELS.put("description", "説明");
    TODO_FIELD_LABELS.put("isCompleted", "完了");
  }
}
