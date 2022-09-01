package com.example.demo.src.userAddress;

import com.example.demo.src.userAddress.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserAddressDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetUserAddressRes> getUserAddresses(int userId){
        String getUserAddressQuery = "select userAddressId\n" +
                "    , case\n" +
                "        when addressCategory = 1 then '집'\n" +
                "        when addressCategory = 2 then '회사'\n" +
                "        when addressAlias is not null then addressAlias\n" +
                "        else addressName\n" +
                "        end as addressName\n" +
                "     , cityName, districtName, roadName, addressDetail\n" +
                "from UserAddress\n" +
                "where userId = ? and status = 'active'\n" +
                "order by addressCategory, updatedAt desc";
        int getUserAddressParams = userId;
        return this.jdbcTemplate.query(getUserAddressQuery,
                (rs, rowNum) -> new GetUserAddressRes(
                        rs.getInt("userAddressId"),
                        rs.getString("addressName"),
                        rs.getString("cityName"),
                        rs.getString("districtName"),
                        rs.getString("roadName"),
                        rs.getString("addressDetail")),
                getUserAddressParams);
    }


    public GetUserAddressDetailRes getUserAddress(int userAddressId){
        String getUserAddressQuery = "select addressName, cityName, districtName, roadName\n" +
                "     , addressDetail, directions, addressCategory, addressAlias, addressLatitude, addressLongitude\n" +
                "from UserAddress\n" +
                "where userAddressId = ? and status = 'active'";
        int getUserAddressParams = userAddressId;
        return this.jdbcTemplate.queryForObject(getUserAddressQuery,
                (rs, rowNum) -> new GetUserAddressDetailRes(
                        rs.getString("addressName"),
                        rs.getString("cityName"),
                        rs.getString("districtName"),
                        rs.getString("roadName"),
                        rs.getString("addressDetail"),
                        rs.getString("directions"),
                        rs.getInt("addressCategory"),
                        rs.getString("addressAlias"),
                        rs.getBigDecimal("addressLatitude"),
                        rs.getBigDecimal("addressLongitude")),
                getUserAddressParams);
    }

    public int createUserAddress(int userId, PostUserAddressReq postUserAddressReq){
        String createUserAddressQuery = "insert into UserAddress (userId, addressName, cityName, districtName, roadName\n" +
                "     , addressDetail, directions, addressCategory, addressAlias, addressLatitude, addressLongitude) \n" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Object[] createUserAddressParams = new Object[]{userId, postUserAddressReq.getAddressName(), postUserAddressReq.getCityName()
                , postUserAddressReq.getDistrictName(), postUserAddressReq.getRoadName(), postUserAddressReq.getAddressDetail(), postUserAddressReq.getDirections()
                , postUserAddressReq.getAddressCategory(), postUserAddressReq.getAddressAlias(), postUserAddressReq.getAddressLatitude(), postUserAddressReq.getAddressLongitude()};
        this.jdbcTemplate.update(createUserAddressQuery, createUserAddressParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public int modifyAddressDetail(PatchUserAddressReq patchUserAddressReq){
        String modifyAddressDetailQuery = "update UserAddress set addressDetail = ? where userAddressId = ?";
        Object[] modifyAddressDetailParams = new Object[]{patchUserAddressReq.getAddressDetail(), patchUserAddressReq.getUserAddressId()};
        return this.jdbcTemplate.update(modifyAddressDetailQuery, modifyAddressDetailParams);
    }

    public int modifyDirections(PatchUserAddressReq patchUserAddressReq){
        String modifyDirectionsQuery = "update UserAddress set directions = ? where userAddressId = ?";
        Object[] modifyDirectionsParams = new Object[]{patchUserAddressReq.getDirections(), patchUserAddressReq.getUserAddressId()};
        return this.jdbcTemplate.update(modifyDirectionsQuery, modifyDirectionsParams);
    }

    public int checkAddressCategory(int userAddressId){
        String checkAddressCategoryQuery = "select addressCategory from UserAddress where userAddressId = ?";
        int checkAddressCategoryParams = userAddressId;
        return this.jdbcTemplate.queryForObject(checkAddressCategoryQuery, int.class, checkAddressCategoryParams);
    }

    public int modifyAddressCategory(PatchUserAddressReq patchUserAddressReq){
        // 주소 카테고리가 3(기타)가 아니면 기존에 있던 addressAlias 값을 null로 변경 -> 기존 값은 놔두고 화면에 노출만 안되게 하는 것이 맞는 것인지 의문
        if(patchUserAddressReq.getAddressCategory() != 3){
            String modifyAddressAliasQuery = "update UserAddress set addressAlias = ? where userAddressId = ?";
            Object[] modifyAddressAliasParams = new Object[]{null, patchUserAddressReq.getUserAddressId()};
            this.jdbcTemplate.update(modifyAddressAliasQuery, modifyAddressAliasParams);
        }
        String modifyAddressCategoryQuery = "update UserAddress set addressCategory = ? where userAddressId = ?";
        Object[] modifyAddressCategoryParams = new Object[]{patchUserAddressReq.getAddressCategory(), patchUserAddressReq.getUserAddressId()};
        return this.jdbcTemplate.update(modifyAddressCategoryQuery, modifyAddressCategoryParams);
    }

    public int modifyAddressAlias(PatchUserAddressReq patchUserAddressReq){
        String modifyAddressAliasQuery = "update UserAddress set addressAlias = ? where userAddressId = ?";
        Object[] modifyAddressAliasParams = new Object[]{patchUserAddressReq.getAddressAlias(), patchUserAddressReq.getUserAddressId()};
        return this.jdbcTemplate.update(modifyAddressAliasQuery, modifyAddressAliasParams);
    }



}
