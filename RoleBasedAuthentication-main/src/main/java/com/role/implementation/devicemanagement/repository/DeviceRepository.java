package com.role.implementation.devicemanagement.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.role.implementation.devicemanagement.model.Device;
import com.role.implementation.model.User;

public interface DeviceRepository extends JpaRepository<Device, Long> {

    List<Device> findByUser(User user);

    long countByUser(User user);

    long countByStatus(boolean status);

    long countByUserAndStatus(User user, boolean status);
}
