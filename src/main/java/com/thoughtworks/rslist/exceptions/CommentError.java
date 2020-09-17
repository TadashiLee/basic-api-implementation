package com.thoughtworks.rslist.exceptions;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentError {
    private String error;
}
