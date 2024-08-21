package com.example.dormitoryspring.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoomDTO {
    private Integer id_room;
    private Integer room_number;
    private Double room_price;
    private Integer room_max_capacity;
    private Integer quantity_person;
    private Boolean status_is_empty_room;
    private Boolean status_room_is_boy;

    private DormitoryDTO dormitory;
    private List<ContractDTOTwo> contracts;
}
