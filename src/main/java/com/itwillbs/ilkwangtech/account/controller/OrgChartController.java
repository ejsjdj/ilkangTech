package com.itwillbs.ilkwangtech.account.controller;

import com.itwillbs.ilkwangtech.account.dto.DepartmentChart;
import com.itwillbs.ilkwangtech.account.dto.EmployeeChart;
import com.itwillbs.ilkwangtech.account.repository.AccountRepository;
import com.itwillbs.ilkwangtech.account.repository.DepartmentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.http.HttpResponse;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Controller
public class OrgChartController {

    DepartmentRepository departmentRepository;
    AccountRepository accountRepository;

    ModelMapper modelMapper;

    public OrgChartController(DepartmentRepository departmentRepository, AccountRepository accountRepository, ModelMapper modelMapper) {
        this.departmentRepository = departmentRepository;
        this.accountRepository = accountRepository;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/account/orgChart")
    public String organizationPage(Model model) {

        return "/account/orgChart";
    }

    @GetMapping("/account/orgCharJson")
    @ResponseBody
    public Map<String, List<?>> orgCharJson() {

        List<DepartmentChart> departments =
                departmentRepository.findAll().stream()
                        .map(department -> modelMapper.map(department, DepartmentChart.class))
                        .collect(toList());

        List<EmployeeChart> employees =
                accountRepository.findAll().stream()
                        .map(employee -> modelMapper.map(employee, EmployeeChart.class))
                        .collect(toList());

        departments.sort(Comparator.comparing(DepartmentChart::getId));

        Map<String, List<?>> result = new HashMap<>();
        result.put("departments", departments);
        result.put("employees", employees);

        return result;

    }

}
