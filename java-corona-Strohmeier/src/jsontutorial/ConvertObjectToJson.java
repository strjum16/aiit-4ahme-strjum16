/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsontutorial;

import com.google.gson.Gson;
import java.util.Arrays;

/**
 *
 * @author JulianFew
 */
public class ConvertObjectToJson {
    public static void main(String[] args) {
        
        
        Employee employee = new Employee();
        employee.setId(1);
        employee.setFirstName("Lokesh");
        employee.setLastName("Gupta");
        employee.setRoles(Arrays.asList("ADMIN", "MANAGER"));
 
        Gson gson = new Gson();
 
        System.out.println(gson.toJson(employee));
    }
}
