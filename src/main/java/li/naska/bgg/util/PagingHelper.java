package li.naska.bgg.util;

public class PagingHelper {

  private final int pageSize;

  private final int pageNumber;

  private final int bggPageSize;

  public PagingHelper(int pageSize, int pageNumber, int bggPageSize) {
    this.pageSize = pageSize;
    this.pageNumber = pageNumber;
    this.bggPageSize = bggPageSize;
  }

  public int getStartIndex() {
    return pageSize * (pageNumber - 1) + 1;
  }

  public int getEndIndex() {
    return getStartIndex() + pageSize - 1;
  }

  public int getBggStartPage() {
    return (int) Math.ceil((double) getStartIndex() / bggPageSize);
  }

  public int getBggEndPage() {
    return (int) Math.ceil((double) getEndIndex() / bggPageSize);
  }

  public int getBggPages() {
    return getBggEndPage() - getBggStartPage() + 1;
  }

  public int getResultStartIndex() {
    return (getStartIndex() - 1) % bggPageSize;
  }

  public int getResultEndIndex() {
    return getResultStartIndex() + pageSize;
  }

}
