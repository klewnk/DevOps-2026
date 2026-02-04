package gr.hua.dit.ds.project20205.service;
import gr.hua.dit.ds.project20205.entities.Role;
import gr.hua.dit.ds.project20205.entities.User;
import gr.hua.dit.ds.project20205.repositories.RoleRepository;
import gr.hua.dit.ds.project20205.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;



@Service
public class UserService implements UserDetailsService {

    @Autowired 
    private EmailService emailService;


    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;

    }

    @Transactional
    public Integer saveUser(User user) {
        String passwd = user.getPassword();
        String encodedPassword = passwordEncoder.encode(passwd);
        user.setPassword(encodedPassword);

        Role role = roleRepository.findByName("ROLE_RENTAL")
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        User savedUser = userRepository.save(user);
        //  ΑΠΟΣΤΟΛΗ EMAIL 
      try {
        emailService.sendWelcomeEmail(savedUser.getEmail(), savedUser.getUsername());
        System.out.println("Email sent to: " + savedUser.getEmail());
     } catch (Exception e) {
        // Καταγράφουμε το σφάλμα αλλά δεν σταματάμε την εγγραφή
        System.err.println("Could not send email: " + e.getMessage());
     }

        return user.getId();
    }

    @Transactional
    public Integer updateUser(User user) {
        user = userRepository.save(user);
        return user.getId();
    }


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> opt = userRepository.findByUsername(username);

        if (opt.isEmpty())
            throw new UsernameNotFoundException("User with email: " + username + " not found !");
        else {
            User user = opt.get();
            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    user.getRoles()
                            .stream()
                            .map(role -> new SimpleGrantedAuthority(role.toString()))
                            .collect(Collectors.toSet())
            );
        }
    }

    @Transactional
    public Object getUsers() {
        return userRepository.findAll();
    }

    @Transactional

    public void updateOrInsertRole(Role role) {
        roleRepository.updateOrInsert(role);
    }

    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
