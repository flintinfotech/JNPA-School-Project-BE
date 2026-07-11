package com.flint.sample_be_springboot.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Base64;

@Table(name = "NEWS_ENTITY")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NewsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "NEWS_ID")
    private Long newsId;

    @NotNull
    @Column(name = "NEWS")
    private String news;

    @Column(name = "NEWS_DESCRIPTION")
    private String newsDescription;

    @Lob
    @Column(name = "NEWS_DATA")
    private byte[] newsData;

    public String getNewsData() {
        if (newsData != null) {
            return Base64.getEncoder().encodeToString(newsData);
        }
        return null;
    }

}
