/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.altamira.bpm;

import br.com.altamira.erp.entity.services.UserAuthenticationServiceEndpoint;
import java.security.MessageDigest;
import java.security.Principal;
import java.security.acl.Group;
import java.util.Map;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import org.camunda.bpm.engine.impl.digest._apacheCommonsCodec.Base64;
import org.jboss.security.SimpleGroup;
import org.jboss.security.SimplePrincipal;
import org.jboss.security.auth.spi.UsernamePasswordLoginModule;

/**
 *
 * @author PARTH
 */
public class AltamiraLoginModule extends UsernamePasswordLoginModule {
    
    @SuppressWarnings("rawtypes")
    public void initialize(Subject subject, 
                           CallbackHandler callbackHandler,
                           Map sharedState,
                           Map options) {
        super.initialize(subject, callbackHandler, sharedState, options);
    }

    @Override
    public boolean login() throws LoginException {
        
        return super.login();
        /*if(super.loginOk)
        {
            Principal principal = this.getIdentity();
            
            if(principal!=null)
            {
                String[] info = this.getUsernameAndPassword();
                String username = info[0];
                String password = info[1];
                
                sharedState.put("javax.security.auth.login.name", username);
                sharedState.put("javax.security.auth.login.password", password);
            }
            return true;
        }
        else
        {
            return false;
        }*/
    }
    
    @Override
    protected String getUsersPassword() throws LoginException {
        
        InitialContext context = null;
        
        try {
            context = new InitialContext();
            UserAuthenticationServiceEndpoint userAuthentication = (UserAuthenticationServiceEndpoint) context.lookup("java:global/altamira-bpm/UserAuthenticationServiceEndpoint");
            String password = userAuthentication.getUsersPassword(super.getUsername());

            return password;
        } catch (NamingException ex) {
            ex.printStackTrace();
            throw new LoginException("Login failed: " + ex.getMessage());
        }
    }

    @Override
    protected boolean validatePassword(String inputPassword, String expectedPassword) {
        
        try {
            
            MessageDigest digest = MessageDigest.getInstance("SHA");
            digest.update(inputPassword.getBytes("UTF-8"));
            byte[] hash = digest.digest();
            String encryptedPassword = "{SHA}" + new String(Base64.encodeBase64(hash));
            
           return super.validatePassword(encryptedPassword, expectedPassword);
            
        } catch (Exception ex) {
            
            ex.printStackTrace();
            return false;
        }
        
    }

    @Override
    protected Group[] getRoleSets() throws LoginException {
        
        Principal principal = this.getIdentity();
        
        SimpleGroup group = new SimpleGroup("Roles");
        try {
            group.addMember(new SimplePrincipal("user_role"));
        } catch(Exception e) {
            throw new LoginException("Failed to create group member for " + group);
        }
        
        return new Group[]{group};
    }
    
}
