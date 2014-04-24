/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.altamira.bpm;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.acl.Group;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.security.auth.login.LoginException;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.impl.digest._apacheCommonsCodec.Base64;
import org.jboss.security.auth.spi.UsernamePasswordLoginModule;

/**
 *
 * @author PARTH
 */
public class AltamiraLoginModule extends UsernamePasswordLoginModule {

    @Inject
    private IdentityService identityService;
    
    @Override
    protected String getUsersPassword() throws LoginException {
        
        String userId = super.getUsername();
        
        String password = identityService.createUserQuery().userId(userId).singleResult().getPassword();
        
        return password;
    }

    @Override
    protected boolean validatePassword(String inputPassword, String expectedPassword) {
        
        try {
            
            MessageDigest digest = MessageDigest.getInstance("SHA");
            digest.update(inputPassword.getBytes("UTF-8"));
            byte[] hash = digest.digest();
            String encryptedPassword = "{SHA}" + new String(Base64.encodeBase64(hash));
            
            if(encryptedPassword.equals(expectedPassword))
            {
                return true;
            }
            else
            {
                return false;
            }
            
        } catch (Exception ex) {
            
            ex.printStackTrace();
            return false;
        }
        
    }

    @Override
    protected Group[] getRoleSets() throws LoginException {
        
        return new Group[]{};
    }
    
}
