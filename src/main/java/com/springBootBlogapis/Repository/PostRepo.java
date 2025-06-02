package com.springBootBlogapis.Repository;

import com.springBootBlogapis.Entity.Post;
import com.springBootBlogapis.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepo extends JpaRepository<Post,Long> {


}
