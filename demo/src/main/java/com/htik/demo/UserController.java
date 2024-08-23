package com.htik.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private service service;

    @GetMapping("/userHome")
    public String userHome(){
        return "userHome";
    }

    @GetMapping("/userupdate")
    public String userupdate(){
        return "userupdate";
    }

    @GetMapping("/vehicleupdate")
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
        return "vehicleupdate";
    }


    @PostMapping("/Vehicleupdate")
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

        return "vehicleupdate";
    }

    @GetMapping("/Userinsertmenu")
    public String Userinsertmenu(){
        return "Userinsertmenu";
    }

    @GetMapping("userinsert")
    public String userinsert(){
        return "userinsert";
    }

    @PostMapping("/Userinsert")
    public String userinsert(@RequestParam("userID") String userID,
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

        return "userinsert";
    }

    @GetMapping("/vehicleinsert")
    public String vehicleinsert(){
        return "vehicleinsert";
    }

    @PostMapping("/Vehicleinsert")
    public String vehicleinsert (@RequestParam("userID") String userID,
                                @RequestParam("plateNo") String plateNo,
                                @RequestParam("vehicleType") String vehicleType,
                                @RequestParam("brand") String brand,
                                @RequestParam("model") String model,
                                Model modelAttribute) {

        if (!userService.isValidICNumber(userID)) {
            modelAttribute.addAttribute("message", "Invalid IC number format. Please enter a valid IC number (xxxxxx-xx-xxxx).");
            return "vehicleinsert";
        }

        if (!userService.checkUserExists(userID)) {
            modelAttribute.addAttribute("message", "User with this IC number does not exist. Please enter a valid IC number.");
            return "vehicleinsert";
        }

        if (!userService.checkPlateAvailable(plateNo)) {
            modelAttribute.addAttribute("message", "Plate number already exists. Please enter a different plate number.");
            return "vehicleinsert";
        }

        if (userService.insertVehicle(plateNo, vehicleType, brand, model, userID)) {
            modelAttribute.addAttribute("message", "Vehicle inserted successfully.");
        } else {
            modelAttribute.addAttribute("message", "Error inserting vehicle. Please try again.");
        }

        return "vehicleinsert";
    }

    @GetMapping("/caruser")
    public String caruser(@RequestParam(value = "user_id", required = false) String userId, Model model) {
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
        return "caruser";
    }

    @GetMapping("/userpageupdate")
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
        return "userpageupdate";
    }

    @PostMapping("/UserPageUpdate")
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

        return "userpageupdate";
    }
}
