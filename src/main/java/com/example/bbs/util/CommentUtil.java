package com.example.bbs.util;


import com.example.bbs.entity.Comment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <pre>
 *     拼装回帖
 * </pre>
 *
 * @author : saysky
 * @date : 2018/7/12
 */
public class CommentUtil {

    /**
     * 获取回帖树
     *
     * @param commentsRoot commentsRoot
     * @return List
     */
    public static List<Comment> getComments(List<Comment> commentsRoot) {
        if(commentsRoot == null || commentsRoot.size() == 0) {
            return Collections.emptyList();
        }
        List<Comment> commentsResult = new ArrayList<>();
        for (Comment comment : commentsRoot) {
            if (comment.getCommentParentId() == 0) {
                commentsResult.add(comment);
            }
        }
        for (Comment comment : commentsResult) {
            comment.setChildComments(getChild(comment.getCommentId(), commentsRoot));
        }
        return commentsResult;
    }

    /**
     * 获取回帖的子回帖
     *
     * @param id           回帖编号
     * @param commentsRoot commentsRoot
     * @return List
     */
    private static List<Comment> getChild(Integer id, List<Comment> commentsRoot) {
        List<Comment> commentsChild = new ArrayList<>();
        for (Comment comment : commentsRoot) {
            if (comment.getCommentParentId() != 0) {
                if (comment.getCommentParentId().equals(id)) {
                    commentsChild.add(comment);
                }
            }
        }
        for (Comment comment : commentsChild) {
            if (comment.getCommentParentId() != 0) {
                comment.setChildComments(getChild(comment.getCommentId(), commentsRoot));
            }
        }
        if (commentsChild.size() == 0) {
            return null;
        }
        return commentsChild;
    }
}
