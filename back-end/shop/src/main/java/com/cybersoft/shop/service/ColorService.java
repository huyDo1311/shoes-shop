package com.cybersoft.shop.service;

import com.cybersoft.shop.request.ColorCreateRequest;
import com.cybersoft.shop.request.ColorUpdateRequest;
import com.cybersoft.shop.response.ColorResponse;

import java.util.List;

public interface ColorService {
    List<ColorResponse> listColor();
    ColorResponse getColorById(int id);
    ColorResponse createColor(ColorCreateRequest req);
    ColorResponse updateColor(int id, ColorUpdateRequest req);
    void deleteColor(int id);
}
