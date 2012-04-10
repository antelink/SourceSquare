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
package com.antelink.sourcesquare.server;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.antelink.sourcesquare.SourceSquareResults;
import com.antelink.sourcesquare.TreemapNode;
import com.antelink.sourcesquare.badge.Badge;
import com.antelink.sourcesquare.client.scan.ScanStatus;
import com.antelink.sourcesquare.event.base.EventBus;
import com.antelink.sourcesquare.event.events.SourceSquareResultsReadyEvent;
import com.google.gson.Gson;

public class Simulator implements Runnable {

    public static final Log logger = LogFactory.getLog(Simulator.class);

    private final EventBus eventBus;

    public Simulator(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void run() {
        int nbFilesToScan = 101;
        ScanStatus.INSTANCE.setNbFilesToScan(nbFilesToScan);
        ScanStatus.INSTANCE.setNbFilesScanned(0);
        ScanStatus.INSTANCE.setNbOSFilesFound(0);
        ScanStatus.INSTANCE.setQuerying();
        for (int i = 0; i < nbFilesToScan; i++) {
            logger.debug("increasing the count by " + i);
            System.out.println(i);
            ScanStatus.INSTANCE.setNbFilesScanned(i);
            if (i - 100 > 0 && i % 8 == 0) {
                ScanStatus.INSTANCE.setNbOSFilesFound(i - 100);
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        StringBuilder builder = new StringBuilder();
        try {
            List<String> object = FileUtils.readLines(new File("src/test/resources/json.txt"));
            for (String o : object) {
                builder.append(o);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String deserialize = builder.toString();
        Gson gson = new Gson();
        TreemapNode root = gson.fromJson(deserialize, TreemapNode.class);
        SourceSquareResults result = new SourceSquareResults();
        result.setRootNode(root);
        List<Badge> badges = new ArrayList<Badge>();
        badges.add(Badge.OS_BLACKBELT);
        badges.add(Badge.OLD);
        result.setBadges(badges);
        result.setNodeLevel(3);
        this.eventBus.fireEvent(new SourceSquareResultsReadyEvent(result));
        ScanStatus.INSTANCE.setComplete();
    }
}
