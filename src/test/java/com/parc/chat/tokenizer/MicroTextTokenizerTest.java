package com.parc.chat.tokenizer;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import org.junit.Test;
import static org.junit.Assert.*;

public class MicroTextTokenizerTest {

	@Test
	public void hostPathAsURL() {
		MicroTextTokenizer tok  = new MicroTextTokenizer("www.parc.com/XLE.html");
		Stack<LabeledToken> tokenStack = tok.tokenize();
		assertEquals("www.parc.com/XLE.html", tokenStack.get(0).getOriginalWord());
		assertEquals(TokenType.URL, tokenStack.get(0).getTokenType());
	}

	@Test
	public void trailingCommaTest1() {
		MicroTextTokenizer tok = new MicroTextTokenizer("the galaxy 4,");
		Stack<LabeledToken> tokenStack = tok.tokenize();
		assertEquals("4", tokenStack.get(2).getOriginalWord());
		assertEquals(",", tokenStack.get(3).getOriginalWord());
	}

	@Test
	public void trailingCommaTest2() {
		MicroTextTokenizer tok = new MicroTextTokenizer("the galaxy s4,");
		Stack<LabeledToken> tokenStack = tok.tokenize();
		assertEquals("s4", tokenStack.get(2).getOriginalWord());
		assertEquals(",", tokenStack.get(3).getOriginalWord());
	}

	@Test
	public void bracketsTest() {
		MicroTextTokenizer tok = new MicroTextTokenizer("aaa <br> bbb");
		Stack<LabeledToken> tokenStack = tok.tokenize();
		assertEquals("aaa", tokenStack.get(0).getOriginalWord());
		assertEquals(0, tokenStack.get(0).getCharacterPosition());
		assertEquals("<", tokenStack.get(1).getOriginalWord());
		assertEquals(4, tokenStack.get(1).getCharacterPosition());
		assertEquals(7, tokenStack.get(3).getCharacterPosition());
	}

	@Test
	public void endingQuestionMarkTest() {
		MicroTextTokenizer tok = new MicroTextTokenizer("xxx?");
		tok.tokenize();
		List<String> tokens = tok.getTokensAsList();
		assertEquals("xxx", tokens.get(0));
		assertEquals("?", tokens.get(1));
	}

	@Test
	public void beginningExclamationPointTest() {
		MicroTextTokenizer tok = new MicroTextTokenizer("!’abc’?");
		tok.tokenize();
		List<String> tokens = tok.getTokensAsList();
		assertEquals("!", tokens.get(0));
		assertEquals("’", tokens.get(1));
		assertEquals("abc", tokens.get(2));
		assertEquals("’", tokens.get(3));
		assertEquals("?", tokens.get(4));
	}

	@Test
	public void singleExclamationPointTest() {
		MicroTextTokenizer tok = new MicroTextTokenizer("!");
		tok.tokenize();
		List<String> tokens = tok.getTokensAsList();
		assertEquals("!", tokens.get(0));
	}

	@Test
	public void singleQuestionMarkTest() {
		MicroTextTokenizer tok = new MicroTextTokenizer("?");
		tok.tokenize();
		List<String> tokens = tok.getTokensAsList();
		assertEquals("?", tokens.get(0));
	}

	@Test
	public void ellipsisNoSpaceTest() {
		MicroTextTokenizer tok = new MicroTextTokenizer("No...it");
		tok.tokenize();
		List<String> tokens = tok.getTokensAsList();
		assertEquals("No", tokens.get(0));
		assertEquals("...", tokens.get(1));
		assertEquals("it", tokens.get(2));
	}

	@Test
	public void ellipsisTest() {
		MicroTextTokenizer tok = new MicroTextTokenizer("Cust: no...it is the 8.9 inch");
		tok.tokenize();
		List<String> tokens = tok.getTokensAsList();
		assertEquals("Cust", tokens.get(0));
		assertEquals(":", tokens.get(1));
		assertEquals("no", tokens.get(2));
		assertEquals("...", tokens.get(3));
		assertEquals("it", tokens.get(4));
		assertEquals("is", tokens.get(5));
		assertEquals("the", tokens.get(6));
		assertEquals("8.9", tokens.get(7));
		assertEquals("inch", tokens.get(8));
	}

	@Test
	public void simpleTest() {
		MicroTextTokenizer tok = new MicroTextTokenizer("Had we but world enough and time");
		Stack<LabeledToken> tokenStack = tok.tokenize();

		assertEquals(1, tokenStack.get(0).getIndex());
		assertEquals("Had", tokenStack.get(0).getOriginalWord());

		assertEquals(2, tokenStack.get(1).getIndex());
		assertEquals("we", tokenStack.get(1).getOriginalWord());

		assertEquals(3, tokenStack.get(2).getIndex());
		assertEquals("but", tokenStack.get(2).getOriginalWord());

		assertEquals(4, tokenStack.get(3).getIndex());
		assertEquals("world", tokenStack.get(3).getOriginalWord());

		assertEquals(5, tokenStack.get(4).getIndex());
		assertEquals("enough", tokenStack.get(4).getOriginalWord());

		assertEquals(6, tokenStack.get(5).getIndex());
		assertEquals("and", tokenStack.get(5).getOriginalWord());

		assertEquals(7, tokenStack.get(6).getIndex());
		assertEquals("time", tokenStack.get(6).getOriginalWord());

	}

	@Test
	public void finalPeriodTest() {
        MicroTextTokenizer tok = new MicroTextTokenizer("Sentences end in a period.");
        Stack<LabeledToken> tokenStack = tok.tokenize();
        assertEquals("period", tokenStack.get(4).getOriginalWord());
        assertEquals("ALPHA", (tokenStack.get(4).getTokenType()).toString());
        assertEquals("PUNCT", tokenStack.get(5).getTokenType().toString());
    }

	@Test
	public void finalCommaTest() {
		MicroTextTokenizer tok = new MicroTextTokenizer("phrases can end with a comma,");
		Stack<LabeledToken> tokenStack = tok.tokenize();
		assertEquals("comma", tokenStack.get(5).getOriginalWord());
		assertEquals("PUNCT", tokenStack.get(6).getTokenType().toString());
	}

	@Test
	public void numberWithPeriodTest() {
		MicroTextTokenizer tok = new MicroTextTokenizer("It costs 34.50");
		Stack<LabeledToken> tokenStack = tok.tokenize();
		assertEquals("34.50", tokenStack.get(2).getStem());
		assertEquals("34.50", tokenStack.get(2).getOriginalWord());
		assertEquals(TokenType.NUMERIC, tokenStack.get(2).getTokenType());
	}

	@Test
	public void characterPositionTest() {
		MicroTextTokenizer tok = new MicroTextTokenizer("Test sentence with 6 tokens.");
		Stack<LabeledToken> tokenStack = tok.tokenize();
		assertEquals(6, tokenStack.size());
		assertEquals(0, tokenStack.get(0).getCharacterPosition());
		assertEquals(5, tokenStack.get(1).getCharacterPosition());
		assertEquals(14, tokenStack.get(2).getCharacterPosition());
		assertEquals(19, tokenStack.get(3).getCharacterPosition());
		assertEquals(21, tokenStack.get(4).getCharacterPosition());
		assertEquals(27, tokenStack.get(5).getCharacterPosition());
	}

	@Test
	public void nonAlphaCharacterPositionTest() {
		MicroTextTokenizer tok = new MicroTextTokenizer("\"Start #hashtag    @name .dot :-) end");
		Stack<LabeledToken> tokenStack = tok.tokenize();
		assertEquals("\"", tokenStack.get(0).getOriginalWord());

		assertEquals(1, tokenStack.get(1).getCharacterPosition());
		assertEquals(7, tokenStack.get(2).getCharacterPosition());
		assertEquals(19, tokenStack.get(3).getCharacterPosition());
		assertEquals(25, tokenStack.get(4).getCharacterPosition());
		assertEquals(26, tokenStack.get(5).getCharacterPosition());
		assertEquals(30, tokenStack.get(6).getCharacterPosition());
		assertEquals(34, tokenStack.get(7).getCharacterPosition());
	}

	@Test
	public void testSentenceEndDot() {
		MicroTextTokenizer tok = new MicroTextTokenizer("the first one.");
		Stack<LabeledToken> tokenStack = tok.tokenize();
		assertEquals("one", tokenStack.get(2).getOriginalWord());
		assertEquals("ALPHA", (tokenStack.get(2).getTokenType()).toString());
		assertEquals("PUNCT", tokenStack.get(3).getTokenType().toString());
		assertEquals(13, tokenStack.get(3).getCharacterPosition());
	}

	@Test
	public void testDotInToken() {
	    MicroTextTokenizer tok = new MicroTextTokenizer("the No. 1 Artist");
		Stack<LabeledToken> tokenStack = tok.tokenize();
		assertEquals("No.", tokenStack.get(1).getOriginalWord());
		assertEquals("1", tokenStack.get(2).getOriginalWord());
		assertEquals(8, tokenStack.get(2).getCharacterPosition());
	}

	@Test
	public void testAbbreviation() {
		MicroTextTokenizer tok = new MicroTextTokenizer("D.H. Lawrence wrote several books");
		Stack<LabeledToken> tokenStack = tok.tokenize();
		assertEquals("[D.H./d.h., Lawrence/lawrence, wrote/wrote, several/several, books/books]",
				tokenStack.toString());
		assertEquals(5, tokenStack.get(1).getCharacterPosition());
	}

	@Test
	public void testEllipsis() {
	    MicroTextTokenizer tok = new MicroTextTokenizer("today... Next sentence");
		Stack<LabeledToken> tokenStack = tok.tokenize();
		assertEquals("today", tokenStack.get(0).getOriginalWord());
		assertEquals("...", tokenStack.get(1).getOriginalWord());
		assertEquals(5, tokenStack.get(1).getCharacterPosition());
		assertEquals("Next", tokenStack.get(2).getOriginalWord());
	}

	@Test
	public void testStartEllipsis() {
	    MicroTextTokenizer tok = new MicroTextTokenizer("...or is");
		Stack<LabeledToken> tokenStack = tok.tokenize();
		assertEquals("PUNCT", tokenStack.get(0).getTokenType().toString());
		assertEquals("...", tokenStack.get(0).getOriginalWord());
		assertEquals(0, tokenStack.get(0).getCharacterPosition());
		assertEquals("or", tokenStack.get(1).getOriginalWord());
	}

	@Test
	public void testSpaceEllipsis() {
	    MicroTextTokenizer tok = new MicroTextTokenizer("today ... Next bit");
		Stack<LabeledToken> tokenStack = tok.tokenize();
		assertEquals("...", tokenStack.get(1).getOriginalWord());
		assertEquals(6, tokenStack.get(1).getCharacterPosition());
	}

	@Test
	public void testDollarAmount() {
	    MicroTextTokenizer tok = new MicroTextTokenizer("amount $90.00 is the");
		Stack<LabeledToken> tokenStack = tok.tokenize();
		assertEquals("$", tokenStack.get(1).getOriginalWord());
		assertEquals("90.00", tokenStack.get(2).getOriginalWord());
		assertEquals(7, tokenStack.get(1).getCharacterPosition());
	}

	@Test
	public void testDollarAmount2() {
	    MicroTextTokenizer tok = new MicroTextTokenizer("$7.00");
	    tok.tokenize();
	    assertEquals(Arrays.asList("$", "7.00"), tok.getTokensAsList());
	}

	@Test
	public void testDecimalStart() {
		MicroTextTokenizer tok = new MicroTextTokenizer(".33 is the correct value");
		Stack<LabeledToken> tokenStack = tok.tokenize();
		assertEquals(".33", tokenStack.get(0).getOriginalWord());
		assertEquals(TokenType.NUMERIC, tokenStack.get(0).getTokenType());
		assertEquals("is", tokenStack.get(1).getOriginalWord());
	}

	@Test
	public void testDecimalNumber() {
		MicroTextTokenizer tok = new MicroTextTokenizer("The value is .33 and change");
		Stack<LabeledToken> tokenStack = tok.tokenize();
		assertEquals(".33", tokenStack.get(3).getOriginalWord());
		assertEquals(TokenType.NUMERIC, tokenStack.get(3).getTokenType());
		assertEquals("and", tokenStack.get(4).getOriginalWord());
	}

	@Test
	public void testDigitEndSentence() {
	    MicroTextTokenizer tok = new MicroTextTokenizer("Dot at end with 23. Then some more");
		Stack<LabeledToken> tokenStack = tok.tokenize();
		assertEquals("23", tokenStack.get(4).getOriginalWord());
	}

	@Test
	public void testStartWithDot() {
	    MicroTextTokenizer tok = new MicroTextTokenizer(".why do");
		Stack<LabeledToken> tokenStack = tok.tokenize();
		assertEquals(".", tokenStack.get(0).getOriginalWord());
	}

	@Test
	public void testDotCom() {
	    MicroTextTokenizer tok = new MicroTextTokenizer("WSJ.com");
		Stack<LabeledToken> tokenStack = tok.tokenize();
		assertEquals("URL", tokenStack.get(0).getTokenType().toString());
		assertEquals("WSJ.com", tokenStack.get(0).getOriginalWord());
	}

	@Test
	public void testHostname() {
	    MicroTextTokenizer tok = new MicroTextTokenizer("www.oceana.org");
		Stack<LabeledToken> tokenStack = tok.tokenize();
		assertEquals("www.oceana.org", tokenStack.get(0).getOriginalWord());
		assertEquals("URL", tokenStack.get(0).getTokenType().toString());
	}

	@Test
	public void testURL() {
		MicroTextTokenizer tok = new MicroTextTokenizer("get the best deals at http://www.gooddeals.com/asdf/asdfh.php. Act now!");
		Stack<LabeledToken> tokenStack = tok.tokenize();
		assertEquals("http://www.gooddeals.com/asdf/asdfh.php", tokenStack.get(5).getOriginalWord());
		assertEquals(TokenType.URL, tokenStack.get(5).getTokenType());
		assertEquals(".", tokenStack.get(6).getOriginalWord());
		assertEquals(TokenType.PUNCT, tokenStack.get(6).getTokenType());
		assertEquals("Act", tokenStack.get(7).getOriginalWord());
	}

	@Test
	public void testCommaURL() {
		MicroTextTokenizer tok = new MicroTextTokenizer("http://www.phonescoop.com/phones/compare.php?p=4075,3801,3217");
		tok.tokenize();
		List<String> tokens = tok.getTokensAsList();
		assertEquals("http://www.phonescoop.com/phones/compare.php?p=4075,3801,3217", tokens.get(0));
	}

	@Test
	public void testNumberAsLetter() {
	    MicroTextTokenizer tok = new MicroTextTokenizer("TAKE CARE 0F ME");
		Stack<LabeledToken> tokenStack = tok.tokenize();
		assertEquals("0f", tokenStack.get(2).getStem());
		assertEquals(10, tokenStack.get(2).getCharacterPosition());
	}

	@Test
	public void testNoLetters() {
	    MicroTextTokenizer tok = new MicroTextTokenizer("word -___- more words");
		Stack<LabeledToken> tokenStack = tok.tokenize();
		assertEquals(TokenType.PUNCT, tokenStack.get(2).getTokenType());
		assertEquals("-", tokenStack.get(1).getOriginalWord());
		assertEquals(5, tokenStack.get(1).getCharacterPosition());
	}

	@Test
	public void testSpecialStems() {
	    MicroTextTokenizer tok = new MicroTextTokenizer("Hello @kdent http://tinyurl.us/asdf :)");
	    Stack<LabeledToken> tokenStack = tok.tokenize();
        assertEquals(6, tokenStack.get(1).getCharacterPosition());
        assertEquals(13, tokenStack.get(2).getCharacterPosition());
	    assertEquals("[Hello/hello, @kdent/ATNAME, http://tinyurl.us/asdf/http://tinyurl.us/asdf, :)/:)]", tokenStack.toString());
	}

	@Test
	public void testRomneyTweet() {
	    MicroTextTokenizer tok = new MicroTextTokenizer("Yeah. And it looks like we are stuck with #Romney RT @rhandi661:"
	            + " @velvethammer #Romney would say, (cont) http://t.co/a7DnygTH");
	    Stack<LabeledToken> tokenStack = tok.tokenize();
	    assertEquals("[Yeah/yeah, ./., And/and, it/it, looks/looks, like/like, we/we, are/are, stuck/stuck, with/with, #Romney/#romney," +
	     " RT/rt, @rhandi661/ATNAME, :/:, @velvethammer/ATNAME, #Romney/#romney, would/would, say/say, ,/,, (/(, cont/cont, )/)," +
	     " http://t.co/a7DnygTH/http://t.co/a7dnygth]", tokenStack.toString());
	}

	@Test
	public void noSpaceBetweenSentencesTest() {
		MicroTextTokenizer tok = new MicroTextTokenizer("First sentence.Followed by the next");
		Stack<LabeledToken> tokenStack = tok.tokenize();
		assertEquals("[First/first, sentence/sentence, ./., Followed/followed, by/by, the/the, next/next]", tokenStack.toString());
	}

	@Test
	public void testNullString() {
	    MicroTextTokenizer tok = new MicroTextTokenizer(null);
	    Stack<LabeledToken> tokenStack = tok.tokenize();
	    assertTrue(tokenStack.isEmpty());
	}

	@Test
	public void testEmptyString() {
        MicroTextTokenizer tok = new MicroTextTokenizer("");
        Stack<LabeledToken> tokenStack = tok.tokenize();
        assertTrue(tokenStack.isEmpty());	    
	}

	@Test
	public void testSingleCharString() {
	    MicroTextTokenizer tok = new MicroTextTokenizer("f");
	    Stack<LabeledToken> tokenStack = tok.tokenize();
	    assertEquals("f/f", tokenStack.pop().toString());
	}

	@Test
	public void getTokensAsListTest() {
		boolean caughtException = false;

		MicroTextTokenizer tok = new MicroTextTokenizer("A string of words.");
		try {
			tok.getTokensAsList();
		} catch(IllegalStateException e) {
			caughtException = true;
		}
		assertTrue(caughtException);
		tok.tokenize();
		List<String> tokenList = tok.getTokensAsList();
		assertEquals("[A, string, of, words, .]", tokenList.toString());

	}

	@Test
	public void getTokenPositionTest() {
		boolean caughtException = false;

		MicroTextTokenizer tok = new MicroTextTokenizer("A string of words.");
		try {
			tok.getTokenPosition(1);
		} catch (IllegalStateException e) {
			caughtException = true;
		}
		assertTrue(caughtException);
		tok.tokenize();
		assertEquals(2, tok.getTokenPosition(1));
	}

	@Test
	public void apostropheSTest() {
		MicroTextTokenizer tok = new MicroTextTokenizer("Mary's lamb");
		tok.tokenize();
		assertEquals("Mary's", tok.getTokensAsList().get(0));
	}

	@Test
	public void contractionTest() {
		MicroTextTokenizer tok = new MicroTextTokenizer("They're aren't going");
		tok.tokenize();
		assertEquals("They're", tok.getTokensAsList().get(0));
		assertEquals("aren't", tok.getTokensAsList().get(1));
	}

	@Test
	public void hyphenTest() {
		MicroTextTokenizer tok = new MicroTextTokenizer("An advantage of grid-energy is");
		tok.tokenize();
		assertEquals("grid", tok.getTokensAsList().get(3));
		assertEquals("energy", tok.getTokensAsList().get(5));
	}

    @Test
    public void hyphenTest2() {
        MicroTextTokenizer tok = new MicroTextTokenizer("This is a sentence with a hyphenated-word.");
        List<LabeledToken> tokenList = tok.tokenize();
        assertEquals(10, tokenList.size());
        assertEquals("hyphenated", tokenList.get(6).getOriginalWord());
        assertEquals("word", tokenList.get(8).getOriginalWord());
        assertEquals(TokenType.HYPHEN, tokenList.get(7).getTokenType());
    }

	@Test
	public void wordAndDigitHyphenTest() {
		MicroTextTokenizer tok = new MicroTextTokenizer("word-22");
		tok.tokenize();
		assertEquals("word", tok.getTokensAsList().get(0));
		assertEquals("22", tok.getTokensAsList().get(2));
	}

	@Test
	public void normalEmailAddressTest() {
		MicroTextTokenizer tok = new MicroTextTokenizer("somebody@example.com");
		tok.tokenize();
		assertEquals("somebody@example.com", tok.getTokensAsList().get(0));
	}

	@Test
	public void twoLevelEmailAddressTest() {
		MicroTextTokenizer tok = new MicroTextTokenizer("somebody@host.example.com");
		tok.tokenize();
		assertEquals("somebody@host.example.com", tok.getTokensAsList().get(0));
	}

	@Test
	public void emailAddressEndPhraseTest() {
		MicroTextTokenizer tok = new MicroTextTokenizer("An example email address is somebody@example.com");
		tok.tokenize();
		assertEquals("somebody@example.com", tok.getTokensAsList().get(5));
	}

	@Test
	public void emailAddressEndSentenceTest() {
		MicroTextTokenizer tok = new MicroTextTokenizer("An example email address is henderson453@yahoo.com.");
		tok.tokenize();
		assertEquals("henderson453@yahoo.com", tok.getTokensAsList().get(5));
	}

	@Test
	public void emailAddressInSentenceTest() {
		MicroTextTokenizer tok = new MicroTextTokenizer("An example email address is somebody@example.com with more afterwards.");
		tok.tokenize();
		assertEquals("somebody@example.com", tok.getTokensAsList().get(5));
		assertEquals("with", tok.getTokensAsList().get(6));
	}

	@Test
	public void mixedDigitAlphaTest() {
		MicroTextTokenizer tok = new MicroTextTokenizer("The tokenizer also tokenizes 0010027skk into 0010027 and skk.");
		tok.tokenize();
		assertEquals("0010027skk", tok.getTokensAsList().get(4));
	}

	@Test
	public void mixedDigitalAlphaTest2() {
		MicroTextTokenizer tok = new MicroTextTokenizer("It also tokenizes 0A1B2C into 0 and A1B2C.");
		tok.tokenize();
		assertEquals("0A1B2C", tok.getTokensAsList().get(3));
	}

	@Test
	public void mixedAlphaDigital() {
		MicroTextTokenizer tok = new MicroTextTokenizer("It doesn’t have a problem with A1B2C3, so it seems to happen when a token begins with a digit.");
		tok.tokenize();
		assertEquals("A1B2C3", tok.getTokensAsList().get(6));
	}

	@Test
	public void filenameAtEnd() {
	    MicroTextTokenizer tok = new MicroTextTokenizer("give me a file called testpage.pdf");
	    List<LabeledToken> tokenList = tok.tokenize();
	    assertEquals("testpage.pdf", tokenList.get(5).getOriginalWord());
	    assertEquals(TokenType.FILENAME, tokenList.get(5).getTokenType());
	}

	@Test
	public void filenameInMiddle() {
        MicroTextTokenizer tok = new MicroTextTokenizer("print testpage.pdf on beech");
        List<LabeledToken> tokenList = tok.tokenize();
        assertEquals("testpage.pdf", tokenList.get(1).getOriginalWord());
        assertEquals(TokenType.FILENAME, tokenList.get(1).getTokenType());
	}

	@Test
	public void filenameAtStart() {
	    MicroTextTokenizer tok = new MicroTextTokenizer("testpage.html is a file");
	    List<LabeledToken> tokenList = tok.tokenize();
	    assertEquals("testpage.html", tokenList.get(0).getOriginalWord());
	    assertEquals(TokenType.FILENAME, tokenList.get(0).getTokenType());
	}

	@Test
	public void substringExtension() {
	    // 'htm' is a substring of 'html'. Make sure both are recognized.
        MicroTextTokenizer tok = new MicroTextTokenizer("testpage.htm is a file");
        List<LabeledToken> tokenList = tok.tokenize();
        assertEquals("testpage.htm", tokenList.get(0).getOriginalWord());
        assertEquals(TokenType.FILENAME, tokenList.get(0).getTokenType());	    
	}

	@Test
	public void endInDotX() {
	    MicroTextTokenizer tok = new MicroTextTokenizer("open /Users/kdent/Documents/speech_rfp.docx");
	    List<LabeledToken> tokenList = tok.tokenize();
	    assertEquals("/Users/kdent/Documents/speech_rfp.docx", tokenList.get(1).getOriginalWord());
	    assertEquals(TokenType.FILENAME, tokenList.get(1).getTokenType());
	}

	@Test
	public void filenameAlone() {
	       MicroTextTokenizer tok = new MicroTextTokenizer("testpage.pdf");
	        List<LabeledToken> tokenList = tok.tokenize();
	        assertEquals("testpage.pdf", tokenList.get(0).getOriginalWord());
	        assertEquals(TokenType.FILENAME, tokenList.get(0).getTokenType());
	}

	@Test
	public void testSmbUrl() {
	    MicroTextTokenizer tok = new MicroTextTokenizer("Open the file at smb://samba.parc.com/tilde/m/maxwell");
	    List<LabeledToken> tokenList = tok.tokenize();
	    assertEquals("smb://samba.parc.com/tilde/m/maxwell", tokenList.get(4).getOriginalWord());
	}

	@Test
	public void testWebDavsUrl() {
	    MicroTextTokenizer tok = new MicroTextTokenizer("transfer it to WebDavS://jtmmp.parc.xerox.com/Public");
	    List<LabeledToken> tokenList = tok.tokenize();
	    assertEquals("WebDavS://jtmmp.parc.xerox.com/Public", tokenList.get(3).getOriginalWord());
	}

	@Test
	public void testSftpUrl() {
	    MicroTextTokenizer tok = new MicroTextTokenizer("the site Sftp://jtmmp.parc.xerox.com/Public has the file.");
	    List<LabeledToken> tokenList = tok.tokenize();
	    assertEquals("Sftp://jtmmp.parc.xerox.com/Public", tokenList.get(2).getOriginalWord());
	}

	@Test
	public void testIPAddrUrl() {
	    MicroTextTokenizer tok = new MicroTextTokenizer("get the file from http://13.101.3.213/tilde/k/kdent");
	    List<LabeledToken> tokenList = tok.tokenize();
	    assertEquals("http://13.101.3.213/tilde/k/kdent", tokenList.get(4).getOriginalWord());
	}

	@Test
	public void testUrlWithPort() {
        MicroTextTokenizer tok = new MicroTextTokenizer("get the file from http://13.101.3.213:8080/tilde/k/kdent");
        List<LabeledToken> tokenList = tok.tokenize();
        assertEquals("http://13.101.3.213:8080/tilde/k/kdent", tokenList.get(4).getOriginalWord());
	}
}
