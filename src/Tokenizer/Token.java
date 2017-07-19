package Tokenizer;
/**
 *
 * @author Adolfo
 */


//Se define la clase token, para ver el token y su tipo

public class Token {

	private String token;
	private TokenType type;
        
    public Token(String token, TokenType type) {
            this.token = token;
            this.type = type;
    }

    public void setToken(String token) {
        this.token = token;
    }
    
    public String getToken() {
		return token;
    }

    public void setType(TokenType type) {
        this.type = type;
    }
	
    public TokenType getType() {
            return type;
    }
         
}