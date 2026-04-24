package gabywald.global.data.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gabywald.global.data.TextualInfoFile;

class TextualInfoFileTests {

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void test() {
		// TextualInfoFile tif = new TextualInfoFile("resources/conf/index.conf");
		Assertions.assertEquals("conf/textualInfos/", TextualInfoFile.getContext(0).toString() );
		// Assertions.assertEquals("conf/textualInfos/1.txt", TextualInfoFile.getContext(1).toString() );
		Assertions.assertNull( TextualInfoFile.getContext(1) );
		Assertions.assertNull( TextualInfoFile.getContext(2) );
		Assertions.assertNull( TextualInfoFile.getContext(3) );
		Assertions.assertNull( TextualInfoFile.getContext(4) );
		Assertions.assertNull( TextualInfoFile.getContext(5) );
		Assertions.assertNull( TextualInfoFile.getContext(6) );
	}

}
