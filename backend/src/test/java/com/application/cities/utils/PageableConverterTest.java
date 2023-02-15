package com.application.cities.utils;

import com.application.cities.model.City;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PageableConverterTest {

    public static final String ITEMS_PER_PAGE = "items_per_page";
    public static final String PAGE_INDEX = "page_index";
    public static final String TOTAL_ITEMS = "total_items";

    @Test
    void whenPageIsNull_thenThrowException() {
        assertThrows(NullPointerException.class,
                () -> PageableConverter.convert(2, 4, null));
    }

    @Test
    void whenPageIsEmpty_thenConvertPageData() {
        var pageMap = PageableConverter.convert(3, 4, Page.empty());
        Map<String, Object> data = (Map) pageMap.get("data");
        List items = (List) data.get("items");

        assertThat(items).hasSize(0);
        assertThat((int) data.get(ITEMS_PER_PAGE)).isEqualTo(4);
        assertThat((int) data.get(PAGE_INDEX)).isEqualTo(3);
        assertThat((long) data.get(TOTAL_ITEMS)).isEqualTo(0);
    }

    @Test
    void whenPageIsNotEmpty_thenConvertPageData() {
        var kyivCity = new City("Kyiv", "http://kyiv.com");
        var ternopilCity = new City("Ternopil", "http://ternopil.com");

        var pageMap = PageableConverter.convert(2, 1, new PageImpl<>(List.of(kyivCity, ternopilCity)));

        Map<String, Object> data = (Map) pageMap.get("data");
        List items = (List) data.get("items");

        assertThat(items).hasSize(2);
        assertThat((int) data.get(ITEMS_PER_PAGE)).isEqualTo(1);
        assertThat((int) data.get(PAGE_INDEX)).isEqualTo(2);
        assertThat((long) data.get(TOTAL_ITEMS)).isEqualTo(2);
    }
}