package com.cybersoft.shop.service;

import com.cybersoft.shop.request.SizeCreateRequest;
import com.cybersoft.shop.request.SizeUpdateRequest;
import com.cybersoft.shop.response.SizeResponse;

import java.util.List;

public interface SizeService {

    List<SizeResponse> listSize();
    SizeResponse getSizeById(int id);
    SizeResponse createSize(SizeCreateRequest req);
    SizeResponse updateSize(int id, SizeUpdateRequest req);
    void deleteSize(int id);
}
