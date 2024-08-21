package com.example.dormitoryspring.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntrospectRequest {
  private  String token;
}
