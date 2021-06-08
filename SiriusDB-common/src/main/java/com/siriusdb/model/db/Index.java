package com.siriusdb.model.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 索引数据结构
 * @author: ylx
 * @date: 2021/05/20 2：49 下午
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Index implements Serializable {

    private String name;

    /**
     * 假设我们的数据库内的索引只能建立在单个属性上，
     * 这个字段是该属性的id，见Attribute属性
     */
    private Integer attribute;

}