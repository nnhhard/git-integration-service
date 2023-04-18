package com.example.gitintegrationservice.rest;

import com.example.gitintegrationservice.dto.FileDto;
import com.example.gitintegrationservice.service.FileProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileProvider filesService;

    @GetMapping("/api/v1/file/{name}")
    public ResponseEntity<FileDto> getFileContentByName(@PathVariable String name) {
        return ResponseEntity.ok(filesService.getContentFileByName(name));
    }

    @GetMapping("/api/v1/file/{name}/{commitId}")
    public ResponseEntity<FileDto> getFileContentByNameAndCommitId(@PathVariable String name, @PathVariable String commitId) {
        return ResponseEntity.ok(filesService.getContentFileByNameAndCommitId(name, commitId));
    }
}
