package com.example.gitintegrationservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FilesProviderGit implements FilesProvider {

    private final GitService gitService;

    @Override
    public String getContentFileByName(String fileName) {
        return gitService.getContentFileByNameAndCommitId(fileName, null);
    }

    @Override
    public String getContentFileByNameAndCommitId(String fileName, String commitId) {
        return gitService.getContentFileByNameAndCommitId(fileName, commitId);
    }
}
