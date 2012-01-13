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
package com.antelink.sourcesquare.client.scan;

import java.text.NumberFormat;
import java.util.Collections;
import java.util.Vector;

public class ScanStatus {

    public static final ScanStatus INSTANCE = new ScanStatus();

    private int nbFilesToScan;
    private int nbOSFilesFound;
    private int nbFilesScanned;
    private String nbFilesToScanString;
    private String nbOSFilesFoundString;
    private String nbFilesScannedString;
    private Vector<Long> avgTimes;

    public enum ScanState {
        INITIALIZING, QUERYING, PROCESSING, COMPLETE;
    }

    private ScanState progressState = ScanState.INITIALIZING;

    private long cumulatedAvgTimes;

    private ScanStatus() {}

    public synchronized void addFilesScanned(int count) {
        setNbFilesScanned(this.nbFilesScanned + count);
    }

    public synchronized void addOSFiles(int count) {
        setNbOSFilesFound(this.nbOSFilesFound + count);
    }

    public synchronized int getNbFilesToScan() {
        return this.nbFilesToScan;
    }

    public synchronized void setNbFilesToScan(int nbFilesToScan) {
        this.nbFilesToScan = nbFilesToScan;
        this.nbFilesToScanString = NumberFormat.getInstance().format(this.nbFilesToScan);
    }

    public synchronized int getNbOSFilesFound() {
        return this.nbOSFilesFound;
    }

    public synchronized void setNbOSFilesFound(int nbOSFilesFound) {
        this.nbOSFilesFound = nbOSFilesFound;
        this.nbOSFilesFoundString = NumberFormat.getInstance().format(this.nbOSFilesFound);
    }

    public synchronized int getNbFilesScanned() {
        return this.nbFilesScanned;
    }

    public synchronized void setNbFilesScanned(int nbFilesScanned) {
        this.nbFilesScanned = nbFilesScanned;
        this.nbFilesScannedString = NumberFormat.getInstance().format(this.nbFilesScanned);
    }

    public synchronized boolean isComplete() {
        return this.progressState == ScanState.COMPLETE;
    }

    public synchronized ScanState getProgressState() {
        return this.progressState;
    }

    public synchronized void setProgressState(ScanState scanState) {
        this.progressState = scanState;
    }

    public synchronized void setComplete() {
        this.progressState = ScanState.COMPLETE;
    }

    public synchronized void setQuerying() {
        this.progressState = ScanState.QUERYING;
    }

    public synchronized void setProcessing() {
        this.progressState = ScanState.PROCESSING;
    }

    public String getNbFilesToScanString() {
        return this.nbFilesToScanString;
    }

    public void setNbFilesToScanString(String nbFilesToScanString) {
        this.nbFilesToScanString = nbFilesToScanString;
    }

    public String getNbOSFilesFoundString() {
        return this.nbOSFilesFoundString;
    }

    public void setNbOSFilesFoundString(String nbOSFilesFoundString) {
        this.nbOSFilesFoundString = nbOSFilesFoundString;
    }

    public String getNbFilesScannedString() {
        return this.nbFilesScannedString;
    }

    public void setNbFilesScannedString(String nbFilesScannedString) {
        this.nbFilesScannedString = nbFilesScannedString;
    }

    public synchronized Vector<Long> getAvgTimes() {
        if (this.avgTimes == null) {
            this.avgTimes = new Vector<Long>();
        }
        return this.avgTimes;
    }

    public synchronized void setAvgTimes(Vector<Long> avgTimes) {
        this.avgTimes = avgTimes;
    }

    public synchronized void addAvgTime(Long computeAvgTime) {
        getAvgTimes().add(computeAvgTime);
        Collections.sort(this.avgTimes);
        this.cumulatedAvgTimes += computeAvgTime;

    }

    public synchronized long getAverageTime() {
        return this.cumulatedAvgTimes / getAvgTimes().size();
    }

}
