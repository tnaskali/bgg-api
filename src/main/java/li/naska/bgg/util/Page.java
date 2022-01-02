package li.naska.bgg.util;

import lombok.Data;

import java.util.List;

@Data
public class Page<T> {

  private final int page;

  private final int size;

  private final int totalPages;

  private final int totalSize;

  private final int pageSize;

  private final List<T> content;

  public Page(int page, int totalPages, int pageSize, int totalSize, List<T> content) {
    this.page = page;
    this.size = content.size();
    this.totalPages = totalPages;
    this.pageSize = pageSize;
    this.totalSize = totalSize;
    this.content = content;
  }

  public static <U> Page<U> of(int page, int totalPages, int pageSize, int totalSize, List<U> content) {
    return new Page<>(page, totalPages, pageSize, totalSize, content);
  }

  public boolean hasNext() {
    return getPage() < getTotalPages();
  }

}
