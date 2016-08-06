/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unapec.cine.model.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author yumarx
 */
public class EMFUtil {
    public static EntityManagerFactory emf = Persistence.createEntityManagerFactory("edu.unapec_cine_jar_1.0-SNAPSHOTPU");
    
}
