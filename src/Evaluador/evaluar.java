/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Evaluador;

import main.GUI;



/**
 *
 * @author Adolfo
 */

//El siguiente es un parser recursivo descendente.
// Utiliza metodos de parseo separados para cada nivel de procedencia de operador

public class evaluar {
    
    
    
    public static double eval(final String str) {
    return new Object() {
        
        
        
        int pos = -1, ch;

        //Se analiza cada posicion de caracteres en un string.
        void nextChar() {
            ch = (++pos < str.length()) ? str.charAt(pos) : -1;
        }

        //Caracteres a eliminar Ejemplo espacios en blanco
        boolean eat(int charToEat) {
            while (ch == ' ') nextChar();
            if (ch == charToEat) {
                nextChar();
                return true;
            }
            return false;
        }

        //Analiza si semanticamente los caracteres corresponden
        double parse() {
            nextChar();
            double x = parseExpression();
            if (pos < str.length()) //throw new RuntimeException("Error 108: Character no esperado " + (char)ch);
            {
                if ((char)ch == '(' && (str.charAt(pos+1) == ')')){
                    GUI.impresor("Error 112: Parentesis Vacío en Pocisión--> " + (pos+1));
                    x = -92e3;
                }
                if ((char)ch == ')'){
                    GUI.impresor("Error 110:  Cerradura de Parentesis cuando no hubo apertura: Pocision--> " + (pos+1) );
                    x = 0;
                }
                if ((char)ch == '('){
                    GUI.impresor("Error 111: Apertura de Parentesis sin cerradura: Pocision--> " + (pos+1));
                    x = 0;
                }
             
                else
                {
                    GUI.impresor("Error 113: Character no esperado -> " + (char)ch + " en Pocision--> " + (pos+1) );
                    //throw new RuntimeException("Error 108: Character no esperado " + (char)ch);
                }
            } 
               
            return x;
        }

        /*Gramatica:
        
        expresion = termino | expresion + termino | expresion - termino
        termino = factor | termino * factor | termino / factor
        factor = + factor | - factor | ( expresion )
        | numero | funcion factor | factor ^ factor
        */
        
        //Si es una expresion osea suma o resta.
        double parseExpression() {
            double x = parseTerm();
            for (;;) {
                if      (eat('+')) x += parseTerm(); // Suma
                else if (eat('-')) x -= parseTerm(); // Resta
                else return x;
            }
        }

        //Terminos corresponden a multiplicacion o division
        double parseTerm() {
            double x = parseFactor();
            for (;;) {
                if      (eat('*')) x *= parseFactor(); // multiplication
                else if (eat('/')) x /= parseFactor(); // division
                else return x;
            }
        }

        //Factores
       double parseFactor() {
            if (eat('+')) return parseFactor(); // Signo de Suma
            if (eat('-')) return -parseFactor(); // Signo de resta

            double x;
            int startPos = this.pos;
            if (eat('(')) { // Parentesis izquierdo
                x = parseExpression();
                eat(')');
            } 
            else if ((ch >= '0' && ch <= '9') || ch == '.') 
                { // numeros
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } 
                else if (ch >= '#') 
                    { //funciones
                    
                        String func = "#";
                        nextChar();
                        
                        x = parseFactor();
                        if (func.equals("#")) 
                            if (x<0){ //Si es un numero negativo imprime error,
                                GUI.impresor("Error 105: No se pueden raices cuadradas en numeros negativos");
                                
                            }else
                            x = Math.sqrt(x);
                        else
                            GUI.impresor("Error 106: Funcion no reconocida " + func);
                            
                            
                    } else {
                            GUI.impresor("Error 107: Character no esperado: " + (char)ch);
                            throw new RuntimeException("Error 107: Character no esperado: " + (char)ch);
                           }

            if (eat('^')) x = Math.pow(x, parseFactor()); // Elevacion

            return x;
        } // Fin de parsing del factor
        
        
    }.parse(); //Fin del objeto
} // Fin metodo eval
    
} //fin de la clase
