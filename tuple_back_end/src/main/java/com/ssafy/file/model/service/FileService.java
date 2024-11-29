package com.ssafy.file.model.service;

import com.ssafy.file.model.FileDto;

import java.util.List;

public interface FileService {
    void saveFile(FileDto fileDto);

    List<FileDto> getFilesByAssociation(String associatedTable, int associatedId);

    void deleteFiles(String associatedTable, List<String> fileUrls);

    int updateAssociatedId(String associatedTable, int associatedId);
}
