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
package com.antelink.sourcesquare.scan;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;

import com.antelink.sourcesquare.client.scan.SourceSquareEngine;
import com.antelink.sourcesquare.event.base.EventBus;
import com.antelink.sourcesquare.event.events.FilesScannedEvent;
import com.antelink.sourcesquare.event.events.OSFilesFoundEvent;
import com.antelink.sourcesquare.query.AntepediaQuery;
import com.antelink.sourcesquare.query.ResultEntry;

public class SourceSquareEngineTest {

    private SourceSquareEngine engine;
    private AntepediaQuery query;
    private EventBus eventbus;

    @Before
    public void init() {
        this.eventbus = createMock(EventBus.class);
        this.query = createMock(AntepediaQuery.class);
        this.engine = new SourceSquareEngine(this.eventbus, this.query);
    }

    @Test
    public void testDiscover() throws Exception {

        Map<String, String> files = new HashMap<String, String>();
        files.put("toto", "totoSha1");
        files.put("tata", "tataSha1");
        files.put("titi", "titiSha1");
        files.put("plop", "plopSha1");

        List<ResultEntry> results = new ArrayList<ResultEntry>();
        ResultEntry totoResult = new ResultEntry("totoSha1");
        results.add(totoResult);

        ResultEntry tataResult = new ResultEntry("tataSha1");
        results.add(tataResult);

        Capture<OSFilesFoundEvent> capturedOSEvent = new Capture<OSFilesFoundEvent>();

        Capture<FilesScannedEvent> capturedScannedEvent = new Capture<FilesScannedEvent>();

        expect(this.query.getResults(files)).andReturn(results);
        this.eventbus.fireEvent(capture(capturedOSEvent));
        expectLastCall();
        this.eventbus.fireEvent(capture(capturedScannedEvent));
        expectLastCall();
        replay(this.query, this.eventbus);

        this.engine.discover(files);

        assertThat(capturedOSEvent.getValue().getFileSet().size(), is(2));
        assertThat(capturedScannedEvent.getValue().getCount(), is(4));
    }

}
