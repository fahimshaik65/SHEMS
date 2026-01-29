package com.role.implementation.devicemanagement.service;

import java.util.List;
import com.role.implementation.devicemanagement.model.Device;

public interface DeviceService {

    void addDevice(Device device);

    List<Device> getDevicesForLoggedUser();

    // ✅ NEW — For Admin to view all devices
    List<Device> getAllDevices();

    void toggleDevice(Long id);

    void deleteDevice(Long id);

    // Dashboard + Home statistics
    long countTotalDevices();

    long countActiveDevices();
}
