package src.components.table.columns;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.LinkedHashMap;
import java.util.Map;

public class TableColumns {

  /** カラム名（英語）と対応する日本語ラベル */
  public final Map<String, String> TODO_LIST_COLUMN_LABELS = new LinkedHashMap<>() {
    {
      put("id", "ID");
      put("title", "タイトル");
      put("description", "説明");
      put("createdByName", "作成者");
      put("updatedByName", "更新者");
      put("createdAt", "作成日時");
      put("updatedAt", "更新日時");
      put("isCompleted", "完了");
      put("sort", "並び順");
    }
  };

  // public final List<String> TODO_LIST_COLUMNS = Arrays.asList(
  // "id", "title", "description", "createdByName",
  // "updatedByName", "createdAt", "updatedAt", "isCompleted", "sort");

  public final Map<String, String> TODO_LIST_COLUMNS = new LinkedHashMap<>() {
    {
      put("id", "String");
      put("title", "String");
      put("description", "String");
      put("createdByName", "String");
      put("updatedByName", "String");
      put("createdAt", "Timestamp");
      put("updatedAt", "Timestamp");
      put("isCompleted", "Boolean");
      put("sort", "Integer");
    }
  };

  /** 列名を除外するメソッド */
  public Map<String, String> omitColumns(Map<String, String> columns, List<String> omitColumns) {
    return columns.entrySet().stream()
        .filter(entry -> !omitColumns.contains(entry.getKey()))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
  }

  /** 指定された列名だけをピックアップするメソッド */
  public Map<String, String> pickColumns(Map<String, String> columns, List<String> pickColumns) {
    return columns.entrySet().stream()
        .filter(entry -> pickColumns.contains(entry.getKey()))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
  }

}
