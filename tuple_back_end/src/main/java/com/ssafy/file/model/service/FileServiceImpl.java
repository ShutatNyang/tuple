package com.ssafy.file.model.service;

import com.ssafy.file.model.FileDto;
import com.ssafy.file.model.mapper.FileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Map;

@Service
public class FileServiceImpl implements FileService {

    private final FileMapper fileMapper;

    @Autowired
    public FileServiceImpl(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    @Override
    public void saveFile(FileDto fileDto) {
        fileMapper.insertFile(fileDto);
    }

    @Override
    public List<FileDto> getFilesByAssociation(String associatedTable, int associatedId) {
        return fileMapper.getFilesByAssociation(Map.of(
                "associatedTable", associatedTable,
                "associatedId", associatedId
        ));
    }

    @Override
    public void deleteFiles(String associatedTable, List<String> fileUrls) {
        for (String fileUrl : fileUrls) {
            // 파일 삭제 로직 구현
            // 예: 파일 시스템에서 삭제하고, 데이터베이스에서 레코드 삭제
            deleteFileFromStorage(associatedTable, fileUrl);
            fileMapper.deleteByFileUrlAndAssociatedTable(fileUrl.substring(fileUrl.lastIndexOf("/") + 1), associatedTable);
        }
    }

    private void deleteFileFromStorage(String associatedTable,String fileUrl) {
        // 파일 시스템에서 파일 삭제 구현
        String projectRoot = System.getProperty("user.dir"); // 현재 작업 디렉토리
        String uploadsDir = projectRoot + "/uploads/" + associatedTable; // uploads 폴더 경로 설정
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1); // 파일명 추출
        File file = new File(uploadsDir, fileName); // 절대 경로로 파일 객체 생성
        if (file.exists()) {
            file.delete();
        }
    }

    @Override
    public int updateAssociatedId(String associatedTable, int associatedId) {
        return fileMapper.updateAssociatedId(associatedTable, associatedId);
    }
}
