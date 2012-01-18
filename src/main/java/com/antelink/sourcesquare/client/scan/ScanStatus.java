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

import java.text.NumberFormat;
import java.util.Date;

public class ScanStatus {

    public static final ScanStatus INSTANCE = new ScanStatus();

    private int nbFilesToScan;
    private int nbOSFilesFound;
    private int nbFilesScanned;
    private String nbOSFilesFoundString = "0";
    private String displayedFilesScannedString = "0";
    private String nbFilesToScanString = "0";
    private long averageScanningTime;
    private long initTime;
    private int displayedFilesScanned;
    private long lastUpdateTime;
    private int nbQueryingFiles;

    public enum ScanState {
        INITIALIZING, QUERYING, PROCESSING, COMPLETE;
    }

    private ScanState progressState = ScanState.INITIALIZING;

    private ScanStatus() {}

    public synchronized void addFilesScanned(int count) {
        setNbFilesScanned(this.nbFilesScanned + count);
        removeNbQueryingFiles(count);
    }

    public synchronized void addOSFiles(int count) {
        setNbOSFilesFound(this.nbOSFilesFound + count);
    }

    public synchronized int getNbFilesToScan() {
        return this.nbFilesToScan;
    }

    public synchronized void setNbFilesToScan(int nbFilesToScan) {
        this.nbFilesToScan = nbFilesToScan;
        setNbFilesToScanString(NumberFormat.getInstance().format(this.nbFilesToScan));
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

    public String getNbOSFilesFoundString() {
        return this.nbOSFilesFoundString;
    }

    public void setNbOSFilesFoundString(String nbOSFilesFoundString) {
        this.nbOSFilesFoundString = nbOSFilesFoundString;
    }

    public synchronized long getAverageScanningTime() {
        return this.averageScanningTime;
    }

    public synchronized void setAverageScanningTime(long averageScanningTime) {
        this.averageScanningTime = averageScanningTime;
    }

    public synchronized long getInitTime() {
        return this.initTime;
    }

    public synchronized void setInitTime(long initTime) {
        this.initTime = initTime;
    }

    public void start() {
        setInitTime(new Date().getTime());
    }

    public synchronized int getDisplayedFilesScanned() {
        return this.displayedFilesScanned;
    }

    public synchronized void setDisplayedFilesScanned(int displayedFilesScanned) {
        this.displayedFilesScanned = displayedFilesScanned;
        this.displayedFilesScannedString = NumberFormat.getInstance().format(
                this.displayedFilesScanned);
    }

    public synchronized long getLastUpdateTime() {
        return this.lastUpdateTime;
    }

    public synchronized void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public synchronized String getDisplayedFilesScannedString() {
        return this.displayedFilesScannedString;
    }

    public synchronized void setDisplayedFilesScannedString(String displayedFilesScannedString) {
        this.displayedFilesScannedString = displayedFilesScannedString;
    }

    public synchronized int getNbQueryingFiles() {
        return this.nbQueryingFiles;
    }

    public synchronized void setNbQueryingFiles(int nbQueryingFiles) {
        this.nbQueryingFiles = nbQueryingFiles;
    }

    public synchronized void addNbQueryingFiles(int nbQueryingFiles) {
        this.nbQueryingFiles += nbQueryingFiles;
    }

    public synchronized void removeNbQueryingFiles(int nbQueryingFiles) {
        this.nbQueryingFiles -= nbQueryingFiles;
    }

    public synchronized String getNbFilesToScanString() {
        return this.nbFilesToScanString;
    }

    public synchronized void setNbFilesToScanString(String nbFilesToScanString) {
        this.nbFilesToScanString = nbFilesToScanString;
    }

}
