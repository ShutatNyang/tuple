package com.ssafy.file.controller;

import com.ssafy.board.model.service.BoardService;
import com.ssafy.file.model.FileDto;
import com.ssafy.file.model.service.FileService;
import com.ssafy.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;
    private final BoardService boardService;

    @Autowired
    public FileController(FileService fileService, BoardService boardService) {
        this.fileService = fileService;
        this.boardService = boardService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile[] files, // MultipartFile[]로 변경
            @RequestParam("associatedTable") String associatedTable,
            @RequestParam(value = "associatedId", required = false) Integer associatedId,
            @RequestHeader("Authorization") String token
    ) {
        try {
            token = token.replace("Bearer ", "");
            String jwtEmail = JwtUtil.getEmailFromToken(token);
            int memberId = boardService.getMemberIdByEmail(jwtEmail);

            // 파일 저장 경로
            String folder = "uploads/" + associatedTable.toLowerCase();
            Path folderPath = Paths.get(folder);
            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath);
            }

            // 업로드된 파일 정보를 저장할 리스트
            List<Map<String, Object>> uploadedFilesInfo = new ArrayList<>();

            for (MultipartFile file : files) {
                if (file.isEmpty()) continue; // 빈 파일은 건너뜀

                // 파일 저장
                String originalFilename = file.getOriginalFilename();
                String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
                String saveFile = UUID.randomUUID() + "." + extension;
                Path filePath = folderPath.resolve(saveFile);
                file.transferTo(filePath);

                // DB 저장
                FileDto fileDto = FileDto.builder()
                        .memberId(memberId)
                        .saveFile(saveFile)
                        .saveFolder(folder)
                        .originFile(originalFilename)
                        .fileType("image")
                        .associatedTable(associatedTable)
                        .associatedId(associatedId) // 초기 업로드 시 null 가능
                        .build();
                fileService.saveFile(fileDto);

                // 절대 URL 저장
                String fileUrl = "http://localhost/uploads/" + associatedTable.toLowerCase() + "/" + saveFile;

                uploadedFilesInfo.add(Map.of(
                        "fileId", fileDto.getId(),
                        "fileUrl", fileUrl
                ));
            }

            return ResponseEntity.ok(uploadedFilesInfo);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드 실패");
        }
    }

    @PostMapping("/upload/signup")
    public ResponseEntity<?> uploadProfile(
            @RequestParam("file") MultipartFile[] files, // MultipartFile[]로 변경
            @RequestParam("associatedTable") String associatedTable,
            @RequestParam(value = "associatedId") Integer associatedId
    ) {
        try {
            // 파일 저장 경로
            String folder = "uploads/" + associatedTable.toLowerCase();
            Path folderPath = Paths.get(folder);
            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath);
            }

            // 업로드된 파일 정보를 저장할 리스트
            List<Map<String, Object>> uploadedFilesInfo = new ArrayList<>();

            for (MultipartFile file : files) {
                if (file.isEmpty()) continue; // 빈 파일은 건너뜀

                // 파일 저장
                String originalFilename = file.getOriginalFilename();
                String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
                String saveFile = UUID.randomUUID() + "." + extension;
                Path filePath = folderPath.resolve(saveFile);
                file.transferTo(filePath);

                // DB 저장
                FileDto fileDto = FileDto.builder()
                        .memberId(associatedId)
                        .saveFile(saveFile)
                        .saveFolder(folder)
                        .originFile(originalFilename)
                        .fileType("image")
                        .associatedTable(associatedTable)
                        .associatedId(associatedId) // 초기 업로드 시 null 가능
                        .build();
                fileService.saveFile(fileDto);

                // 절대 URL 저장
                String fileUrl = "http://localhost/uploads/" + associatedTable.toLowerCase() + "/" + saveFile;

                uploadedFilesInfo.add(Map.of(
                        "fileId", fileDto.getId(),
                        "fileUrl", fileUrl
                ));
            }

            return ResponseEntity.ok(uploadedFilesInfo);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드 실패");
        }
    }

    @PostMapping("/updateAssociatedId")
    public ResponseEntity<?> updateAssociatedId(@RequestBody Map<String, String> params) {
        try {
            String associatedTable = params.get("associatedTable");
            int associatedId = Integer.parseInt(params.get("associatedId"));

            int updatedCount = fileService.updateAssociatedId(associatedTable, associatedId);

            if (updatedCount > 0) {
                return ResponseEntity.ok("연관 데이터 업데이트 성공");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("업데이트 실패");
            }
        } catch (Exception e) {
            log.error("연관 데이터 업데이트 실패:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
        }
    }

    @GetMapping(value={"/{associatedTable}/{associatedId}","/{associatedTable}"})
    public ResponseEntity<?> getFiles(
            @PathVariable String associatedTable,
            @PathVariable(required = false) Integer associatedId,
            @RequestHeader(value = "Authorization", required = false) String token) {
        try {
            if (associatedId == null) {
                token = token.replace("Bearer ", "");
                if (!JwtUtil.validateToken(token)) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰");
                }
                String email = JwtUtil.getEmailFromToken(token);
                associatedId = boardService.getMemberIdByEmail(email);
            }

            List<FileDto> files = fileService.getFilesByAssociation(associatedTable, associatedId);
            if (!files.isEmpty()) {
                return ResponseEntity.ok(files);
            } else {
                return ResponseEntity.ok("프로필 없음");
            }
        } catch (Exception e) {
            log.error("파일 조회 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "파일 조회 실패"));
        }
    }


    @DeleteMapping("/delete/{associatedTable}")
    public ResponseEntity<?> deleteFiles(
            @PathVariable String associatedTable,
            @RequestBody Map<String, List<String>> payload // 삭제할 파일 URL 목록
    ) {
        try {
            List<String> fileUrls = payload.get("fileUrls");
            log.debug(fileUrls.toString());
            fileService.deleteFiles(associatedTable, fileUrls);
            return ResponseEntity.ok("파일 삭제 성공");
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 삭제 실패");
        }
    }

}
