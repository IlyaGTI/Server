package com.example.watchprog.Entity;

import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;

@Data
@Entity
public class ImageModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String name;
    @Lob
    @Column(columnDefinition = "LONGBLOG")
    private byte[] imageBytes;
    @JsonIgnore
    private long userId;
    @JsonIgnore
    private long postId;
}
