package li.naska.bgg.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * Helps calculating the number of consecutive pages to fetch from BGG based on the request. Also helps trimming
 * correctly the list of items fetched.
 */
public class PagingHelper {

  private final int requestedPageSize;

  private final int requestedPageNumber;

  private final int bggPageSize;

  public PagingHelper(int requestedPageSize, int requestedPageNumber, int bggPageSize) {
    this.requestedPageSize = requestedPageSize;
    this.requestedPageNumber = requestedPageNumber;
    this.bggPageSize = bggPageSize;
  }

  private int getStartIndex() {
    return requestedPageSize * (requestedPageNumber - 1) + 1;
  }

  private int getEndIndex() {
    return getStartIndex() + requestedPageSize - 1;
  }

  public int getBggStartPage() {
    return (int) Math.ceil((double) getStartIndex() / bggPageSize);
  }

  public int getLastPage(int bggTotalSize) {
    return Math.max(1, (int) Math.ceil((double) bggTotalSize / requestedPageSize));
  }

  private int getBggEndPage(int bggTotalSize) {
    return Math.max(1, (int) Math.ceil((double) Math.min(getEndIndex(), bggTotalSize) / bggPageSize));
  }

  public int getBggPages(int bggTotalSize) {
    return getBggEndPage(bggTotalSize) - getBggStartPage() + 1;
  }

  private int getTrimStartIndex() {
    return (getStartIndex() - 1) % bggPageSize;
  }

  private int getTrimEndIndex() {
    return getTrimStartIndex() + requestedPageSize;
  }

  public Flux<Integer> getBggPagesRange(Integer bggTotalSize) {
    if (bggTotalSize == null) {
      return Flux.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("page %d does not exist", requestedPageNumber)));
    }
    int lastPage = getLastPage(bggTotalSize);
    if (requestedPageNumber > lastPage) {
      return Flux.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("page %d does not exist (last page is %d)", requestedPageNumber, lastPage)));
    }
    return Flux.range(getBggStartPage(), getBggPages(bggTotalSize));
  }

  public <T> Page<T> buildPage(List<T> intermediateList, int bggTotalSize) {
    int totalPages = getLastPage(bggTotalSize);
    List<T> finalList = intermediateList.subList(
        Math.min(
            getTrimStartIndex(),
            intermediateList.size()),
        Math.min(
            getTrimEndIndex(),
            intermediateList.size()));
    return Page.of(requestedPageNumber, totalPages, requestedPageSize, bggTotalSize, finalList);
  }

}
