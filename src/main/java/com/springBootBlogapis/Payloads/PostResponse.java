package com.springBootBlogapis.Payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private List<PostDTO> cont ;
    private int pageno;
    private int pagesiz;
    private long totalEl;
    private int totalpages;
    private boolean Last;








}
