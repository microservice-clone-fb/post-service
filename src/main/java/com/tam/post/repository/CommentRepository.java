package com.tam.post.repository;

import com.tam.post.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {
    Page<Comment> findByPostIdAndParentCommentIdIsNull(String postId, Pageable pageable);
    List<Comment> findByParentCommentId(String parentCommentId);
    long countByPostId(String postId);
    void deleteByPostId(String postId);
}

