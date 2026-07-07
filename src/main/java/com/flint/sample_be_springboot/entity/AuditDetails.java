package com.flint.sample_be_springboot.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Embeddable
@Data
public class AuditDetails implements Serializable {

    private static final long serialVersionUID = 8485247275204527510L;

    public AuditDetails() {
        super();
    }

    @Column(name = "CR_USR")
    protected String createUser;

    @Column(name = "MD_USR")
    protected String modifyUser;

    @Column(name = "CR_TS")
    protected LocalDateTime createTime;

    @Column(name = "MD_TS")
    protected LocalDateTime modifyTime;

    public AuditDetails(String createUser, LocalDateTime createTime, String modifyUser, LocalDateTime modifyTime) {
        super();
        this.createUser = createUser;
        this.createTime = createTime;
        this.modifyUser = modifyUser;
        this.modifyTime = modifyTime;
    }

    public AuditDetails(String createUser, LocalDateTime createTime) {
        super();
        this.createUser = createUser;
        this.createTime = createTime;
    }

}
