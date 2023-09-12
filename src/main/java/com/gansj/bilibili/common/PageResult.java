package com.gansj.bilibili.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PageResult<T> {

    //总数目
    Long total;

    //查出的列表
    List<T> list;
}
