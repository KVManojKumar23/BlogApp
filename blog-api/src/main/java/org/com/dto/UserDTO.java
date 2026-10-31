package org.com.dto;

import java.util.Set;

import org.com.model.Roles;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Builder 
public class UserDTO {
    private int id;
    private String name;
    private String phoneNum;
    private String email;
    private String address;
    private String pin;
    private String state;
    private String district;
    private String country;
    private Set<Roles> roles;

    private String password;
    private boolean active;
}
