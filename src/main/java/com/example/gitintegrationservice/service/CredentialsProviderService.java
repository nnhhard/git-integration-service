package com.example.gitintegrationservice.service;

import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

public interface CredentialsProviderService {
    UsernamePasswordCredentialsProvider getUserCredentialsProvider();
}
