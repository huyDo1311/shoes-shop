package com.cybersoft.shop.controller;

import com.cybersoft.shop.request.SizeCreateRequest;
import com.cybersoft.shop.request.SizeUpdateRequest;
import com.cybersoft.shop.response.ResponseObject;
import com.cybersoft.shop.response.SizeResponse;
import com.cybersoft.shop.service.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sizes")
public class SizeController {

    @Autowired
    private SizeService sizeService;

    @GetMapping
    public ResponseEntity<?> listSizes(){
        List<SizeResponse> data = sizeService.listSize();
        ResponseObject res = new ResponseObject();
        res.setStatus(HttpStatus.OK.value());
        res.setMessage("Get sizes successfully");
        res.setData(data);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSize(@PathVariable int id){

        var data = sizeService.getSizeById(id);
        ResponseObject res = new ResponseObject();
        res.setStatus(HttpStatus.OK.value());
        res.setMessage("Get size successfully");
        res.setData(data);
        return ResponseEntity.ok(res);
    }

    @PostMapping
    public ResponseEntity<?> createSize(@RequestBody SizeCreateRequest req){
        var data = sizeService.createSize(req);
        ResponseObject res = new ResponseObject();
        res.setStatus(HttpStatus.OK.value());
        res.setMessage("Create size successfully");
        res.setData(data);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSize(@PathVariable int id, @RequestBody SizeUpdateRequest req){
        var data = sizeService.updateSize(id, req);
        ResponseObject res = new ResponseObject();
        res.setStatus(HttpStatus.OK.value());
        res.setMessage("Update size successfully");
        res.setData(data);
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSize(@PathVariable int id){
        sizeService.deleteSize(id);
        ResponseObject res = new ResponseObject();
        res.setStatus(HttpStatus.OK.value());
        res.setMessage("Update size successfully");
        res.setData(null);
        return ResponseEntity.ok(res);
    }
}