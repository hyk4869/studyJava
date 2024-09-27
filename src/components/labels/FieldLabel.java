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

  /** 英語フィールド名と日本語ラベルのマッピング */
  public final Map<String, String> SEARCH_FIELD_LABELS = new LinkedHashMap<>() {
    {
      put("title", "タイトル");
      put("description", "説明");
      put("createdByName", "作成者");
      put("updatedByName", "更新者");
      put("createdAt", "作成日時");
      put("updatedAt", "更新日時");
      put("deletedAt", "削除日時");
      put("isCompleted", "完了");
      put("sort", "並び順");
    }
  };

  /** フィールドで使うものを宣言 */
  public final Map<String, String> SEARCH_FIELD_CONFIG = new LinkedHashMap<>() {
    {
      put("title", "text");
      put("description", "text");
      put("createdByName", "text");
      put("updatedByName", "text");
      put("createdAt", "date");
      put("updatedAt", "date");
      put("deletedAt", "date");
      put("isCompleted", "check");
      put("sort", "numeric");
    }
  };
}
