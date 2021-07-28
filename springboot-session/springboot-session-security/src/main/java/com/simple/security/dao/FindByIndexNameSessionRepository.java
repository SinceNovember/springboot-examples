package com.simple.security.dao;

// FindByIndexNameSessionRepository.java

import org.springframework.session.Session;
import org.springframework.session.SessionRepository;

import java.util.Map;

public interface FindByIndexNameSessionRepository<S extends Session>
        extends SessionRepository<S> {

    String PRINCIPAL_NAME_INDEX_NAME = FindByIndexNameSessionRepository.class.getName()
            .concat(".PRINCIPAL_NAME_INDEX_NAME");

    Map<String, S> findByIndexNameAndIndexValue(String indexName, String indexValue);

    default Map<String, S> findByPrincipalName(String principalName) {
        return findByIndexNameAndIndexValue(PRINCIPAL_NAME_INDEX_NAME, principalName);
    }

}