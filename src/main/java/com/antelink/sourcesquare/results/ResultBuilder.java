/**
 * Copyright (C) 2009-2011 Antelink SAS
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

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.antelink.sourcesquare.SourceSquareResults;
import com.antelink.sourcesquare.badge.BadgesProcessor;
import com.antelink.sourcesquare.event.base.EventBus;
import com.antelink.sourcesquare.event.events.ScanCompleteEvent;
import com.antelink.sourcesquare.event.events.SourceSquareResultsReadyEvent;
import com.antelink.sourcesquare.event.handlers.ScanCompleteEventHandler;

public class ResultBuilder {

    private static final Log logger = LogFactory.getLog(ResultBuilder.class);

    private static final long NODE_LIMIT = 700;
    private final EventBus eventBus;
    private final TreeMapBuilder treemap;
    private final BadgesProcessor badgesProcessor;

    private ArrayList<Integer> levels;

    public ResultBuilder(EventBus eventbus, TreeMapBuilder treemap) {
        this.eventBus = eventbus;
        this.treemap = treemap;
        this.badgesProcessor = new BadgesProcessor(eventbus);
        this.badgesProcessor.bind();
    }

    public void bind() {

        this.eventBus.addHandler(ScanCompleteEvent.TYPE, new ScanCompleteEventHandler() {

            @Override
            public String getId() {
                return getClass().getCanonicalName() + ": " + ScanCompleteEventHandler.class.getName();
            }

            @Override
            public void handle(ArrayList<Integer> levels) {
                logger.info("Process results");
                ResultBuilder.this.levels = levels;
                ResultBuilder.this.processResults();
            }
        });
    }

    private void analyzeTreeMapLevels(SourceSquareResults results) {
        long cumulated = 0;
        int nodeLevel = 0;
        for (int i = 0; i < this.levels.size(); i++) {
            cumulated = cumulated + this.levels.get(i);
            if (cumulated > NODE_LIMIT) {
                nodeLevel = i;
                break;
            } else {
                nodeLevel = i + 1;
            }
        }
        results.setNodeLevel(nodeLevel);
    }

    protected void processResults() {
        this.treemap.processFinalTreeMap(this.treemap.getRootNode());
        SourceSquareResults results = new SourceSquareResults();
        analyzeTreeMapLevels(results);
        results.setRootNode(this.treemap.getRootNode());
        results.setBadges(this.badgesProcessor.process());
        this.eventBus.fireEvent(new SourceSquareResultsReadyEvent(results));
    }
}
