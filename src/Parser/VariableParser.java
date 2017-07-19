/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Parser;


import Evaluador.evaluar;
import Tokenizer.Cmd_Reservados;
import Tokenizer.Token;
import Tokenizer.TokenData;
import Tokenizer.TokenType;
import Tokenizer.Tokenizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.regex.Pattern;
import main.GUI;
import main.Variable;


/**
 *
 * @author Adolfo
 */
public class VariableParser {
    
    private String line;
    private Tokenizer tokenizer;
    private String tipo;
    
    //Los siguientes valores corresponde al nombre de la variable y su valor.
    private String name;
    private String value;
    private Variable varobj = new Variable();
    
    //Los siguientes van hacer regex predefinidos
    
   
    
              
    public String PRIMERA = "(DEF|DEFI) ([A-Z][0-9]*|[A-Z])+=[-+]?([A-Z]*[0-9]*|[A-Z]*|[0-9]*\\.?[0-9]*|true|false)";
    public String SEGUNDA = "([A-Z][0-9]*|[A-Z])+=[-+]?([A-Z]*[0-9]*|[A-Z]*|[0-9]*\\.?[0-9]*)";
    public String TERCERA = "([A-Z][0-9]*|[A-Z])+=[-+]?([A-Z]*[0-9]*|[A-Z]*|[0-9]*\\.?[0-9]*) (DEF|DEFI)"; //ejemplo  a=10 def
    public String CUARTA = "(LIST|LIS)"; //Comando para listar variables
    public String QUINTA = "(CALC|CAL) (.*)";
    public String SEXTA = "(BORR|BOR) (.*)";
    public String SEPTIMA = "(GRAB|GRA) ([A-Z]*)";

    //Octava Forma, Vali.
    //Se utiliza para encontrar un regex de comparacion relacional.
    //Se podria obviar pero es para evitar más de un operador relacional.
    //Por ejemplo evitar  a>a<=20?
    //Por ende si no lo encuentra en la octava forma, esta mal formulado.
    public String OCTAVA = "((.*)=(.*)\\?|"
                         + "(.*)>(.*)\\?|"
                         + "(.*)<(.*)\\?|"
                         + "(.*)>=(.*)\\?|"
                         + "(.*)<=(.*)\\?|"
                         + "(.*)<>(.*)\\?)";
    
    
    
    public VariableParser(String line, Tokenizer tokenizer){
        
        this.line = line;
        this.tokenizer = tokenizer;
        
    }

    //Booleano que da true si encuentra el regex
    public boolean verificar_regex() {
          
            return line.matches(PRIMERA+"|"+SEGUNDA+"|"+TERCERA+"|"+CUARTA+"|"+QUINTA+"|"+SEXTA+"|"+SEPTIMA+"|"+OCTAVA);
              
	}
    
    //Este metodo da true si la palabra es reservada
    private boolean isreserved(TokenType tokentype){
        
         //if (tokenizer.getType().equals("RESERVED"))
         if (tokentype == TokenType.RESERVED)
             return true;
         
         return false;
    }
    
    public void principal(){
        
        
        //* Existen 3 formas para encontrar una definicion de variables
        // Estas estan definidas en el regex
        
        //Primera Forma
        if (line.matches(PRIMERA)){
            //En este caso se quiere saltar el token def.
            tokenizer.nextToken(); //Saltado
            name=tokenizer.nextToken().getToken();  //Primera
            
            if(name.length()<6){ //Deben ser variables menores a 5 caracteres
                
                //Se debe evaluar que no sea reservado
                if (!isreserved(tokenizer.getType()))
                {
                    tokenizer.nextToken(); //Para saltarse el =
                    value=tokenizer.nextToken().getToken() ;
                    if(!isreserved(tokenizer.getType()))
                    {
                        varobj.setValue(value);
                        varobj.setType(tokenizer.getType().toString());
                        almacenar_variables(name,varobj);
                    }
                        
                    else
                        GUI.impresor("Error 102: No puede utilizar: " + value + " Es reservada!"); 
                }
                else 
                 GUI.impresor("Error 102: No puede utilizar: " + name + " Es reservada!");   
            }else
                GUI.impresor(("Error 103: Las variables debe contener como máximo 5 caracteres"));
        }
        //Fin de la primera forma
        
        //De la segunda forma posible
        if (line.matches(SEGUNDA)){
            name=tokenizer.nextToken().getToken();
            String var1_tipo=tokenizer.getType().toString();
            System.out.println(var1_tipo);
           if(name.length()<6){ //Deben ser variables menores a 5 caracteres
               
               
            //Evaluacion de reservados
            if (!isreserved(tokenizer.getType()))
            {
               
               tokenizer.nextToken(); //Para saltarse el =
               value=tokenizer.nextToken().getToken(); 
               String var2_tipo=tokenizer.getType().toString();
               System.out.println(var2_tipo);
               if(!isreserved(tokenizer.getType()))
               {
                   varobj.setValue(value);
                   varobj.setType(tokenizer.getType().toString());
                   almacenar_variables(name,varobj);
               }
                     
               else
                   GUI.impresor("Error 102: No puede utilizar: " + value + " Es reservada!"); 
            }
            else
                GUI.impresor("Error 102: No puede utilizar: " + name + " Es reservada!"); 
        }else
              GUI.impresor(("Error 103: Las variables debe contener como máximo 5 caracteres")); 
            
        }
        
        //De la tercera forma posible
        if (line.matches(TERCERA)){
            name=tokenizer.nextToken().getToken();
            if(name.length()<6){ //Deben ser variables menores a 5 caracteres
                //Evaluacion de reservados
                if (!isreserved(tokenizer.getType()))
                {
                   tokenizer.nextToken(); //Para saltarse el =
                   value=tokenizer.nextToken().getToken() ; 
                   if(!isreserved(tokenizer.getType()))
                   {
                     varobj.setValue(value);
                     varobj.setType(tokenizer.getType().toString());
                     almacenar_variables(name,varobj);
                   }
                        
                   else
                       GUI.impresor("Error 102: No puede utilizar: " + value + " Es reservada!"); 
                }
                else
                    GUI.impresor("Error 102: No puede utilizar: " + name + " Es reservada!");
            }else
                GUI.impresor(("Error 103: Las variables debe contener como máximo 5 caracteres")); 
        }// Fin de la Tercera forma
        
        //Cuarta forma
        
        if (line.matches(CUARTA)){
            listar_variables();
        }
        
        if (line.matches(QUINTA)){
          
          String nuevo_string = remplazar_literales();
          if(nuevo_string != null)
              //Una vez que las variables han sido remplazadas, hay que verificar que no sean booleanas

              if(!nuevo_string.contains("true") && !nuevo_string.contains("false")){
              Double resultado = Evaluador.evaluar.eval(nuevo_string);
              GUI.ultimoresultado = resultado.toString(); //Guardamos temporalmente en memoria el ultimo resultado para usar GRAB.
              GUI.impresor(resultado.toString());
              }else
                  GUI.impresor("Error 142: No se pueden utilizar booleanos en una operacion de calculo");
            
        }
        
        //Metodo para borrar variables de memoria
        if (line.matches(SEXTA)){
            String variable_borrar = line.replaceAll("(BORR|BOR)", "");
            variable_borrar = variable_borrar.replaceAll(" ", "");
            borrar_variable(variable_borrar); //Invoca al metodo con el key que se quiere borrar
        } // Fin de regex para borrar variables
        
        
        //Este metodo es para grabar el ultimo calculo en una variable.
        //Utiliza la misma Clase utilizando la primera forma.
        
        if(line.matches(SEPTIMA)){ 
                        
            String string_limpio = line.replaceAll("(GRAB|GRA)", "");
            string_limpio = string_limpio.replaceAll(" ", "");
            if (GUI.ultimoresultado==null){
                GUI.impresor("Error 122:  Calc no ha sido invocado, no existe último calculo en memoria");
            }
            else
            {
                
                //Aqui vamos hacer un truco para volver a llamar a la clase para evitar hacer mas codigo.
                //Simplemente le damos forma a la variable new_source, y se ejecuta el metodo principal.
                
                String new_source = "DEF " + string_limpio + "=" + GUI.ultimoresultado;
                Tokenizer tokenizer = new Tokenizer(new_source); //Lo importante del tokenizer para saber el tipo
                VariableParser vparser = new VariableParser(new_source, tokenizer);
                vparser.principal();
                
            }
            
        }
        //El siguiente método es para el Comando VALI
        if(line.matches(OCTAVA)){
            line = line.replaceAll("\\?", "");
            System.out.println("Entre en el toque de vali");
            Tokenizer tokenizer = new Tokenizer(line);
            
            //La idea de las siguientes 3 variables es despedazar la igualdad en dos terminos y un operador
            String operador = "";
            String primer_termino = "";
            String segundo_termino = "";
            Double valor_izquierdo = 0.00;
            Double valor_derecho = 0.00;
            
            
            
            //El siguiente while es para sacar todos los tokens
            //Se separan los dos terminos y al centro el operador
            //La mejor forma de discernir es encontrando el operador relacional
            while (tokenizer.hasNextToken()) {
            Token t = tokenizer.nextToken();
            if (t.getType().toString() == "RELACIONAL"){
                operador = operador + t.getToken();
            }else
                if (operador == "")
                primer_termino = primer_termino + t.getToken();
                else
                segundo_termino = segundo_termino + t.getToken();
            
            }
            
            //Una vez separados los terminos y su operador
            //Se procede a sacar el calculo de cada termino.
            //La funcion CALC trae todo lo que se ocupa.
            
            System.out.println("Primer Termino: " + primer_termino);
            System.out.println("Segundo Termino: " + segundo_termino);
            
            
            //Se extrae el primer termino
            primer_termino = "CALC  " + primer_termino;
            tokenizer = new Tokenizer(primer_termino); 
            VariableParser vparser = new VariableParser(primer_termino, tokenizer);
            primer_termino = vparser.remplazar_literales();
            
            if (primer_termino == null) {
                GUI.impresor("Error 131:  Problemas con el primer termino");
            }else
                valor_izquierdo = Evaluador.evaluar.eval(primer_termino);
                
            
            //Se extrae el segundo termino
            segundo_termino = "CALC  " + segundo_termino;
            tokenizer = new Tokenizer(segundo_termino);
            vparser = new VariableParser(segundo_termino, tokenizer);
            segundo_termino = vparser.remplazar_literales();
            
            if (segundo_termino == null) {
                GUI.impresor("Error 132:  Problemas con el segundo termino");
            }else
                valor_derecho = Evaluador.evaluar.eval(segundo_termino);
            
            //Con ambos terminos listos ahora es solo de comparar para dar true o false.
            
            System.out.println("Este es el primer termino " + primer_termino);
            System.out.println("Este es el segundo termino " + segundo_termino);
            System.out.println("Este es el operador " + operador);
            System.out.println("Valor a la izquierda: " + valor_izquierdo);
            System.out.println("Valor a la derecha: " + valor_derecho);
                    
            GUI.impresor(comparador_relacional(valor_izquierdo, valor_derecho,operador).toString());
            GUI.ultimoresultado = comparador_relacional(valor_izquierdo, valor_derecho,operador).toString();
            
        }
      
        
    } //Fin de metodo para encontrar variables a partir de regex
    
    
    private Boolean comparador_relacional(Double valor_izquierdo, Double valor_derecha,String operador){
    
        
        if(operador.equals("="))
            if(valor_izquierdo.equals(valor_derecha))
            return true;
        if(operador.equals(">"))
            if(valor_izquierdo > valor_derecha)
            return true;
        if(operador.equals("<"))
            if(valor_izquierdo < valor_derecha)
            return true;
        if(operador.equals(">="))
            if(valor_izquierdo >= valor_derecha)
            return true;
        if(operador.equals("<="))
            if(valor_izquierdo <= valor_derecha)
            return true;
        if(operador.equals("<>"))
            if(!valor_izquierdo.equals(valor_derecha))
            return true;
        
        
        return false;
        
    }
    
    
    
    
    //El siguiente ciclo va a iterar sobre todos los tokens
    //Cuando encuentre un token tipo Identifier entonces buscara
    //En la tabla de variables a ver si encuentra el valor para tomarlo de ahi.
    public String remplazar_literales(){
        
        String remplazo ="";
         while (tokenizer.hasNextToken()){
                tokenizer.nextToken();
                //Si es identificador, es una posible variable
                if(tokenizer.getType() == TokenType.IDENTIFIER)
                {
                    if(extraer_Variable(tokenizer.getToken()) == null){
                        GUI.impresor("Error 105: La Variable --> " + tokenizer.getToken() + " <-- No ha sido definida, no se puede calcular " );
                        return null;
                    }else{
                        Variable var = extraer_Variable(tokenizer.getToken());
                        remplazo = remplazo + var.getValue();
                    }
                  
                }else
                {
                    remplazo = remplazo + tokenizer.getToken();
                }
                   
        }//Fin del while
        remplazo = remplazo.replaceAll("(CALC|CAL)", "");
        return remplazo;
    }
    
    //Metodo para encontrar una variable ya definida y extraer su valor 
    // este ejemplo sirve para remplazar A=B,, donde B debe estar definida
    
    public Variable extraer_Variable(String name){
        
        Set set = GUI.variablesmap.entrySet();
        Iterator i = set.iterator();
        
        while(i.hasNext()){
            Map.Entry me = (Map.Entry)i.next();
            varobj = (Variable)me.getValue(); // Casting del objeto a variable
            
            if (name.equals(me.getKey())){
                return varobj;
            }
        }
        
        return null;
    }//fin de metodo que encuentra la variable
    
   //El siguiente metodo itera sobre la memoria
   //Encuentra la variable y extrae su tipo
   
    public String tipo_predefinido(String name){
        
        
        
      return null;   
    }
    
    
    //Almacenar Variables
    
    private void almacenar_variables(String name, Variable varobject){
      
        
        if(extraer_Variable(varobject.getValue()) == null)
        {
            //Si no existe variable, pero el tipo es un identificador
            //Significa que esta tratando de apuntar a una variable
            //Que no ha sido definida anteriormente, por lo tanto es error
            if("IDENTIFIER".equals(varobject.getType())){
                GUI.impresor("Error 104: No se puede apuntar a una variable no definida");
                
            }
            else
            {
                //Al no encontrar variable con ese nombre almacena.
                
                if (GUI.variablesmap.containsKey(name))
                {   
                    //continuar
                    
                    Variable current_mem_object = (Variable)GUI.variablesmap.get(name);
                    Variable new_mem_object = varobject;
                    String old_type = current_mem_object.getType();
                    String new_type = new_mem_object.getType();
                    
                    //Este tipo de evaluacion lo que va hacer es fijarse si ya existia una variable con ese nombre
                    //Al hacerlo entonces verifica que el tipo almacenado sea igual al nuevo por almacenar
                    //De lo contrario estaria escribiendo con un nuevo tipo y por ende seria error
                    
                    if (old_type==new_type){
                      GUI.variablesmap.put(name, varobject);
                      GUI.impresor("Variable Actualizada");  
                    }else
                    {
                        GUI.impresor("Error 119: No se puede actualizar variable, Tipos incompatibles:");
                        GUI.impresor("Tipo Original: " + old_type + " Nuevo Tipo: " + new_type);
                    }
                    
                    
                }else
                {
                    GUI.variablesmap.put(name, varobject);
                    GUI.impresor("Variable Creada");
                }
                
                
            }
            
        }
        
        else
            {
                //Si encuentra un nombre entonces copia su valor para almacenar
                //Antes de apuntar al nuevo valor hay que analizar si son de los mismos tipos.
                
                
                Variable from_mem_object = (Variable)GUI.variablesmap.get(name);
                Variable to_mem_object = (Variable)GUI.variablesmap.get(varobject.getValue());
                String from_type = from_mem_object.getType();
                String to_type = to_mem_object.getType();
                
                System.out.println("Me indican que " + name + "Fue definida de tipo " + to_type);
                System.out.println("Esta debe copiar de " + varobject.getValue()  + " El cual ya tiene un valor de " + from_type );
                
                  if (from_type != to_type){ //Si los dos tipos no son iguales, no es compatible referenciar.
                    GUI.impresor("Error 121:  No se puede referenciar variable, Tipos incompatibles:");
                    GUI.impresor("Variable: " + name + " Tipo: " + to_type + " Variable: " + varobject.getValue() + " Tipo: " + from_type  );
                    
                }else
                  {
                      //Si todo esta bien, entonces se puede copiar.
                    varobject = extraer_Variable(varobject.getValue());
                    System.out.println("Almaceno con tipo " + varobject.getType());
                    GUI.variablesmap.put(name, varobject);
                    GUI.impresor("Variable Copiada");  
                  }
                
            }
      
    }//Fin de metodo para almacenar variables
    
    //Imprimir Colección de Variables
    //Ya vienen ordenadas ya que variables.map es un Treemap
    
    public void listar_variables(){
        GUI.impresor("Listando Variables");
        Set set = GUI.variablesmap.entrySet();
        
        Iterator i = set.iterator();
        
        
        while(i.hasNext()){
            Map.Entry me = (Map.Entry)i.next();
            varobj = (Variable)me.getValue();
            GUI.impresor("Variable: " + me.getKey() + " Valor: " + varobj.getValue() + " Tipo: " + varobj.getType());
        }
        
    }
    
    //Metodo para borrar variables
    public void borrar_variable(String variable_borrar){
        
        if(GUI.variablesmap.containsKey(variable_borrar)){
            GUI.variablesmap.remove(variable_borrar);
            GUI.impresor("Variable " + variable_borrar + " Borrada!");
        }else
            GUI.impresor("Error 117:  La variable " + variable_borrar + " No existe en memoria");
    }

    
    
    
}
