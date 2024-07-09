package com.training.accounts.service.impl;

import com.training.accounts.dto.AccountsDto;
import com.training.accounts.dto.CustomerDto;
import com.training.accounts.entity.Accounts;
import com.training.accounts.entity.Customer;
import com.training.accounts.exception.CustomerAlreadyExistsException;
import com.training.accounts.exception.ResourceNotFoundException;
import com.training.accounts.mapper.AccountsMapper;
import com.training.accounts.mapper.CustomerMapper;
import com.training.accounts.repository.AccountsRepository;
import com.training.accounts.repository.CustomerRepository;
import com.training.accounts.service.IAccountsService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
@Transactional
public class AccountsServiceImpl implements IAccountsService {

    private CustomerRepository customerRepository;
    private AccountsRepository accountsRepository;

    @Override
    public void createAccount(CustomerDto customerDto){


        Optional<Customer> foundCustomer= customerRepository.findByMobileNumber(customerDto.getMobileNumber());
        if(foundCustomer.isPresent()){
            throw new CustomerAlreadyExistsException("Customer already exists for this mobile number");
        }
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        customer.setCreatedAt(LocalDateTime.now());
        customer.setCreatedBy("Me");
        customerRepository.save(customer);

        Accounts accounts = createNewAccount(customer.getCustomerId());
        accountsRepository.save(accounts);
    }

    private Accounts createNewAccount(Long customerId){
        Accounts accounts = new Accounts();
        accounts.setCustomerId(customerId);
        accounts.setAccountType("SAVINGS");
        long randAcc = 100000000L + new Random().nextInt( 90000000);
        accounts.setAccountNumber(String.valueOf(randAcc));
        accounts.setBranchAddress("abc Home at post xyz colony");
        accounts.setCreatedAt(LocalDateTime.now());
        accounts.setCreatedBy("me");
        return accounts;
    }

    @Override
    public CustomerDto getAccountDetails(String mobileNumber){
        Customer foundCustomer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(()->
                new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));
        Accounts accounts = accountsRepository.findByCustomerId(foundCustomer.getCustomerId()).orElseThrow(()->
                new ResourceNotFoundException("Accounts", "customerId", foundCustomer.getCustomerId().toString()));

        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(foundCustomer, new CustomerDto());
        AccountsDto accountsDto = AccountsMapper.mapToAccountsDto(accounts, new AccountsDto());

        customerDto.setAccountsDto(accountsDto);

        return customerDto;
    }

    @Override
    public CustomerDto changeAccountType(String mobileNumber, String accountType){
        Customer foundCustomer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(()->
                new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));
        Accounts accounts = accountsRepository.findByCustomerId(foundCustomer.getCustomerId()).orElseThrow(()->
                new ResourceNotFoundException("Accounts", "customerId", foundCustomer.getCustomerId().toString()));

        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(foundCustomer, new CustomerDto());
        AccountsDto accountsDto = AccountsMapper.mapToAccountsDto(accounts, new AccountsDto());

        accountsDto.setAccountType(accountType);
        customerDto.setAccountsDto(accountsDto);

        return customerDto;

    }

    @Override
    public void deleteAccount(String accountNumber) {
        accountsRepository.deleteByAccountNumber(accountNumber);
    }


}
