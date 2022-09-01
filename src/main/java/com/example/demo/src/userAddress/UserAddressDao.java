package com.example.demo.src.userAddress;

import com.example.demo.src.userAddress.model.GetUserAddressDetailRes;
import com.example.demo.src.userAddress.model.GetUserAddressRes;
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

}
