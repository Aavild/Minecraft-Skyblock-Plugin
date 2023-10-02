package io.github.aavild;

import java.io.Serializable;

public class Prefix implements Serializable {
    String PermissionNode;
    String Name;
    public Prefix(String PermissionNode, String Name)
    {
        this.PermissionNode = PermissionNode;
        this.Name = Name;
    }
}
