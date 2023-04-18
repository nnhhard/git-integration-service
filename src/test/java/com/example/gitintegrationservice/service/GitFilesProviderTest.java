package com.example.gitintegrationservice.service;

import com.example.gitintegrationservice.dto.FileDto;
import com.example.gitintegrationservice.exception.GitException;
import com.example.gitintegrationservice.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HexFormat;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {GitFilesProvider.class})
public class GitFilesProviderTest {

    @Autowired
    private GitFilesProvider gitFilesProvider;

    @MockBean
    private GitService gitService;

    private static final FileDto EXPECTED_FILE = FileDto.builder()
            .file(HexFormat.of().parseHex("e04fd020ea3a6910a2d808002b30309d"))
            .name("test.xml")
            .commitId(UUID.randomUUID().toString())
            .build();

    @Test
    public void shouldCorrectReturnDataFromFindByName() {
        when(gitService.getLastCommitId()).thenReturn(EXPECTED_FILE.getCommitId());
        when(gitService.getContentFileByNameAndCommitId(EXPECTED_FILE.getName(), EXPECTED_FILE.getCommitId()))
                .thenReturn(Optional.of(EXPECTED_FILE));

        var actualFileDto = gitFilesProvider.getContentFileByName(EXPECTED_FILE.getName());

        assertThat(actualFileDto).isEqualTo(EXPECTED_FILE);
    }

    @Test
    public void shouldCorrectReturnNotFoundExceptionFromFindByName() {
        when(gitService.getLastCommitId()).thenReturn(EXPECTED_FILE.getCommitId());
        when(gitService.getContentFileByNameAndCommitId(EXPECTED_FILE.getName(), EXPECTED_FILE.getCommitId()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> gitFilesProvider.getContentFileByName(EXPECTED_FILE.getName()))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("File " + EXPECTED_FILE.getName() + " not found");
    }

    @Test
    public void shouldCorrectReturnGitExceptionFromFindByName() {
        String expectedMessageException = "Branch not found";

        when(gitService.getLastCommitId()).thenThrow(new GitException(expectedMessageException));

        assertThatThrownBy(() -> gitFilesProvider.getContentFileByName(EXPECTED_FILE.getName()))
                .isInstanceOf(GitException.class)
                .hasMessageContaining(expectedMessageException);
    }

    @Test
    public void shouldCorrectReturnDataFromFindByNameAndCommitId() {
        when(gitService.getContentFileByNameAndCommitId(EXPECTED_FILE.getName(), EXPECTED_FILE.getCommitId()))
                .thenReturn(Optional.of(EXPECTED_FILE));

        var actualFileDto = gitFilesProvider.getContentFileByNameAndCommitId(EXPECTED_FILE.getName(), EXPECTED_FILE.getCommitId());

        assertThat(actualFileDto).isEqualTo(EXPECTED_FILE);
    }

    @Test
    public void shouldCorrectReturnNotFoundExceptionFromFindByNameAndCommitId() {
        when(gitService.getContentFileByNameAndCommitId(EXPECTED_FILE.getName(), EXPECTED_FILE.getCommitId()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> gitFilesProvider.getContentFileByNameAndCommitId(EXPECTED_FILE.getName(), EXPECTED_FILE.getCommitId()))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("File " + EXPECTED_FILE.getName() + " not found");
    }

    @Test
    public void shouldCorrectReturnGitExceptionFromFindByNameAndCommitId() {
        String expectedMessageException = "Branch not found";

        when(gitService.getContentFileByNameAndCommitId(EXPECTED_FILE.getName(), EXPECTED_FILE.getCommitId()))
                .thenThrow(new GitException(expectedMessageException));

        assertThatThrownBy(() -> gitFilesProvider.getContentFileByNameAndCommitId(EXPECTED_FILE.getName(), EXPECTED_FILE.getCommitId()))
                .isInstanceOf(GitException.class)
                .hasMessageContaining(expectedMessageException);
    }
}
