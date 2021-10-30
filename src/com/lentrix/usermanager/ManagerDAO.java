package com.lentrix.usermanager;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ManagerDAO {

    public static void addRoleToUser(User user, Role role, Connection conn) throws SQLException {
        CallableStatement cs = conn.prepareCall("{call Add_role_to_user (?,?)}");
        cs.setInt(1, user.getId());
        cs.setInt(2, role.getId());
        cs.executeUpdate();

        //insert user permissions based on the permissions of the newly added role
        List<Permission> permissions = RoleDAO.getPermissions(role, conn);
        for(Permission p: permissions) {
            addPermissionToUser(user, p, conn);
        }
    }

    public static void addPermissionToRole(Role role, Permission permission, Connection conn) throws SQLException {
        CallableStatement cs = conn.prepareCall("{call Add_permission_to_role (?,?)}");
        cs.setInt(1, role.getId());
        cs.setInt(2, permission.getId());
        cs.executeUpdate();
    }

    public static void addPermissionToUser(User user, Permission permission, Connection conn) throws SQLException {
        CallableStatement cs = conn.prepareCall("{call Add_permission_to_user (?,?)}");
        cs.setInt(1, user.getId());
        cs.setInt(2, permission.getId());
        cs.executeUpdate();
    }

    public static void removeRolePermission(Role role, Permission permission, Connection conn) throws SQLException {
        CallableStatement cs = conn.prepareCall("{call Remove_role_permission (?,?)}");
        cs.setInt(1, role.getId());
        cs.setInt(2, permission.getId());
        cs.executeUpdate();
    }

    public static void removeUserPermission(User user, Permission permission, Connection conn) throws SQLException {
        CallableStatement cs = conn.prepareCall("{call Remove_user_permission (?,?)}");
        cs.setInt(1, user.getId());
        cs.setInt(2, permission.getId());
        cs.executeUpdate();
    }

    public static void removeUserRole(User user, Role role, Connection conn) throws SQLException {
        CallableStatement cs = conn.prepareCall("{call Remove_user_role (?,?)}");
        cs.setInt(1, user.getId());
        cs.setInt(2, role.getId());
        cs.executeUpdate();
    }
}
