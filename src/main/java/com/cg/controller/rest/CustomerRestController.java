package com.cg.controller.rest;


import com.cg.exception.EmailExistsException;
import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.dto.CustomerDTO;
import com.cg.model.dto.DepositDTO;
import com.cg.model.dto.LocationRegionDTO;
import com.cg.service.customer.CustomerService;
import com.cg.utils.AppUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
public class CustomerRestController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AppUtil appUtil;

    @GetMapping
    public ResponseEntity<?> getAllCustomer() {
        List<CustomerDTO> customerDTOList = customerService.findAllCustomerDTOByDeletedIsFalse();

        return new ResponseEntity<>(customerDTOList,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable Long id){
        Optional<Customer> customerOptional = customerService.findById(id);
        if (!customerOptional.isPresent()) {
            throw new EmailExistsException("Invalid customer Id");
        }

        return new ResponseEntity<>(customerOptional.get().toCustomerDTO(),HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> doCreate(@RequestBody CustomerDTO customerDTO) {

        customerDTO.setId(0L);
        customerDTO.setBalance(new BigDecimal(0L));
        customerDTO.getLocationRegion().setId(0L);

        Boolean exitsEmail = customerService.existsByEmail(customerDTO.getEmail());

        if (exitsEmail) {
//            return new ResponseEntity<>("Email exits", HttpStatus.CONFLICT);
            throw new EmailExistsException("Email already exists");
        }

        Customer newCustomer = customerService.save(customerDTO.toCustomer());

        return new ResponseEntity<>(newCustomer.toCustomerDTO(), HttpStatus.CREATED);
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> doDeposit(@Validated @RequestBody DepositDTO depositDTO, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()){
            return appUtil.mapErrorToResponse(bindingResult);
        }
        Optional<Customer> customerOptional = customerService.findById(Long.parseLong(depositDTO.getCustomerId()));
        if (!customerOptional.isPresent()) {
            throw new EmailExistsException("Invalid customer Id");
        }
        Customer customer = customerOptional.get();
        Deposit deposit = depositDTO.toDeposit(customer);

        CustomerDTO customerDTOResponse = customerService.deposit(customer,deposit);


        return new ResponseEntity<>(customerDTOResponse, HttpStatus.CREATED);
    }
}
