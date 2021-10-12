package com.lentrix.usermanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;

public class Demo {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter user name:");
        String user1 = sc.nextLine();

        System.out.println("Enter password:");
        String pass1 = sc.nextLine();

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String conString = "jdbc:sqlserver://DESKTOP-JKEV4AP;Database=lntrxdb;IntegratedSecurity=true";
            Connection connection = DriverManager.getConnection(conString);

            User user = UserDAO.login(user1, pass1, connection);
            String[] perms = {"create-user","update-user","view-own-profile","view-other-profile","delete-user"};

            for(String p: perms) {
                System.out.println(ActiveUser.getUser().getUserName() + " can " + p + "? " + ActiveUser.getUser().can(p));
            }
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
