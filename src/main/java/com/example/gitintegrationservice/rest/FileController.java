package com.example.gitintegrationservice.rest;

import com.example.gitintegrationservice.service.FilesProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FileController {

    private final FilesProvider filesService;

    @GetMapping("/api/v1/file/{name}")
    public ResponseEntity<String> getFileContentByName(@PathVariable String name) {
        String file = filesService.getContentFileByName(name);
        if(file == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(file);
        }
    }

    @GetMapping("/api/v1/file/{name}/{commitId}")
    public ResponseEntity<String> getFileContentByNameAndCommitId(@PathVariable String name, @PathVariable String commitId) {
        String file = filesService.getContentFileByNameAndCommitId(name, commitId);
        if(file == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(file);
        }
    }
}
