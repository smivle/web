/*
 * Copyright 2016 Dmitriy Malakhov.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.stagirs.search;

import com.github.stagirs.common.text.TextUtils;
import java.util.List;

/**
 *
 * @author Dmitriy Malakhov
 */
public class Query {
    private String[][] terms;

    public Query(String query) {
        List<String> words = TextUtils.splitWords(query, true);
        terms = new String[words.size()][1];
        for (int i = 0; i < terms.length; i++) {
            terms[i][0] = words.get(i);
        }
    }

    public String[][] getTerms() {
        return terms;
    }

    
}
