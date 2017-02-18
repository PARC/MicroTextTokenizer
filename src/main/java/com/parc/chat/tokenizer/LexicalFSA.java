package com.parc.chat.tokenizer;

public class LexicalFSA {

    public enum State {
        START,
        ON_HASH,
        ON_AT,
        ON_AMPERSAND,
        ON_DOT,
        ON_PUNCT,
        IN_WORD,
        IN_AT_NAME,
        IN_HASH_TAG,
        ON_DIGIT,
        BETWEEN_TOKENS,
        ON_EPOINT_OR_QMARK,
        ON_ELLIPSIS,
        URL,
        ON_UNKNOWN,
        ERROR
    };

    public enum Alphabet {
        ALPHA,
        DIGIT,
        QMARK,
        EPOINT,
        DECIMAL,
        PERIOD,
        COLON,
        SEMICOLON,
        AMPERSAND,
        PUNCT,
		AT_SIGN,
        HASH,
        OPEN_PAREN,
        CLOSE_PAREN,
        OPEN_ANGLE,
        CLOSE_ANGLE,
        SPACE,
        UNKNOWN
    };

    private static State[][] stateTable = {
    	// START
    	{State.IN_WORD, 				// ALPHA
    	 State.ON_DIGIT, 				// DIGIT
    	 State.ON_EPOINT_OR_QMARK, 		// QMARK
    	 State.ON_EPOINT_OR_QMARK, 		// EPOINT
    	 State.ON_DOT, 					// DECIMAL
    	 State.ON_DOT, 			// PERIOD
    	 State.ON_PUNCT, 		// COLON
    	 State.ON_PUNCT, 		// SEMI-COLON
    	 State.ON_AMPERSAND,			// AMPERSAND
    	 State.ON_PUNCT,			// PUNCT 
    	 State.ON_AT,					// AT_SIGN
    	 State.ON_HASH,					// HASH
    	 State.ON_PUNCT,		// OPEN_PAREN
    	 State.ON_PUNCT,		// CLOSE_PAREN
    	 State.ON_PUNCT,		// OPEN_ANGLE
    	 State.ON_PUNCT,		// CLOSE_ANGLE
    	 State.BETWEEN_TOKENS,			// SPACE
    	 State.ON_UNKNOWN},					// UNKNOWN
    	// ON_HASH
    	{State.IN_HASH_TAG,				// ALPHA
    	 State.IN_HASH_TAG,				// DIGIT
    	 State.ON_EPOINT_OR_QMARK,		// QMARK
    	 State.ON_EPOINT_OR_QMARK,		// EPOINT
    	 State.ON_DIGIT,					// DECIMAL
    	 State.ON_PUNCT, 			// PERIOD
    	 State.ON_PUNCT,			// COLON
    	 State.ON_PUNCT,			// SEMI-COLON
    	 State.ON_AMPERSAND,			// AMPERSAND
    	 State.ON_PUNCT,			// PUNCT 
    	 State.ON_AT,					// AT_SIGN
    	 State.ON_HASH,					// HASH
    	 State.ON_PUNCT, 			// OPEN_PAREN
    	 State.ON_PUNCT,		// CLOSE_PAREN
    	 State.ON_PUNCT,		// OPEN_ANGLE
    	 State.ON_PUNCT,		// CLOSE_ANGLE
    	 State.BETWEEN_TOKENS,			// SPACE
    	 State.ON_UNKNOWN,					// UNKNOWN
     	},
     	// ON_AT
     	{State.IN_AT_NAME,				// ALPHA
       	 State.IN_AT_NAME,				// DIGIT
    	 State.ON_EPOINT_OR_QMARK,		// QMARK
    	 State.ON_EPOINT_OR_QMARK,		// EPOINT
    	 State.ON_DIGIT,					// DECIMAL
    	 State.ON_PUNCT, 			// PERIOD
    	 State.ON_PUNCT,					// COLON
    	 State.ON_PUNCT,					// SEMI-COLON
    	 State.ON_AMPERSAND,			// AMPERSAND
    	 State.ON_PUNCT,			// PUNCT 
    	 State.ON_AT,					// AT_SIGN
    	 State.ON_HASH,					// HASH
    	 State.ON_PUNCT, 			// OPEN_PAREN
    	 State.ON_PUNCT, 				// CLOSE_PAREN
    	 State.ON_PUNCT, 				// OPEN_ANGLE
    	 State.ON_PUNCT,				// CLOSE_ANGLE
    	 State.BETWEEN_TOKENS,			// SPACE
    	 State.ON_UNKNOWN,					// UNKNOWN
     	},
    	// ON_AMPERSAND
    	{State.IN_WORD,				// ALPHA
    	 State.ON_DIGIT,				// DIGIT
    	 State.ON_EPOINT_OR_QMARK,		// QMARK
    	 State.ON_EPOINT_OR_QMARK,		// EPOINT
    	 State.ON_DIGIT,					// DECIMAL
    	 State.ON_PUNCT, 			// PERIOD
    	 State.ON_PUNCT,					// COLON
    	 State.ON_PUNCT,					// SEMI-COLON
    	 State.ON_AMPERSAND,			// AMPERSAND
    	 State.ON_PUNCT,			// PUNCT 
    	 State.ON_AT,					// AT_SIGN
    	 State.ON_HASH,					// HASH
    	 State.ON_PUNCT, 				// OPEN_PAREN
    	 State.ON_PUNCT, 				// CLOSE_PAREN
    	 State.ON_PUNCT, 				// OPEN_ANGLE
    	 State.ON_PUNCT,				// CLOSE_ANGLE
    	 State.BETWEEN_TOKENS,			// SPACE
    	 State.ON_UNKNOWN,					// UNKNOWN
     	},
    	// ON_DOT
    	{State.IN_WORD,				// ALPHA
    	 State.ON_DIGIT,				// DIGIT
    	 State.ON_EPOINT_OR_QMARK,		// QMARK
    	 State.ON_EPOINT_OR_QMARK,		// EPOINT
    	 State.ON_ELLIPSIS,					// DECIMAL
    	 State.ON_ELLIPSIS, 			// PERIOD
    	 State.ON_PUNCT,					// COLON
    	 State.ON_PUNCT,					// SEMI-COLON
    	 State.ON_AMPERSAND,			// AMPERSAND
    	 State.ON_PUNCT,			// PUNCT 
    	 State.ON_AT,					// AT_SIGN
    	 State.ON_HASH,					// HASH
    	 State.ON_PUNCT, 				// OPEN_PAREN
    	 State.ON_PUNCT, 				// CLOSE_PAREN
    	 State.ON_PUNCT, 				// OPEN_ANGLE
    	 State.ON_PUNCT,				// CLOSE_ANGLE
    	 State.BETWEEN_TOKENS,			// SPACE
    	 State.ON_UNKNOWN,					// UNKNOWN
     	},
    	// ON_PUNCT
    	{State.IN_WORD, 				// ALPHA
    	 State.ON_DIGIT, 				// DIGIT
    	 State.ON_EPOINT_OR_QMARK, 		// QMARK
    	 State.ON_EPOINT_OR_QMARK, 		// EPOINT
    	 State.ON_DIGIT, 					// DECIMAL
    	 State.ON_PUNCT, 			// PERIOD
    	 State.ON_PUNCT, 		// COLON
    	 State.ON_PUNCT, 		// SEMI-COLON
    	 State.ON_AMPERSAND,			// AMPERSAND
    	 State.ON_PUNCT,			// PUNCT 
    	 State.ON_AT,					// AT_SIGN
    	 State.ON_HASH,					// HASH
    	 State.ON_PUNCT,		// OPEN_PAREN
    	 State.ON_PUNCT,		// CLOSE_PAREN
    	 State.ON_PUNCT,		// OPEN_ANGLE
    	 State.ON_PUNCT,		// CLOSE_ANGLE
    	 State.ON_PUNCT,			// SPACE
    	 State.ON_UNKNOWN},					// UNKNOWN
    	// IN_WORD
     	{State.IN_WORD,				// ALPHA
       	 State.IN_WORD,				// DIGIT
       	 State.ON_EPOINT_OR_QMARK,		// QMARK
       	 State.ON_EPOINT_OR_QMARK,		// EPOINT
       	 State.ON_DIGIT,					// DECIMAL
       	 State.ON_DOT, 			// PERIOD
       	 State.ON_PUNCT,			// COLON
       	 State.ON_PUNCT,			// SEMI-COLON
    	 State.IN_WORD,				// AMPERSAND
    	 State.ON_PUNCT,			// PUNCT 
       	 State.ON_AT,					// AT_SIGN
       	 State.ON_HASH,					// HASH
       	 State.ON_PUNCT, 		// OPEN_PAREN
       	 State.ON_PUNCT, 		// CLOSE_PAREN
       	 State.ON_PUNCT, 		// OPEN_ANGLE
       	 State.ON_PUNCT,		// CLOSE_ANGLE
       	 State.BETWEEN_TOKENS,			// SPACE
       	 State.ON_UNKNOWN,					// UNKNOWN
        },    
     	// IN_AT_NAME
     	{State.IN_AT_NAME,				// ALPHA
       	 State.IN_AT_NAME,				// DIGIT
    	 State.ON_EPOINT_OR_QMARK,		// QMARK
    	 State.ON_EPOINT_OR_QMARK,		// EPOINT
    	 State.ON_DIGIT,					// DECIMAL
    	 State.ON_DOT, 			// PERIOD
    	 State.ON_PUNCT,					// COLON
    	 State.ON_PUNCT,					// SEMI-COLON
    	 State.ON_AMPERSAND,			// AMPERSAND
    	 State.ON_PUNCT,			// PUNCT 
    	 State.ON_AT,					// AT_SIGN
    	 State.ON_HASH,					// HASH
    	 State.ON_PUNCT, 			// OPEN_PAREN
    	 State.ON_PUNCT,		// CLOSE_PAREN
    	 State.ON_PUNCT,		// OPEN_ANGLE
    	 State.ON_PUNCT,		// CLOSE_ANGLE
    	 State.BETWEEN_TOKENS,			// SPACE
    	 State.ON_UNKNOWN,					// UNKNOWN
     	},
    	// ON_HASH_TAG
    	{State.IN_HASH_TAG,				// ALPHA
    	 State.IN_HASH_TAG,				// DIGIT
    	 State.ON_EPOINT_OR_QMARK,		// QMARK
    	 State.ON_EPOINT_OR_QMARK,		// EPOINT
    	 State.ON_DIGIT,					// DECIMAL
    	 State.ON_DOT, 			// PERIOD
    	 State.ON_PUNCT,			// COLON
    	 State.ON_PUNCT,			// SEMI-COLON
    	 State.ON_AMPERSAND,			// AMPERSAND
    	 State.ON_AT,					// AT_SIGN
    	 State.ON_PUNCT,			// PUNCT 
    	 State.ON_HASH,					// HASH
    	 State.ON_PUNCT, 		// OPEN_PAREN
    	 State.ON_PUNCT, 		// CLOSE_PAREN
    	 State.ON_PUNCT, 		// OPEN_ANGLE
    	 State.ON_PUNCT,		// CLOSE_ANGLE
    	 State.BETWEEN_TOKENS,			// SPACE
    	 State.ON_UNKNOWN,					// UNKNOWN
     	},
    	// ON_DIGIT
    	{State.ON_DIGIT, 				// ALPHA
    	 State.ON_DIGIT, 				// DIGIT
    	 State.ON_EPOINT_OR_QMARK, 		// QMARK
    	 State.ON_EPOINT_OR_QMARK, 		// EPOINT
    	 State.ON_DIGIT, 				// DECIMAL
    	 State.ON_PUNCT, 				// PERIOD
    	 State.ON_DIGIT, 				// COLON
    	 State.BETWEEN_TOKENS, 		// SEMI-COLON
    	 State.ON_AMPERSAND,			// AMPERSAND
    	 State.ON_PUNCT,			// PUNCT 
    	 State.ON_AT,					// AT_SIGN
    	 State.ON_HASH,					// HASH
    	 State.ON_PUNCT,		// OPEN_PAREN
    	 State.ON_PUNCT,		// CLOSE_PAREN
    	 State.ON_PUNCT,		// OPEN_ANGLE
    	 State.ON_PUNCT,		// CLOSE_ANGLE
    	 State.BETWEEN_TOKENS,			// SPACE
    	 State.ON_UNKNOWN},					// UNKNOWN
     	// BETWEEN_TOKENS
    	{State.IN_WORD, 				// ALPHA
       	 State.ON_DIGIT, 				// DIGIT
       	 State.ON_EPOINT_OR_QMARK, 		// QMARK
       	 State.ON_EPOINT_OR_QMARK, 		// EPOINT
       	 State.ON_DIGIT, 					// DECIMAL
       	 State.ON_DOT, 			// PERIOD
       	 State.ON_PUNCT, 				// COLON
       	 State.ON_PUNCT, 				// SEMI-COLON
    	 State.ON_AMPERSAND,			// AMPERSAND
    	 State.ON_PUNCT,			// PUNCT 
       	 State.ON_AT,					// AT_SIGN
       	 State.ON_HASH,					// HASH
       	 State.ON_PUNCT,			// OPEN_PAREN
       	 State.ON_PUNCT,				// CLOSE_PAREN
       	 State.ON_PUNCT,				// OPEN_ANGLE
       	 State.ON_PUNCT,				// CLOSE_ANGLE
       	 State.BETWEEN_TOKENS,			// SPACE
       	 State.ON_UNKNOWN},					// UNKNOWN
       	// ON_EPOINT_OR_QMARK
      	{State.IN_WORD, 				// ALPHA
       	 State.ON_DIGIT, 				// DIGIT
       	 State.ON_EPOINT_OR_QMARK, 		// QMARK
       	 State.ON_EPOINT_OR_QMARK, 		// EPOINT
       	 State.ON_DIGIT, 					// DECIMAL
       	 State.ON_PUNCT, 			// PERIOD
       	 State.ON_PUNCT, 				// COLON
       	 State.ON_PUNCT, 				// SEMI-COLON
    	 State.ON_AMPERSAND,			// AMPERSAND
    	 State.ON_PUNCT,			// PUNCT 
       	 State.ON_AT,					// AT_SIGN
       	 State.ON_HASH,					// HASH
       	 State.ON_PUNCT,			// OPEN_PAREN
       	 State.ON_PUNCT,			// CLOSE_PAREN
       	 State.ON_PUNCT,			// OPEN_ANGLE
       	 State.ON_PUNCT,			// CLOSE_ANGLE
       	 State.BETWEEN_TOKENS,			// SPACE
       	 State.ON_UNKNOWN},					// UNKNOWN
        // ON_ELLIPSIS
       	{State.IN_WORD, 				// ALPHA
         State.ON_DIGIT, 				// DIGIT
         State.ON_EPOINT_OR_QMARK, 		// QMARK
         State.ON_EPOINT_OR_QMARK, 		// EPOINT
         State.ON_ELLIPSIS, 			// DECIMAL
         State.ON_ELLIPSIS, 			// PERIOD
         State.ON_PUNCT, 				// COLON
         State.ON_PUNCT, 				// SEMI-COLON
     	 State.ON_AMPERSAND,			// AMPERSAND
    	 State.ON_PUNCT,			// PUNCT 
         State.ON_AT,					// AT_SIGN
         State.ON_HASH,					// HASH
         State.ON_PUNCT,			// OPEN_PAREN
         State.ON_PUNCT,			// CLOSE_PAREN
         State.ON_PUNCT,			// OPEN_ANGLE
         State.ON_PUNCT,			// CLOSE_ANGLE
         State.BETWEEN_TOKENS,			// SPACE
         State.ON_UNKNOWN},					// UNKNOWN
         // URL
        {State.URL,						// ALPHA
         State.URL,						// DIGIT
         State.URL,						// QMARK
         State.URL,						// EPOINT
         State.URL,						// DECIMAL
         State.URL, 					// PERIOD
         State.URL,						// COLON
         State.URL,			// SEMI-COLON
         State.URL,			// AMPERSAND
     	 State.URL,			// PUNCT 
         State.URL,						// AT_SIGN
         State.URL,						// HASH
         State.URL, 		// OPEN_PAREN
         State.URL, 		// CLOSE_PAREN
         State.ON_PUNCT, 		// OPEN_ANGLE
         State.ON_PUNCT,		// CLOSE_ANGLE
         State.BETWEEN_TOKENS,			// SPACE
         State.ON_UNKNOWN,				// UNKNOWN
         },    
       	// ON_UNKNOWN
      	{State.IN_WORD, 				// ALPHA
       	 State.ON_DIGIT, 				// DIGIT
       	 State.ON_EPOINT_OR_QMARK,      // QMARK
       	 State.ON_EPOINT_OR_QMARK,      // EPOINT
       	 State.ON_DIGIT, 					// DECIMAL
       	 State.ON_DOT, 			// PERIOD
       	 State.ON_PUNCT,			// COLON
       	 State.ON_PUNCT,			// SEMI-COLON
    	 State.ON_AMPERSAND,			// AMPERSAND
    	 State.ON_PUNCT,			// PUNCT 
       	 State.ON_AT,					// AT_SIGN
       	 State.ON_HASH,					// HASH
       	 State.ON_PUNCT,		// OPEN_PAREN
       	 State.ON_PUNCT,		// CLOSE_PAREN
       	 State.ON_PUNCT,		// OPEN_ANGLE
       	 State.ON_PUNCT,		// CLOSE_ANGLE
       	 State.BETWEEN_TOKENS,			// SPACE
       	 State.ON_UNKNOWN},					// UNKNOWN
        // ON_ERROR
       	{State.START, 					// ALPHA
       	 State.START, 					// DIGIT
       	 State.START, 					// QMARK
       	 State.START, 					// EPOINT
       	 State.START, 					// DECIMAL
       	 State.START, 					// PERIOD
      	 State.START, 					// COLON
       	 State.START, 					// SEMI-COLON
    	 State.START,					// AMPERSAND
    	 State.ON_PUNCT,			// PUNCT 
       	 State.START,					// AT_SIGN
       	 State.START,					// HASH
       	 State.START,					// OPEN_PAREN
       	 State.START,					// CLOSE_PAREN
       	 State.START,					// OPEN_ANGLE
       	 State.START,					// CLOSE_ANGLE
       	 State.START,					// SPACE
       	 State.ON_UNKNOWN},					// UNKNOWN
    };
    
    public static State getNextState(State curState, Alphabet symbol) {
		return stateTable[curState.ordinal()][symbol.ordinal()];
    }

    public static Alphabet getSymbol(char c, char lastchar, char lookahead) {
        Alphabet symbol = null;

        if (Character.isWhitespace(c))
            symbol = Alphabet.SPACE;
        else if (c == '-' || c == (char)0x2012 || c == (char)0x2013 || c == (char)0x2014 || c == (char)0x2015)
        	if (Character.isLetter(lastchar) && (Character.isLetter(lookahead) || Character.isDigit(lookahead)))
        		symbol = Alphabet.ALPHA;
        	else
        		symbol = Alphabet.PUNCT;
        else if (c == '=')
        	symbol = Alphabet.PUNCT;
        else if (Character.isLetter(c))
            symbol = Alphabet.ALPHA;
        else if (Character.isDigit(c))
            symbol = Alphabet.DIGIT;
        else if (c == '"' || c == (char)0x201c || c == (char)0x201d || c == (char)0x201e || c == (char)0x201f || c == (char)0x301d || c == (char)0x301e || c == (char)0x301f || c == (char)0xff02)
            symbol = Alphabet.PUNCT;
        else if (c == '\'' ||  c == (char)0x2018 || c == (char)0x2019 || c == (char)0x201a || c == (char)0x201b || c == (char)0xff07)
        	if (lookahead == 's')
        		symbol = Alphabet.ALPHA;
        	else if (Character.isLetter(lookahead) && Character.isLetter(lastchar))
        		symbol = Alphabet.ALPHA;
        	else
        		symbol = Alphabet.PUNCT;
        else if (c == '?')
            symbol = Alphabet.QMARK;
        else if (c == '!')
            symbol = Alphabet.EPOINT;
        else if (c == '.')
        	if (Character.isDigit(lookahead)) {
        		symbol = Alphabet.DECIMAL;
        	} else {
        		symbol = Alphabet.PERIOD;
        	}
        else if (c == ',')
            symbol = Alphabet.PUNCT;
        else if (c == '&')
        	symbol = Alphabet.AMPERSAND;
        else if (c == ':')
        	symbol = Alphabet.COLON;
        else if (c == ';')
            symbol = Alphabet.SEMICOLON;
        else if (c == '\\')
            symbol = Alphabet.PUNCT;
        else if (c == '/')
            symbol = Alphabet.PUNCT;
        else if (c == '@')
            symbol = Alphabet.AT_SIGN;
        else if (c == '#')
            symbol = Alphabet.HASH;
        else if (c == '(' || c == '[')
            symbol = Alphabet.OPEN_PAREN;
        else if (c == ')')
            symbol = Alphabet.CLOSE_PAREN;
        else if (c == '<')
            symbol = Alphabet.OPEN_ANGLE;
        else if (c == '>')
            symbol = Alphabet.CLOSE_ANGLE;
        else if (c == '_')
        	symbol = Alphabet.ALPHA;
        else if (c == '$')
        	symbol = Alphabet.PUNCT;
        else if (c == '*')
        	symbol = Alphabet.ALPHA;
        else
            symbol = Alphabet.UNKNOWN;

        return symbol;
    }

}
