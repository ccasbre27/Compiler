package Tokenizer;
/**
 *
 * @author Adolfo
 */

//Tipos de Token

public enum TokenType {

        /** Token Reservado */
        RESERVED,
    
	/** Token Vacio. */
	EMPTY,
	
	/** Cualquier Token, por ejemplo, ( ) = , */
	OPERADOR,
	
	/** La primera es una letra puede seguir de letras o numeros */
	IDENTIFIER,
	
	/** Numero Entero */
	INTEGER_LITERAL,
        
        DOUBLE_LITERAL,
	
	/** Cualquier cosa encerrado entre comillas "Hola"  */
	STRING_LITERAL,
        
        // Se utiliza para el signo de pregunta ?
        LOGIC,
        
        //los parentesis
        AGRUPACION,
        
        //Relacionales
        RELACIONAL,
        
        //Booleano
        BOOLEAN,
        
        /** Alphanumerico utilizado para variables */
        NONALPHA;
        
        
}