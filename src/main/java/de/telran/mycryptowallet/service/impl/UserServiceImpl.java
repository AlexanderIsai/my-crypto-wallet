package de.telran.mycryptowallet.service.impl;
import de.telran.mycryptowallet.dto.userDTO.UserAddDTO;
import de.telran.mycryptowallet.entity.User;
import de.telran.mycryptowallet.entity.entityEnum.UserRole;
import de.telran.mycryptowallet.entity.entityEnum.UserStatus;
import de.telran.mycryptowallet.mapper.userMapper.UserMapper;
import de.telran.mycryptowallet.repository.UserRepository;
import de.telran.mycryptowallet.service.interfaces.UserService;
import de.telran.mycryptowallet.service.utils.validators.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserValidator userValidator;
    private final UserMapper userMapper;
    @Override
    public void addNewUser(UserAddDTO userAddDTO) {
        userValidator.isEmailPresent(getAllUsers(), userAddDTO.getEmail());
        User user = userMapper.toEntity(userAddDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(UserRole.ROLE_USER);
        user.setStatus(UserStatus.ONLINE);
        userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        userValidator.isUserNotFound(user);
        return user;
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void updateUser(Long id, User updatedUser) {
        updatedUser.setId(id);
        userRepository.save(updatedUser);
    }

    @Override
    public List<User> getUsersByStatus(UserStatus status) {
        return userRepository.findUsersByStatus(status);
    }

    @Override
    public Boolean isExistUserByEmail(String email) {
        return userRepository.existsUserByEmail(email);
    }

    @Override
    public void toggleBlockUser(Long id) {
        User user = getUserById(id);
        if(!user.getStatus().equals(UserStatus.BLOCKED)){
            user.setStatus(UserStatus.BLOCKED);
        }
        else {
            user.setStatus(UserStatus.OFFLINE);
        }
        updateUser(user.getId(), user);
    }

    @Override
    public void changeUserPassword(Long id, String newPassword) {
        User user = getUserById(id);
        user.setPassword(passwordEncoder.encode(newPassword));
        updateUser(id, user);
    }
}
