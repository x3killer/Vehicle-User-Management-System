package com.htik.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


@Service
public class UserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean isValidICNumber(String icNumber) {
        return icNumber.matches("\\d{6}-\\d{2}-\\d{4}"); // Example format validation
    }

    public boolean checkUserExists(String userID) {
        String checkSql = "SELECT COUNT(*) FROM User WHERE user_id = ?";
        int count = jdbcTemplate.queryForObject(checkSql, new Object[]{userID}, Integer.class);
        return count > 0;
    }

    public boolean checkPlateAvailable(String plateNo) {
        String checkSql = "SELECT COUNT(*) FROM Vehicle WHERE plateNo = ?";
        int count = jdbcTemplate.queryForObject(checkSql, new Object[]{plateNo}, Integer.class);
        return count == 0;
    }

    public boolean insertVehicle(String plateNo, String vehicleType, String brand, String model, String userID) {
        String insertSql = "INSERT INTO vehicle (plateNo, vehicleType, brand, model, user_id) VALUES (?, ?, ?, ?, ?)";
        int rowsAffected = jdbcTemplate.update(insertSql, plateNo, vehicleType, brand, model, userID);
        return rowsAffected > 0;
    }

    public boolean insertUser(String userID, String firstName, String lastName, String phoneNo) {
        String insertSql = "INSERT INTO User (user_id, first_name, last_name, phoneNo) VALUES (?, ?, ?, ?)";
        int rowsAffected = jdbcTemplate.update(insertSql, userID, firstName, lastName, phoneNo);
        return rowsAffected > 0;
    }

    public List<User> getAllUsers() {
        String sql = "SELECT user_id, first_name, last_name, phoneNo FROM User";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            User user = new User();
            user.setUserId(rs.getString("user_id"));
            user.setFirstName(rs.getString("first_name"));
            user.setLastName(rs.getString("last_name"));
            user.setPhoneNo(rs.getString("phoneNo"));
            return user;
        });
    }

    public List<vehicle> getAllVehicle() {
        String sql = "SELECT plateNo, vehicleType, brand, model, user_id FROM vehicle";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            vehicle vehicle = new vehicle();
            vehicle.setPlateNo(rs.getString("plateNo"));
            vehicle.setVehicleType(rs.getString("vehicleType"));
            vehicle.setBrand(rs.getString("brand"));
            vehicle.setModel(rs.getString("model"));
            vehicle.setUserId(rs.getString("user_id"));

            return vehicle;
        });
    }

    public vehicleuser getUserWithVehicleDetails(String userIC) {
        String userSql = "SELECT * FROM User WHERE user_id = ?";
        String vehicleSql = "SELECT * FROM Vehicle WHERE user_id = ?";
        vehicleuser details = new vehicleuser();
        details.setUserID(userIC);

        try {
            // Fetch user details
            jdbcTemplate.queryForObject(userSql, new Object[]{userIC}, (rs, rowNum) -> {
                details.setFirstName(rs.getString("first_name"));
                details.setLastName(rs.getString("last_name"));
                details.setPhoneNo(rs.getString("phoneNo"));
                return details;
            });

            // Fetch vehicle details
            List<vehicle> vehicles = jdbcTemplate.query(vehicleSql, new Object[]{userIC}, (rs, rowNum) -> {
                vehicle vehicle = new vehicle();
                vehicle.setPlateNo(rs.getString("plateNo"));
                vehicle.setVehicleType(rs.getString("vehicleType"));
                vehicle.setBrand(rs.getString("brand"));
                vehicle.setModel(rs.getString("model"));
                return vehicle;
            });
            details.setVehicles(vehicles);

        } catch (Exception e) {
            // Handle exception (e.g., logging)
            e.printStackTrace();
            return null;
        }

        return details;
    }

    public boolean deleteVehicleByPlateNo(String plateNo) {
        String checkSql = "SELECT COUNT(*) FROM Vehicle WHERE plateNo = ?";
        String deleteSql = "DELETE FROM Vehicle WHERE plateNo = ?";

        try {
            // Check if the vehicle exists
            int count = jdbcTemplate.queryForObject(checkSql, new Object[]{plateNo}, Integer.class);

            if (count > 0) {
                // Vehicle exists, proceed to delete
                int rowsDeleted = jdbcTemplate.update(deleteSql, plateNo);
                return rowsDeleted > 0;
            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUserByIC(String userIC) {
        String deleteVehicleSql = "DELETE FROM Vehicle WHERE user_id = ?";
        String deleteUserSql = "DELETE FROM User WHERE user_id = ?";

        try {
            jdbcTemplate.update(deleteVehicleSql, userIC);
            int rowsDeleted = jdbcTemplate.update(deleteUserSql, userIC);
            return rowsDeleted > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public vehicle getVehicleByPlateNo(String plateNo) {
        String sql = "SELECT * FROM Vehicle WHERE plateNo = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{plateNo}, (rs, rowNum) -> {
                vehicle vehicle = new vehicle();
                vehicle.setPlateNo(rs.getString("plateNo"));
                vehicle.setVehicleType(rs.getString("vehicleType"));
                vehicle.setBrand(rs.getString("brand"));
                vehicle.setModel(rs.getString("model"));
                vehicle.setUserId(rs.getString("user_id"));
                return vehicle;
            });
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean updateVehicle(String plateNo, String vehicleType, String brand, String model) {
        String sql = "UPDATE Vehicle SET vehicleType = ?, brand = ?, model = ? WHERE plateNo = ?";
        int rowsUpdated = jdbcTemplate.update(sql, vehicleType, brand, model, plateNo);
        return rowsUpdated > 0;
    }

    public User getUserByIC(String user_id) {
        String sql = "SELECT * FROM user WHERE user_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{user_id}, (rs, rowNum) -> {
                User user = new User();
                user.setUserId(rs.getString("user_id"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setPhoneNo(rs.getString("phoneNo"));
                return user;
            });
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean updateUser(String user_id, String first_name, String last_name, String phoneNo) {
        String sql = "UPDATE user SET first_name = ?, last_name = ?, phoneNo = ? WHERE user_id = ?";
        int rowsUpdated = jdbcTemplate.update(sql, first_name, last_name, phoneNo, user_id);
        return rowsUpdated > 0;
    }
}
