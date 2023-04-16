package com.example.gitintegrationservice.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FileDto {
    private String name;
    private String commitId;
    private byte[] file;
}