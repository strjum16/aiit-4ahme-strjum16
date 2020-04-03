/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsontutorial;

import com.google.gson.Gson;

/**
 *
 * @author JulianFew
 */
public class ConvertJsonToObject {
    public static void main(String[] args) {
        
        Gson gson = new Gson();
 
        System.out.println(
        gson.fromJson("{'id':1,'firstName':'Lokesh','lastName':'Gupta','roles':['ADMIN','MANAGER']}", 
        Employee.class));
        
    }
}
