package com.example.dormitoryspring.dto.response;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshResponse {
    private String token;
    private String message;
}
