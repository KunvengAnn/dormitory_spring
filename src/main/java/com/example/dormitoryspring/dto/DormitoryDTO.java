package com.example.dormitoryspring.dto;

import lombok.*;
import java.util.Set;

@Getter
@Setter
public class DormitoryDTO {
    private Integer id_dormitory;
    private String dormitory_name;
    private Set<RoomDTO> rooms;
    private Set<ContractDTO> contracts;
}
