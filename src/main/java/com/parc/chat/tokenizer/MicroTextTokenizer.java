package com.parc.chat.tokenizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.parc.chat.tokenizer.LexicalFSA.State;

/**
 * The MicroTextTokenizer class provides methods to break up a string into individual tokens. It has special
 * features to deal with micro-text as it appears in micro-blogs like Twitter and SMS and chat messages.
 * 
 * Obtain a new instance for each string to be tokenized. Typical usage:
 * 
 * <pre>
 * {@code
 * MicroTextTokenizer tokenizer = new MicroTextTokenizer("It's easier to buy something than to make something.");
 * Stack<LabeledToken> tokenStack = tokenizer.tokenize();
 * LabeledToken firstToken = tokenStack.get(0);
 * ...
 * }
 * </pre>
 * 
 * @author Kyle Dent
 *
 */
public class MicroTextTokenizer {

    static Pattern p = Pattern.compile("^[a-z0]+$");
    private Stack<LabeledToken> tokenStack = null;
    private StringBuilder surfaceFormBuffer = new StringBuilder();
    private StringBuilder lexemeBuffer = new StringBuilder();
	private String originalText;
	private int tokenCount = 0;
	private int charPos = 0;
	private int currentTokenPos = 0;

    public static List<String> abbreviationList = Arrays.asList(
      "mr", "mrs", "dr", "ms", "st", "rd", "no");

    // Define a transition network for URLs and hostnames as a regular expression.
    private static String topLevelDomainList = "com|edu|org|net|gov|mil|co|us";
    private static String domainPart = "([A-Za-z0-9-]+\\.)+(" + topLevelDomainList + ")";
    private static String protocolList = "http|https|mailto|ftp";
    private static String urlRegex = "^(((" + protocolList + "):\\/\\/)?" + domainPart + "(\\S+|$)).*";
    private static Pattern urlPattern = Pattern.compile(urlRegex);

    // Define a transition network for email addresses as a regular expression.
    // Warning: this doesn't recognize email addresses with quoted strings or comments.
    private static String localpart = "[^\\.\\s][a-zA-Z0-9!#$%&'*+\\-/=?\\^_`{|}~]*";
    private static String emailAddressRegex = "^(" + localpart + "@" + domainPart + ").*";
    private static Pattern emailPattern = Pattern.compile(emailAddressRegex);

    // Define a transition network for file names as a regular expression.
    private static String fileExtension = "aiff?|au|avi|bat|bmp|class|csv|cvs|dbf|dif|docx?|eps|exe|fm3|gif|hqx|html?|java|jpeg"
           + "|jpg|mac|map|mdb|mid|midi|mov|mtb|mtw|pdf|png|ppt|pptx|psd|psp|qt|qxd|ra|rtf|sit|tar|tif|txt|wav|xls|xlsx|zip";
    private static String filenameRegex = "^(\\S+\\.(" + fileExtension + ")).*";
    private static Pattern filenamePattern = Pattern.compile(filenameRegex, Pattern.CASE_INSENSITIVE);

    public MicroTextTokenizer(String tweetString) {
		this.originalText = tweetString;
	}

	/**
	 * Invokes the engine to execute the tokenizing process. This method must be called before
	 * other methods that provide token information. It breaks the original text string into
	 * words and symbols, and assigns attributes to each one creating a LabeledToken object
	 * for each token.
	 * @return a Stack of LabeledTokens
	 */
	public Stack<LabeledToken> tokenize() {
		tokenStack = new Stack<LabeledToken>();
        LexicalFSA.State curState = null;
        LexicalFSA.State nextState = null;
        LexicalFSA.Alphabet symbol = null;

        if (originalText == null || originalText.isEmpty())
            return tokenStack;  // Return the empty stack.

        char c, lastChar = 0, nextChar = 0;
        curState = LexicalFSA.State.START;

        for (charPos = 0; charPos < originalText.length(); charPos++)
        {
        	c = originalText.charAt(charPos);
            nextChar = getLookahead(charPos);
            symbol = LexicalFSA.getSymbol(c, lastChar, nextChar);
            nextState = LexicalFSA.getNextState(curState, symbol);
            if ( nextState != curState) {
            	State newState = exitState(curState, nextState, c, lastChar, nextChar);
            	if (newState != curState) {		// On epsilon transition, skip enter arc.
            		nextState = newState;
            	} else {
            		nextState = enterState(nextState, curState, c, lastChar, nextChar);
            	}
            } else {
            	nextState = steadyState(curState, c, lastChar, nextChar);
            }
            curState = nextState;
            lastChar = c;
        }
        exitState(nextState, null, (char)0, lastChar, nextChar);

        return tokenStack;
	}

	public String getText() {
		return originalText;
	}

    public String toString() {
    	return originalText;
    }

    /**
     * Retrieves the list of tokens as strings. The list contains the tokens in their
     * surface form as they appeared in the original string. You must call tokenize() before
     * calling this method or an IllegalStateException will be thrown.
     * @return a list of tokenized Strings
     */
    public List<String> getTokensAsList() {
    	List<String> tokenList = new ArrayList<String>();

    	if (tokenStack == null) {
    		throw new IllegalStateException("You must call tokenize() before requesting token information.");
    	}

    	for (LabeledToken token : tokenStack) {
    		tokenList.add(token.getOriginalWord());
    	}

    	return tokenList;

    }

    /**
     * Retrieves the character position in the original string of the specified token.
     * @param tokenIndex the index into the token stack
     * @return an integer giving the character position of the token
     */
    public int getTokenPosition(int tokenIndex) {
    	if (tokenStack == null) {
    		throw new IllegalStateException("You must call tokenize() before requesting token information.");
    	}
    	return tokenStack.get(tokenIndex).getCharacterPosition();
    }

/*-------------------- End of public methods --------------------*/

    private LexicalFSA.State enterState(LexicalFSA.State state, LexicalFSA.State lastState, char c, char lastChar, char nextChar) {

    	State newState = checkTransitionNetworks(c);
    	if (newState != null) {
    		return newState;
    	}

		switch(state) {
		case ON_DOT:
			if (nextChar == '.') {
				newState = LexicalFSA.State.ON_ELLIPSIS;
			} else if (tokenStack.size() > 1) {
				String previousWord = tokenStack.peek().getStem();
				if (isAbbreviation(previousWord)) {
					LabeledToken tok = tokenStack.pop();
					tok.setOriginalWord(tok.getOriginalWord() + ".");
					tok.setStem(tok.getStem() + ".");
					tokenStack.push(tok);
					break;
				}
			 }
			 currentTokenPos = charPos;
			 appendSymbol(state, c, lastChar, nextChar);
			 break;
		case ON_PUNCT:
			currentTokenPos = charPos;
			appendSymbol(state, c, lastChar, nextChar);
			saveToken(TokenType.PUNCT);
			state = State.BETWEEN_TOKENS;
			break;
        case ON_HYPHEN:
            currentTokenPos = charPos;
            appendSymbol(state, c, lastChar, nextChar);
            saveToken(TokenType.HYPHEN);
            state = State.BETWEEN_TOKENS;
            break;
		case IN_AT_NAME:
		case IN_HASH_TAG:
		case ON_ELLIPSIS:
			// @names and #tag tokens already started, so don't increase the currentTokenPos
			appendSymbol(state, c, lastChar, nextChar);
			break;
		case BETWEEN_TOKENS:
			if (lastChar == '.') {
				resolveAbbreviationPeriods();
			}
			break;
		case ERROR:
			break;
		default:
			currentTokenPos = charPos;
			appendSymbol(state, c, lastChar, nextChar);
		}

		return state;
	}

    private LexicalFSA.State steadyState(LexicalFSA.State state, char c, char lastChar, char nextChar) {

    	State newState = checkTransitionNetworks(c);
    	if (newState != null) {
    		return newState;
    	}

    	if (state != State.BETWEEN_TOKENS) {
    		appendSymbol(state, c, lastChar, nextChar);
    	}

        return state;
    }

    private LexicalFSA.State exitState(LexicalFSA.State state, LexicalFSA.State nextState, char c, char lastChar, char nextChar) {

    	switch (state) {
    		case ON_ELLIPSIS:
    			saveToken(TokenType.PUNCT);
    			break;
   			case ON_AT:
   				if (nextState != LexicalFSA.State.IN_AT_NAME) {
   					saveToken(TokenType.PUNCT);
   				}
   				break;
   			case ON_HASH:
   				if (nextState != LexicalFSA.State.IN_HASH_TAG) {
   					saveToken(TokenType.PUNCT);
   				}
   				break;
   			case ON_DOT:
   				if (nextState != LexicalFSA.State.ON_ELLIPSIS && nextState != LexicalFSA.State.ON_DIGIT) {
   					saveToken(TokenType.PUNCT);
   				}
   				break;
            case ON_HYPHEN:
                saveToken(TokenType.HYPHEN);
   			case ON_DIGIT:
				saveToken(TokenType.NUMERIC);
   				break;
   			case IN_AT_NAME:
   				saveToken(TokenType.AT_NAME);
   				break;
   			case IN_HASH_TAG:
   				saveToken(TokenType.HASH_TAG);
   				break;
   			case URL:
   				saveToken(TokenType.URL);
   				break;
   			case START:
                break;
   			default:
   		    	saveToken(TokenType.ALPHA);
    	}

    	return state;
    }

    private State checkTransitionNetworks(char c) {
    	State state = null;

    	// If the current character can start an emoticon, branch into the EmoticonFST.
		if (EmoticonFST.isInitialEmoticonChar(c)) {
			int emoticonLen = EmoticonFST.recognizeEmoticon(originalText.substring(charPos));
			if (emoticonLen > 0) {
				String emoticonStr = originalText.substring(charPos, charPos + emoticonLen);
				lexemeBuffer.append(emoticonStr);
				surfaceFormBuffer.append(emoticonStr);
				currentTokenPos = charPos;
				saveToken(TokenType.EMOTICON);
				charPos += emoticonLen;
				return state = State.BETWEEN_TOKENS;
			}
		}

		// Check if the next token can be recognized as an email address.
		Matcher m = emailPattern.matcher(originalText.substring(charPos));
		if (m.matches()) {
			String emailAddress = m.group(1);
			lexemeBuffer.append(emailAddress.toLowerCase());
			surfaceFormBuffer.append(emailAddress);
			currentTokenPos = charPos;
			saveToken(TokenType.EMAIL_ADDR);
			charPos += emailAddress.length();
			return state = State.BETWEEN_TOKENS;
		}

		// Check if the next token can be recognized as a URL.
	    m = urlPattern.matcher(originalText.substring(charPos));
	    if (m.matches()) {
			String url = m.group(1);
			// Remove final period.
			if (url.endsWith(".")) {
				url = url.substring(0, url.length() - 1);
			}
			lexemeBuffer.append(url.toLowerCase());
			surfaceFormBuffer.append(url);
			currentTokenPos = charPos;
			saveToken(TokenType.URL);
			charPos += url.length() - 1;
			return state = State.BETWEEN_TOKENS;
	    }

	    m = filenamePattern.matcher(originalText.substring(charPos));
	    if (m.matches()) {
	        String filename = m.group(1);
	        lexemeBuffer.append(filename.toLowerCase());
	        surfaceFormBuffer.append(filename);
	        currentTokenPos = charPos;
	        saveToken(TokenType.FILENAME);
	        charPos += filename.length() - 1;
	        return state = State.BETWEEN_TOKENS;
	    }

	    return state;
    }

    private void resolveAbbreviationPeriods() {

		if (tokenStack.size() < 2) {
			return;
		}

		LabeledToken periodToken = tokenStack.pop();
		LabeledToken tok = tokenStack.pop();
		if (tok.getOriginalWord().length() == 1) {
			Stack<LabeledToken> abbreviationStack = new Stack<LabeledToken>();
			abbreviationStack.push(periodToken);
			abbreviationStack.push(tok);
			while (tokenStack.size() > 1) {
				periodToken = tokenStack.pop();
				tok = tokenStack.pop();
				if (tok.getOriginalWord().length() == 1) {
					abbreviationStack.push(periodToken);
					abbreviationStack.push(tok);
				} else {
					break;
				}
			}
			LabeledToken combinedToken = new LabeledToken();
			StringBuilder combinedWord = new StringBuilder();
			StringBuilder combinedStem = new StringBuilder();
			for (LabeledToken tokenPart : abbreviationStack) {
				combinedWord.insert(0, tokenPart.getOriginalWord());
				combinedStem.insert(0, tokenPart.getStem());
			}
			combinedToken.setCharacterPosition(abbreviationStack.get(0).getCharacterPosition());
			combinedToken.setIndex(abbreviationStack.get(0).getIndex());
			combinedToken.setOriginalWord(combinedWord.toString());
			combinedToken.setStem(combinedStem.toString());
			combinedToken.setTokenType(TokenType.ALPHA);
			tokenStack.push(combinedToken);
		} else {
			tokenStack.push(tok);
			tokenStack.push(periodToken);
		}
    }

    private static boolean isAbbreviation(String str) {
    	return abbreviationList.contains(str.toLowerCase());
    }

    private void appendSymbol(LexicalFSA.State state, char curChar, char lastChar, char nextChar)
    {
    	surfaceFormBuffer.append(curChar);
    	if (curChar == '\'' && (state == LexicalFSA.State.IN_WORD && nextChar != 's')) {
    		return;
    	}

    	// Don't append repeating characters to the lexeme. But always append digits.
    	// If we're in a token and the character is a '0' convert it to a 'o'.
    	if (state == LexicalFSA.State.IN_WORD && curChar == '0') {
    		lexemeBuffer.append('o');
    	} else {
    		lexemeBuffer.append(Character.toLowerCase(curChar));
    	}
    }

    private void clearBuffers() {
		surfaceFormBuffer = new StringBuilder();
        lexemeBuffer = new StringBuilder();
    }

    private char getLookahead(int pos) {
    	char lookaheadChar;
		if (originalText.length() <= pos+1 )
			lookaheadChar = 0;
		else
			lookaheadChar = originalText.charAt(pos+1);
		return lookaheadChar;
	}

    private void saveToken(TokenType type)
    {
    	String lexeme = lexemeBuffer.toString();
    	String surface = surfaceFormBuffer.toString();
    	if (lexeme == null || lexeme.length() == 0)
    		return;
    	LabeledToken currentToken = new LabeledToken();
    	if (type == TokenType.AT_NAME) {
    	    currentToken.setStem("ATNAME");
    	} else {
    	    currentToken.setStem(lexeme);
    	}
    	currentToken.setOriginalWord(surface);
    	currentToken.setCharacterPosition(currentTokenPos);

    	if (type == TokenType.ALPHA) {
    		if (currentToken.getStem().matches("^[\\d\\-\\.\\,\\$]+$")) {  // All number stuff.
    			currentToken.setTokenType(TokenType.NUMERIC);
    		} else if (! currentToken.getStem().matches(".*[A-Za-z].*")) {  // contains no letters
    			currentToken.setTokenType(TokenType.PUNCT);
    		} else {
    			currentToken.setTokenType(TokenType.ALPHA);
    		}
    	} else {
    		currentToken.setTokenType(type);
    	}

		tokenCount++;
		currentToken.setIndex(tokenCount);
    	tokenStack.push(currentToken);
    	clearBuffers();
   }

}
