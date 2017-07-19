package Tokenizer;
/**
 *
 * @author Adolfo
 */

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import main.GUI;


public class Tokenizer {

	private final ArrayList<TokenData> tokenDatas;
	
	private String str;
	
	private Token lastToken;
	private boolean pushBack;
        
        
        
	
	public Tokenizer(String str) {
		this.tokenDatas = new ArrayList<TokenData>();
		this.str = str;

                
                //Primero se va a revisar que no hayan comandos reservados
                
                Cmd_Reservados reservados = new Cmd_Reservados();
                String[] cmd_reservado = reservados.getlist();
                
                //Este for es para sacar la lista de comandos reservados
                for(String res : cmd_reservado){
                    tokenDatas.add(new TokenData(Pattern.compile("^(" + res + ")"), TokenType.RESERVED));
                }
                
                tokenDatas.add(new TokenData(Pattern.compile("^([A-Z][A-Z0-9]*)"), TokenType.IDENTIFIER));
                tokenDatas.add(new TokenData(Pattern.compile("^[-+]?[0-9]*\\.[0-9]*"), TokenType.DOUBLE_LITERAL));
                tokenDatas.add(new TokenData(Pattern.compile("^((-)?[0-9]+)"), TokenType.INTEGER_LITERAL));
                tokenDatas.add(new TokenData(Pattern.compile("^(\".*\")"), TokenType.STRING_LITERAL));
		
                for (String t : new String[] {"\\(", "\\)"}) {
			tokenDatas.add(new TokenData(Pattern.compile("^(" + t + ")"), TokenType.AGRUPACION));
		}
                
		for (String t : new String[] { "\\*", "\\-", "\\/", "\\#", "\\^", "\\+"}) {
			tokenDatas.add(new TokenData(Pattern.compile("^(" + t + ")"), TokenType.OPERADOR));
		}
                
                for (String t : new String[] {"=", "\\>=", "\\<=", "\\>", "\\<", ">", "<"}) {
			tokenDatas.add(new TokenData(Pattern.compile("^(" + t + ")"), TokenType.RELACIONAL));
		}
               
                tokenDatas.add(new TokenData(Pattern.compile("\\?"), TokenType.LOGIC));
                tokenDatas.add(new TokenData(Pattern.compile("true"), TokenType.BOOLEAN));
                tokenDatas.add(new TokenData(Pattern.compile("false"), TokenType.BOOLEAN));
                
                 
	}
	
        
        //Metodo para ver el tipo de token.
        public TokenType getType(){
            //return lastToken.getType().toString();
            return lastToken.getType();
        }
        
        public void setType(TokenType token){
            lastToken.setType(token);
        }
        
        public String getToken(){
            return lastToken.getToken();
        }
        
        
	public Token nextToken() {
		str = str.trim();
		
		if (pushBack) {
			pushBack = false;
			return lastToken;
		}
		
		if (str.isEmpty()) {
			return (lastToken = new Token("", TokenType.EMPTY));
		}
		
		for (TokenData data : tokenDatas) {
			Matcher matcher = data.getPattern().matcher(str);
			
			if (matcher.find()) {
				String token = matcher.group().trim();
				str = matcher.replaceFirst("");
				
				if (data.getType() == TokenType.STRING_LITERAL) {
					return (lastToken = new Token(token.substring(1, token.length() - 1), TokenType.STRING_LITERAL));
				}
				
				else {
					return (lastToken = new Token(token, data.getType()));
				}
			}
		}
		
                GUI.impresor("Error 100: No puedo parsear " + str);
		throw new IllegalStateException("Error 105: No puedo parsear " + str);
                
	}
	
	public boolean hasNextToken() {
		return !str.isEmpty();
	}
	
	public void pushBack() {
		if (lastToken != null) {
			this.pushBack = true;
		}
	}
}