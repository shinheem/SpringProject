package org.iclass.mvc.dto;

import lombok.*;

@Data
@AllArgsConstructor
@Builder
@Getter
@Setter
public class HeartDto {
    
    //회원 아이디
    private String email;
    //heart idx
    private long idx;
    //좋아요 갯수
    private boolean status;

    //private int count;


}
