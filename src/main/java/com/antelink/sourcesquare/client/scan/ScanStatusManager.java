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

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.antelink.sourcesquare.event.base.EventBus;
import com.antelink.sourcesquare.event.events.FilesIdentifiedEvent;
import com.antelink.sourcesquare.event.events.FilesScannedEvent;
import com.antelink.sourcesquare.event.events.OSFilesFoundEvent;
import com.antelink.sourcesquare.event.events.ScanCompleteEvent;
import com.antelink.sourcesquare.event.handlers.FilesIdentifiedEventHandler;
import com.antelink.sourcesquare.event.handlers.FilesScannedEventHandler;
import com.antelink.sourcesquare.event.handlers.OSFilesFoundEventHandler;
import com.antelink.sourcesquare.event.handlers.ScanCompleteEventHandler;
import com.antelink.sourcesquare.event.handlers.StartScanEventHandler;

public class ScanStatusManager {

    private final static Log logger = LogFactory.getLog(ScanStatusManager.class);

    private final EventBus eventbus;

    public ScanStatusManager(EventBus eventbus) {
        super();
        this.eventbus = eventbus;

    }

    public void bind() {

        this.eventbus.addHandler(FilesIdentifiedEvent.TYPE, new FilesIdentifiedEventHandler() {

            @Override
            public String getId() {
                return getClass().getCanonicalName() + ": " + StartScanEventHandler.class.getName();
            }

            @Override
            public void handle(TreeSet<File> fileSet) {
                ScanStatus.INSTANCE.setNbFilesToScan(fileSet.size());
                ScanStatus.INSTANCE.setQuerying();
            }
        });

        this.eventbus.addHandler(ScanCompleteEvent.TYPE, new ScanCompleteEventHandler() {

            @Override
            public String getId() {
                return getClass().getCanonicalName() + ": "
                        + ScanCompleteEventHandler.class.getName();
            }

            @Override
            public void handle(ArrayList<Integer> levels) {
                logger.info("Scan finished, processing results...");
                ScanStatus.INSTANCE.setProcessing();
            }
        });

        this.eventbus.addHandler(FilesScannedEvent.TYPE, new FilesScannedEventHandler() {

            @Override
            public String getId() {
                return getClass().getCanonicalName() + ": "
                        + FilesScannedEventHandler.class.getName();
            }

            @Override
            public void handle(int count) {
                ScanStatus.INSTANCE.addFilesScanned(count);
                long averageTime = ScanStatusManager.this.computeAverageTime();
                ScanStatus.INSTANCE.setAverageScanningTime(averageTime);
                ScanStatus.INSTANCE.setLastUpdateTime(new Date().getTime());
                logger.info("Finish scan for " + count + " files, average scanning time: "
                        + averageTime + " ms");

            }

        });

        this.eventbus.addHandler(OSFilesFoundEvent.TYPE, new OSFilesFoundEventHandler() {

            @Override
            public String getId() {
                return getClass().getCanonicalName() + ": "
                        + OSFilesFoundEventHandler.class.getName();
            }

            @Override
            public void handle(Set<String> fileSet) {
                logger.info(fileSet.size() + " open source files found");
                ScanStatus.INSTANCE.addOSFiles(fileSet.size());
            }
        });

    }

    protected long computeAverageTime() {
        return (new Date().getTime() - ScanStatus.INSTANCE.getInitTime())
                / ScanStatus.INSTANCE.getNbFilesScanned();
    }
}
