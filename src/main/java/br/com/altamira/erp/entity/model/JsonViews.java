/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.altamira.erp.entity.model;

/**
 *
 * @author pci109
 */
public class JsonViews 
{
    // Request
    public static class RequestOnly {}
    public static class RequestExtended extends RequestOnly {}
    
    // Request Item
    public static class RequestItemOnly {}
    public static class RequestItemExtended extends RequestItemOnly {}
}
