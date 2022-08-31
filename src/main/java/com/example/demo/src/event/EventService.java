package com.example.demo.src.event;

import com.example.demo.config.BaseException;
import com.example.demo.src.event.model.PatchEventReq;
import com.example.demo.src.event.model.PostEventReq;
import com.example.demo.src.event.model.PostEventRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class EventService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final EventDao eventDao;
    private final EventProvider eventProvider;

    @Autowired
    public EventService(EventDao eventDao, EventProvider eventProvider){
        this.eventDao = eventDao;
        this.eventProvider = eventProvider;
    }

    //POST
    public PostEventRes createEvent(PostEventReq postEventReq) throws BaseException {
        try{
            int eventId = eventDao.createEvent(postEventReq);
            int eventImgId = eventDao.createEventImg(eventId, postEventReq);
            return new PostEventRes(eventId, eventImgId);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //PATCH
    public void modifyEventTitle(PatchEventReq patchEventReq) throws BaseException {
        try{
            int result = eventDao.modifyEventTitle(patchEventReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_EVENTINFO);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyEventContent(PatchEventReq patchEventReq) throws BaseException {
        try{
            int result = eventDao.modifyEventContent(patchEventReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_EVENTINFO);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
