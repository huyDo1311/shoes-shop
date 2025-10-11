package com.cybersoft.shop.service.imp;

import com.cybersoft.shop.entity.Color;
import com.cybersoft.shop.repository.ColorRepository;
import com.cybersoft.shop.request.ColorCreateRequest;
import com.cybersoft.shop.request.ColorUpdateRequest;
import com.cybersoft.shop.response.ColorResponse;
import com.cybersoft.shop.service.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColorServiceImp implements ColorService {

    private ColorResponse toRes(Color c) {
        return ColorResponse.builder()
                .id(c.getId())
                .colorName(c.getColorName())
                .build();
    }

    @Autowired
    private ColorRepository colorRepository;

    @Override
    public List<ColorResponse> listColor() {
        return colorRepository.findAll().stream().map(this::toRes).toList();
    }

    @Override
    public ColorResponse getColorById(int id) {
        var c = colorRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Color not found"));
        return toRes(c);
    }

    @Override
    public ColorResponse createColor(ColorCreateRequest req) {
        if (colorRepository.existsByColorNameIgnoreCase(req.getColorName()))
            throw new IllegalArgumentException("Color name already exists");
        var c = Color.builder().colorName(req.getColorName().trim()).build();
        return toRes(colorRepository.save(c));
    }

    @Override
    public ColorResponse updateColor(int id, ColorUpdateRequest req) {
        var c = colorRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Color not found"));
        if (req.getColorName() != null && !req.getColorName().isBlank()) {
            if (colorRepository.existsByColorNameIgnoreCase(req.getColorName()))
                throw new IllegalArgumentException("Color name already exists");
            c.setColorName(req.getColorName().trim());
        }
        return toRes(colorRepository.save(c));
    }

    @Override
    public void deleteColor(int id) {
        colorRepository.deleteById(id);
    }
}
