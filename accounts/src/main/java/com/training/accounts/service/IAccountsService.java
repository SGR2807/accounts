package com.training.accounts.service;

import com.training.accounts.dto.AccountsDto;
import com.training.accounts.dto.CustomerDto;
import com.training.accounts.entity.Customer;


public interface IAccountsService {

    void createAccount(CustomerDto customerDto);

    CustomerDto getAccountDetails(String mobileNumber);

    CustomerDto changeAccountType(String mobileNumber, String accountType);

    void deleteAccount(String accountNumber);
}
