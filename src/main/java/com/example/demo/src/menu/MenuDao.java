package com.example.demo.src.menu;

import com.example.demo.src.menu.model.GetMenuDetailOptionRes;
import com.example.demo.src.menu.model.GetMenuDetailRes;
import com.example.demo.src.menu.model.GetMenuImgRes;
import com.example.demo.src.menu.model.GetMenuRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class MenuDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetMenuImgRes> getMenuImgs(int menuId){
        String getMenuImgQuery = "select menuImgId, menuImgUrl, isRepImg\n" +
                "from MenuImg\n" +
                "where menuId = ?";
        int getMenuImgParams = menuId;
        return this.jdbcTemplate.query(getMenuImgQuery,
                (rs, rsNum) -> new GetMenuImgRes(
                        rs.getInt("menuImgId"),
                        rs.getString("menuImgUrl"),
                        rs.getString("isRepImg")),
                getMenuImgParams);
    }

    public List<GetMenuDetailOptionRes> getMenuDetailOptions(int menuDetailId){
        String getMenuDetailOptionQuery = "select menuDetailOptionId, optionName, optionPrice\n" +
                "from MenuDetailOption\n" +
                "where menuDetailId = ? and status = 'active'";
        int getMenuDetailOptionParams = menuDetailId;
        return this.jdbcTemplate.query(getMenuDetailOptionQuery,
                (rs, rsNum) ->  new GetMenuDetailOptionRes(
                        rs.getInt("menuDetailOptionId"),
                        rs.getString("optionName"),
                        rs.getInt("optionPrice")),
                getMenuDetailOptionParams);
    }

    public List<GetMenuDetailRes> getMenuDetails(int menuId){
        String getMenuDetailQuery = "select menuDetailId, detailName\n" +
                "     , ( case\n" +
                "         when required = 'Y' then '필수 선택'\n" +
                "         else 0 end\n" +
                "        ) as required\n" +
                "from MenuDetail\n" +
                "where menuId = ? and status = 'active'";
        int getMenuDetailParams = menuId;
        return this.jdbcTemplate.query(getMenuDetailQuery,
                (rs, rsNum) -> new GetMenuDetailRes(
                        rs.getInt("menuDetailId"),
                        rs.getString("detailName"),
                        rs.getString("required"),
                        getMenuDetailOptions(rs.getInt("menuDetailId"))),
                getMenuDetailParams);
    }

    public List<GetMenuRes> getMenus(int menuId){
        String getMenuQuery = "select menuName, menuContent, menuPrice\n" +
                "from Menu\n" +
                "where menuId = ? and status = 'active'";
        int getMenuParams = menuId;
        return this.jdbcTemplate.query(getMenuQuery,
                (rs, rsNum) -> new GetMenuRes(
                        getMenuImgs(menuId),
                        rs.getString("menuName"),
                        rs.getString("menuContent"),
                        rs.getInt("menuPrice"),
                        getMenuDetails(menuId)),
                getMenuParams);

    }

}
