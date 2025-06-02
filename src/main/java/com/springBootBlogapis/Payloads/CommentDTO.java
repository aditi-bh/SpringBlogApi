package com.springBootBlogapis.Payloads;



import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentDTO {


    private long id;

    @NotEmpty(message = "name should not be empty")
    private String name;

    @NotEmpty(message = "email should not ne empty")
    @Email
    private String email;

    @NotEmpty
    @Size(min = 2 , max = 100 , message = "the comment should have at least 2 characters and at most 100 characters")
    private String comment;

}
