package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.blog.entity.Tag;
import com.blog.mapper.TagMapper;
import com.blog.service.TagService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    private final TagMapper tagMapper;

    public TagServiceImpl(TagMapper tagMapper) {
        this.tagMapper = tagMapper;
    }

    @Override
    public List<Tag> listAll() {
        return tagMapper.selectList(new LambdaQueryWrapper<Tag>().orderByAsc(Tag::getId));
    }
}
