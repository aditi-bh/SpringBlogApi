package com.springBootBlogapis.Repository;

import com.springBootBlogapis.Entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepo extends JpaRepository<Comments , Long> {

    List<Comments> findByPostId(long PostId);



}
