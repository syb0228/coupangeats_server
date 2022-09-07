package com.example.demo.src.mainMenuCategory;

import com.example.demo.src.mainMenuCategory.model.GetMainMenuCategoryRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class MainMenuCategoryDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetMainMenuCategoryRes> getMainMenuCategories(){
        String getMainMenuCategoriesQuery = "select MainMenuCategory.mainMenuCategoryId, mainMenuCategoryName, mainMenuCategoryImgUrl\n" +
                "from MainMenuCategory\n" +
                "inner join MainMenuCategoryImg MCI on MainMenuCategory.mainMenuCategoryId = MCI.mainMenuCategoryId\n" +
                "where MainMenuCategory.status = 'active'";
        return this.jdbcTemplate.query(getMainMenuCategoriesQuery,
                (rs,rowNum) -> new GetMainMenuCategoryRes(
                        rs.getInt("mainMenuCategoryId"),
                        rs.getString("mainMenuCategoryName"),
                        rs.getString("mainMenuCategoryImgUrl")
                ));
    }

    public List<GetMainMenuCategoryRes> getMainMenuCategoriesByName(String mainMenuCategoryName){
        String getMainMenuCategoriesQuery = "select MainMenuCategory.mainMenuCategoryId, mainMenuCategoryName, mainMenuCategoryImgUrl\n" +
                "from MainMenuCategory\n" +
                "inner join MainMenuCategoryImg MCI on MainMenuCategory.mainMenuCategoryId = MCI.mainMenuCategoryId\n" +
                "where MainMenuCategory.status = 'active' and mainMenuCategoryName = ?";
        String getMainMenuCategoryParams = mainMenuCategoryName;
        return this.jdbcTemplate.query(getMainMenuCategoriesQuery,
                (rs,rowNum) -> new GetMainMenuCategoryRes(
                        rs.getInt("mainMenuCategoryId"),
                        rs.getString("mainMenuCategoryName"),
                        rs.getString("mainMenuCategoryImgUrl")),
                getMainMenuCategoryParams);
    }



}
