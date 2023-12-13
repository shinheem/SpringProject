package org.iclass.mvc.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PayCardDto {

    private String number;
    private String approveNo;
    private boolean userCardPoint;
    private Long amount; // 가격 정보
    private String cardType;
    private String ownerType;
    private String acquireStatus;
}