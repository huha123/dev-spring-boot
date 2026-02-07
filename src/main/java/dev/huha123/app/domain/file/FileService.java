package dev.huha123.app.domain.file;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import dev.huha123.app.domain.board.BoardEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileService {

    private final FileRepository fileRepository;
    // private final S3FileUploadUtil s3FileUploadUtil;

    @Value("${spring.cloud.aws.s3.bucket:test-bucket}")
    private String bucketName;

    @Value("${spring.cloud.aws.s3.region:ap-northeast-2}")
    private String region;

    // public S3FileUploadUtil getS3FileUploadUtil() {
    // return s3FileUploadUtil;
    // }

    @Transactional
    public List<FileEntity> saveFiles(List<MultipartFile> files, BoardEntity board) {
        if (files == null || files.isEmpty()) {
            return new ArrayList<>();
        }

        List<FileEntity> savedFiles = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue;
            }

            // Generate S3 key
            String s3Key = generateS3Key(file.getOriginalFilename());

            // TODO: S3 업로드

            // DB에 파일 메타데이터 저장
            FileEntity fileEntity = FileEntity.builder().originalName(file.getOriginalFilename())
                    .contentType(file.getContentType()).sizeBytes(file.getSize()).s3Bucket(bucketName)
                    .s3Key(s3Key).s3Region(region).board(board).build();
            log.info("Saved file: {} -> s3://{}/{}", file.getOriginalFilename(), bucketName, s3Key);
            savedFiles.add(fileRepository.save(fileEntity));
        }

        return savedFiles;
    }

    @Transactional
    public List<FileEntity> saveFiles(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            return new ArrayList<>();
        }

        List<FileEntity> savedFiles = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue;
            }

            // Generate S3 key
            String s3Key = generateS3Key(file.getOriginalFilename());

            // TODO: S3 업로드

            // DB에 파일 메타데이터 저장
            log.info("Saved file: {} -> s3://{}/{}", file.getOriginalFilename(), bucketName, s3Key);
            savedFiles.add(FileEntity.builder().originalName(file.getOriginalFilename())
                    .contentType(file.getContentType()).sizeBytes(file.getSize()).s3Bucket(bucketName)
                    .s3Key(s3Key).s3Region(region).build());
        }

        return savedFiles;
    }

    public List<FileDto> getFilesByBoardId(Long boardId) {
        return fileRepository.findByBoardId(boardId).stream().map(FileDto::fromEntity).toList();
    }

    @Transactional
    public void deleteFilesByBoardId(Long boardId) {
        List<FileEntity> files = fileRepository.findByBoardId(boardId);
        // S3에서 실제 파일 삭제
        // for (FileEntity file : files) {
        // s3FileUploadUtil.deleteFileByKey(file.getS3Key());
        // log.info("Deleted file from S3: s3://{}/{}", file.getS3Bucket(),
        // file.getS3Key());
        // }
        fileRepository.deleteByBoardId(boardId);
    }

    public FileEntity getFileById(Long fileId) {
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new IllegalArgumentException("File not found: " + fileId));
    }

    private String generateS3Key(String originalFilename) {
        String uuid = UUID.randomUUID().toString();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        return "board/" + uuid + extension;
    }
}
