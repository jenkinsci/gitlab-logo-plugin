package org.jenkinsci.plugins.gitlablogo;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.views.ListViewColumn;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.StaplerRequest;

public class GitlabLogoColumn extends ListViewColumn {
    @Extension
    public static class GitlabLogoColumnDescriptor extends Descriptor<ListViewColumn>
    {
        @Override
        public String getDisplayName() {
            return "Gitlab Repository Icon";
        }

        @Override
        public ListViewColumn newInstance(StaplerRequest request, JSONObject formData) throws FormException {
            return new GitlabLogoColumn();
        }
    }
}
