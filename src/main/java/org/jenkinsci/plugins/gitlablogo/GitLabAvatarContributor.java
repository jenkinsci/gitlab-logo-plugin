package org.jenkinsci.plugins.gitlablogo;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import hudson.Extension;
import jenkins.model.Jenkins;

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
    
    private static final Logger LOGGER = Logger.getLogger(GitLabAvatarContributor.class.getName());
    
    /**
     * Returns a space-separated list of URLs to add to the CSP img-src directive.
     * This allows GitLab project avatars to be displayed in Jenkins 2.539+.
     * 
     * <p>This method includes both gitlab.com (SaaS) and any configured self-hosted
     * GitLab endpoint from the plugin's global configuration.</p>
     * 
     * Method signature matches jenkins.security.csp.AvatarContributorAllows#getUrlAllowList()
     * 
     * @return Space-separated list of allowed URL patterns for GitLab domains
     */
    public String getUrlAllowList() {
        Set<String> allowedDomains = new HashSet<>();
        
        // Always allow GitLab SaaS
        allowedDomains.add("https://gitlab.com");
        allowedDomains.add("https://*.gitlab.com");
        
        // Add configured self-hosted GitLab endpoint if present
        Jenkins jenkins = Jenkins.getInstanceOrNull();
        if (jenkins != null) {
            GitlabLogoProperty.DescriptorImpl descriptor = 
                jenkins.getDescriptorByType(GitlabLogoProperty.DescriptorImpl.class);
            
            if (descriptor != null) {
                String endpointUrl = descriptor.getEndpointUrl();
                if (endpointUrl != null && !endpointUrl.isEmpty()) {
                    try {
                        java.net.URL url = new java.net.URL(endpointUrl);
                        String domain = url.getProtocol() + "://" + url.getHost();
                        if (url.getPort() != -1 && url.getPort() != 80 && url.getPort() != 443) {
                            domain += ":" + url.getPort();
                        }
                        allowedDomains.add(domain);
                        LOGGER.fine("Adding self-hosted GitLab domain to CSP allowlist: " + domain);
                    } catch (java.net.MalformedURLException e) {
                        LOGGER.warning("Invalid GitLab endpoint URL in configuration: " + endpointUrl);
                    }
                }
            }
        }
        
        // Join all domains with spaces
        return String.join(" ", allowedDomains);
    }
}