/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Parser;

import Tokenizer.Tokenizer;
import java.awt.Component;
import javax.swing.JOptionPane;
import main.GUI;

/**
 *
 * @author Adolfo
 */

//Esta clase contiene comandos de sistema

public class SystemParser {
    
    private String line;
    private Tokenizer tokenizer;
    
    
    //Constructor
    public SystemParser(String line, Tokenizer tokenizer){
        this.line = line;
        this.tokenizer = tokenizer;
    }
    
    //REGEX De comandos del sistema
    
    public String LIMP = "LIMP";
    public String VERS = "VERS";
    public String TERM = "TERM";
    
    //Verifica si existe el regex asociado en este parser
    public boolean verificar_regex(){
        
        return line.matches(LIMP+"|"+VERS+"|"+TERM);        
    }
    
    //Metodo principal para actuar
    public void principal(){
        
        if (line.matches(TERM)){
            
            //Sale del Programa, previa advertencia.
            int dialogButton = 0;
            int dialogResult = JOptionPane.showConfirmDialog (null, "Desea salir del Programa?","Advertencia",dialogButton);
            if(dialogResult == JOptionPane.YES_OPTION)
            {
                System.exit(0);
            }
        }
        
        if (line.matches(VERS)){
            // Imprime la version del Programa en un dialogo.
            Component frame = null;
            JOptionPane.showMessageDialog(frame, "Analizador Léxico 0.1 - Olman Rojas"); 
        }
        
        if (line.matches(LIMP)){
            
            //Limpia la pantalla
            int dialogButton = 0;
            int dialogResult = JOptionPane.showConfirmDialog (null, "Desea limpiar la pantalla?","Advertencia",dialogButton);
            if(dialogResult == JOptionPane.YES_OPTION)
                {
                    GUI.screen.setText("");
                }
        }
        
        
    } // Fin de metodo ejecutarComando()
            
    
} //Fin de la clase
