package org.iclass.mvc.dto;

import lombok.*;

import java.sql.Date;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RentDto {
    private int rentno;
    private String email;
    private int homeIdx;
    private Date checkin;
    private Date checkout;
    private int np; //인원수
    private String hname;
    private String filenames;
    private long rentprice;
}