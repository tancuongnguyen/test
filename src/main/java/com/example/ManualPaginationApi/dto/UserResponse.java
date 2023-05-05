package com.example.ManualPaginationApi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private long totalItems;
    private List<UserDto> data;
    private int totalPages;
    private int currentPage;
}
