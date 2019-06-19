package qqhl.andaalarmmaster.service;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class UserInfo {
    @Id
    private String id;
    private Integer orgId;
    private String realName;
    private String address;
}
