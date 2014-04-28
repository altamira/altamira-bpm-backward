/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.altamira.erp.entity.services;

import javax.ejb.Stateless;
import javax.inject.Inject;
import org.camunda.bpm.engine.IdentityService;

/**
 *
 * @author PARTH
 */

@Stateless
public class UserAuthenticationServiceEndpoint {
    
    @Inject
    private IdentityService identityService;
    
    public String getUsersPassword(String userId) {
        
        String password = identityService.createUserQuery().userId(userId).singleResult().getPassword();
        
        return password;
        
    }
    
}
