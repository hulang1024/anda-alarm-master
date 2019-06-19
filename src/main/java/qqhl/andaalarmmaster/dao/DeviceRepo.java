package qqhl.andaalarmmaster.dao;

import org.springframework.data.repository.CrudRepository;
import qqhl.andaalarmmaster.entity.Device;

public interface DeviceRepo extends CrudRepository<Device, Device.PK> {
}
