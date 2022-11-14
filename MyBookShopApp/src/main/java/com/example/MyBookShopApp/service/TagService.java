package com.example.MyBookShopApp.service;

import com.example.MyBookShopApp.data.TagRepository;
import com.example.MyBookShopApp.struct.tag.TagEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TagService {

    private TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<TagEntity> getAllTags() {
       return tagRepository.findAll();
    }

    public Map<String, BigInteger> getMapOfTagPopularity(){
        List<Object[]> list = tagRepository.getAllTagsCount();
        Map<String, BigInteger> tagMap = new HashMap<>();
        for (Object[] ob : list){
            String key = (String)ob[0];
            BigInteger value = (BigInteger)ob[1];
            tagMap.put(key, value);
        }

        return rangePopularityToCategories(tagMap);
    }

    public Map<String, BigInteger> rangePopularityToCategories(Map<String, BigInteger> map){
        int range = getProportion();
        map.replaceAll((k,v) -> v = BigInteger.valueOf((int) Math.floor(v.doubleValue() / range)));
        return map;
    }

    public int getProportion(){
        int pr = getMaxTagCount() - getMinTagCount();
        return (int) Math.ceil((double) pr / 4);
    }

    public int getMaxTagCount() {
        return tagRepository.getMaxTagCount();
    }

    public int getMinTagCount() {
        return tagRepository.getMinTagCount();
    }
}
