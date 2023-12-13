package org.iclass.mvc.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
@Builder
public class ReviewDto {

    private long idx;
    private String filenames;
    private String id;
    private String comments;
    private String writer;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdAt;
    private long rnum;

    private List<MultipartFile> pics;
}
