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

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * This class is entitled to execute a query pack (a query pack is a set of
 * query requests to the Antepedia API). It uses the "worker" design pattern. It
 * launches and monitors the execution of a thread that performs the queries.
 * This worker is equipped with a retry mechanism.
 * 
 * @author Freddy Munoz, PhD Antelink S.A.S
 * 
 */
public class ProcessWorker {

    private final SourceSquareEngine engine;

    private static final Log logger = LogFactory.getLog(ProcessWorker.class);
    /**
     * current worker identifier
     */
    private final int id;

    /**
     * determines whether the worker is available or note
     */
    private boolean status = true;

    /**
     * number of retries before the worker gives up
     */
    private static int RETRIES = 3;

    private final Object lock;

    private Thread executor;

    /**
     * 
     * @param i
     *            id of this worker
     * @param engine
     *            external communication engine
     * @param lock
     *            lock to be notified when this engine finishes its work.
     */
    public ProcessWorker(int i, SourceSquareEngine engine, Object lock) {
        this.engine = engine;
        this.lock = lock;
        this.id = i;
    }

    /**
     * Launches the process of a set of files.
     * 
     * @param tempMap
     *            a map containing a set of tuples (filename,hash)
     */
    public synchronized void process(final HashMap<String, String> tempMap) {

        this.status = false;

        logger.debug("Worker " + this.id + " analyzing files " + tempMap.values());

        Runnable runner = new Runnable() {

            @Override
            public void run() {
                try {
                    analyzeMapWithCount(0, tempMap);
                } catch (Exception e) {
                    logger.error("Error while processing", e);
                } finally {
                    ProcessWorker.this.status = true;
                    synchronized (ProcessWorker.this.lock) {
                        ProcessWorker.this.lock.notifyAll();
                    }
                }
            }
        };
        this.executor = new Thread(runner);
        this.executor.start();
    }

    private void analyzeMapWithCount(int times, HashMap<String, String> tempMap) throws Exception {
        try {
            logger.debug("pass " + times + ": analyzing files " + tempMap.values());
            this.engine.discover(tempMap);
        } catch (Exception e) {
            logger.debug("Error while analyzing", e);
            if (times > RETRIES) {
                logger.error(RETRIES + " time retry done... giving up!");
                // do as if the result where found
                this.engine.DummyPass(e, tempMap.size());
                throw e;
            } else {
                logger.debug("retrying...");
                analyzeMapWithCount(times + 1, tempMap);
            }
        }
    }

    public synchronized boolean isAvailable() {
        return this.status;
    }

    public synchronized Thread getExecutor() {
        return this.executor;
    }

}
