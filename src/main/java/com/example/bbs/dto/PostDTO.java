package com.example.bbs.dto;

import com.example.bbs.entity.Category;
import com.example.bbs.entity.Post;
import com.example.bbs.entity.Tag;
import com.example.bbs.entity.User;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PostDTO extends Post {
    private User user;

    private Category category;

    private List<Tag> tagList = new ArrayList<>();

//    private List<Comment> comments = new ArrayList<>();
}
