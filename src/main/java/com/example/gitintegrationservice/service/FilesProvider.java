package com.example.gitintegrationservice.service;

public interface FilesProvider {

    String getContentFileByName(String fileName);

    String getContentFileByNameAndCommitId(String fileName, String commitId);
}
