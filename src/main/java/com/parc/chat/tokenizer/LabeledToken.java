package com.parc.chat.tokenizer;

import java.io.Serializable;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


/**
 * Java bean class to hold a token that has been tagged with its lexical
 * category and has been stemmed.
 */
public class LabeledToken implements Serializable {
	private static final long serialVersionUID = 1L;
    // XML
	public static final String TOKEN_ELEMENT = "token";
	public static final String POS_ATTRIBUTE = "part-of-speech";
	public static final String STEM_ATTRIBUTE = "lemma";

	private String originalWord;
	private String stem;
	private TokenType tokenType;
	private int index = -1;
	private int charPos = -1;


	/**
	 * Creates a new labeled token with none of its features set.
	 */
	public LabeledToken() {
		;
	}

	/**
	 * Creates a new labeled token with its original word value set.
	 * @param originalWord the original form of the token
	 */
	public LabeledToken(String originalWord) {
		this.originalWord = originalWord;
	}

	/**
	 * Gets the token in its original form.
	 * @return the token
	 */
	public String getOriginalWord() {
		return originalWord;
	}

	/**
	 * Sets the token. You should use this to store the token in its
	 * original form.
	 * @param token the token in its original form
	 */
	public void setOriginalWord(String token) {
		this.originalWord = token;
	}

	/**
	 * Gets the stemmed form (lemma) for this token.
	 * @return the stemmed form of the token
	 */
	public String getStem() {
		return stem;
	}

	/**
	 * Sets the stemmed form of this token.
	 * @param stem the stemmed form of the token
	 */
	public void setStem(String stem) {
		this.stem = stem;
	}

	/**
	 * Get the token type assigned to this token (see the TokenType enum).
	 * @return the type assigned to this token
	 */
	public TokenType getTokenType() {
        return tokenType;
    }

	/**
	 * Set the token type on this token (see the TokenType enum).
	 * @param tokenType the type to assign to this token
	 */
    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    /**
     * Get the index of this token into its sentence.
     * @return the index of this token in its sentence
     */
    public int getIndex() {
        return index;
    }

    /**
     * Set the index of this token into its sentence.
     * @param index the index of this token in its sentence
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Get character position where this token starts in the sentence.
     * @return the character position where this token starts in the sentence
     */
    public int getCharacterPosition() {
    	return charPos;
    }

    /**
     * Set the character position where this token starts in the sentence.
     * @param charPos the integer that indicates the token's starting point
     */
    public void setCharacterPosition(int charPos) {
    	this.charPos = charPos;
    }


    /**
	 * Returns a string representation showing the original token and its 
	 * part-of-speech separated by a slash.
	 */
	public String toString() {
		return originalWord + "/" + stem;
	}

    public Node makeXML(Document doc) {
        DocumentFragment docFragment = doc.createDocumentFragment();
        Element tokenNode = doc.createElement(TOKEN_ELEMENT);
        tokenNode.appendChild(doc.createTextNode(originalWord));
        tokenNode.setAttribute(STEM_ATTRIBUTE, stem);
        docFragment.appendChild(tokenNode);
        return docFragment;
    }

}
