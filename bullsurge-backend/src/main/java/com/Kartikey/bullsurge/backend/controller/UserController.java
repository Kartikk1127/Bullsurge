package com.Kartikey.bullsurge.backend.controller;

import com.Kartikey.bullsurge.backend.dto.AuthRequest;
import com.Kartikey.bullsurge.backend.model.User;
import com.Kartikey.bullsurge.backend.repository.UserRepository;
import com.Kartikey.bullsurge.backend.service.JwtService;
import com.Kartikey.bullsurge.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getUsers(){
        return new ArrayList<>(userRepository.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<User> getUserById(@PathVariable String id)
    {
        User user = userRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found with id " + id));
        return ResponseEntity.ok(user);
    }

    @PostMapping("/add")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> addIncomeAndExpense(@RequestBody User user)
    {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.addUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @PutMapping("/{id}/income/update")
    public ResponseEntity<User> updateIncomeAndExpenses(@PathVariable String id, @RequestBody User updatedUser){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id " + id));
        System.out.println(user); //unupdated user
        if(updatedUser.getIncome()<updatedUser.getExpense()){
            throw new RuntimeException("income cannot be lesser than expense");
        }
            user.setIncome(updatedUser.getIncome());
            user.setExpense(updatedUser.getExpense());
            user.setSavings(updatedUser.getIncome()-updatedUser.getExpense());
        System.out.println("income and expense set"); //user is updated now
            User savedUser =  userRepository.save(user); //updated user saved in database
        System.out.println("user saved in db");

            return ResponseEntity.ok(savedUser);
    }

    @DeleteMapping("/{id}/income/delete")
    public ResponseEntity<User> deleteIncomeAndExpense(@PathVariable String id)
    {
        User tempUser = userRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("user not founf with id" + id));

        tempUser.setIncome(null);
        tempUser.setExpense(null);
        tempUser.setSavings(null);
        User savedUser = userRepository.save(tempUser);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/authenticate")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if(authentication.isAuthenticated())
        {
            return  jwtService.generateToken(authRequest.getUsername());
        }
        else {
            throw new UsernameNotFoundException("invalid username!");
        }
    }


}
