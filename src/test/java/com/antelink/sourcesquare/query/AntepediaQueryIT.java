/**
 * Copyright (C) 2009-2012 Antelink SAS
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License Version 3 as published
 * by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License Version 3
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License Version
 * 3 along with this program. If not, see http://www.gnu.org/licenses/agpl.html
 *
 * Additional permission under GNU AGPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or combining it with
 * Eclipse Java development tools (JDT) or Jetty (or a modified version of these
 * libraries), containing parts covered by the terms of Eclipse Public License 1.0,
 * the licensors of this Program grant you additional permission to convey the
 * resulting work. Corresponding Source for a non-source form of such a combination
 * shall include the source code for the parts of Eclipse Java development tools
 * (JDT) or Jetty used as well as that of the covered work.
 */
package com.antelink.sourcesquare.query;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.antelink.sourcesquare.query.AntepediaQuery;
import com.antelink.sourcesquare.query.ResultEntry;

public class AntepediaQueryIT {

    private AntepediaQuery query;

    @Before
    public void init() {
        this.query = new AntepediaQuery();
    }

    @Test
    public void testAnswerNotFound() throws Exception {

        Map<String, String> files = new HashMap<String, String>();
        files.put("toto", "632a990d82e7381e7afeeb3478342d17e9dfb835");
        List<ResultEntry> results = this.query.getResults(files);

        assertNotNull(results);
        assertThat(results.size(), is(0));
    }

    @Test
    public void testAnswerFound() throws Exception {

        Map<String, String> files = new HashMap<String, String>();
        files.put("toto", "5394464a808b908dfd0e5b1bcde5d0a9c712c3f5");
        List<ResultEntry> results = this.query.getResults(files);

        assertNotNull(results);
        assertThat(results.size(), is(1));
    }

}
