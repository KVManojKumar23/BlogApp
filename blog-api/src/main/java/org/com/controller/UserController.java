package org.com.controller;

import java.util.List;

import org.com.dto.UserDTO;
import org.com.service.Iservices.IUserServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController 
@RequestMapping("/admin/user")
public class UserController {

    private final IUserServices userServices;

    public UserController(IUserServices userServices) {
        this.userServices = userServices;
    }

    @PostMapping("/add")
    public ResponseEntity<Void> createUser(@RequestBody  UserDTO userDTO) {
        this.userServices.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping ("/update")
    public ResponseEntity<Void> updateUser(@RequestBody UserDTO userDTO) {
        this.userServices.updateUser(userDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/password")
    public ResponseEntity<Void> savePassword(@RequestParam int id, @RequestParam String password) {
        this.userServices.savePassword(id, password);
        return ResponseEntity.status(HttpStatus.OK).build();
    }



    @GetMapping("/getUser")
    public ResponseEntity<UserDTO> getUserById(@RequestParam int id) {
        UserDTO user = this.userServices.getUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> userDTOs = this.userServices.getUsers();
        return ResponseEntity.status(HttpStatus.OK).body(userDTOs);
    }

    @DeleteMapping("/bin")
    public ResponseEntity<Void> deleteUser(@RequestParam int id) {
        this.userServices.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("trash")
    public ResponseEntity<Void> parmentalyDeleteUser(@RequestParam int id) {
         this.userServices.parmentalyDeleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
