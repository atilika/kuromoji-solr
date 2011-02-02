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
import java.util.Map;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.solr.analysis.BaseTokenizerFactory;
import org.apache.solr.common.SolrException;
import org.atilika.kuromoji.Tokenizer.Mode;

public class KuromojiTokenizerFactory extends BaseTokenizerFactory{
	private static final String MODE = "mode";

	private static final String USER_DICT_PATH = "user-dictionary";

	private  org.atilika.kuromoji.Tokenizer tokenizer;

	@Override
	public void init(Map<String,String> args) {
		this.args = args;
		Mode mode = args.get(MODE) != null ? Mode.valueOf(args.get(MODE).toUpperCase()) : Mode.NORMAL;
		String userDictionaryPath = args.get(USER_DICT_PATH);
		this.tokenizer = org.atilika.kuromoji.Tokenizer.builder().mode(mode).userDictionary(userDictionaryPath).build();
	}

	@Override
	public Tokenizer create(Reader input) {
		try {
			return new KuromojiTokenizer(tokenizer, input);
		} catch (IOException e) {
			throw new SolrException(SolrException.ErrorCode.SERVER_ERROR, e);
		}
	}

}
