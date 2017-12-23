package com.my.transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Created by tufei on 2017/12/16.
 */
@MyService
public class UserService implements IUserService{

    @Transactional
    public void insertAndUpdate() throws Exception{
        User user = new User();
        user.setAge(20);
        user.setId(2);
        user.setSex("M");
        user.setUserName("zhangsan");
        insert(user);

        user.setId(1);
        user.setUserName("chengzhang");
        user.setAge(29);
        user.setSex("M");
        update(user);

        /**
         * case 1
         */
        //throw new RuntimeException("test");
        /**
         * case 2
         */
        IAddressService addressService =(IAddressService) ApplicationContextProvider.getBean("addressService");
        addressService.insert();
    }

    private void update(User user) throws Exception{
        Connection conn = ConnectionHolder.getCurrentConnection();
        String sql = "update users set user_name='"+user.getUserName()+"',sex='"+user.getSex()+"',age="+user.getAge()+" where id="+user.getId();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.executeUpdate();
        ps.close();
    }

    private void insert(User user) throws Exception{
        Connection conn = ConnectionHolder.getCurrentConnection();
        String sql = "insert into users(id,user_name,sex,age) values(?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1,user.getId());
        ps.setString(2,user.getUserName());
        ps.setString(3,user.getSex());
        ps.setInt(4,user.getAge());
        ps.executeUpdate();
        ps.close();
    }
}
