package K23cnt3.nht._2.model;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SearchCriteria {
    private String keyword;
    private Integer maLoai;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private String sortBy;
    private String sortOrder;
    private Integer page;
    private Integer size;

    public SearchCriteria() {
        this.keyword = "";
        this.sortBy = "maSP";
        this.sortOrder = "desc";
        this.page = 1;
        this.size = 12;
    }

    // Kiểm tra có từ khóa tìm kiếm không
    public boolean hasKeyword() {
        return keyword != null && !keyword.trim().isEmpty();
    }

    // Kiểm tra có lọc theo loại không
    public boolean hasMaLoai() {
        return maLoai != null && maLoai > 0;
    }

    // Kiểm tra có lọc theo giá không
    public boolean hasPriceFilter() {
        return minPrice != null || maxPrice != null;
    }

    // Kiểm tra có sắp xếp không
    public boolean hasSorting() {
        return sortBy != null && !sortBy.isEmpty();
    }

    // Lấy offset cho phân trang
    public int getOffset() {
        return (page - 1) * size;
    }

    // Tạo query string từ criteria
    public String toQueryString() {
        StringBuilder query = new StringBuilder();

        if (hasKeyword()) {
            query.append("keyword=").append(keyword).append("&");
        }

        if (hasMaLoai()) {
            query.append("maLoai=").append(maLoai).append("&");
        }

        if (minPrice != null) {
            query.append("minPrice=").append(minPrice).append("&");
        }

        if (maxPrice != null) {
            query.append("maxPrice=").append(maxPrice).append("&");
        }

        if (hasSorting()) {
            query.append("sortBy=").append(sortBy).append("&");
            query.append("sortOrder=").append(sortOrder).append("&");
        }

        query.append("page=").append(page).append("&");
        query.append("size=").append(size);

        return query.toString();
    }
}