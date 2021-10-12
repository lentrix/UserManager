package com.lentrix.usermanager;

public class Permission {
    private String permission;
    private String remarks;

    public Permission(String permission, String remarks) {
        this.permission = permission;
        this.remarks = remarks;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
