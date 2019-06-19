package qqhl.andaalarmmaster.dao;

import org.springframework.stereotype.Repository;
import qqhl.andaalarmmaster.service.UserInfo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class UserDao {
    @PersistenceContext
    private EntityManager entityManager;

    public UserInfo findById(String id) {
        Query query = entityManager.createNativeQuery(
            "select real_name, address, (select org_id from org_device where org_device.device_code=user.device_code) org_id from user where id=?");
        query.setParameter(1, id);
        List ret = query.getResultList();
        if (!ret.isEmpty()) {
            Object[] cols = (Object[]) ret.get(0);
            UserInfo userInfo = new UserInfo();
            userInfo.setId(id);
            userInfo.setRealName((String) cols[0]);
            userInfo.setAddress((String) cols[1]);
            userInfo.setOrgId((Integer) cols[2]);
            return userInfo;
        } else {
            return null;
        }
    }
}
