package com.example.gitintegrationservice.service;

import com.example.gitintegrationservice.dto.FileDto;

public interface FileProvider {

    FileDto getContentFileByName(String fileName);

    FileDto getContentFileByNameAndCommitId(String fileName, String commitId);
}
