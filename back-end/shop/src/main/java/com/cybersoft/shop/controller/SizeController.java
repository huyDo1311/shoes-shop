package com.cybersoft.shop.controller;

import com.cybersoft.shop.request.InsertSizeRequest;
import com.cybersoft.shop.services.SizeServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sizes")
public class SizeController {

    @Autowired
    private SizeServices sizeServices;

    @GetMapping
    public ResponseEntity<?> listSizes(){
        return ResponseEntity.ok(sizeServices.listSize());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSizes(@PathVariable int id){
        return ResponseEntity.ok(sizeServices.getSize(id));
    }

    @PostMapping
    public ResponseEntity<?> insertSizes(@RequestBody InsertSizeRequest req){
        return ResponseEntity.ok(sizeServices.insertSize(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSizes(@PathVariable int id, @RequestBody InsertSizeRequest req){
        return ResponseEntity.ok(sizeServices.updateSize(id,req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSizes(@PathVariable int id){
        return ResponseEntity.ok(sizeServices.deleteSize(id));
    }
}
