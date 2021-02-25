package com.example.bbs.dto;

import com.example.bbs.entity.Comment;
import lombok.Data;

@Data
public class CommentDTO extends Comment {
    private String userDisplayName;
}
