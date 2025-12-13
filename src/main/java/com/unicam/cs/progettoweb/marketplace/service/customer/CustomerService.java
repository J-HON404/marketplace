package com.unicam.cs.progettoweb.marketplace.service.customer;

import com.unicam.cs.progettoweb.marketplace.exception.MarketplaceException;
import com.unicam.cs.progettoweb.marketplace.model.customer.Customer;
import com.unicam.cs.progettoweb.marketplace.repository.customer.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers(){
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Long customerId){
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new MarketplaceException(HttpStatus.NOT_FOUND, "customer not found with id: " + customerId));
    }

    public Customer saveCustomer(Customer customer){
        return customerRepository.save(customer);
    }

    public void deleteCustomer(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new MarketplaceException(HttpStatus.NOT_FOUND, "customer not found with id: " + customerId);
        }
        customerRepository.deleteById(customerId);
    }
}
