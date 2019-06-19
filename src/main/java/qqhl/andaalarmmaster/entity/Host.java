package qqhl.andaalarmmaster.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name="anda_tel")
@Entity
@Data
public class Host {
    public static final Host Null = new Host();
    @Id
    private String hostId;
    private String telNumber;
    private Date addTime;
}
