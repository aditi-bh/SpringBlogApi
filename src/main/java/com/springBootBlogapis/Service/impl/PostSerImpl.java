package com.springBootBlogapis.Service.impl;

import com.springBootBlogapis.Entity.Comments;
import com.springBootBlogapis.Entity.Post;
import com.springBootBlogapis.Exceptions.ResouceNotFoundException;
import com.springBootBlogapis.Payloads.CommentDTO;
import com.springBootBlogapis.Payloads.PostDTO;
import com.springBootBlogapis.Payloads.PostResponse;
import com.springBootBlogapis.Repository.PostRepo;
import com.springBootBlogapis.Service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostSerImpl implements PostService {

    private final PostRepo postrepo;

    public PostSerImpl(PostRepo postrepo) {
        this.postrepo = postrepo;
    }

    @Override
    public PostDTO createPost(PostDTO postdto) {

        Post post = maptoEntity(postdto);

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
     private  Post maptoEntity(PostDTO postdto){
         Post post = new Post();//converting dto to entity
         post.setTitle(postdto.getTitle());
         post.setDescription(postdto.getDescription());
         post.setContent(postdto.getContent());
         return post;

     }
}
