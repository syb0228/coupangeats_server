package com.example.demo.src.event;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.event.model.GetEventRes;
import com.example.demo.src.event.model.PostEventReq;
import com.example.demo.src.event.model.PostEventRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.POST_EVENT_EMPTY_TITLE;
import static com.example.demo.config.BaseResponseStatus.POST_EVENT_INVALID_EXPIREDAT;

@RestController
@RequestMapping("/app/events")
public class EventController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final EventProvider eventProvider;

    @Autowired
    private final EventService eventService;

    @Autowired
    private final JwtService jwtService;

    public EventController(EventProvider eventProvider, EventService eventService, JwtService jwtService){
        this.eventProvider = eventProvider;
        this.eventService = eventService;
        this.jwtService = jwtService;
    }

    /**
     * 이벤트 조회 API
     * [GET] /events
     * 이벤트 번호 검색 조회 API
     * [GET] /users? eventId=
     * @return BaseResponse<List<GetEventRes>>
     */
    //Query String
    @ResponseBody
    @GetMapping("") // (GET) 127.0.0.1:9000/app/events
    public BaseResponse<List<GetEventRes>> getEvents(){
        try{
            // Get Events
            List<GetEventRes> getEventRes = eventProvider.getEvents();
            return new BaseResponse<>(getEventRes);
        } catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 특정 이벤트 조회 API
     * [GET] /events/:eventId
     * @return BaseResponse<GetEventRes>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/{eventId}") // (GET) 127.0.0.1:9000/app/events/:eventId
    public BaseResponse<GetEventRes> getEvent(@PathVariable("eventId") int eventId){
        // Get Event
        try{
            GetEventRes getEventRes = eventProvider.getEvent(eventId);
            return new BaseResponse<>(getEventRes);
        } catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 이벤트 등록 API
     * [POST] /events
     * @return BaseResponse<PostEventRes>
     */
    // Body
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostEventRes> createEvent(@RequestBody PostEventReq postEventReq){
        // 이벤트 제목 유효성 검사
        if(postEventReq.getEventTitle() == null){ // null 값
            return new BaseResponse<>(POST_EVENT_EMPTY_TITLE);
        }

        if(postEventReq.getExpiredAt() != null){
            // 이벤트 만료일 유효성 검사
            Date date = new Date();
            Timestamp ts = new Timestamp(date.getTime());
            Timestamp expiredAt = postEventReq.getExpiredAt();
            if(expiredAt.before(ts)){ // 만료일이 현재 날짜보다 빠른 경우
                return new BaseResponse<>(POST_EVENT_INVALID_EXPIREDAT);
            }
        }

        try{
            PostEventRes postEventRes = eventService.createEvent(postEventReq);
            return new BaseResponse<>(postEventRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }


}
