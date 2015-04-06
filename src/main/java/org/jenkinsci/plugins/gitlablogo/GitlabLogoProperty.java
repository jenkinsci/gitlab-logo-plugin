package org.jenkinsci.plugins.gitlablogo;

import hudson.Extension;
import hudson.model.Job;
import hudson.model.JobProperty;
import hudson.model.JobPropertyDescriptor;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.jenkinsci.plugins.gitlablogo.api.GitlabApi;
import org.jenkinsci.plugins.gitlablogo.api.Project;
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
   * We'll use this from the <tt>config.jelly</tt>.
   */
  public String getRepositoryName() {
    return repositoryName;
  }

  public boolean isAvailable(){
    return StringUtils.isNotEmpty(repositoryName) && StringUtils.isNotEmpty(getIconUrl());
  }

  @Override
  public DescriptorImpl getDescriptor(){
    return (DescriptorImpl)super.getDescriptor();
  }

  public String getIconUrl(){
    GitlabLogoProperty.DescriptorImpl descriptor = getDescriptor();
    GitlabApi api = new GitlabApi(descriptor.getEndpointUrl(), descriptor.getPrivateToken());
    Project project = api.getCachedProject(getRepositoryName());

    if(project == null){
      return "";
    } else{
      return project.avatarUrl;
    }
  }

  @Extension
  public static final class DescriptorImpl extends JobPropertyDescriptor
  {
    private String privateToken;
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
    public GitlabLogoProperty newInstance(StaplerRequest req, JSONObject formData) throws FormException{
      return req.bindJSON(GitlabLogoProperty.class, formData);
    }

    @Override
    public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
      // To persist global configuration information,
      // set that to properties and call save().
      privateToken = formData.getString("privateToken");
      endpointUrl  = formData.getString("endpointUrl");
      // ^Can also use req.bindJSON(this, formData);
      //  (easier when there are many fields; need set* methods for this, like setUseFrench)
      save();
      return super.configure(req, formData);
    }

    public String getPrivateToken() {
      return privateToken;
    }

    public String getEndpointUrl() {
      return endpointUrl;
    }

    public void doClearCache(StaplerRequest req, StaplerResponse rsp){
      GitlabApi.clearCache();
    }
  }
}
