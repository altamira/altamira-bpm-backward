/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.altamira.bpm;

import java.sql.Types;
import org.hibernate.dialect.Oracle10gDialect;
import org.hibernate.type.StandardBasicTypes;

/**
 *
 * @author PARTH
 */
public class AltamiraCustomDialect extends Oracle10gDialect
{
    public AltamiraCustomDialect()
    {
        registerHibernateType(Types.NVARCHAR, StandardBasicTypes.STRING.getName());
    }
}
