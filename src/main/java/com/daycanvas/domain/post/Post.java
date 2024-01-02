package com.daycanvas.domain.post;

import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
@Table(name = "post")
@Getter @Setter @NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, insertable = false, updatable = false)
    private int id;

    @CreationTimestamp
    @Column(name = "write_date", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime writeDate;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "image_path", nullable = false)
    private String imagePath;

}
