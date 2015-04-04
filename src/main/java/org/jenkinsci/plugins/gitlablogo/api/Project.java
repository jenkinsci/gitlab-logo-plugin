package org.jenkinsci.plugins.gitlablogo.api;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Project {
  @JsonProperty("avatar_url")
  public String avatarUrl;
}
