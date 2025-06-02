package com.springBootBlogapis.Service.impl;

import com.springBootBlogapis.Entity.Comments;
import com.springBootBlogapis.Entity.Post;
import com.springBootBlogapis.Exceptions.BlogAPIException;
import com.springBootBlogapis.Exceptions.ResouceNotFoundException;
import com.springBootBlogapis.Payloads.CommentDTO;
import com.springBootBlogapis.Repository.CommentRepo;
import com.springBootBlogapis.Repository.PostRepo;
import com.springBootBlogapis.Service.CommentsService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class CommentSerImpl implements CommentsService {

    private CommentRepo commrepo;
    private PostRepo postrep ;

    public CommentSerImpl(CommentRepo commrepo, PostRepo postrep) {
        this.commrepo = commrepo;
        this.postrep = postrep;
    }

    @Override
    public CommentDTO CreateComment(long PostId, CommentDTO commentdto) {
        Comments Comet = ComToEntity(commentdto);
        Post post = postrep.findById(PostId).orElseThrow(()-> new ResouceNotFoundException("Post","Id",PostId));
        Comet.setPost(post);
        Comments newcomment =  commrepo.save(Comet);


        return ComToDto(newcomment);
    }

    @Override
    public List<CommentDTO> getcommentsbypostId(long PostId) {
        List<Comments> commentsList = commrepo.findByPostId(PostId);

        return commentsList.stream().map(comments -> ComToDto(comments)).collect(Collectors.toList());
    }

    @Override
    public CommentDTO getcommentbyId(long PostId, long commentId) {
        Post post = postrep.findById(PostId).orElseThrow(()-> new ResouceNotFoundException("Post","Id",PostId));

        Comments commets = commrepo.findById(commentId).orElseThrow(()->new ResouceNotFoundException("comment","Id",commentId));
        if(!commets.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment does not belong to a post");
        }
        return ComToDto(commets);
    }

    @Override
    public CommentDTO updateComment(long PostId, long commentId, CommentDTO commentRequest) {
        Post post = postrep.findById(PostId).orElseThrow(()-> new ResouceNotFoundException("Post","Id",PostId));

        Comments commets = commrepo.findById(commentId).orElseThrow(()->new ResouceNotFoundException("comment","Id",commentId));

        if(!commets.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment does not belong to a post");
        }
        commets.setName(commentRequest.getName());
        commets.setEmail(commentRequest.getEmail());
        commets.setComment(commentRequest.getComment());

        Comments Updatedcomment = commrepo.save(commets);


        return ComToDto(Updatedcomment) ;
    }

    @Override
    public void DeleteComment(long PostId, long CommentId) {
        Post post = postrep.findById(PostId).orElseThrow(()-> new ResouceNotFoundException("Post","Id",PostId));
        Comments commets = commrepo.findById(CommentId).orElseThrow(()->new ResouceNotFoundException("comment","Id",CommentId));

        commrepo.delete(commets);

    }


    private CommentDTO ComToDto(Comments comments){
        CommentDTO commdto = new CommentDTO();
        commdto.setId(comments.getCom_id());
        commdto.setName(comments.getName());
        commdto.setEmail(comments.getEmail());
        commdto.setComment(comments.getComment());
        return commdto;
    }

    private Comments ComToEntity(CommentDTO commentdto){
        Comments commen = new Comments();
        commen.setCom_id(commentdto.getId());
        commen.setName(commentdto.getName());
        commen.setEmail(commentdto.getEmail());
        commen.setComment(commentdto.getComment());
        return commen;
    }
}
