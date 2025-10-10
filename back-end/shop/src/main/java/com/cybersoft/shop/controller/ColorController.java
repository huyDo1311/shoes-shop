package com.cybersoft.shop.controller;

import com.cybersoft.shop.request.ColorCreateRequest;
import com.cybersoft.shop.request.ColorUpdateRequest;
import com.cybersoft.shop.response.ColorResponse;
import com.cybersoft.shop.response.ResponseObject;
import com.cybersoft.shop.service.ColorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("/colors")
public class ColorController {

    @Autowired
    private ColorService colorService;

    @GetMapping
    public ResponseEntity<?> getAllColors() {
        List<ColorResponse> data = colorService.listColor();
        ResponseObject res = new ResponseObject();
        res.setStatus(HttpStatus.OK.value());
        res.setMessage("Get colors successfully");
        res.setData(data);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getColorById(@PathVariable int id) {
        ColorResponse data = colorService.getColorById(id);
        ResponseObject res = new ResponseObject();
        res.setStatus(HttpStatus.OK.value());
        res.setMessage("Get color successfully");
        res.setData(data);
        return ResponseEntity.ok(res);
    }

    @PostMapping
    public ResponseEntity<?> createColor(@Valid @RequestBody ColorCreateRequest req) {
        ColorResponse data = colorService.createColor(req);
        ResponseObject res = new ResponseObject();
        res.setStatus(HttpStatus.CREATED.value());
        res.setMessage("Create color successfully");
        res.setData(data);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateColor(@PathVariable int id, @Valid @RequestBody ColorUpdateRequest req) {
        ColorResponse data = colorService.updateColor(id, req);
        ResponseObject res = new ResponseObject();
        res.setStatus(HttpStatus.OK.value());
        res.setMessage("Update color successfully");
        res.setData(data);
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteColor(@PathVariable int id) {
        colorService.deleteColor(id);
        ResponseObject res = new ResponseObject();
        res.setStatus(HttpStatus.OK.value());
        res.setMessage("Delete color successfully");
        res.setData(null);
        return ResponseEntity.ok(res);
    }
}
