package qqhl.andaalarmmaster.dao;

import org.springframework.data.repository.CrudRepository;
import qqhl.andaalarmmaster.entity.Device;
import qqhl.andaalarmmaster.entity.DeviceUserBinding;

import java.util.Optional;

public interface DeviceUserBindingRepo extends CrudRepository<DeviceUserBinding, Device.PK> {
    Iterable<DeviceUserBinding> findByUserId(String userId);
    Optional<DeviceUserBinding> findByPkDeviceIdAndPkDeviceType(String deviceId, String deviceType);
    Optional<DeviceUserBinding> findByUserIdAndPkDeviceType(String userId, String deviceType);
}
