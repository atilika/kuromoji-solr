package org.atilika.kuromoji.lucene;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.atilika.kuromoji.solr.KuromojiTokenizer;

public class KuromojiAnalyzer extends Analyzer {
	org.atilika.kuromoji.Tokenizer tokenizer;

	public KuromojiAnalyzer(org.atilika.kuromoji.Tokenizer tokenizer) {
		this.tokenizer = tokenizer;
	}
	
	@Override
	public TokenStream tokenStream(String fieldName, Reader input) {
		try {
			return new KuromojiTokenizer(this.tokenizer, input);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public TokenStream reusableTokenStream(String fieldName, Reader input) throws IOException {
		Tokenizer tokenizer = (Tokenizer) getPreviousTokenStream();
		if (tokenizer == null) {
			tokenizer = new KuromojiTokenizer(this.tokenizer, input);
			setPreviousTokenStream(tokenizer);
		} else
			tokenizer.reset(input);
		return tokenizer;
	}
}
