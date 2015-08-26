package gov.nyc.doitt.gis.geoclient.parser.regex;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import gov.nyc.doitt.gis.geoclient.parser.AbstractSpecTest;
import gov.nyc.doitt.gis.geoclient.parser.Input;
import gov.nyc.doitt.gis.geoclient.parser.ParseContext;
import gov.nyc.doitt.gis.geoclient.parser.regex.ZipParser;
import gov.nyc.doitt.gis.geoclient.parser.token.Chunk;
import gov.nyc.doitt.gis.geoclient.parser.token.ChunkType;
import gov.nyc.doitt.gis.geoclient.parser.token.Token;
import gov.nyc.doitt.gis.geoclient.parser.token.TokenType;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZipParserTest extends AbstractSpecTest 
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ZipParserTest.class);
	private ZipParser parser;

	@Before
	public void setUp() throws Exception
	{
		parser = new ZipParser();
	}
	
	@Test
	public void testTokens()
	{
		testParser(parser, LOGGER);
	}
	
	@Test
	public void testParseSuccessChangesCurrentChunk()
	{
		String originalText = "280 Riverside Dr NY NY 10025 ";
		ParseContext context = new ParseContext(new Input("zip-chunk-state-change1", originalText));
		Chunk initialChunk = context.getCurrent();
		assertThat(initialChunk.getType(), is(equalTo(ChunkType.ORIGINAL_INPUT)));
		parser.parse(context);
		assertThat("ParseContext.isParsed should be",context.isParsed(),is(false));
		assertThat("Chunk.Type of initial Chunk:",initialChunk.getType(), is(equalTo(ChunkType.COUNTY)));
		assertThat("Initial Chunk contains:",initialChunk.contains(new Token(TokenType.ZIP, "10025", 23, 28)), is(true));		
		assertThat("Initial Chunk token count:", initialChunk.tokenCount(),is(1));
		Chunk actualChunk = context.getCurrent();
		assertThat(actualChunk.getType(), is(equalTo(ChunkType.SUBSTRING)));
		assertThat(actualChunk.getText(),is(equalTo("280 Riverside Dr NY NY")));
	}

}
