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
package com.antelink.sourcesquare.results;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;

import org.junit.Ignore;
import org.junit.Test;

import com.antelink.sourcesquare.SourceSquareResults;
import com.antelink.sourcesquare.client.scan.SourceSquareEngine;
import com.antelink.sourcesquare.client.scan.SourceSquareFSWalker;
import com.antelink.sourcesquare.event.base.EventBus;
import com.antelink.sourcesquare.event.events.SourceSquareResultsReadyEvent;
import com.antelink.sourcesquare.event.events.StartScanEvent;
import com.antelink.sourcesquare.event.handlers.SourceSquareResultsReadyEventHandler;
import com.antelink.sourcesquare.query.AntepediaQuery;
import com.antelink.sourcesquare.results.TreeMapBuilder;

public class TreeMapBuilderIT {

    private static final String RESOURCE_DIR = "src/test/resources";

    private boolean stopped;

    @Test
    @Ignore
    public void testGenerateTreeMap() throws InterruptedException {

        final EventBus eventBus = new EventBus();

        AntepediaQuery query = new AntepediaQuery();

        SourceSquareEngine engine = new SourceSquareEngine(eventBus, query);

        TreeMapBuilder builder = new TreeMapBuilder(eventBus);
        builder.bind();

        final SourceSquareFSWalker walker = new SourceSquareFSWalker(engine, eventBus, builder);
        walker.bind();

        File dir = new File(RESOURCE_DIR + "/toScan");

        eventBus.addHandler(SourceSquareResultsReadyEvent.TYPE, new SourceSquareResultsReadyEventHandler() {

            @Override
            public String getId() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void handle(SourceSquareResults results) {
                System.out.println("root: " + results.getRootNode().getName());
                assertThat(results.getRootNode().getData().get$area(), is(5));
                TreeMapBuilderIT.this.stopped = true;
            }
        });

        eventBus.fireEvent(new StartScanEvent(dir));
        this.stopped = false;

        while (!this.stopped) {
            Thread.sleep(100);
        }
    }

}
