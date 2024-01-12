package com.ictasl.java.projectinventory.Service;


import com.ictasl.java.projectinventory.Persistence.entity.User;
import com.ictasl.java.projectinventory.Web.dto.UserDto;
import com.ictasl.java.projectinventory.Web.error.ResourceNotFoundException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    User findUserByUserName(String userName);
    UserDto findUserById(long id) throws ResourceNotFoundException;
    List<UserDto> getAllUsers(int status);
    void createUserAccount(UserDto user);
    UserDto updateUser (Long id, UserDto userDto) throws ResourceNotFoundException;
    void deleteUser(Long id) throws ResourceNotFoundException, Exception;
    Page<User> findPaginated(int pageNo, int pageSize);
}
