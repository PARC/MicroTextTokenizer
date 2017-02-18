package com.parc.chat.tokenizer;

import org.junit.Test;
import static org.junit.Assert.*;

public class EmoticonFSTTest {

	@Test
	public void simpleTest1() {
		int len = EmoticonFST.recognizeEmoticon(":-)");
		assertEquals(3, len);
	}

	@Test
	public void simpleTest2() {
		int len = EmoticonFST.recognizeEmoticon(":)");
		assertEquals(2, len);
	}

	@Test
	public void testNegCase1() {
		int len = EmoticonFST.recognizeEmoticon("Nothing to see here");
		assertEquals(0, len);
	}

	@Test
	public void testAtBeginingOfString() {
		int len = EmoticonFST.recognizeEmoticon(":-) followed by more");
		assertEquals(3, len);
	}


}
