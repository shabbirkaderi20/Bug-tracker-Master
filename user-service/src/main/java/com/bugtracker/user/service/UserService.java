package com.bugtracker.user.service;

import com.bugtracker.user.VO.ResponseTemplateVO;
import com.bugtracker.user.VO.Role;
import com.bugtracker.user.constant.Constant;
import com.bugtracker.user.dto.UpdateUserDto;
import com.bugtracker.user.dto.UserDto;
import com.bugtracker.user.entity.User;
import com.bugtracker.user.exception.*;
import com.bugtracker.user.helper.UpdateUserDtoToModel;
import com.bugtracker.user.helper.UserDtoToModel;
import com.bugtracker.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDtoToModel userDtoToModel;

    @Autowired
    private UpdateUserDtoToModel updateUserDtoToModel;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MailService mailService;

    public UserService() {
    }

    public User registerUser(UserDto userDto) {

        User user= userDtoToModel.convertUserDtoToModel(userDto);

        Optional<User> userNameValidator= userRepository.findByUserName(user.getUserName());
        Optional<User> emailValidator= userRepository.findByEmail(user.getEmail());
        Optional<User> contactValidator= userRepository.findByContactNo(user.getContactNo());

        if(userNameValidator.isPresent()){
            throw new UserNameAlreadyExistsException();
        }else if(emailValidator.isPresent()){
            throw new EmailAlreadyTakenException();
        }else if(contactValidator.isPresent()){
            throw new ContactAlreadyExistsException();
        }else {
            if(user.getRoleId()== 1) {
                throw new ForbiddenAuthorityException();
            }else {
                User userReturned= userRepository.save(user);
                mailService.sendEmail(userReturned.getEmail(),
                        Constant.EMAIL_START_WARNING+ Constant.EMAIL_USERID+
                                userReturned.getUserId()+ Constant.EMAIL_USERNAME+
                                userReturned.getUserName()+ Constant.EMAIL_PASSWORD+
                                userReturned.getPassword()+ Constant.EMAIL_CONTACT+
                                userReturned.getContactNo()+ Constant.EMAIL_END_WARNING,
                                Constant.ACCOUNT_REGISTRATION_SUBJECT);
                return userReturned;
            }
        }
    }

    public ResponseTemplateVO getUser(Long userId) {

        ResponseTemplateVO responseTemplateVO= new ResponseTemplateVO();
        User user= userRepository.findByUserId(userId);

        Role role=
                restTemplate.getForObject("http://ROLE-SERVICE/roles/role/"+ user.getRoleId()
                        ,Role.class);

        responseTemplateVO.setUser(user);
        responseTemplateVO.setRole(role);

        return responseTemplateVO;
    }

    public String userLogin(String userName, String password, Long roleId) {

        Optional<User> userReturned= userRepository.findByUserName(userName);
        long roleIdReturned= userReturned.map(User:: getRoleId).orElse(0l);
        String passwordReturned= userReturned.map(User:: getPassword).orElse("");
        if(userReturned.isEmpty()) {
            throw new UserNotFoundException();
        }else if(!passwordReturned.equals(password)) {
            throw new IncorrectCredentials();
        }else if(roleIdReturned != roleId) {
            throw new DomainIncorrectException();
        }else  {
            return "User login successfully";
        }
    }

    public User updateUser(UpdateUserDto updateUserDto) {

        User user = updateUserDtoToModel.convertUpdateUserDtoToModel(updateUserDto);

        User userReturned = userRepository.findByUserId(user.getUserId());
        userReturned.setFirstName(user.getFirstName());
        userReturned.setLastName(user.getLastName());
        userReturned.setContactNo(user.getContactNo());
        userReturned.setBirthDate(user.getBirthDate());
        userReturned.setAddress(user.getAddress());

        if (userReturned.getUserId() == 0l) {
            throw new UserNotFoundException();
        } else if (!userReturned.getPassword().equals(user.getPassword())) {
            throw new IncorrectCredentials();
        } else {
            User userUpdated= userRepository.save(userReturned);
            mailService.sendEmail(userUpdated.getEmail(),
                    Constant.EMAIL_START_WARNING+ Constant.EMAIL_UPDATED_FIRSTNAME+
                            userUpdated.getFirstName()+ Constant.EMAIL_UPDATED_LASTNAME+
                            userUpdated.getLastName()+ Constant.EMAIL_CONTACT+
                            userUpdated.getContactNo()+ Constant.EMAIL_UPDATED_BIRTHDATE+
                            userUpdated.getBirthDate()+ Constant.EMAIL_UPDATED_ADDRESS+
                            userUpdated.getAddress() +Constant.EMAIL_END_WARNING,
                            Constant.ACCOUNT_UPDATING_SUBJECT);
            return userUpdated;
        }
    }

    public User changePassword(Long userId, String currentPassword, String newPassword) {

        User userReturned= userRepository.findByUserId(userId);

        if (userReturned.getUserId() == 0l) {
            throw new UserNotFoundException();
        } else if (!userReturned.getPassword().equals(currentPassword)) {
            throw new IncorrectCredentials();
        } else {
            userReturned.setPassword(newPassword);
            User userWithUpdatedPassword= userRepository.save(userReturned);
            mailService.sendEmail(userWithUpdatedPassword.getEmail(),
                    Constant.EMAIL_START_WARNING+ Constant.EMAIL_UPDATED_PASSWORD+
                    userWithUpdatedPassword.getPassword()+ Constant.EMAIL_END_WARNING
                    , Constant.UPDATED_PASSWORD_SUBJECT);
            return userWithUpdatedPassword;
        }
    }

    public User changeEmail(Long userId, String password, String email) {

        User userReturned= userRepository.findByUserId(userId);

        if (userReturned.getUserId() == 0L) {
            throw new UserNotFoundException();
        } else if (!userReturned.getPassword().equals(password)) {
            throw new IncorrectCredentials();
        } else {
            userReturned.setEmail(email);
            User userWithUpdatedEmail= userRepository.save(userReturned);
            mailService.sendEmail(userWithUpdatedEmail.getEmail(),
                    Constant.EMAIL_START_WARNING+ Constant.EMAIL_UPDATED+ Constant.EMAIL_END_WARNING
                    , Constant.EMAIL_UPDATING_SUBJECT);
            return userWithUpdatedEmail;
        }
    }

    public User viewStaffById(Long userId) {

        User userReturned= userRepository.findByUserId(userId);

        if (userReturned.getUserId() == 0L || userReturned.getRoleId()!= 2L) {
            throw new UserNotFoundException();
        }  else {
            return userReturned;
        }
    }

    public User viewClientById(Long userId) {

        User userReturned= userRepository.findByUserId(userId);

        if (userReturned.getUserId() == 0L || userReturned.getRoleId()!= 3L) {
            throw new UserNotFoundException();
        }  else {
            return userReturned;
        }
    }

    public List<User> viewAllClient() {

        return userRepository.findByRoleId(3L);
    }

    public List<User> viewAllStaff() {

        return userRepository.findByRoleId(2L);
    }

    public Boolean deleteClientById(Long clientId) {
        User userReturned= userRepository.findByUserId(clientId);

        if(userReturned.getUserId() == 0) {
            throw new UserNotFoundException();
        }else if(userReturned.getRoleId()!= 3) {
            throw new DomainIncorrectException();
        }else {
            return userRepository.deleteByUserId(clientId);
        }
    }

    public Boolean deleteStaffById(Long staffId) {
        User userReturned= userRepository.findByUserId(staffId);

        if(userReturned.getUserId() == 0) {
            throw new UserNotFoundException();
        }else if(userReturned.getRoleId()!= 2) {
            throw new DomainIncorrectException();
        }else {
            return userRepository.deleteByUserId(staffId);
        }
    }
}
