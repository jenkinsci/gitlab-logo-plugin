package org.jenkins_ci.plugins.gitlab_logo;  

import hudson.Extension;

/**
 * Allows GitLab avatar URLs to be loaded in Jenkins UI by adding them to the
 * Content Security Policy (CSP) allowlist.
 * 
 * <p>This extension uses duck typing to work with jenkins.security.csp.AvatarContributorAllows
 * which is available in Jenkins 2.539+. On older Jenkins versions, this class is harmless
 * and will be ignored.</p>
 * 
 * @since 1.1
 */
@Extension(optional = true)
public class GitLabAvatarContributor {
    
    /**
     * Returns a space-separated list of URLs to add to the CSP img-src directive.
     * This allows GitLab project avatars to be displayed in Jenkins 2.539+.
     * 
     * Method signature matches jenkins.security.csp.AvatarContributorAllows#getUrlAllowList()
     * 
     * @return Space-separated list of allowed URL patterns for GitLab domains
     */
    public String getUrlAllowList() {
        // Allow GitLab SaaS (gitlab.com) and all subdomains
        return "https://gitlab.com https://*.gitlab.com";
    }
}