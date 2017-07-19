/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

/**
 *
 * @author Adolfo
 */

//Esta clase va a contener el valor y el tipo de de Variable, para que pueda ser almacenado
//como un objeto en el hashmap

public class Variable {

    private String value;
    private String type;
    
     public Variable (String value, String type){
        this.value = value;
        this.type = type;
    }
     
    public Variable(){
        
    }
    
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    
    public void setType(String type) {
        this.type = type;
    }
    
   
    
}
