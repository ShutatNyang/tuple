package com.ssafy.file.model.mapper;

import com.ssafy.file.model.FileDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface FileMapper {

    void insertFile(FileDto fileDto);

    List<FileDto> getFilesByAssociation(Map<String, Object> params);

    void deleteByFileUrlAndAssociatedTable(String fileUrl, String associatedTable);

    int updateAssociatedId(String associatedTable, int associatedId);
}
