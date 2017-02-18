package com.parc.chat.tokenizer;

/**
 * Finite-state transducer that recognizers emoticons.
 *
 * @author Kyle Dent
 *
 */
public class EmoticonFST {

	/**
	 * Determines if the next token in the supplied string is an emoticon. The function tries to match any of many
	 * types of emoticons up until it encounters the first space in the string. If an emoticon is found, it returns
	 * the length of the emoticon and otherwise returns 0.
	 * @param str - the string to be checked for an emoticon
	 * @return an integer representing the length of the emoticon from the beginning of the string or 0 if an emoticon cannot be matched
	 */
	public static int recognizeEmoticon(String str) {
		int emoticonLength = 0;

		int i;
		Alphabet currentSymbol = null;
		State currentState = State.START;
		for (i = 0; i < str.length(); i++) {
			if (Character.isWhitespace(str.charAt(i))) {
				break;
			}
			currentSymbol = getSymbol(str.charAt(i));
			if (currentSymbol == null) {
				break;
			}
			currentState = getState(currentState, currentSymbol);
		}

		// Check for a final state.
		if (currentState == State.ON_EYES || currentState == State.ON_MOUTH) {
			emoticonLength = i;
		} else {
			emoticonLength = 0;
		}
		return emoticonLength;
	}

	public static boolean isInitialEmoticonChar(char c) {
		boolean isInitialChar = false;
		if (isEyes(getSymbol(c))) {
			isInitialChar = true;
		} else if (c == ')' || c == '(') {
			isInitialChar = true;
		} else {
			isInitialChar = false;
		}
		return isInitialChar;
	}

	private static enum Alphabet {
		COLON,
		HYPHEN,
		OPEN_PAREN,
		CLOSE_PAREN,
		OPEN_BRACKET,
		CLOSE_BRACKET,
		PIPE,
		UNDERSCORE,
		THREE,
		EIGHT,
		ZERO,
		CAPITAL_B,
		CAPITAL_D,
		CAPITAL_O,
		CAPITAL_P,
		LOWER_O,
		LOWER_P,
		APOSTROPHE,
		BACKSLASH,
		FORWARDSLASH,
		LESS_THAN,
		GREATER_THAN,
		SEMI_COLON,
		ASTERISK
	};

	private static enum State {
		START,
		ON_EYES,
		ON_NOSE,
		ON_MOUTH,
		ON_BROW,
		ON_TEAR,
		FAIL
	};

	private static State getState(State currentState, Alphabet currentSymbol) {
		State newState = State.FAIL;

		switch(currentState) {
		case START:
			switch(currentSymbol) {
			case COLON:
			case SEMI_COLON:
				newState = State.ON_EYES;
				break;
			default:
				newState = State.FAIL;
			}
			break;
		case ON_EYES:
			if (currentSymbol == Alphabet.HYPHEN) {
				newState = State.ON_NOSE;
			} else if (isMouth(currentSymbol)) {
				newState = State.ON_MOUTH;
			} else {
				newState = State.FAIL;
			}
			break;
		case ON_NOSE:
			if (currentSymbol == Alphabet.COLON) {
				newState = State.ON_EYES;
			} else if (isMouth(currentSymbol)) {
				newState = State.ON_MOUTH;
			} else {
				newState = State.FAIL;
			}
			break;
		default:
			newState = State.FAIL;
		}

		return newState;
	}

	private static Alphabet getSymbol(char c) {
		Alphabet symbol = null;

		switch(c) {
		case ':':
			symbol = Alphabet.COLON;
			break;
		case '-':
			symbol = Alphabet.HYPHEN;
			break;
		case '(':
			symbol = Alphabet.OPEN_PAREN;
			break;
		case ')':
			symbol = Alphabet.CLOSE_PAREN;
			break;
		case '[':
			symbol = Alphabet.OPEN_BRACKET;
			break;
		case ']':
			symbol = Alphabet.CLOSE_BRACKET;
			break;
		case '|':
			symbol = Alphabet.PIPE;
			break;
		case '_':
			symbol = Alphabet.UNDERSCORE;
			break;
		case '3':
			symbol = Alphabet.THREE;
			break;
		case '8':
			symbol = Alphabet.EIGHT;
			break;
		case '0':
			symbol = Alphabet.ZERO;
			break;
		case 'B':
			symbol = Alphabet.CAPITAL_B;
			break;
		case 'D':
			symbol = Alphabet.CAPITAL_D;
			break;
		case 'O':
			symbol = Alphabet.CAPITAL_O;
			break;
		case 'P':
			symbol = Alphabet.CAPITAL_P;
			break;
		case 'o':
			symbol = Alphabet.LOWER_O;
			break;
		case 'p':
			symbol = Alphabet.LOWER_P;
			break;
		case '\'':
			symbol = Alphabet.APOSTROPHE;
			break;
		case '\\':
			symbol = Alphabet.BACKSLASH;
			break;
		case '/':
			symbol = Alphabet.FORWARDSLASH;
			break;
		case '<':
			symbol = Alphabet.LESS_THAN;
			break;
		case '>':
			symbol = Alphabet.GREATER_THAN;
			break;
		case ';':
			symbol = Alphabet.SEMI_COLON;
			break;
		case '*':
			symbol = Alphabet.ASTERISK;
			break;
		default:
			symbol = null;
		}

		return symbol;
	}

	private static boolean isEyes(Alphabet symbol) {
		if (symbol == Alphabet.CAPITAL_B
		|| symbol == Alphabet.COLON
		|| symbol == Alphabet.EIGHT
		|| symbol == Alphabet.SEMI_COLON)
		{
			return true;
		} else {
			return false;
		}
	}

	private static boolean isMouth(Alphabet symbol) {
		if (symbol == Alphabet.OPEN_BRACKET
		|| symbol == Alphabet.CLOSE_BRACKET
		|| symbol == Alphabet.OPEN_PAREN
		|| symbol == Alphabet.CLOSE_PAREN
		|| symbol == Alphabet.CAPITAL_D
		|| symbol == Alphabet.CAPITAL_O
		|| symbol == Alphabet.LOWER_O
		|| symbol == Alphabet.CAPITAL_P
		|| symbol == Alphabet.LOWER_P)
		{
			return true;
		} else {
			return false;
		}

	}

}
