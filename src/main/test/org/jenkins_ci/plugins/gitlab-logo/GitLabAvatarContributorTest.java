package org.jenkinsci.plugins.gitlablogo;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for GitLabAvatarContributor to ensure proper CSP allowlist configuration.
 */
public class GitLabAvatorContributorTest {
    
    @Test
    public void testUrlAllowListContainsGitLabSaaSDomains() {
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
    
    @Test
    public void testUrlAllowListHandlesNullConfiguration() {
        // This test verifies the method doesn't crash when configuration is null/empty
        GitLabAvatarContributor contributor = new GitLabAvatarContributor();
        String allowList = contributor.getUrlAllowList();
        
        // Should at minimum contain gitlab.com even if no custom endpoint configured
        assertNotNull("Allow list should not be null", allowList);
        assertTrue("Should contain gitlab.com as fallback", 
                   allowList.contains("gitlab.com"));
    }
}