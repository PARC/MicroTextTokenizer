package com.parc.chat.tokenizer;

/**
 * This enum defines the types of tokens according to their use in text. Names should
 * be self-explanatory, but note that 'UH' is used for any kind of interjection.
 */
public enum TokenType {
    ALPHA,
    PUNCT,
    BRACKET,
    AT_NAME,
    HASH_TAG,
    EMOTICON,
    URL,
    EMAIL_ADDR,
    FILENAME,
    NUMERIC
}

