package org.jenkinsci.plugins.gitlablogo;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.views.ListViewColumn;
import hudson.views.ListViewColumnDescriptor;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.StaplerRequest2;

public class GitlabLogoColumn extends ListViewColumn {
    @Extension
    public static class GitlabLogoColumnDescriptor extends ListViewColumnDescriptor
    {
        @Override
        public boolean shownByDefault() {
            return false;
        }

        @Override
        public String getDisplayName() {
            return "Gitlab Repository Icon";
        }

        @Override
        public ListViewColumn newInstance(StaplerRequest2 request, JSONObject formData) throws FormException {
            return new GitlabLogoColumn();
        }
    }
}
