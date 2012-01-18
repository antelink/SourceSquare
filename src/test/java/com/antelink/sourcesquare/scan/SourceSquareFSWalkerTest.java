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
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;

import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;

import com.antelink.sourcesquare.client.scan.SourceSquareEngine;
import com.antelink.sourcesquare.client.scan.SourceSquareFSWalker;
import com.antelink.sourcesquare.event.base.EventBus;
import com.antelink.sourcesquare.event.events.FilesIdentifiedEvent;
import com.antelink.sourcesquare.results.TreeMapBuilder;

public class SourceSquareFSWalkerTest {

    private static final String RESOURCE_DIR = "src/test/resources";

    private SourceSquareFSWalker walker;
    private EventBus eventBus;
    private SourceSquareEngine engine;

    private TreeMapBuilder builder;

    @Before
    public void init() {
        this.engine = createMock(SourceSquareEngine.class);
        this.eventBus = createMock(EventBus.class);
        this.builder = new TreeMapBuilder(this.eventBus);
        this.walker = new SourceSquareFSWalker(this.engine, this.eventBus, this.builder);
    }

    @Test
    public void testIdentifyFiles() {

        File dir = new File(RESOURCE_DIR + "/toScan");

        Capture<FilesIdentifiedEvent> capturedFileEvent = new Capture<FilesIdentifiedEvent>();

        this.eventBus.fireEvent(capture(capturedFileEvent));
        expectLastCall();
        replay(this.eventBus);

        this.walker.identifyFiles(dir);

        assertThat(capturedFileEvent.getValue().getFileSet().size(), is(5));
        assertThat(this.builder.getRootNode().getName(), is(dir.getName()));
        assertThat(this.builder.getRootNode().getChildren().size(), is(3));
        assertThat(this.builder.getTreeMap().size(), is(6));

    }

}
