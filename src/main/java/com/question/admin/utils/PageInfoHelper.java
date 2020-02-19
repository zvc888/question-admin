package com.question.admin.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.function.Function;
import java.util.stream.Collectors;

public class PageInfoHelper {

    public static <T, J> IPage<J> copyMap(IPage<T> source, Function<? super T, ? extends J> mapper) {
        IPage<J> newPage = new Page<>();
        newPage.setCurrent(source.getCurrent());
        newPage.setSize(source.getSize());
        newPage.setPages(source.getPages());
        newPage.setTotal(source.getTotal());
        newPage.setRecords(source.getRecords().stream().map(mapper).collect(Collectors.toList()));

        return newPage;
    }
}
