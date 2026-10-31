package org.com.service.Iservices;

import java.util.List;

import org.com.dto.AuthRequest;
import org.com.dto.AuthResponse;
import org.com.dto.UserDTO;

public interface IUserServices {

    public void createUser(UserDTO userDTO);
    public void updateUser(UserDTO userDTO);
    public void deleteUser(int id);
    public void parmentalyDeleteUser(int id);
    public UserDTO getUserById(int id);
    public List<UserDTO> getUsers();
    public void savePassword(int id, String password);
    public AuthResponse login(AuthRequest authRequest);


}
