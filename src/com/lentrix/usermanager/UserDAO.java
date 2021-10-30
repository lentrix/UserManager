package com.lentrix.usermanager;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public static List<Role> getRoles(User user, Connection connection) throws SQLException {
        CallableStatement cs = connection.prepareCall("{call Roles_of_a_user (?)}");
        cs.setInt(1, user.getId());
        ResultSet rs = cs.executeQuery();

        ArrayList<Role> roles = new ArrayList();

        while(rs.next()) {
            roles.add(new Role(
                    rs.getInt("id"),
                    rs.getString("role"),
                    rs.getString("description")
            ));
        }

        return roles;
    }

    private static List<Permission> getPermissions(User user, Connection cxn) throws SQLException {
        CallableStatement cs = cxn.prepareCall("{call Permissions_of_a_user (?)}");
        cs.setInt(1, user.getId());
        ResultSet rs = cs.executeQuery();
        List<Permission> permissions = new ArrayList<Permission>();

        while(rs.next()) {
            Permission p = new Permission(rs.getInt("id"), rs.getString("permission"), rs.getString("remarks"));
            enlistPermission(permissions, p);
        }

        for(Role role: getRoles(user, cxn)) {
            for(Permission p: RoleDAO.getPermissions(role, cxn)) {
                enlistPermission(permissions, p);
            }
        }

        return permissions;
    }

    private static void enlistPermission(List<Permission> permissions, Permission p) {
        if(!permissions.contains(p)) {
            permissions.add(p);
        }
    }

    public static List<User> getAll(Connection conn) throws SQLException {
        CallableStatement cs = conn.prepareCall("{call Get_all_users}");
        ResultSet rs = cs.executeQuery();
        ArrayList<User> users = new ArrayList();
        while(rs.next()) {
            users.add(new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("fullname"),
                    rs.getString("designation"),
                    rs.getString("phone")
            ));
        }
        return users;
    }
}
