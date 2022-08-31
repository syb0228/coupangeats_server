package com.example.demo.src.event.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class PostEventReq {
    private String eventTitle;
    private String eventContent;
    private Timestamp expiredAt;
}
