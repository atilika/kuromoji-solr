/**
 * Copyright © 2010-2011 Atilika Inc.  All rights reserved.
 *
 * Atilika Inc. licenses this file to you under the Apache License, Version
 * 2.0 (the "License"); you may not use this file except in compliance with
 * the License.  A copy of the License is distributed with this work in the
 * LICENSE.txt file.  You may also obtain a copy of the License from
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
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
