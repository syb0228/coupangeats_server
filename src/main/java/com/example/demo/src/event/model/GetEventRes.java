package com.example.demo.src.event.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetEventRes {
    private int eventId;
    private String eventTitle;
    private String eventImgUrl;
    private String eventContent;
}
