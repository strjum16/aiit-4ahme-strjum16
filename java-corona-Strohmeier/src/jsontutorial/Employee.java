/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsontutorial;

import java.util.Date;
import java.util.List;

/**
 *
 * @author JulianFew
 */
public class Employee
{
   private Integer id;
   private String firstName;
   private String lastName;
   private List<String> roles;
    
   public Employee(){      
   }
    
   public Employee(Integer id, String firstName, String lastName, Date birthDate){
      this.id = id;
      this.firstName = firstName;
      this.lastName = lastName;
   }
    
   //Getters and setters
    
   @Override
   public String toString()
   {
      return "Employee [id=" + id + ", firstName=" + firstName + ", " +
            "lastName=" + lastName + ", roles=" + roles + "]";
   }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
   
   
   
}