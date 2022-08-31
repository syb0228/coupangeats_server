package com.example.demo.src.event;

import com.example.demo.src.event.model.GetEventRes;
import com.example.demo.src.event.model.PostEventReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class EventDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetEventRes> getEvents(){
        String getEventsQuery = "select * from Event";

        return this.jdbcTemplate.query(getEventsQuery,
                (rs,rowNum) -> new GetEventRes(
                        rs.getInt("eventId"),
                        rs.getString("eventTitle"),
                        rs.getString("eventContent"),
                        rs.getTimestamp("expiredAt"))
                );
    }

    public GetEventRes getEvent(int eventId){
        String getEventQuery = "select * from Event where eventId = ?";
        int getEventParams = eventId;
        return this.jdbcTemplate.queryForObject(getEventQuery,
                (rs,rowNum) -> new GetEventRes(
                        rs.getInt("eventId"),
                        rs.getString("eventTitle"),
                        rs.getString("eventContent"),
                        rs.getTimestamp("expiredAt")),
                getEventParams);
    }

    public int createEvent(PostEventReq postEventReq){
        String createEventQuery = "insert into Event (eventTitle, eventContent, expiredAt) VALUES (?, ?, ?)";
        Object[] createEventParams = new Object[]{postEventReq.getEventTitle(), postEventReq.getEventContent(), postEventReq.getExpiredAt()};
        this.jdbcTemplate.update(createEventQuery, createEventParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }


}
