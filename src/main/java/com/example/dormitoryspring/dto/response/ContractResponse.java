package com.example.dormitoryspring.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

@Getter
@Setter
public class ContractResponse {
    private Integer id_contract;
    private String id_student;
    private Integer id_dormitory;
    private Integer id_room;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date_start_contract;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date_end_contract;
}