package org.iclass.mvc.dto;

import lombok.*;

import java.sql.Date;

@Data
@AllArgsConstructor
@Builder
@Getter
@Setter
public class HeartHomeDto {

    //회원 아이디
    private String email;
    //heart idx
    private long idx;
    //좋아요 갯수
    private boolean status;

    private long hidx;
    private String hname;
    private String address;
    private long price;
    private String content;
    private String filenames;
    private String fcontent;
    private String hometype;
    private int people;
    private int readcount;
    private long heart;
    private Date startdate;
    private Date enddate;
    private float lat;
    private float lng;


}