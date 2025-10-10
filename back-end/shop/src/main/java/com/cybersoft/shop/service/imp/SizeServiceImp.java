package com.cybersoft.shop.service.imp;

import com.cybersoft.shop.entity.Size;
import com.cybersoft.shop.repository.SizeRepository;
import com.cybersoft.shop.request.SizeCreateRequest;
import com.cybersoft.shop.request.SizeUpdateRequest;
import com.cybersoft.shop.response.SizeResponse;
import com.cybersoft.shop.service.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SizeServiceImp implements SizeService {

    private SizeResponse toRes(Size s){
        return SizeResponse.builder()
                .id(s.getId())
                .sizeValue(s.getSizeValue())
                .build();
    }

    @Autowired
    private SizeRepository sizeRepository;

    @Override
    public List<SizeResponse> listSize(){
        return sizeRepository.findAll().stream().map(this::toRes).toList();
    }

    @Override
    public SizeResponse getSizeById(int id){
        var s = sizeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Size not found"));
        return toRes(s);
    }

    @Override
    public SizeResponse createSize(SizeCreateRequest req){
        if(sizeRepository.existsBySizeValue(req.getSizeValue()))
            throw new IllegalArgumentException("Size value already exists");
        var s = Size.builder().sizeValue(req.getSizeValue()).build();
        return toRes(sizeRepository.save(s));
    }

    @Override
    public SizeResponse updateSize(int id, SizeUpdateRequest req){
        var s = sizeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Size not found"));
        if(sizeRepository.existsBySizeValue(req.getSizeValue()))
            throw new IllegalArgumentException("Size value already exists");
        s.setSizeValue(req.getSizeValue());
        return toRes(sizeRepository.save(s));
    }


    @Override
    public void deleteSize(int id) {
        sizeRepository.deleteById(id);
    }
}
