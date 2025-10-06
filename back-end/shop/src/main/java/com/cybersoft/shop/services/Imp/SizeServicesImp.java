package com.cybersoft.shop.services.Imp;

import com.cybersoft.shop.entity.Sizes;
import com.cybersoft.shop.repository.SizeRepository;
import com.cybersoft.shop.request.InsertSizeRequest;
import com.cybersoft.shop.services.SizeServices;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SizeServicesImp implements SizeServices {

    @Autowired
    private SizeRepository sizeRepository;

    @Override
    public List<Sizes> listSize(){
        return sizeRepository.findAll();
    }

    @Override
    public Optional<Sizes> getSize(int id){
        return sizeRepository.findById(id);
    }

    @Override
    public String insertSize(InsertSizeRequest req){
        Sizes sizes = new Sizes();
        sizes.setValue(req.getSizeValue());
        sizeRepository.save(sizes);

        return "Insert size success!";
    }

    @Override
    public String updateSize(int id, InsertSizeRequest req){
        Optional<Sizes> sizes = sizeRepository.findById(id);
        if(sizes.isPresent()){
            Sizes sizeEntity = sizes.get();
            sizeEntity.setValue(req.getSizeValue());

            sizeRepository.save(sizeEntity);
        }
        return "Update size success!";
    }

    @Override
    public String deleteSize(int id){
        Optional<Sizes> sizes = sizeRepository.findById(id);
        if(sizes.isPresent()){
            Sizes sizeEntity = sizes.get();
            sizeRepository.delete(sizeEntity);
            return "Delete size succsess!";
        } else
            return  "Size is not exist!";
    }
}
