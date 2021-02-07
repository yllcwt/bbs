package com.example.bbs.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.bbs.dto.PaginationDTO;
import com.example.bbs.entity.Post;
import com.example.bbs.mapper.PostMapper;
import com.example.bbs.service.PostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yl
 * @since 2021-02-05
 */
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {

    @Autowired
    private PostMapper postMapper;

//    @Override
//    public void addPost(Post post) {
//        postMapper.insert(post);
//    }
    @Override
    public void insertOrUpdatePost(Post post) {
        if (post.getPostId() == null) {
            insertPost(post);
        }else {
//            updatePost(post);
        }
    }

    private void insertPost(Post post) {
        postMapper.insert(post);
    }

    @Override
    public PaginationDTO listPost(Integer pageIndex, Integer pageSize, String search, String tag, String category) {

        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalPage;


        QueryWrapper<Post> postQueryWrapper = new QueryWrapper<>();
        Integer totalCount = postMapper.selectCount(postQueryWrapper);

        if (totalCount % pageSize == 0) {
            totalPage = totalCount / pageSize;
        } else {
            totalPage = totalCount / pageSize + 1;
        }

        if (pageIndex < 1) {
            pageIndex = 1;
        }
        if (pageIndex > totalPage) {
            pageIndex = totalPage;
        }

        paginationDTO.setPagination(totalPage, pageIndex);
        Integer offset = pageIndex < 1 ? 0 : pageSize * (pageIndex - 1);

        List<Post> list = postMapper.selectPost(offset, pageSize);
        paginationDTO.setData(list);
        return paginationDTO;
    }

}
