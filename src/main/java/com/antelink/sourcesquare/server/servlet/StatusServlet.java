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
package com.antelink.sourcesquare.server.servlet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.antelink.sourcesquare.client.scan.ScanStatus;
import com.google.gson.Gson;

public class StatusServlet extends HttpServlet {

    private static final Log logger = LogFactory.getLog(StatusServlet.class);

    private static final long serialVersionUID = -424274857615445559L;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            Gson gson = new Gson();
            response.setContentType("application/json;charset=utf-8");
            int displayedFilesScanned = computeDisplayedFilesScanned();

            ScanStatus.INSTANCE.setDisplayedFilesScanned(displayedFilesScanned);
            ScanStatus status = ScanStatus.INSTANCE;
            response.getWriter().append(gson.toJson(status));
            logger.trace("replying application advance status");
        } catch (IOException e) {
            logger.debug("Error dispatching status", e);
        }
    }

    private int computeDisplayedFilesScanned() {
        long timeSinceStart = new Date().getTime() - ScanStatus.INSTANCE.getInitTime();
        long timeUntilLastScan = ScanStatus.INSTANCE.getLastUpdateTime()
                - ScanStatus.INSTANCE.getInitTime();
        if (timeUntilLastScan <= 0) {
            return ScanStatus.INSTANCE.getDisplayedFilesScanned();
        }
        int displayedfilesScanned = (int) (timeSinceStart * ScanStatus.INSTANCE.getNbFilesScanned() / timeUntilLastScan);
        this.logger.debug("timeSinceStart: " + timeSinceStart);
        this.logger.debug("timeUntilLastScan: " + timeUntilLastScan);
        this.logger.debug("NbFilesScanned: " + ScanStatus.INSTANCE.getNbFilesScanned());
        this.logger.debug("displayedFilesScanned: " + displayedfilesScanned);
        this.logger.debug("NbQueryingFiles: " + ScanStatus.INSTANCE.getNbQueryingFiles());
        if (displayedfilesScanned > ScanStatus.INSTANCE.getNbFilesScanned()
                + ScanStatus.INSTANCE.getNbQueryingFiles()
                || displayedfilesScanned > ScanStatus.INSTANCE.getNbFilesToScan()
                || displayedfilesScanned < ScanStatus.INSTANCE.getDisplayedFilesScanned()) {
            return ScanStatus.INSTANCE.getDisplayedFilesScanned();
        }
        return displayedfilesScanned;
    }
}
