package org.jasig.portal.security;

import javax.portlet.PortletRequest;
import javax.servlet.http.HttpSession;

/**
 * Manages workflow around use of the identity swapper features.
 * 
 * @author Eric Dalquist
 */
public interface IdentitySwapperManager {
    /**
     * Check if the currentUser can impersonate the targetUsername, returns true if they can, false if not.
     */
    boolean canImpersonateUser(IPerson currentUser, String targetUsername);
    
    /**
     * Check if the currentUser can impersonate the targetUsername, returns true if they can, false if not.
     */
    boolean canImpersonateUser(String currentUserName, String targetUsername);
    
    /**
     * Setup the request so that a subsequent redirect to the login servlet will result in impersonation
     * 
     * @throws RuntimeAuthorizationException if the current user cannot impersonate the target user
     */
    void impersonateUser(PortletRequest portletRequest, IPerson currentUser, String targetUsername);
    
    /**
     * Setup the request so that a subsequent redirect to the login servlet will result in impersonation
     * 
     * @throws RuntimeAuthorizationException if the current user cannot impersonate the target user
     */
    void impersonateUser(PortletRequest portletRequest, String currentUserName, String targetUsername);
    
    /**
     * During impersonation of targetUsername sets the original user to currentUserName for later
     * retrieval by {@link #getOriginalUsername(HttpSession)}
     * 
     * @throws RuntimeAuthorizationException if the current user cannot impersonate the target user
     */
    void setOriginalUser(HttpSession session, String currentUserName, String targetUsername);
    
    /**
     * @return The original user if the current user is an impersonation, null if no impersonation is happening
     */
    String getOriginalUsername(HttpSession session);
    
    /**
     * @return The target of impersonation, null if there is no impersonation target
     */
    String getTargetUsername(HttpSession session);
}
