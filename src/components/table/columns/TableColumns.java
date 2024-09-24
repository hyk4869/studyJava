package src.components.table.columns;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.LinkedHashMap;
import java.util.Map;

public class TableColumns {

  /** カラム名（英語）と対応する日本語ラベル */
  public final Map<String, String> TODO_LIST_COLUMN_LABELS = new LinkedHashMap<>();

  public TableColumns() {
    TODO_LIST_COLUMN_LABELS.put("id", "ID");
    TODO_LIST_COLUMN_LABELS.put("title", "タイトル");
    TODO_LIST_COLUMN_LABELS.put("description", "説明");
    TODO_LIST_COLUMN_LABELS.put("createdByName", "作成者");
    TODO_LIST_COLUMN_LABELS.put("updatedByName", "更新者");
    TODO_LIST_COLUMN_LABELS.put("createdAt", "作成日時");
    TODO_LIST_COLUMN_LABELS.put("updatedAt", "更新日時");
    TODO_LIST_COLUMN_LABELS.put("isCompleted", "完了");
    TODO_LIST_COLUMN_LABELS.put("sort", "並び順");
  }

  public final List<String> TODO_LIST_COLUMNS = Arrays.asList(
      "id", "title", "description", "createdByName",
      "updatedByName", "createdAt", "updatedAt", "isCompleted", "sort");

  /** 列名を除外するメソッド */
  public List<String> omitColumns(List<String> columns, List<String> omitColumns) {
    return columns.stream()
        .filter(col -> !omitColumns.contains(col))
        .collect(Collectors.toList());
  }

  /** 指定された列名だけをピックアップするメソッド */
  public List<String> pickColumns(List<String> columns, List<String> pickColumns) {
    return columns.stream()
        .filter(pickColumns::contains)
        .collect(Collectors.toList());
  }
}

// List<String> omitColumns =
// tableColumns.omitColumns(tableColumns.TODO_LIST_COLUMNS,
// Arrays.asList("createdByName", "updatedByName"));

// List<String> pickColumns =
// tableColumns.pickColumns(tableColumns.TODO_LIST_COLUMNS,
// Arrays.asList("id", "createdByName", "updatedByName"));
