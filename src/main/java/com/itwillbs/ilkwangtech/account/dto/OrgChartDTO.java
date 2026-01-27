package com.itwillbs.ilkwangtech.account.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrgChartDTO {
    private CeoDTO ceo;
    private List<DivisionDTO> divisions;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CeoDTO {
        private String name;
        private String title;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DivisionDTO {
        private String id;
        private String name;
        private String headName;
        private String headTitle;
        private String theme;
        private List<DepartmentDTO> departments;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DepartmentDTO {
        private Integer id;
        private String name;
        private List<MemberDTO> members;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberDTO {
        private String name;
        private String title;
    }
}
