package qqhl.andaalarmmaster.dao;

import org.springframework.data.repository.CrudRepository;
import qqhl.andaalarmmaster.entity.AlarmNoticePhone;

import java.util.List;

public interface AlarmNoticePhoneRepo extends CrudRepository<AlarmNoticePhone, AlarmNoticePhone.PK> {
    List<AlarmNoticePhone> findByPkHostId(String userId);
}
