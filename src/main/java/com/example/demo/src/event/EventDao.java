package com.example.demo.src.event;

import com.example.demo.src.event.model.GetEventRes;
import com.example.demo.src.event.model.GetEventsRes;
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
}
