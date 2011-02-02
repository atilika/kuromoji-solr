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
