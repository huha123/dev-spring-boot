package dev.huha123.app.domain.file;

import lombok.Builder;

@Builder(toBuilder = true)
public record FileDto(
    Long id,
    String originalName,
    String contentType,
    Long sizeBytes,
    String s3Bucket,
    String s3Key,
    String s3Region,
    Long boardId
) {
    public static FileDto fromEntity(FileEntity entity) {
        return FileDto.builder()
                .id(entity.getId())
                .originalName(entity.getOriginalName())
                .contentType(entity.getContentType())
                .sizeBytes(entity.getSizeBytes())
                .s3Bucket(entity.getS3Bucket())
                .s3Key(entity.getS3Key())
                .s3Region(entity.getS3Region())
                .boardId(entity.getBoard() != null ? entity.getBoard().getId() : null)
                .build();
    }

    public FileEntity toEntity() {
        return FileEntity.builder()
                .id(id())
                .originalName(originalName())
                .contentType(contentType())
                .sizeBytes(sizeBytes())
                .s3Bucket(s3Bucket())
                .s3Key(s3Key())
                .s3Region(s3Region())
                .build();
    }
}
