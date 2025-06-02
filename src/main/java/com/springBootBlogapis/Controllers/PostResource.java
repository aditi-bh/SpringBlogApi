package com.springBootBlogapis.Controllers;

import com.springBootBlogapis.Payloads.PostDTO;
import com.springBootBlogapis.Payloads.PostResponse;
import com.springBootBlogapis.Service.PostService;
import com.springBootBlogapis.Utils.AppConstants;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/posts")
public class PostResource {

    private final PostService postserve;

    public PostResource(PostService postserve) {
        this.postserve = postserve;
    }



    //add posts to the database
    @PostMapping
    public ResponseEntity<PostDTO> createPost(@Valid  @RequestBody PostDTO postdto){
        return new ResponseEntity<>(postserve.createPost(postdto), HttpStatus.CREATED);


    }




    //get all posts
    @GetMapping
    public PostResponse getallpos(@RequestParam(value = "pagenum", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pagenum ,
                                  @RequestParam(value = "pagesize" , defaultValue = AppConstants.DEFAULT_PAGE_SIZE , required = false) int pagesize,
                                  @RequestParam (value = "sortby" , defaultValue = AppConstants.DEFAULT_SORTING , required = false) String sortby){
        return postserve.getallPosts(pagenum,pagesize,sortby);

    }

    //get a post which is id specific
    @GetMapping("/{Id}")
    public ResponseEntity<PostDTO> getpostbyId(@PathVariable(name ="Id") long Id){
        return ResponseEntity.ok(postserve.getpostbyId(Id));
    }


    //update your post
    @PutMapping("/{Id}")
    public ResponseEntity<PostDTO> updatepost(@Valid  @RequestBody PostDTO postdto, @PathVariable (name="Id") long Id){
       PostDTO postres =  postserve.updatepost(postdto , Id);
       return new ResponseEntity<>(postres , HttpStatus.OK);

    }

    //delete your post
    @DeleteMapping("/{Id}")
    public ResponseEntity<String> deletepost(@PathVariable (name="Id") long Id){
        postserve.DeletepostbyId(Id);
        return new ResponseEntity<>("post deleted successfully" , HttpStatus.OK);


    }




}
