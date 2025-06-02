package com.springBootBlogapis.Payloads;


import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class UserDTO {

    private int id;
    private String username;
    private Set<PostDTO> posts = new HashSet<>();


}
