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
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findByCustomerId(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        return convertToDTO(customer);
    }

    @Override
    @Transactional
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        Customer customer = convertToEntity(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return convertToDTO(savedCustomer);
    }

    @Override
    @Transactional
    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
        Customer existingCustomer = customerRepository.findByCustomerId(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        
        updateCustomerFromDTO(existingCustomer, customerDTO);
        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return convertToDTO(updatedCustomer);
    }

    @Override
    @Transactional
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findByCustomerId(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        customerRepository.delete(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerDTO> searchCustomers(String name, String businessNo, String manager) {
        List<Customer> customers;
        
        if (name != null && !name.trim().isEmpty()) {
            customers = customerRepository.findByCustomerNameContaining(name);
        } else if (businessNo != null && !businessNo.trim().isEmpty()) {
            customers = customerRepository.findByBusinessNumberContaining(businessNo);
        } else if (manager != null && !manager.trim().isEmpty()) {
            customers = customerRepository.findByContactPersonContaining(manager);
        } else {
            customers = customerRepository.findAll();
        }
        
        return customers.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private CustomerDTO convertToDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setCustomerId(customer.getCustomerId());
        dto.setCustomerName(customer.getCustomerName());
        dto.setBusinessNumber(customer.getBusinessNumber());
        dto.setRepresentativeName(customer.getRepresentativeName());
        dto.setPhoneNumber(customer.getPhoneNumber());
        dto.setFaxNumber(customer.getFaxNumber());
        dto.setAddress(customer.getAddress());
        dto.setEmail(customer.getEmail());
        dto.setContactPerson(customer.getContactPerson());
        dto.setRemark(customer.getRemark());

        dto.setBusinessType(customer.getBusinessType());
        dto.setStatus(customer.getStatus());
        dto.setManagerPhone(customer.getManagerPhone());
        dto.setManagerEmail(customer.getManagerEmail());
        dto.setWebsite(customer.getWebsite());
        dto.setCreatedAt(customer.getCreatedAt());
        dto.setUpdatedAt(customer.getUpdatedAt());
        dto.setCreatedBy(customer.getCreatedBy());
        dto.setUpdatedBy(customer.getUpdatedBy());
        
        return dto;
    }

    private Customer convertToEntity(CustomerDTO dto) {
        Customer customer = new Customer();
        customer.setCustomerId(dto.getCustomerId());
        customer.setCustomerName(dto.getCustomerName());
        customer.setBusinessNumber(dto.getBusinessNumber());
        customer.setRepresentativeName(dto.getRepresentativeName());
        customer.setPhoneNumber(dto.getPhoneNumber());
        customer.setFaxNumber(dto.getFaxNumber());
        customer.setAddress(dto.getAddress());
        customer.setEmail(dto.getEmail());
        customer.setContactPerson(dto.getContactPerson());
        customer.setRemark(dto.getRemark());
        customer.setBusinessType(dto.getBusinessType());
        customer.setStatus(dto.getStatus());
        customer.setManagerPhone(dto.getManagerPhone());
        customer.setManagerEmail(dto.getManagerEmail());
        customer.setWebsite(dto.getWebsite());
        customer.setCreatedBy(dto.getCreatedBy());
        customer.setUpdatedBy(dto.getUpdatedBy());
        return customer;
    }

    private void updateCustomerFromDTO(Customer customer, CustomerDTO dto) {
        customer.setCustomerName(dto.getCustomerName());
        customer.setBusinessNumber(dto.getBusinessNumber());
        customer.setRepresentativeName(dto.getRepresentativeName());
        customer.setPhoneNumber(dto.getPhoneNumber());
        customer.setFaxNumber(dto.getFaxNumber());
        customer.setAddress(dto.getAddress());
        customer.setEmail(dto.getEmail());
        customer.setContactPerson(dto.getContactPerson());
        customer.setRemark(dto.getRemark());
        customer.setBusinessType(dto.getBusinessType());
        customer.setStatus(dto.getStatus());
        customer.setManagerPhone(dto.getManagerPhone());
        customer.setManagerEmail(dto.getManagerEmail());
        customer.setWebsite(dto.getWebsite());
        customer.setUpdatedBy(dto.getUpdatedBy());
    }
}
