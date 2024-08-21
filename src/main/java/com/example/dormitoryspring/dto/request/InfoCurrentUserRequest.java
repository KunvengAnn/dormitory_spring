package com.example.dormitoryspring.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class InfoCurrentUserRequest {
    private String email;
}
