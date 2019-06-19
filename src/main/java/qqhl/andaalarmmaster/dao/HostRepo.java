package qqhl.andaalarmmaster.dao;

import org.springframework.data.repository.CrudRepository;
import qqhl.andaalarmmaster.entity.Host;

public interface HostRepo extends CrudRepository<Host, String> {
}