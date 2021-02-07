package com.example.bbs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class Tag implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String tagExplanation;
}
