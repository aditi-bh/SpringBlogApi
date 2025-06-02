package com.springBootBlogapis.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResouceNotFoundException extends RuntimeException{
    private String resourcename;
    private String fieldname;
    private long fieldValue;

    public ResouceNotFoundException(String resourcename, String fieldname , long fieldValue ) {
        super(String.format("'%s' not found with %s : '%s'",resourcename,fieldname,fieldValue));
        this.resourcename = resourcename;
        this.fieldname = fieldname;
        this.fieldValue = fieldValue;

    }

    public String getResourcename() {
        return resourcename;
    }

    public String getFieldname() {
        return fieldname;
    }

    public long getFieldValue() {
        return fieldValue;
    }
}
