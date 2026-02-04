package gr.hua.dit.ds.project20205.controllers;
import gr.hua.dit.ds.project20205.entities.Role;
import gr.hua.dit.ds.project20205.entities.User;
import gr.hua.dit.ds.project20205.repositories.RoleRepository;
import gr.hua.dit.ds.project20205.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class UserController {

    private UserService userService;

    private RoleRepository roleRepository;

    public UserController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/register")
    public String register(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "auth/register";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute User user, Model model) {
        System.out.println("Roles: " + user.getRoles());
        Integer id = userService.saveUser(user);
        String message = "User '" + id + "' saved successfully !";
        model.addAttribute("msg", message);
        return "auth/login";
    }
    @GetMapping("/users")
    public String showUsers(Model model){
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("roles", roleRepository.findAll());
        return "auth/users";
    }

    @GetMapping("/user/{user_id}")
    public String showUser(@PathVariable Long user_id, Model model){
        model.addAttribute("user", userService.getUser(user_id));
        return "auth/user";
    }

    @PostMapping("/user/{user_id}")
    public String saveUser(@PathVariable Long user_id, @ModelAttribute("user") User user, Model model) {
        User the_user = (User) userService.getUser(user_id);
        the_user.setEmail(user.getEmail());
        the_user.setUsername(user.getUsername());
        userService.updateUser(the_user);
        model.addAttribute("users", userService.getUsers());
        return "auth/users";
    }

    @Transactional
    @GetMapping("/user/role/delete/{user_id}/{role_id}")
    public String deleteRolefromUser(@PathVariable Long user_id, @PathVariable Integer role_id, Model model) {
        User user = userService.getUser(user_id);
        Role role = roleRepository.findById(role_id).orElseThrow(() -> new RuntimeException("Role not found"));

        if (user.getRoles().contains(role)) {
            user.getRoles().remove(role);
            userService.updateUser(user); // Χρησιμοποιούμε το UserService αντί για userRepository
        }

        model.addAttribute("users", userService.getUsers());
        model.addAttribute("roles", roleRepository.findAll());
        return "auth/users";
    }


    @GetMapping("/user/role/add/{user_id}/{role_id}")
    public String addRoletoUser(@PathVariable Long user_id, @PathVariable Integer role_id, Model model) {
        User user = (User) userService.getUser(user_id);
        Role role = roleRepository.findById(role_id).get();
        user.getRoles().add(role);
        System.out.println("Roles: " + user.getRoles());
        userService.updateUser(user);
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("roles", roleRepository.findAll());
        return "auth/users";

    }

    // Διαχείριση χρηστών
    @GetMapping("/auth/users")
    public String manageUsers(Model model) {
        model.addAttribute("users", userService.getUsers());
        return "auth/users"; // Συνδέεται με το manage-users.html
    }

    // Διαγραφή χρήστη
    @PostMapping("/delete-user/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/auth/users";
    }

}

