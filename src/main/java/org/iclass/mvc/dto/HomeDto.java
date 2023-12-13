package org.iclass.mvc.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomeDto {
    private long idx;
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
    private String detailaddress;


    private List<MultipartFile> pics;

}
