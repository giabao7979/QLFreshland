package com.freshland.controller;

import com.freshland.dto.UserDTO;
import com.freshland.model.User;
import com.freshland.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/admin/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "views/admin/user/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new UserDTO());
        model.addAttribute("roles", User.Role.values());
        return "views/admin/user/form";
    }

    @PostMapping("/create")
    public String createUser(@Valid @ModelAttribute("user") UserDTO userDTO,
                            BindingResult result,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("roles", User.Role.values());
            return "views/admin/user/form";
        }
        
        // Check if username or email already exists
        if (userService.getUserByUsername(userDTO.getUsername()).isPresent()) {
            result.rejectValue("username", "error.user", "Username already exists");
            model.addAttribute("roles", User.Role.values());
            return "views/admin/user/form";
        }
        
        if (userService.getUserByEmail(userDTO.getEmail()).isPresent()) {
            result.rejectValue("email", "error.user", "Email already exists");
            model.addAttribute("roles", User.Role.values());
            return "views/admin/user/form";
        }
        
        User user = new User();
        user.setUsername(userDTO.getUsername());
        // In a real application, you would hash the password
        user.setPassword(userDTO.getPassword());
        user.setFullName(userDTO.getFullName());
        user.setEmail(userDTO.getEmail());
        user.setRole(userDTO.getRole());
        
        userService.saveUser(user);
        redirectAttributes.addFlashAttribute("successMessage", "User created successfully!");
        
        return "redirect:/admin/users";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<User> userOptional = userService.getUserById(id);
        
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setUsername(user.getUsername());
            // Don't set password for security reasons
            userDTO.setFullName(user.getFullName());
            userDTO.setEmail(user.getEmail());
            userDTO.setRole(user.getRole());
            
            model.addAttribute("user", userDTO);
            model.addAttribute("roles", User.Role.values());
            return "views/admin/user/form";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "User not found!");
            return "redirect:/admin/users";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateUser(@PathVariable Long id,
                            @Valid @ModelAttribute("user") UserDTO userDTO,
                            BindingResult result,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("roles", User.Role.values());
            return "views/admin/user/form";
        }
        
        Optional<User> userOptional = userService.getUserById(id);
        
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            
            // Check if username is changed and already exists
            if (!user.getUsername().equals(userDTO.getUsername()) && 
                userService.getUserByUsername(userDTO.getUsername()).isPresent()) {
                result.rejectValue("username", "error.user", "Username already exists");
                model.addAttribute("roles", User.Role.values());
                return "views/admin/user/form";
            }
            
            // Check if email is changed and already exists
            if (!user.getEmail().equals(userDTO.getEmail()) && 
                userService.getUserByEmail(userDTO.getEmail()).isPresent()) {
                result.rejectValue("email", "error.user", "Email already exists");
                model.addAttribute("roles", User.Role.values());
                return "views/admin/user/form";
            }
            
            user.setUsername(userDTO.getUsername());
            // Only update password if provided
            if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                // In a real application, you would hash the password
                user.setPassword(userDTO.getPassword());
            }
            user.setFullName(userDTO.getFullName());
            user.setEmail(userDTO.getEmail());
            user.setRole(userDTO.getRole());
            
            userService.saveUser(user);
            redirectAttributes.addFlashAttribute("successMessage", "User updated successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "User not found!");
        }
        
        return "redirect:/admin/users";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting user: " + e.getMessage());
        }
        
        return "redirect:/admin/users";
    }
}