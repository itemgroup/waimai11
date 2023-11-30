package com.amx.luckincoffee.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Category implements Serializable {
    private Integer id;
    private String title;
    private String desc;
    private String indexImg;
}
