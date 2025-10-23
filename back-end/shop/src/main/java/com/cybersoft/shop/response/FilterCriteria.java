package com.cybersoft.shop.response;

import com.cybersoft.shop.enums.SortType;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class FilterCriteria {
    private String category;
    private  String  brand;
    private  List<Long>  color;
    private List<String> size;
    private String keyword;
    private Double minPrice;
    private Double maxPrice;
    private SortType sort;
    private int page = 0;
    private int limit = 4;
}
