package com.springBootBlogapis.Controllers;

import com.springBootBlogapis.Entity.User;
import com.springBootBlogapis.Payloads.PostDTO;
import com.springBootBlogapis.Payloads.PostResponse;
import com.springBootBlogapis.Repository.UserRepo;
import com.springBootBlogapis.Service.PostService;
import com.springBootBlogapis.Utils.AppConstants;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/posts")
public class PostResource {

    private final PostService postserve;

    private final UserRepo userRepo;

    public PostResource(PostService postserve, UserRepo userRepo) {
        this.postserve = postserve;
        this.userRepo = userRepo;
    }

    //method for getting all posts that belong to a specific user
    @GetMapping("/user/posts")
    public ResponseEntity<List<PostDTO>> getMyPosts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepo.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<PostDTO> posts = postserve.getPostsByUserId((long) user.getId());
        return ResponseEntity.ok(posts);
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
