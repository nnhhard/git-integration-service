package com.example.gitintegrationservice.service;

import com.example.gitintegrationservice.dto.FileDto;
import org.springframework.lang.Nullable;
import java.util.Optional;

public interface GitService {

    String getLastCommitId();

    Optional<FileDto> getContentFileByNameAndCommitId(String fileName, @Nullable String commitId);
}
