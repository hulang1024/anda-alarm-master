package qqhl.andaalarmmaster.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Component
public class ChannelQuery {
    @PersistenceContext
    private EntityManager entityManager;

    public String getChannelByHostId(String hostId) {
        Query query = entityManager.createNativeQuery(
            "select org_id from ided_device where device_type='telephone' and device_id=?");
        query.setParameter(1, hostId);
        Integer orgId = (Integer)query.getSingleResult();
        return String.valueOf(orgId != null ? orgId : 1);
    }
}
