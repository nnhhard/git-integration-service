package com.example.gitintegrationservice.service;

import com.example.gitintegrationservice.dto.FileDto;
import com.example.gitintegrationservice.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentMap;

@Service
public class GitFilesProvider implements FileProvider {

    private final GitService gitService;
    private final  ConcurrentMap<String, FileDto> hashFiles;

    public GitFilesProvider(GitService gitService, @Qualifier("concurrentHashMapFileDto") ConcurrentMap<String, FileDto> hashFiles) {
        this.gitService = gitService;
        this.hashFiles = hashFiles;
    }

    @Override
    public FileDto getContentFileByName(String fileName) {
        var commitId = gitService.getLastCommitId();

        if (hashFiles.containsKey(fileName + commitId)) {
            return hashFiles.get(fileName + commitId);
        }

        var fileDto = gitService.getContentFileByNameAndCommitId(fileName, commitId).orElseThrow(
                () -> new NotFoundException("File " + fileName + " not found")
        );

        hashFiles.putIfAbsent(fileDto.getName() + fileDto.getCommitId(), fileDto);

        return fileDto;
    }

    @Override
    public FileDto getContentFileByNameAndCommitId(String fileName, String commitId) {

        if (hashFiles.containsKey(fileName + commitId)) {
            return hashFiles.get(fileName + commitId);
        }

        var fileDto = gitService.getContentFileByNameAndCommitId(fileName, commitId).orElseThrow(
                () -> new NotFoundException("File " + fileName + " not found in commit " + commitId)
        );

        hashFiles.putIfAbsent(fileDto.getName() + fileDto.getCommitId(), fileDto);

        return fileDto;
    }
}
