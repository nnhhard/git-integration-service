package com.example.gitintegrationservice.service;

import com.example.gitintegrationservice.dto.FileDto;
import com.example.gitintegrationservice.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GitFilesProvider implements FileProvider {

    private final GitService gitService;

    @Override
    public FileDto getContentFileByName(String fileName) {
        var commitId = gitService.getLastCommitId();

        return gitService.getContentFileByNameAndCommitId(fileName, commitId).orElseThrow(
                () -> new NotFoundException("File " + fileName + " not found")
        );
    }

    @Override
    public FileDto getContentFileByNameAndCommitId(String fileName, String commitId) {
        return gitService.getContentFileByNameAndCommitId(fileName, commitId).orElseThrow(
                () -> new NotFoundException("File " + fileName + " not found in commit " + commitId)
        );
    }
}
