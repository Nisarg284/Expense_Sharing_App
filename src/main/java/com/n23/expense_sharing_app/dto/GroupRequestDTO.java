package com.n23.expense_sharing_app.dto;


import lombok.Data;

import java.util.List;

@Data
public class GroupRequestDTO {

    private String name;
    private String description;
    private List<Long> userIds;
}
