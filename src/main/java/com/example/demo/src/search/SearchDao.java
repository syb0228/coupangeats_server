package com.example.demo.src.search;

import com.example.demo.src.search.model.GetSearchRes;
import com.example.demo.src.search.model.GetUserSearchRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class SearchDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetSearchRes> getSearchList(){
        String getSearchListQuery = "select searchId, searchName, Count(searchName) as searchCount, SUBSTRING(NOW(), 12, 5) as searchRankUpdateTime\n" +
                "from Search\n" +
                "where status = 'active'\n" +
                "group by searchName\n" +
                "order by COUNT(searchName) DESC\n" +
                "Limit 10";
        return this.jdbcTemplate.query(getSearchListQuery,
                (rs, rowNum) -> new GetSearchRes(
                        rs.getInt("searchId"),
                        rs.getString("searchName"),
                        rs.getInt("searchCount"),
                        rs.getString("searchRankUpdateTime")
                ));
    }

    public List<GetUserSearchRes> getUserSearchList(int userId){
        String getUserSearchListQuery = "select TB.searchId, TB.searchName, DATE_FORMAT(TB.updatedAt, '%m.%d') as searchTime\n" +
                "from (select ROW_NUMBER() over (PARTITION BY searchName order by updatedAt DESC) as rnum, searchId, searchName, updatedAt\n" +
                "from Search\n" +
                "where userId = ? and status = 'active') TB\n" +
                "where TB.rnum = 1\n" +
                "order by updatedAt DESC";
        int getUserSearchParams = userId;
        return this.jdbcTemplate.query(getUserSearchListQuery,
                (rs, rowNum) -> new GetUserSearchRes(
                        rs.getInt("searchId"),
                        rs.getString("searchName"),
                        rs.getString("searchTime")),
                getUserSearchParams);
    }

}
