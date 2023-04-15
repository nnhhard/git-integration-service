package com.example.gitintegrationservice.service;

import com.example.gitintegrationservice.config.GitDetailsConfig;
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
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
@RequiredArgsConstructor
public class GitServiceImpl implements GitService {

    private final GitDetailsConfig gitDetailsConfig;
    private final CredentialsProviderService credentialsProviderService;

    @Override
    @Nullable
    public String getContentFileByNameAndCommitId(String fileName, @Nullable String commitId) {
        try {
            InMemoryRepository repo = new InMemoryRepository(new DfsRepositoryDescription());
            Git git = new Git(repo);
            git.fetch()
                    .setRemote(gitDetailsConfig.getUrl())
                    //.setCredentialsProvider(credentialsProviderService.getUserCredentialsProvider())
                    .setRefSpecs(new RefSpec("+refs/heads/*:refs/heads/*"))
                    .call();

            repo.getObjectDatabase();

            ObjectId lastCommitId = commitId != null ? ObjectId.fromString(commitId) : repo.resolve("refs/heads/" + gitDetailsConfig.getBranch());
            RevWalk revWalk = new RevWalk(repo);
            RevCommit commit = revWalk.parseCommit(lastCommitId);
            RevTree tree = commit.getTree();

            TreeWalk treeWalk = new TreeWalk(repo);
            treeWalk.addTree(tree);
            treeWalk.setRecursive(true);
            treeWalk.setFilter(PathFilter.create(gitDetailsConfig.getFolder() + fileName));

            if (!treeWalk.next()) {
                return null;
            }

            ObjectId objectId = treeWalk.getObjectId(0);
            ObjectLoader loader = repo.open(objectId);

            String content;
            try(ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                loader.copyTo(out);
                content = out.toString();
            }

            return content.equals("") ? null : content;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
