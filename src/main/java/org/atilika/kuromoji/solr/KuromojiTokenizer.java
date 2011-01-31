package org.atilika.kuromoji.solr;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.atilika.kuromoji.Token;

public class KuromojiTokenizer extends Tokenizer{
	private final TermAttribute termAtt = (TermAttribute) addAttribute(TermAttribute.class);

	private final OffsetAttribute offsetAtt = (OffsetAttribute) addAttribute(OffsetAttribute.class);
	
	private final TypeAttribute typeAtt = (TypeAttribute) addAttribute(TypeAttribute.class);

	private final org.atilika.kuromoji.Tokenizer tokenizer;
	
	private String str;
	
	private List<Token> tokens;
	
	private int tokenIndex = 0;

	public KuromojiTokenizer(org.atilika.kuromoji.Tokenizer tokenizer, Reader input) throws IOException {
		super(input);
		this.tokenizer = tokenizer;
		str = IOUtils.toString(input);
		init();
	}
	
	private void init() {
		tokenIndex = 0;
		tokens = tokenizer.tokenize(str);
	}

	@Override
	public boolean incrementToken() {
		if(tokenIndex == tokens.size()) {
			return false;
		}
		
		Token token = tokens.get(tokenIndex);
		String surfaceForm = token.getSurfaceForm();
		int position = token.getPosition();
		int length = surfaceForm.length();
		
		termAtt.setTermBuffer(str, position, length);
		offsetAtt.setOffset(correctOffset(position), correctOffset(position + length));
		typeAtt.setType(token.getPartOfSpeech());
		tokenIndex++;
		return true;
	}
	
	@Override
	public void end() {
		final int ofs = correctOffset(str.length());
		offsetAtt.setOffset(ofs, ofs);
	}
	
	@Override
	public void reset(Reader input) throws IOException{
		super.reset(input);
		str = IOUtils.toString(input);
		init();
	}

}
