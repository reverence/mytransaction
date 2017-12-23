package com.my.transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Created by tufei on 2017/12/23.
 */
@MyService
public class AddressService implements IAddressService {

    //@Transactional
    @Transactional(propagation = TransactionManager.PROAGTION_REQUIRED_NEW)
    public void insert() throws Exception{
        for(int i=1;i<3;i++){
            Address address = new Address();
            address.setCity("shanghai"+i);
            address.setId(i);
            Connection connection =  ConnectionHolder.getCurrentConnection();
            String sql = "insert into address(id,city)values(?,?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1,address.getId());
            ps.setString(2,address.getCity());
            ps.executeUpdate();
            ps.close();
        }
    }
}
