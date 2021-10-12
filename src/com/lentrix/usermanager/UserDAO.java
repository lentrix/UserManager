package com.lentrix.usermanager;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public static User login(String userName, String password, Connection cxn) throws Exception {
        CallableStatement cs = cxn.prepareCall("{call usrLogin (?,?)}");
        cs.setString(1, userName);
        cs.setString(2,password);
        ResultSet rs = cs.executeQuery();

        if(!rs.next()) {
            throw new Exception("Invalid user credentials");
        }

        User user = new User(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("fullname"),
                rs.getString("designation"),
                rs.getString("phone")
        );

        user.setPermissions(getPermissions(user, cxn));

        ActiveUser.setUser(user);

        return user;
    }

    private static List<Permission> getPermissions(User user, Connection cxn) throws Exception {
        CallableStatement cs = cxn.prepareCall("{call Permissions_of_a_user (?)}");
        cs.setInt(1, user.getId());
        ResultSet rs = cs.executeQuery();
        List<Permission> permissions = new ArrayList<Permission>();

        while(rs.next()) {
            permissions.add(new Permission(rs.getString("permission"), rs.getString("remarks")));
        }

        return permissions;
    }
}
