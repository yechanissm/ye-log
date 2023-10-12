package com.yelog.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@ToString
@Getter
public class PostCreate {

    private String title;

    private String content;


}
