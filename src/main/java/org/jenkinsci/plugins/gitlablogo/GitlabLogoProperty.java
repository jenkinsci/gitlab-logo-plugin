package org.jenkinsci.plugins.gitlablogo;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.Job;
import hudson.model.JobProperty;
import hudson.model.JobPropertyDescriptor;
import hudson.util.Secret;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.ProjectApi;
import org.gitlab4j.api.models.Project;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

public class GitlabLogoProperty extends JobProperty<Job<?, ?>> {
  private final String repositoryName;

  // Fields in config.jelly must match the parameter names in the "DataBoundConstructor"
  @DataBoundConstructor
  public GitlabLogoProperty(String repositoryName) {
    this.repositoryName = repositoryName;
  }

  /**
   * We'll use this from the config.jelly.
   */
  public String getRepositoryName() {
    return repositoryName;
  }

  public boolean isAvailable(){
    return StringUtils.isNotEmpty(repositoryName);
  }

  public boolean isDefaultIcon(){
    return StringUtils.isEmpty(getIconUrl());
  }

  @Override
  public DescriptorImpl getDescriptor(){
    return (DescriptorImpl)super.getDescriptor();
  }

  public String getIconUrl(){
    Project project = getProject();

    if(project == null){
      return "";
    } else{
      return project.getAvatarUrl();
    }
  }

  public String getRepositoryUrl(){
    Project project = getProject();

    if(project == null){
      return "";
    } else{
      return project.getWebUrl();
    }
  }

  private Project getProject() {
    return GitlabLogoProjectCache.PROJECT_CACHE.computeIfAbsent(getRepositoryName(), name -> {
      DescriptorImpl descriptor = getDescriptor();
      GitLabApi gitLabApi = new GitLabApi(descriptor.getEndpointUrl(), descriptor.getPrivateToken().getPlainText());
      ProjectApi projectApi = gitLabApi.getProjectApi();
      try {
        return projectApi.getProject(name);
      } catch (GitLabApiException e) {
        throw new RuntimeException(e);
      }
    });
  }

  @Extension
  public static final class DescriptorImpl extends JobPropertyDescriptor
  {
    private Secret privateToken;
    private String endpointUrl;

    public DescriptorImpl(){
      super(GitlabLogoProperty.class);
      super.load();
    }

    @Override
    public String getDisplayName()
    {
      return "GitLab logo";
    }

    @Override
    public boolean isApplicable(Class<? extends Job> jobType)
    {
      return true;
    }

    @Override
    public GitlabLogoProperty newInstance(@NonNull StaplerRequest req, JSONObject formData) throws FormException{
      return req.bindJSON(GitlabLogoProperty.class, formData);
    }

    @Override
    public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
      // To persist global configuration information,
      // set that to properties and call save().
      privateToken = Secret.fromString(formData.getString("privateToken"));
      endpointUrl  = formData.getString("endpointUrl");
      // ^Can also use req.bindJSON(this, formData);
      //  (easier when there are many fields; need set* methods for this, like setUseFrench)
      save();
      return super.configure(req, formData);
    }

    public Secret getPrivateToken() {
      return privateToken;
    }

    public String getEndpointUrl() {
      return endpointUrl;
    }

    public void doClearCache(StaplerRequest req, StaplerResponse rsp){
      GitlabLogoProjectCache.PROJECT_CACHE.clear();
    }
  }
}
