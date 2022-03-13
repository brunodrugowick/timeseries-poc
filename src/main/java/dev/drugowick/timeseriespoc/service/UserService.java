package dev.drugowick.timeseriespoc.service;

import dev.drugowick.timeseriespoc.domain.repository.UserRepository;
import dev.drugowick.timeseriespoc.service.dto.UserDTO;
import dev.drugowick.timeseriespoc.service.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    /**
     * Save a User
     *
     * @param userDto the entity to save.
     * @return the saved entity.
     */
    public UserDTO save(UserDTO userDto) {
        log.info("Request to save User : {}", userDto.getEmail());
        return userMapper.toDto(userRepository.save(userMapper.toEntity(userDto)));
    }

    /**
     * Get all Users
     *
     * @param pageable the pagination information
     * @return the list of UsersDTO
     */
    @Transactional(readOnly = true)
    public Page<UserDTO> findAll(Pageable pageable) {
//        log.debug("Request to get all Users");
//        return userRepository.findAll(pageable)
//                .map(userMapper::toDto);
        return Page.empty();
    }

    /**
     * Find one specific User
     *
     * @param email the id of the User to find
     * @return the User DTO if found
     */
    @Transactional(readOnly = true)
    public Optional<UserDTO> findOne(String email) {
        log.debug("Request to get User : {}", email);
        return userRepository.findById(email)
                .map(userMapper::toDto);
    }

    /**
     * Delete User by id
     *
     * @param username the id of the User to delete
     */
    public void delete(String username) {
//        log.debug("Request to delete User : {}", username);
//        userRepository.deleteById(username);
    }

    /**
     * Find a User by provider and providerId
     *
     * @param provider may be "google", "github" or other implemented provider
     * @param providerId unique provider identification
     * @return an Optional UserDTO
     */
    public Optional<UserDTO> findByProviderAndProviderId(String provider, String providerId) {
        return userRepository.findByProviderAndProviderId(provider, providerId)
                .map(userMapper::toDto);
    }
}
