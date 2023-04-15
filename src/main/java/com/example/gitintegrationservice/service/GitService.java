package com.example.gitintegrationservice.service;

import org.springframework.lang.Nullable;

public interface GitService {
    @Nullable
    String getContentFileByNameAndCommitId(String fileName, @Nullable String commitId);
}
