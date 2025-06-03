package com.springBootBlogapis.Service;

import com.springBootBlogapis.Payloads.PostDTO;
import com.springBootBlogapis.Payloads.PostResponse;

import java.util.List;

public interface PostService {

    PostDTO createPost(PostDTO postdto);

    PostResponse getallPosts(int pagenum , int pagesize , String sortby);

    PostDTO getpostbyId(long Id);

    PostDTO updatepost(PostDTO postdto , long Id);

    void DeletepostbyId(long Id);

    List<PostDTO> getPostsByUserId(Long userId);



}
