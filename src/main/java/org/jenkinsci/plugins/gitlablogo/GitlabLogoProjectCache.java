package org.jenkinsci.plugins.gitlablogo;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.gitlab4j.api.models.Project;

class GitlabLogoProjectCache {
  static final ConcurrentMap<String, Project> PROJECT_CACHE = new ConcurrentHashMap<>();
}
