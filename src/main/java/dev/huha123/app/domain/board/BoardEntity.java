package dev.huha123.app.domain.board;

import java.util.ArrayList;
import java.util.List;

import dev.huha123.app.domain.category.CategoryEntity;
import dev.huha123.app.domain.file.FileEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "board")
public class BoardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = true)
    private Integer viewCount;
    private boolean isVisible;
    private boolean isNotice;
    private boolean isSecret;

    private String writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<BoardCommentEntity> comments = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<FileEntity> files = new ArrayList<>();

    @jakarta.persistence.Transient
    private long likeCount;

    @jakarta.persistence.Transient
    private boolean liked;

    public void addComment(BoardCommentEntity comment) {
        this.comments.add(comment);
    }

    public void removeComment(BoardCommentEntity comment) {
        this.comments.remove(comment);
    }

    public void setLikeCount(long likeCount) {
        this.likeCount = likeCount;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public void addFile(FileEntity file) {
        this.files.add(file);
        file.setBoard(this);
    }

    public void addFiles(List<FileEntity> files) {
        for (FileEntity file : files) {
            addFile(file);
        }
    }
}
