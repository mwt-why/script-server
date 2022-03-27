package com.mwt.beans.task;

import com.mwt.beans.res.Account;
import com.mwt.beans.res.Role;
import lombok.Data;

import java.util.List;

@Data
public class TaskAccount extends Account {
    private int curRoleIndex;

    private List<Role> roles;

}
