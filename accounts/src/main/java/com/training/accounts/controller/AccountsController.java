package com.training.accounts.controller;

import com.training.accounts.dto.AccountsDto;
import com.training.accounts.dto.CustomerDto;
import com.training.accounts.dto.ResponseDto;
import com.training.accounts.service.IAccountsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
@AllArgsConstructor
public class AccountsController {

    private final IAccountsService iAccountsService ;

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createAccount(@RequestBody CustomerDto customerDto){
        iAccountsService.createAccount(customerDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body( new ResponseDto("201", "Created Successfully"));

    }

    @GetMapping("/fetch")
    public ResponseEntity<CustomerDto> fetchAccount(@RequestParam String mobileNumber){
        CustomerDto customerDto = iAccountsService.getAccountDetails(mobileNumber);
        return ResponseEntity.status(HttpStatus.OK).body(customerDto);
    }

    @PutMapping("/update")
    public ResponseEntity<CustomerDto> updateAccountType(@RequestParam String mobileNumber, @RequestParam String accountType){
        CustomerDto customerDto = iAccountsService.changeAccountType(mobileNumber, accountType);
        return ResponseEntity.status(HttpStatus.OK).body(customerDto);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteAccountByNumber(@RequestParam String accountNumber){
        iAccountsService.deleteAccount(accountNumber);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto("201", "deleted succesfully"));
    }

    @GetMapping("/hello")
    public ResponseEntity<String> helloWorld(){
        return ResponseEntity.status(HttpStatus.OK).body("Hello Shailesh");
    }

}
