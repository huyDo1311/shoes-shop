package com.cybersoft.shop.services;

import com.cybersoft.shop.entity.Sizes;
import com.cybersoft.shop.request.InsertSizeRequest;


import java.util.List;
import java.util.Optional;

public interface SizeServices {

    List<Sizes> listSize();

    Optional<Sizes> getSize(int id);

    String insertSize(InsertSizeRequest req);

    String updateSize(int id, InsertSizeRequest req);

    String deleteSize(int id);
}
