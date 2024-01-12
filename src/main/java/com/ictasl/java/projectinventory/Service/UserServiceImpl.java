package com.ictasl.java.projectinventory.Service;

import com.ictasl.java.projectinventory.Persistence.dao.RoleRepository;
import com.ictasl.java.projectinventory.Persistence.dao.UserRepository;
import com.ictasl.java.projectinventory.Persistence.entity.ExistingSolution;
import com.ictasl.java.projectinventory.Persistence.entity.Role;
import com.ictasl.java.projectinventory.Persistence.entity.User;
import com.ictasl.java.projectinventory.Web.dto.ExistingSolutionDto;
import com.ictasl.java.projectinventory.Web.dto.UserDto;
import com.ictasl.java.projectinventory.Web.error.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Autowired
    private HttpServletRequest request;

    @Override
    public User findUserByUserName(String userName) {
        String ip = getClientIP();
        if (loginAttemptService.isBlocked(ip)) {
            throw new RuntimeException("blocked");
        }
        return userRepository.findByUserName(userName);
    }

    @Override
    public UserDto findUserById(long id) throws ResourceNotFoundException {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            throw new ResourceNotFoundException("User Solution not found.");
        }
        User usr = user.get();
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(usr,userDto);
        return userDto;
    }

    @Override
    public List<UserDto> getAllUsers(int status) {
        List<User> userList = userRepository.findAllByActive(status);
        List<UserDto> userDtos = new ArrayList<>();
        for (User user : userList) {
            if (!userList.isEmpty()){
                UserDto userDto = UserDto
                        .builder()
                        .id(user.getId())
                        .insId(user.getInsId())
                        .userName(user.getUserName())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .build();
                userDtos.add(userDto);
            }
        }
        return userDtos;
    }

    @Override
    public void createUserAccount(UserDto accountDto){
        final User user = new User();
        user.setFirstName(accountDto.getFirstName());
        user.setLastName(accountDto.getLastName());
        user.setUserName(accountDto.getUserName());
        user.setPasswordEnc(bCryptPasswordEncoder.encode(accountDto.getPassword()));
        user.setActive(1);
        user.setInsId(accountDto.getInsId());
        user.setInsName(accountDto.getInsName());
        Role userRole = new Role();
        userRole.setId(accountDto.getRoleId());
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        userRepository.save(user);
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) throws ResourceNotFoundException {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            throw new ResourceNotFoundException("User not found.");
        }

        User usr = user.get();
        //usr.setUserName(userDto.getUserName());
        usr.setFirstName(userDto.getFirstName());
        usr.setLastName(userDto.getLastName());
        usr.setPasswordEnc(bCryptPasswordEncoder.encode(userDto.getPassword()));
        userRepository.save(usr);
        return userDto;
    }

    @Override
    public void deleteUser(Long id) throws Exception{

        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            throw new ResourceNotFoundException("User not found.");
        }

        User usr = user.get();
        usr.setActive(0);
        userRepository.save(usr);

    }

    @Override
    public Page<User> findPaginated(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo,pageSize);

        return this.userRepository.findAllByActive(1,pageable);
    }

    private String getClientIP() {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

}
