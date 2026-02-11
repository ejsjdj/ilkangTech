package com.itwillbs.ilkwangtech.sales.service.customer;

import com.itwillbs.ilkwangtech.sales.constant.CustomerStatus;
import com.itwillbs.ilkwangtech.sales.dto.CustomerDTO;
import com.itwillbs.ilkwangtech.sales.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    /**
     * 협력사 추가
     * @param customerDTO
     */
    @Override
    @Transactional
    public void createCustomer(CustomerDTO customerDTO) {
        if (customerRepository.findByName(customerDTO.getName()) != null)
            throw new IllegalArgumentException("이미 존재하는 고객입니다.");
        customerDTO.setValid(CustomerStatus.ACTIVE);
        customerRepository.save(customerDTO);
    }

    /**
     * 협력사 리스트 조회
     * @param pageable
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<CustomerDTO> getCustomerList(Pageable pageable) {
        long offset = pageable.getOffset();
        int pageSize = pageable.getPageSize();
        return customerRepository.findAll(offset, pageSize);
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalCount() {
        return customerRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerDTO getCustomerById(Long customerId) {
        return customerRepository.findById(customerId).orElseThrow(() ->
                new IllegalArgumentException("해당 고객사를 찾을 수 없습니다."));
    }

    @Override
    @Transactional
    public void updateCustomer(Long customerId,CustomerDTO customerDTO) {
        CustomerDTO customer = customerRepository.findById(customerId).orElseThrow(() ->
                new IllegalArgumentException("해당 고객사를 찾을 수 없습니다."));

        customer.setName(customerDTO.getName());
        customer.setEmail(customerDTO.getEmail());
        customer.setPhone(customerDTO.getPhone());

        customerRepository.update(customerDTO);
    }

    @Override
    @Transactional
    public void invalidCustomer(Long customerId) {
        customerRepository.invalid(customerId);
    }

    @Override
    @Transactional
    public void validCustomer(Long customerId) {
        customerRepository.valid(customerId);
    }

}