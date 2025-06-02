package com.springBootBlogapis.Payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;


import java.util.HashSet;
import java.util.Set;

@Data
public class PostDTO {

    private Long id;



    @NotEmpty
    @Size(min = 2 , message = "Title should have at least 2 characters")
    private String title;

    @NotEmpty
    @Size(min = 50 , message = "Description should have at least 50 characters")
    private String description;

    @NotEmpty
    @Size(min = 100 , message = "Your blog should have at least 100 charcters")
    private String content;


    private Set<CommentDTO> comments = new HashSet<>();




}
