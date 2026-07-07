package com.flint.sample_be_springboot.util;

import com.flint.sample_be_springboot.config.UserContext;
import com.flint.sample_be_springboot.entity.AuditDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BaseService {

    @Autowired
    private UserContext userContext;

    public AuditDetails addAuditDetails(AuditDetails auditDetails) {
        String username = userContext.getUsername();
        if (auditDetails != null){
            auditDetails.setModifyUser(username != null ? username:"SYSTEM");
            auditDetails.setModifyTime(LocalDateTime.now());
            return auditDetails;
        } else {
            AuditDetails auditDetails1 = new AuditDetails();
            auditDetails1.setCreateUser(username != null ? username:"SYSTEM");
            auditDetails1.setCreateTime(LocalDateTime.now());
            auditDetails1.setModifyUser(username != null ? username:"SYSTEM");
            auditDetails1.setModifyTime(LocalDateTime.now());
            return auditDetails1;
        }
    }

    public String getUserName() {
        return userContext.getUsername();
    }

}

