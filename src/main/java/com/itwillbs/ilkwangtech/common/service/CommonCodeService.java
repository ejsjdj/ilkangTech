package com.itwillbs.ilkwangtech.common.service;

public interface CommonCodeService {

    String getDepartmentName(Integer id);

    String getPositionName(Integer id);

    String getBankName(Integer id);

    String getRoleName(Long id);

    void updateDepartment(Integer id, String newName);

    void updatePosition(Integer id, String newName);

    void updateBank(Integer id, String newName);

    void updateRole(Long id, String newName);

}
