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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.antelink.sourcesquare.TreemapNode;
import com.antelink.sourcesquare.event.base.EventBus;
import com.antelink.sourcesquare.event.events.ErrorEvent;
import com.antelink.sourcesquare.event.events.FilesIdentifiedEvent;
import com.antelink.sourcesquare.event.events.HiddenFileFoundEvent;
import com.antelink.sourcesquare.event.events.ScanCompleteEvent;
import com.antelink.sourcesquare.event.events.StartScanEvent;
import com.antelink.sourcesquare.event.handlers.FilesIdentifiedEventHandler;
import com.antelink.sourcesquare.event.handlers.StartScanEventHandler;
import com.antelink.sourcesquare.results.TreeMapBuilder;

public class SourceSquareFSWalker {

    private final static Log logger = LogFactory.getLog(SourceSquareFSWalker.class);

    private static final int MAX_FILE_PER_QUERY = 400;

    private static final int COMPUTE_WAIT_TIME = 1000;

    private final EventBus eventBus;

    private final TreeMapBuilder treemap;

    private int filePerQuery = 2;

    private final ArrayList<ProcessWorker> workers = new ArrayList<ProcessWorker>();

    /*contains number of folders per level*/
    private final ArrayList<Integer> levels;

    private final Object lock;

    private long total = 0;

    public SourceSquareFSWalker(SourceSquareEngine engine, EventBus eventBus, TreeMapBuilder treemap) {
        this.eventBus = eventBus;
        this.levels = new ArrayList<Integer>();
        this.lock = new Object();
        this.treemap = treemap;
        this.workers.add(new ProcessWorker(0, engine, this.lock));
        this.workers.add(new ProcessWorker(1, engine, this.lock));
        this.workers.add(new ProcessWorker(2, engine, this.lock));
    }

    public void bind() {
        this.eventBus.addHandler(StartScanEvent.TYPE, new StartScanEventHandler() {

            @Override
            public String getId() {
                return getClass().getCanonicalName() + ": " + StartScanEventHandler.class.getName();
            }

            @Override
            public void handle(File toScan) {
                logger.info("Counting files...");
                SourceSquareFSWalker.this.identifyFiles(toScan);
            }
        });

        this.eventBus.addHandler(FilesIdentifiedEvent.TYPE, new FilesIdentifiedEventHandler() {

            @Override
            public String getId() {
                return getClass().getCanonicalName() + ": " + StartScanEventHandler.class.getName();
            }

            @Override
            public void handle(TreeSet<File> fileSet) {
                try {
                    logger.info("Start scan for " + fileSet.size() + " files");
                    ScanStatus.INSTANCE.start();
                    SourceSquareFSWalker.this.queryFiles(fileSet);
                } catch (Exception e) {
                    logger.debug("Error handling tree identification", e);
                }

            }
        });
    }

    public synchronized void queryFiles(TreeSet<File> fileSet) throws InterruptedException {
        HashMap<String, String> toAnalyze = new HashMap<String, String>();
        Iterator<File> iterator = fileSet.iterator();
        logger.debug(fileSet.size() + " files to analyze");
        long count = 0;
        long timer = System.currentTimeMillis();
        while (iterator.hasNext()) {

            File file = iterator.next();
            logger.trace("adding analyze file to the pool: " + file.getAbsolutePath());
            try {
                String sha1 = FileAnalyzer.calculateHash("SHA-1", file);
                toAnalyze.put(file.getAbsolutePath(), sha1);
                count++;
            } catch (Exception e) {
                logger.error("skipping files " + file, e);
            }

            if (toAnalyze.size() == this.filePerQuery
                    || System.currentTimeMillis() - timer > COMPUTE_WAIT_TIME) {
                // dispatch analysis
                timer = System.currentTimeMillis();
                analyzeMap(toAnalyze);
                this.filePerQuery = Math.min(MAX_FILE_PER_QUERY, this.filePerQuery * 2);
                logger.trace("new counter: " + count);

            }
        }
        analyzeMap(toAnalyze);

        while (!allProcessDone()) {
            synchronized (this.lock) {
                this.lock.wait();
            }

        }
        this.eventBus.fireEvent(new ScanCompleteEvent(this.levels));

        logger.info("Analysis done " + count);
    }

    private synchronized void analyzeMap(HashMap<String, String> tempMap)
            throws InterruptedException {
        if (tempMap == null || tempMap.isEmpty()) {
            return;
        }
        ProcessWorker worker = null;
        while ((worker = getAvailableProcessor()) == null) {
            synchronized (this.lock) {
                this.lock.wait();
            }

        }
        worker.process(new HashMap<String, String>(tempMap));
        tempMap.clear();
    }

    public void identifyFiles(File directory) {

        logger.debug("counting files for: " + directory.getAbsolutePath());
        TreeSet<File> fileSet = new TreeSet<File>();
        try {
            TreemapNode root = reccursiveIdentifyFiles(directory, fileSet, 0);
            this.treemap.setRoot(root);
            this.eventBus.fireEvent(new FilesIdentifiedEvent(fileSet));
        } catch (OutOfMemoryError e) {
            this.eventBus
                    .fireEvent(new ErrorEvent(
                            "Out of memory: Try again with a smaller directory\nor change your JVM parameters."));
        }

    }

    private TreemapNode reccursiveIdentifyFiles(File directory, TreeSet<File> fileSet, int depth) {

        if (this.levels.size() < depth + 1) {
            this.levels.add(0);
        }

        logger.trace("Counting going down to directory : " + directory.getAbsolutePath());
        if (directory.isHidden()) {
            this.eventBus.fireEvent(new HiddenFileFoundEvent(directory));
            return null;
        }
        if (directory.isFile()) {
            fileSet.add(directory);
            return null;
        }
        if (!directory.isDirectory()) {
            return null;
        }
        // Protection if the directory forbids listing
        if (directory.listFiles() == null) {
            return null;
        }
        Set<TreemapNode> children = new HashSet<TreemapNode>();
        int nbFiles = 0;
        int nbDirs = 0;
        for (File child : directory.listFiles()) {
            if (child.isDirectory() || !child.isHidden()) {
                if (child.isDirectory()) {
                    nbDirs++;
                    TreemapNode childNode = reccursiveIdentifyFiles(child, fileSet, depth + 1);
                    if (childNode != null) {
                        children.add(childNode);
                    }
                } else if (child.isFile()) {
                    nbFiles++;
                    fileSet.add(child);
                }
            }
        }
        this.total = this.total + nbFiles;
        ScanStatus.INSTANCE.setNbFilesToScan((int) this.total);
        this.levels.set(depth, this.levels.get(depth) + nbDirs);
        return this.treemap.createTreeMapNode(directory.getAbsolutePath(), children, nbFiles);
    }

    private ProcessWorker getAvailableProcessor() {
        for (ProcessWorker worker : this.workers) {
            if (worker.isAvailable()) {
                return worker;
            }
        }
        return null;

    }

    private boolean allProcessDone() {
        for (ProcessWorker worker : this.workers) {
            if (!worker.isAvailable()) {
                return false;
            }
        }
        return true;
    }

}
