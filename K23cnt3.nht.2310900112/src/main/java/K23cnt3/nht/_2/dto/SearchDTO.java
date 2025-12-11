package K23cnt3.nht._2.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SearchDTO {
    private String keyword;
    private Integer maLoai;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private String sortBy;
    private String sortOrder;

    public SearchDTO() {
        this.sortBy = "maSP";
        this.sortOrder = "desc";
    }

    public boolean hasKeyword() {
        return keyword != null && !keyword.trim().isEmpty();
    }

    public boolean hasMaLoai() {
        return maLoai != null && maLoai > 0;
    }

    public boolean hasPriceRange() {
        return minPrice != null || maxPrice != null;
    }
}