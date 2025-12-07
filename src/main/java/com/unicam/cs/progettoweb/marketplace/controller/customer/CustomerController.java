package com.unicam.cs.progettoweb.marketplace.controller.customer;

import com.unicam.cs.progettoweb.marketplace.model.customer.Customer;
import com.unicam.cs.progettoweb.marketplace.service.account.DefaultAccountService;
import com.unicam.cs.progettoweb.marketplace.service.customer.CustomerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final DefaultAccountService defaultAccountService;
    private final CustomerService customerService;

    public CustomerController(DefaultAccountService defaultAccountService, CustomerService customerService) {
        this.defaultAccountService = defaultAccountService;
        this.customerService = customerService;
    }

    @GetMapping("/{customerId}")
    public Customer getCustomer(Long customerId){
        return customerService.getCustomerById(customerId);
    }

    @GetMapping
    public List<Customer> getAllCustomers(){
        return customerService.getAllCustomers();
    }

    @PostMapping
    public Customer createCustomer(@RequestBody Customer customer){
        return customerService.saveCustomer(customer);
    }

    @DeleteMapping("/{customerId}")
    public void deleteCustomer(Long customerId){
         customerService.deleteCustomer(customerId);
    }
}
