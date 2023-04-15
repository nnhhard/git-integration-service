package com.example.gitintegrationservice.service;

import com.example.gitintegrationservice.config.CredentialsProviderConfig;
import lombok.RequiredArgsConstructor;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CredentialsProviderServiceImpl implements CredentialsProviderService {

    private final CredentialsProviderConfig credentialsProviderConfig;

    @Override
    public UsernamePasswordCredentialsProvider getUserCredentialsProvider() {
        return new UsernamePasswordCredentialsProvider(
                credentialsProviderConfig.getLogin(),
                credentialsProviderConfig.getPassword()
        );
    }
}
