package com.example.wiqaya.Controller;

import com.example.wiqaya.ApiResponse.ApiResponse;
import com.example.wiqaya.DTO.IN.UserDTOIN;
import com.example.wiqaya.Model.User;
import com.example.wiqaya.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/wiqaya/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // CRUD

    // Get
    @GetMapping("/get")
    public ResponseEntity<?> getAllUsers(){
        return ResponseEntity.status(200).body(userService.getAllUsers());
    }

    // Create .. add
    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody @Valid UserDTOIN user){
        userService.addUser(user);
        return ResponseEntity.status(200).body(new ApiResponse("User added"));
    }

    // update .. put
    @PutMapping("/update/user-id/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id,@RequestBody @Valid UserDTOIN user){
        userService.updateUser(id,user);
        return ResponseEntity.status(200).body(new ApiResponse("User Updated"));
    }

    // delete
    @DeleteMapping("/delete/user-id/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id){
        userService.deleteUser(id);
        return ResponseEntity.status(200).body(new ApiResponse("User deleted"));
    }

    // --------------------------------------------------

    // // Endpoint No.1
    // admin check Eng isVerified
    @PutMapping("/verified/eng/user-id/{user_id}/eng-id/{eng_id}/status/{status}")
    public ResponseEntity<?> verifiedEng(@PathVariable Integer user_id,@PathVariable Integer eng_id,@PathVariable String status, @RequestParam String rejectionReason){
        userService.verifiedEng(user_id,eng_id,status,rejectionReason);
        return ResponseEntity.status(200).body(new ApiResponse("Check Engineer done"));
    }

    // Endpoint No.5
    @PutMapping("/verified/provider/user-id/{user_id}/provider-id/{provider_id}/status/{status}")
    public ResponseEntity<?> verifiedProvider(@PathVariable Integer user_id,@PathVariable Integer provider_id,@PathVariable String status, @RequestParam String rejectionReason){
        userService. verifiedProvider(user_id,provider_id,status,rejectionReason);
        return ResponseEntity.status(200).body(new ApiResponse("Provider is active now!"));
    }

    // Endpoint No.
    // get All Request For One User using user id
    @GetMapping("/display-all-requests-by-user-id/{id}")
    public ResponseEntity<?> getAllRequestForOneUser(@PathVariable Integer id){
        return ResponseEntity.status(200).body(userService.getAllRequestForOneUser(id));
    }

    @GetMapping("get-all-eng-under-review/adminId/{adminId}")
    public ResponseEntity<?> getAllEngUnderReviewReview(@PathVariable Integer adminId){
        return ResponseEntity.status(200).body(userService.getAllEngUnderReview(adminId));
    }

}
