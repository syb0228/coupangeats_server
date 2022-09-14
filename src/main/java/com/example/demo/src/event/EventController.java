package com.example.demo.src.event;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.event.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/app/events")
public class EventController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final EventProvider eventProvider;

    public EventController(EventProvider eventProvider){
        this.eventProvider = eventProvider;
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
    public BaseResponse<List<GetEventsRes>> getEvents(){
        try{
            // Get Events
            List<GetEventsRes> getEventsRes = eventProvider.getEvents();
            return new BaseResponse<>(getEventsRes);
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


}
