package com.example.demo.src.event;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.event.model.GetEventRes;
import com.example.demo.src.event.model.GetEventsRes;
import com.example.demo.src.event.model.PostEventReq;
import com.example.demo.src.event.model.PostEventRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class EventProvider {

    private final EventDao eventDao;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public EventProvider(EventDao eventDao){
        this.eventDao = eventDao;
    }

    public List<GetEventsRes> getEvents() throws BaseException{
        try{
            List<GetEventsRes> getEventsRes = eventDao.getEvents();
            return getEventsRes;
        }
        catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetEventRes getEvent(int eventId) throws BaseException {
        try {
            GetEventRes getEventRes = eventDao.getEvent(eventId);
            return getEventRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
