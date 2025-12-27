package org.jenkins_ci.plugins.gitlab_logo;  // Change this line

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Tests for GitLabAvatarContributor to ensure proper CSP allowlist configuration.
 */
public class GitLabAvatarContributorTest {
    
    @Test
    public void testUrlAllowListContainsGitLabDomains() {
        GitLabAvatarContributor contributor = new GitLabAvatarContributor();
        String allowList = contributor.getUrlAllowList();
        
        assertNotNull("Allow list should not be null", allowList);
        assertTrue("Allow list should contain gitlab.com", 
                   allowList.contains("gitlab.com"));
        assertTrue("Allow list should contain wildcard for GitLab subdomains", 
                   allowList.contains("*.gitlab.com"));
        assertFalse("Allow list should not be empty", 
                    allowList.trim().isEmpty());
    }
    
    @Test
    public void testUrlAllowListUsesHttpsProtocol() {
        GitLabAvatarContributor contributor = new GitLabAvatarContributor();
        String allowList = contributor.getUrlAllowList();
        
        String[] urls = allowList.split("\\s+");
        for (String url : urls) {
            assertTrue("All URLs should use HTTPS protocol: " + url, 
                       url.startsWith("https://"));
        }
    }
}