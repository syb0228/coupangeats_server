package com.example.demo.src.menuCategory;

import com.example.demo.src.menuCategory.model.GetMenuCategoryRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class MenuCategoryDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetMenuCategoryRes> getMenuCategories(){
        String getMenuCategoriesQuery = "select MenuCategory.menuCategoryId, menuCategoryName, menuCategoryImgUrl\n" +
                "from MenuCategory\n" +
                "inner join MenuCategoryImg MCI on MenuCategory.menuCategoryId = MCI.menuCategoryId\n" +
                "where MenuCategory.status = 'active'";
        return this.jdbcTemplate.query(getMenuCategoriesQuery,
                (rs,rowNum) -> new GetMenuCategoryRes(
                        rs.getInt("menuCategoryId"),
                        rs.getString("menuCategoryName"),
                        rs.getString("menuCategoryImgUrl")
                ));
    }

    public List<GetMenuCategoryRes> getMenuCategoriesByName(String menuCategoryName){
        String getMenuCategoriesQuery = "select MenuCategory.menuCategoryId, menuCategoryName, menuCategoryImgUrl\n" +
                "from MenuCategory\n" +
                "inner join MenuCategoryImg MCI on MenuCategory.menuCategoryId = MCI.menuCategoryId\n" +
                "where MenuCategory.status = 'active' and menuCategoryName = ?";
        String getMenuCategoryParams = menuCategoryName;
        return this.jdbcTemplate.query(getMenuCategoriesQuery,
                (rs,rowNum) -> new GetMenuCategoryRes(
                        rs.getInt("menuCategoryId"),
                        rs.getString("menuCategoryName"),
                        rs.getString("menuCategoryImgUrl")),
                getMenuCategoryParams);
    }



}
