package com.springBootBlogapis.Controllers;


import com.springBootBlogapis.Payloads.CommentDTO;
import com.springBootBlogapis.Service.CommentsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class CommentResource {

    private CommentsService commserve;

    public CommentResource(CommentsService commserve) {
        this.commserve = commserve;
    }

    @PostMapping("/posts/{PostId}/Comments")
    public ResponseEntity<CommentDTO> createComment(@PathVariable(value = "PostId") long PostId,@Valid  @RequestBody CommentDTO commdt) {
        return new ResponseEntity<>(commserve.CreateComment(PostId, commdt), HttpStatus.CREATED);
    }

    @GetMapping("/posts/{PostId}/Comments")
    public List<CommentDTO> getcommentsbyPostId(@PathVariable(value = "PostId") long PostId) {


        return commserve.getcommentsbypostId(PostId);

    }


    @GetMapping("/posts/{PostId}/Comments/{CommentId}")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable(value = "PostId") long PostId, @PathVariable(value = "CommentId") long CommentId) {
        CommentDTO cometdto = commserve.getcommentbyId(PostId, CommentId);
        return new ResponseEntity<>(cometdto, HttpStatus.OK);

    }

    @PutMapping("/posts/{PostId}/Comments/{CommentId}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable(value = "PostId") long PostId, @PathVariable(value = "CommentId") long CommentId,@Valid  @RequestBody CommentDTO commentdto) {

        CommentDTO updatedcomment = commserve.updateComment(PostId, CommentId,commentdto);
        return  new ResponseEntity<>(updatedcomment,HttpStatus.OK);
    }

    @DeleteMapping("posts/{PostId}/Comments/{CommentId}")
    public ResponseEntity<String> DeleteComment(@PathVariable(value = "PostId") long PostId,@PathVariable(value = "CommentId") long CommentId){
        commserve.DeleteComment(PostId,CommentId);
        return new ResponseEntity<>("Comment deleted successfully", HttpStatus.OK);
    }
}