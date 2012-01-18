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
package com.antelink.sourcesquare.client.scan;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.web.client.RestClientException;

import com.antelink.sourcesquare.event.base.EventBus;
import com.antelink.sourcesquare.event.events.FilesScannedEvent;
import com.antelink.sourcesquare.event.events.OSFilesFoundEvent;
import com.antelink.sourcesquare.query.AntepediaQuery;
import com.antelink.sourcesquare.query.ResultEntry;

public class SourceSquareEngine {

    public static final Log logger = LogFactory.getLog(SourceSquareEngine.class);
    private final AntepediaQuery query;

    private final EventBus eventBus;

    public SourceSquareEngine(EventBus eventBus, AntepediaQuery query) {
        this.eventBus = eventBus;
        this.query = query;
    }

    public void discover(Map<String, String> files) throws RestClientException,
            JsonGenerationException, JsonMappingException, IOException {
        ScanStatus.INSTANCE.addNbQueryingFiles(files.size());
        List<ResultEntry> results = this.query.getResults(files);
        Map<String, ResultEntry> mappedResults = null;

        if (!results.isEmpty()) {
            logger.debug(results.size() + " results");
            mappedResults = mapResults(files, results);
            this.eventBus.fireEvent(new OSFilesFoundEvent(mappedResults.keySet()));
        }
        logger.debug("done with " + files.size() + " files");
        this.eventBus.fireEvent(new FilesScannedEvent(files.size()));
    }

    private Map<String, ResultEntry> mapResults(Map<String, String> files, List<ResultEntry> results) {
        HashMap<String, ResultEntry> processed = new HashMap<String, ResultEntry>();
        for (Entry<String, String> entry : files.entrySet()) {
            for (ResultEntry resultEntry : results) {
                if (entry.getValue().equals(resultEntry.getSha1())) {
                    processed.put(entry.getKey(), resultEntry);
                }
            }
        }
        return processed;
    }

    public void DummyPass(Exception e, int size) {
        logger.error("increasing file counter following an unrecoverable error...", e);
        this.eventBus.fireEvent(new FilesScannedEvent(size));
    }
}
