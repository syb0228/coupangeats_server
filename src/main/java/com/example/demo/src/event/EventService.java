package com.example.demo.src.event;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.event.model.PostEventReq;
import com.example.demo.src.event.model.PostEventRes;
import com.example.demo.src.user.model.PostUserRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class EventService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final EventDao eventDao;
    private final EventProvider eventProvider;
    private final JwtService jwtService;

    @Autowired
    public EventService(EventDao eventDao, EventProvider eventProvider, JwtService jwtService){
        this.eventDao = eventDao;
        this.eventProvider = eventProvider;
        this.jwtService = jwtService;
    }

    //POST
    public PostEventRes createEvent(PostEventReq postEventReq) throws BaseException {
        try{
            int eventId = eventDao.createEvent(postEventReq);
            return new PostEventRes(eventId);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
