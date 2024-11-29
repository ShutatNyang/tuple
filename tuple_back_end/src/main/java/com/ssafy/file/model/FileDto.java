package com.ssafy.file.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileDto {
    private int id;
    private int memberId;
    private String saveFile;
    private String saveFolder;
    private String originFile;
    private String fileType; // image, document, video, etc
    private String associatedTable;
    private int associatedId;
    private LocalDateTime uploadedTime;
}
