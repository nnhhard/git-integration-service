package com.example.gitintegrationservice.service;

import com.example.gitintegrationservice.config.GitDetailsConfig;
import com.example.gitintegrationservice.dto.FileDto;
import com.example.gitintegrationservice.exception.GitException;
import lombok.RequiredArgsConstructor;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.internal.storage.dfs.DfsRepositoryDescription;
import org.eclipse.jgit.internal.storage.dfs.InMemoryRepository;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GitServiceImpl implements GitService {

    private final GitDetailsConfig gitDetailsConfig;
    private final CredentialsProviderService credentialsProviderService;

    @Override
    public String getLastCommitId() {
        try (InMemoryRepository repo = new InMemoryRepository(new DfsRepositoryDescription());
             Git git = new Git(repo)) {

            gitFetch(git);

            return repo.resolve("refs/heads/" + gitDetailsConfig.getBranch()).getName();
        } catch (Exception ex) {
            throw new GitException(ex.getMessage());
        }
    }

    @Override
    public Optional<FileDto> getContentFileByNameAndCommitId(String fileName, String commitId) {
        try (InMemoryRepository repo = new InMemoryRepository(new DfsRepositoryDescription());
             Git git = new Git(repo)) {

            gitFetch(git);

            byte[] file = findCommit(repo, commitId, fileName);

            return file == null ?
                    Optional.empty() :
                    Optional.of(FileDto.builder()
                            .name(fileName)
                            .commitId(commitId)
                            .file(file)
                            .build());
        } catch (Exception e) {
            throw new GitException(e.getMessage());
        }
    }

    private void gitFetch(Git git) {
        try {
            git.fetch()
                    .setRemote(gitDetailsConfig.getUrl())
                    .setCredentialsProvider(credentialsProviderService.getUserCredentialsProvider())
                    .setRefSpecs(new RefSpec("+refs/heads/*:refs/heads/*"))
                    .call();
        } catch (Exception e) {
            throw new GitException(e.getMessage());
        }
    }

    private byte[] findCommit(InMemoryRepository repo, String commitId, String fileName) {
        try (RevWalk revWalk = new RevWalk(repo)) {
            RevCommit commit = revWalk.parseCommit(ObjectId.fromString(commitId));

            return findFileInTree(repo, commit.getTree(), fileName);

        } catch (Exception e) {
            throw new GitException(e.getMessage());
        }
    }

    private byte[] findFileInTree(InMemoryRepository repo, RevTree tree, String fileName) {
        try (TreeWalk treeWalk = new TreeWalk(repo);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            treeWalk.addTree(tree);
            treeWalk.setRecursive(true);
            treeWalk.setFilter(PathFilter.create(gitDetailsConfig.getFolder() + fileName));

            if (!treeWalk.next()) {
                return null;
            }

            ObjectId objectId = treeWalk.getObjectId(0);
            ObjectLoader loader = repo.open(objectId);

            loader.copyTo(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new GitException(e.getMessage());
        }
    }
}
