package K23cnt3.nht._2.model;

import lombok.Data;
import java.util.List;

@Data
public class Pagination<T> {
    private List<T> content;
    private int currentPage;
    private int totalPages;
    private long totalItems;
    private int pageSize;
    private boolean hasNext;
    private boolean hasPrevious;

    public Pagination() {}

    public Pagination(List<T> content, int currentPage, int pageSize, long totalItems) {
        this.content = content;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalItems = totalItems;
        this.totalPages = (int) Math.ceil((double) totalItems / pageSize);
        this.hasNext = currentPage < totalPages;
        this.hasPrevious = currentPage > 1;
    }

    // Tạo danh sách số trang để hiển thị
    public List<Integer> getPageNumbers() {
        int start = Math.max(1, currentPage - 2);
        int end = Math.min(totalPages, currentPage + 2);

        return java.util.stream.IntStream.rangeClosed(start, end)
                .boxed()
                .collect(java.util.stream.Collectors.toList());
    }
}