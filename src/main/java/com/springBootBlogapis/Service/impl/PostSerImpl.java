package com.springBootBlogapis.Service.impl;

import com.springBootBlogapis.Entity.Comments;
import com.springBootBlogapis.Entity.Post;
import com.springBootBlogapis.Entity.User;
import com.springBootBlogapis.Exceptions.ResouceNotFoundException;
import com.springBootBlogapis.Payloads.CommentDTO;
import com.springBootBlogapis.Payloads.PostDTO;
import com.springBootBlogapis.Payloads.PostResponse;
import com.springBootBlogapis.Repository.PostRepo;
import com.springBootBlogapis.Repository.UserRepo;
import com.springBootBlogapis.Service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostSerImpl implements PostService {

    private final PostRepo postrepo;

    private final UserRepo userRepo;

    public PostSerImpl(PostRepo postrepo, UserRepo userRepo) {
        this.postrepo = postrepo;
        this.userRepo = userRepo;
    }

    @Override
    public PostDTO createPost(PostDTO postdto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // username or email based on your logic

        User user = userRepo.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Post post = maptoEntity(postdto, user);




        Post newpos = postrepo.save(post);
        PostDTO postresponse = maptodto(newpos);
        return  postresponse;


    }

    @Override
    public PostResponse getallPosts(int pagenum , int pagesize, String sortby) {



        Pageable pageable = PageRequest.of(pagenum, pagesize , Sort.by(sortby)); //pagination support

        Page<Post> posts = postrepo.findAll(pageable);
        List<Post> listofposts = posts.getContent();
        List<PostDTO> cont = listofposts.stream().map(post -> maptodto(post)).collect(Collectors.toList());


        PostResponse postres = new PostResponse();
        postres.setCont(cont);
        postres.setPageno(posts.getNumber());
        postres.setPagesiz(posts.getSize());
        postres.setTotalEl(posts.getTotalElements());
        postres.setTotalpages(posts.getTotalPages());
        postres.setLast(posts.isLast());

        return postres;


    }

    @Override
    public PostDTO getpostbyId(long Id) {
        Post post = postrepo.findById(Id).orElseThrow(() -> new ResouceNotFoundException("Post" , "Id" , Id));

        return maptodto(post);
    }

    @Override
    public PostDTO updatepost(PostDTO postdto, long Id) {
        Post post = postrepo.findById(Id).orElseThrow(() -> new ResouceNotFoundException("Post" , "Id" , Id));
        post.setTitle(postdto.getTitle());
        post.setDescription(postdto.getDescription());
        post.setContent(postdto.getContent());

        Post updatedpost = postrepo.save(post);

        return maptodto(updatedpost);

    }

    @Override
    public void DeletepostbyId(long Id) {
        Post post = postrepo.findById(Id).orElseThrow(() -> new ResouceNotFoundException("Post" , "Id" , Id));
         postrepo.delete(post);
    }

    @Override
    public List<PostDTO> getPostsByUserId(Long userId) {
        List<Post> posts = postrepo.findByUserId(userId);
        return posts.stream().map(this::maptodto).collect(Collectors.toList());
    }

    private CommentDTO mapToCommentDto(Comments comment) {
        CommentDTO dto = new CommentDTO();
        dto.setName(comment.getName());
        dto.setEmail(comment.getEmail());
        dto.setComment(comment.getComment());
        return dto;
    }


    private PostDTO maptodto(Post post){
        PostDTO posdto = new PostDTO();
        posdto.setId(post.getId());
        posdto.setTitle(post.getTitle());
        posdto.setDescription(post.getDescription());
        posdto.setContent(post.getContent());
        Set<CommentDTO> commentDTOs = post.getCommentz()
                .stream()
                .map(this::mapToCommentDto)
                .collect(Collectors.toSet());

        posdto.setComments(commentDTOs);
        return posdto;
    }
     private  Post maptoEntity(PostDTO postdto, User user){
         Post post = new Post();//converting dto to entity
         post.setTitle(postdto.getTitle());
         post.setDescription(postdto.getDescription());
         post.setContent(postdto.getContent());
         post.setUser(user);
         return post;

     }
}
