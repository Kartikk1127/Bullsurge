package com.Kartikey.bullsurge.backend.controller;

import com.Kartikey.bullsurge.backend.model.User;
import com.Kartikey.bullsurge.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public List<User> getUsers(){
        return new ArrayList<>(userRepository.findAll());
    }

    @PostMapping("/add")
    public ResponseEntity<User> addIncomeAndExpense(@RequestBody User user)
    {
        User savedUser = userRepository.save(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);

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
        User savedUser = userRepository.save(tempUser);
        return ResponseEntity.ok(savedUser);
    }


}
