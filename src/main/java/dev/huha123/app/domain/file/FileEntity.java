package dev.huha123.app.domain.file;


import dev.huha123.app.domain.board.BoardEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "original_name", length = 255, nullable = true)
    private String originalName;

    @Column(name = "content_type", length = 127)
    private String contentType;

    @Column(name = "size_bytes", nullable = true)
    private Long sizeBytes;

    // --- S3 메타데이터 (필수 + 권장) ---
    @Column(name = "s3_bucket", length = 128, nullable = true)
    private String s3Bucket;

    @Column(name = "s3_key", length = 1024, nullable = true)
    private String s3Key;

    @Column(name = "s3_region", length = 32)
    private String s3Region;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = true)
    private BoardEntity board;
}
