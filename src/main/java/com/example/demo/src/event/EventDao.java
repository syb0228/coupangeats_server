package com.example.demo.src.event;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.event.model.GetEventRes;
import com.example.demo.src.event.model.GetEventsRes;
import com.example.demo.src.event.model.PatchEventReq;
import com.example.demo.src.event.model.PostEventReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Repository
public class EventDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetEventsRes> getEvents(){
        String getEventsQuery = "select Event.eventId, EI.eventImgUrl\n" +
                "     , case\n" +
                "         when Event.expiredAt is not null\n" +
                "             then CONCAT('~', REPLACE(SUBSTRING(Event.expiredAt, 6, 5), '-', '.'), '까지')\n" +
                "         else 0\n" +
                "         end as expiredAt\n" +
                "from Event\n" +
                "inner join EventImg EI on Event.eventId = EI.eventId\n" +
                "where isRepImg = 'Y' and Event.status = 'active'\n" +
                "  and (expiredAt is null or (expiredAt is not null and expiredAt > NOW()))";

        return this.jdbcTemplate.query(getEventsQuery,
                (rs,rowNum) -> new GetEventsRes(
                        rs.getInt("eventId"),
                        rs.getString("eventImgUrl"),
                        rs.getString("expiredAt"))
                );
    }

    public GetEventRes getEvent(int eventId){
        String getEventQuery = "select EI.eventId, eventTitle, eventImgUrl, eventContent\n" +
                "from Event\n" +
                "inner join EventImg EI on Event.eventId = EI.eventId\n" +
                "where Event.eventId = ? and Event.status = 'active' and isRepImg = 'N'";
        int getEventParams = eventId;

        return this.jdbcTemplate.queryForObject(getEventQuery,
                (rs,rowNum) -> new GetEventRes(
                        rs.getInt("eventId"),
                        rs.getString("eventTitle"),
                        rs.getString("eventImgUrl"),
                        rs.getString("eventContent")),
                getEventParams);
    }

    public int createEvent(PostEventReq postEventReq){
        String createEventQuery = "insert into Event (eventTitle, eventContent, expiredAt) VALUES (?, ?, ?)";
        Object[] createEventParams = new Object[]{postEventReq.getEventTitle(), postEventReq.getEventContent(), postEventReq.getExpiredAt()};
        this.jdbcTemplate.update(createEventQuery, createEventParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public int createEventImg(int eventId, PostEventReq postEventReq){
        String createEventImgQuery = "insert into EventImg (eventId, eventImgUrl, isRepImg) VALUES (?, ?, ?)";
        Object[] createEventImgParams1 = new Object[]{eventId, postEventReq.getRepEventImgUrl(), "Y"};
        Object[] createEventImgParams2 = new Object[]{eventId, postEventReq.getNoRepEventImgUrl(), "N"};
        this.jdbcTemplate.update(createEventImgQuery, createEventImgParams1);
        this.jdbcTemplate.update(createEventImgQuery, createEventImgParams2);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public int modifyEventTitle(PatchEventReq patchEventReq){
        String modifyEventTitleQuery = "update Event set eventTitle = ? where eventId = ?";
        Object[] modifyEventTitleParams = new Object[]{patchEventReq.getEventTitle(), patchEventReq.getEventId()};

        return this.jdbcTemplate.update(modifyEventTitleQuery, modifyEventTitleParams);
    }

    public int modifyEventContent(PatchEventReq patchEventReq){
        String modifyEventContentQuery = "update Event set eventContent = ? where eventId = ?";
        Object[] modifyEventContentParams = new Object[]{patchEventReq.getEventContent(), patchEventReq.getEventId()};

        return this.jdbcTemplate.update(modifyEventContentQuery, modifyEventContentParams);
    }

}
