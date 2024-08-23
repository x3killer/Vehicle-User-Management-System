package com.htik.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class MyController {

    @Autowired
    private UserService userService;

    @Autowired
    private service service;

    @GetMapping("/home")
    public String home(Model model, Principal principle) {

        return "index";
    }

    @GetMapping("/insert")
    public String insert() {
        return "insert";
    }

    @GetMapping("/show")
    public String show() {
        return "show";
    }


    @GetMapping("/insertuser")
    public String insertUser() {
        return "insertuser";
    }

    @GetMapping("/insertvehicle")
    public String insertVehicle() {
        return "insertvehicle";
    }

    @GetMapping("/delete")
    public String delete() {
        return "delete";
    }

    @GetMapping("/showuser")
    public String showUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "showuser";
    }

    @GetMapping("/showvehicle")
    public String showvehicle(Model model) {
        List<vehicle> vehicles = userService.getAllVehicle();
        model.addAttribute("vehicles", vehicles);
        return "showvehicle";
    }

    @GetMapping("/deleteuser")
    public String showDeleteUserForm() {
        return "deleteuser";
    }

    @PostMapping("/insertVehicle")
    public String insertVehicle(@RequestParam("userID") String userID,
                                @RequestParam("plateNo") String plateNo,
                                @RequestParam("vehicleType") String vehicleType,
                                @RequestParam("brand") String brand,
                                @RequestParam("model") String model,
                                Model modelAttribute) {

        if (!userService.isValidICNumber(userID)) {
            modelAttribute.addAttribute("message", "Invalid IC number format. Please enter a valid IC number (xxxxxx-xx-xxxx).");
            return "insertvehicle";
        }

        if (!userService.checkUserExists(userID)) {
            modelAttribute.addAttribute("message", "User with this IC number does not exist. Please enter a valid IC number.");
            return "insertvehicle";
        }

        if (!userService.checkPlateAvailable(plateNo)) {
            modelAttribute.addAttribute("message", "Plate number already exists. Please enter a different plate number.");
            return "insertvehicle";
        }

        if (userService.insertVehicle(plateNo, vehicleType, brand, model, userID)) {
            modelAttribute.addAttribute("message", "Vehicle inserted successfully.");
        } else {
            modelAttribute.addAttribute("message", "Error inserting vehicle. Please try again.");
        }

        return "insertvehicle";
    }

    @PostMapping("/insertUser")
    public String insertUser(@RequestParam("userID") String userID,
                             @RequestParam("firstName") String firstName,
                             @RequestParam("lastName") String lastName,
                             @RequestParam("phoneNo") String phoneNo,
                             Model model) {

        if (!userService.isValidICNumber(userID)) {
            model.addAttribute("message", "Invalid IC number format. Please enter a valid IC number (xxxxxx-xx-xxxx).");
            return "insertuser";
        }

        if (userService.checkUserExists(userID)) {
            model.addAttribute("message", "IC number already exists. Please enter a different IC number.");
            return "insertuser";
        }

        if (userService.insertUser(userID, firstName, lastName, phoneNo)) {
            model.addAttribute("message", "User inserted successfully.");
        } else {
            model.addAttribute("message", "Error inserting user. Please try again.");
        }

        return "insertuser";
    }

    @GetMapping("/showuserCar")
    public String showUserCarForm() {
        return "showuserCar";
    }


    @GetMapping("/Update")
    public String Update(){
        return "Update";
    }

    @GetMapping("/showusercar")
    public String showUserCar(@RequestParam(value = "user_id", required = false) String userId, Model model) {
        if (userId != null && !userId.isEmpty()) {
            if (!userService.isValidICNumber(userId)) {
                model.addAttribute("message", "Invalid IC number format.");
            } else {
                vehicleuser userDetails = userService.getUserWithVehicleDetails(userId);
                if (userDetails == null) {
                    model.addAttribute("message", "No user found with IC number: " + userId);
                } else {
                    model.addAttribute("userDetails", userDetails);
                }
            }
        }
        return "showusercar";
    }

    @GetMapping("/deletevehicle")
    public String showDeleteVehicleForm() {
        return "deletevehicle";
    }

    @PostMapping("/deleteVehicle")
    public String deleteVehicle(@RequestParam("plateNo") String plateNo, Model model) {
        if (plateNo == null || plateNo.trim().isEmpty()) {
            model.addAttribute("message", "Plate number is required.");
            return "deletevehicle";
        }

        boolean isDeleted = userService.deleteVehicleByPlateNo(plateNo);
        if (isDeleted) {
            model.addAttribute("message", "Vehicle with plate number " + plateNo + " deleted successfully.");
        } else {
            model.addAttribute("message", "No vehicle found with plate number " + plateNo + ".");
        }

        return "deletevehicle";
    }

    @PostMapping("/deleteuser")
    public String deleteUser(@RequestParam("userIC") String userIC, Model model) {
        String icFormat = "^\\d{6}-\\d{2}-\\d{4}$";
        if (!userIC.matches(icFormat)) {
            model.addAttribute("message", "Invalid IC number format. Please enter a valid IC number (xxxxxx-xx-xxxx).");
            model.addAttribute("messageType", "error");
            return "deleteuser";
        }

        boolean isDeleted = userService.deleteUserByIC(userIC);
        if (isDeleted) {
            model.addAttribute("message", "User deleted successfully.");
            model.addAttribute("messageType", "success");
        } else {
            model.addAttribute("message", "No user found with IC number: " + userIC);
            model.addAttribute("messageType", "error");
        }
        return "deleteuser";
    }

    @GetMapping("/updatevehicle")
    public String showUpdateVehicleForm(@RequestParam(value = "plateNo", required = false) String plateNo, Model model) {
        System.out.println("Received request for /updateVehicle with plateNo: " + plateNo);
        if (plateNo != null && !plateNo.isEmpty()) {
            vehicle vehicleDetails = userService.getVehicleByPlateNo(plateNo);
            if (vehicleDetails == null) {
                model.addAttribute("message", "No vehicle found with Plate Number: " + plateNo);
            } else {
                model.addAttribute("vehicleDetails", vehicleDetails);
            }
        }
        return "updatevehicle";
    }


    @PostMapping("/updateVehicle")
    public String updateVehicle(@RequestParam("plateNo") String plateNo,
                                @RequestParam("vehicleType") String vehicleType,
                                @RequestParam("brand") String brand,
                                @RequestParam("model") String model,
                                Model modelAttribute) {

        boolean isUpdated = userService.updateVehicle(plateNo, vehicleType, brand, model);
        if (isUpdated) {
            modelAttribute.addAttribute("message", "Vehicle updated successfully.");
        } else {
            modelAttribute.addAttribute("message", "Failed to update vehicle.");
        }

        return "updatevehicle";
    }

    @GetMapping("/updateuser")
    public String showUpdateUserForm(@RequestParam(value = "user_id", required = false) String user_id, Model model) {
        if (user_id != null && !user_id.isEmpty()) {
            System.out.println("Received request to search for user with ID: " + user_id); // Debugging
            User userDetails = userService.getUserByIC(user_id);
            if (userDetails == null) {
                model.addAttribute("message", "No user found with User IC Number: " + user_id);
            } else {
                model.addAttribute("userDetails", userDetails);
                System.out.println("User found: " + userDetails);
            }
        }
        return "updateuser";
    }

    @PostMapping("/updateUser")
    public String updateUser(@RequestParam("user_id") String user_id,
                             @RequestParam("first_name") String first_name,
                             @RequestParam("last_name") String last_name,
                             @RequestParam("phoneNo") String phoneNo,
                             Model modelAttribute) {

        System.out.println("Received request to update user with ID: " + user_id); // Debugging
        boolean isUpdated = userService.updateUser(user_id, first_name, last_name, phoneNo);
        if (isUpdated) {
            modelAttribute.addAttribute("message", "User updated successfully.");
            System.out.println("User updated successfully.");
        } else {
            modelAttribute.addAttribute("message", "Failed to update user.");
            System.err.println("Failed to update user with ID: " + user_id);
        }

        return "updateuser";
    }

    @GetMapping("/registration")
    public String getRegistrationPage(@ModelAttribute("user") UserDto userDto) {
        return "register";
    }

    @PostMapping("/registration")
    public String saveUser(@ModelAttribute("user") UserDto userDto, Model model) {
        service.save(userDto);
        model.addAttribute("message", "Registered Successfuly!");
        return "register";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }


}