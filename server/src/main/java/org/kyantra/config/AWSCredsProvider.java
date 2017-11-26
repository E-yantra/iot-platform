package org.kyantra.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;

public class AWSCredsProvider implements AWSCredentialsProvider {

    AWSCredentials creds;

    public AWSCredsProvider(BasicAWSCredentials credentials){
        super();
        this.creds = credentials;
    }

    @Override
    public AWSCredentials getCredentials() {
        return creds;
    }

    @Override
    public void refresh() {

    }
}
