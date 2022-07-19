package com.cg.service.customer;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.LocationRegion;
import com.cg.model.Transfer;
import com.cg.model.dto.CustomerDTO;
import com.cg.repository.CustomerRepository;
import com.cg.repository.DepositRepository;
import com.cg.repository.LocationRegionRepository;
import com.cg.repository.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private DepositRepository depositRepository;

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private LocationRegionRepository locationRegionRepository;

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public List<Customer> findAllByIdNot(Long id) {
        return customerRepository.findAllByIdNot(id);
    }

    @Override
    public Customer getById(Long id) {
        return customerRepository.getById(id);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return customerRepository.existsByEmail(email);
    }

    @Override
    public Customer save(Customer customer) {
        LocationRegion locationRegion = locationRegionRepository.save(customer.getLocationRegion());
        customer.setLocationRegion(locationRegion);
        return customerRepository.save(customer);
    }



    @Override
    public CustomerDTO deposit(Customer customer, Deposit deposit) {

        customerRepository.incrementBalance(customer.getId(), deposit.getTransactionAmount());

        depositRepository.save(deposit);

        return customerRepository.findById(customer.getId()).get().toCustomerDTO();
    }

    @Override
    public List<CustomerDTO> findAllCustomerDTOByDeletedIsFalse() {
        return customerRepository.findAllCustomerDTOByDeletedIsFalse();
    }

    @Override
    public void doTransfer(Transfer transfer) {

        customerRepository.reduceBalance(transfer.getSender().getId(), transfer.getTransactionAmount());

        customerRepository.incrementBalance(transfer.getRecipient().getId(), transfer.getTransferAmount());

        transferRepository.save(transfer);
    }

    @Override
    public void remove(Long id) {

    }
}
