package com.itwillbs.ilkwangtech.sales.service;

import com.itwillbs.ilkwangtech.sales.dto.CustomerDTO;
import com.itwillbs.ilkwangtech.sales.entity.Customer;
import com.itwillbs.ilkwangtech.sales.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        return convertToDTO(customer);
    }

    @Override
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        Customer customer = convertToEntity(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return convertToDTO(savedCustomer);
    }

    @Override
    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        
        customer.setCustomerName(customerDTO.getCustomerName());
        customer.setBusinessNumber(customerDTO.getBusinessNumber());
        customer.setRepresentativeName(customerDTO.getRepresentativeName());
        customer.setPhoneNumber(customerDTO.getPhoneNumber());
        customer.setFaxNumber(customerDTO.getFaxNumber());
        customer.setAddress(customerDTO.getAddress());
        customer.setEmail(customerDTO.getEmail());
        customer.setContactPerson(customerDTO.getContactPerson());
        customer.setRemark(customerDTO.getRemark());
        
        return convertToDTO(customer);
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    private CustomerDTO convertToDTO(Customer customer) {
        return CustomerDTO.builder()
                .customerId(customer.getCustomerId())
                .customerName(customer.getCustomerName())
                .businessNumber(customer.getBusinessNumber())
                .representativeName(customer.getRepresentativeName())
                .phoneNumber(customer.getPhoneNumber())
                .faxNumber(customer.getFaxNumber())
                .address(customer.getAddress())
                .email(customer.getEmail())
                .contactPerson(customer.getContactPerson())
                .remark(customer.getRemark())
                .build();
    }

    private Customer convertToEntity(CustomerDTO dto) {
        Customer customer = new Customer();
        customer.setCustomerName(dto.getCustomerName());
        customer.setBusinessNumber(dto.getBusinessNumber());
        customer.setRepresentativeName(dto.getRepresentativeName());
        customer.setPhoneNumber(dto.getPhoneNumber());
        customer.setFaxNumber(dto.getFaxNumber());
        customer.setAddress(dto.getAddress());
        customer.setEmail(dto.getEmail());
        customer.setContactPerson(dto.getContactPerson());
        customer.setRemark(dto.getRemark());
        return customer;
    }
}
