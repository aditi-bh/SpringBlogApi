package com.springBootBlogapis.Service;

import com.springBootBlogapis.Payloads.CommentDTO;

import java.util.List;

public interface CommentsService {

    CommentDTO CreateComment(long PostId , CommentDTO commentdto);

    List<CommentDTO> getcommentsbypostId(long PostId);

    CommentDTO getcommentbyId(long PostId , long commentId);

    CommentDTO updateComment(long PostId , long commentId, CommentDTO commentRequest);

    void DeleteComment(long PostId , long CommentId);


}
