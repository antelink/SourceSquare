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
package com.antelink.sourcesquare;

import java.io.File;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.antelink.sourcesquare.client.scan.ScanStatus;
import com.antelink.sourcesquare.client.scan.ScanStatusManager;
import com.antelink.sourcesquare.client.scan.SourceSquareEngine;
import com.antelink.sourcesquare.client.scan.SourceSquareFSWalker;
import com.antelink.sourcesquare.event.base.EventBus;
import com.antelink.sourcesquare.event.events.StartScanEvent;
import com.antelink.sourcesquare.gui.controller.ExitController;
import com.antelink.sourcesquare.gui.controller.SourceSquareController;
import com.antelink.sourcesquare.gui.view.ExitSourceSquareView;
import com.antelink.sourcesquare.gui.view.SourceSquareView;
import com.antelink.sourcesquare.query.AntepediaQuery;
import com.antelink.sourcesquare.results.ResultBuilder;
import com.antelink.sourcesquare.results.TreeMapBuilder;
import com.antelink.sourcesquare.server.ServerController;

public class SourceSquare {

    private final static Log logger = LogFactory.getLog(SourceSquare.class);

    public static void main(String[] args) {

        logger.debug("starting.....");

        final EventBus eventBus = new EventBus();

        AntepediaQuery query = new AntepediaQuery();

        SourceSquareEngine engine = new SourceSquareEngine(eventBus, query);

        ScanStatusManager manager = new ScanStatusManager(eventBus);
        manager.bind();

        TreeMapBuilder treemap = new TreeMapBuilder(eventBus);
        treemap.bind();

        ResultBuilder builder = new ResultBuilder(eventBus, treemap);
        builder.bind();

        final SourceSquareFSWalker walker = new SourceSquareFSWalker(engine, eventBus, treemap);
        walker.bind();

        ServerController.bind(eventBus);
        if (args.length != 0) {
            final File toScan = new File(args[0]);

            if (!toScan.isDirectory()) {
                logger.error("The argument is not a directory");
                logger.info("exiting SourceSquare");
                System.exit(0);
            }

            eventBus.fireEvent(new StartScanEvent(toScan));

            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", "SourceSquare");

            logger.info("Scan complete");
            logger.info("Number of files to scan: " + ScanStatus.INSTANCE.getNbFilesToScan());
            logger.info("Number of files Scanned: " + ScanStatus.INSTANCE.getNbFilesScanned());
            logger.info("Number of files open source: " + ScanStatus.INSTANCE.getNbOSFilesFound());
        } else {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException e) {
                logger.info("Error launching the UI", e);
            } catch (InstantiationException e) {
                logger.info("Error launching the UI", e);
            } catch (IllegalAccessException e) {
                logger.info("Error launching the UI", e);
            } catch (UnsupportedLookAndFeelException e) {
                logger.info("Error launching the UI", e);
            }
            SourceSquareView view = new SourceSquareView();
            SourceSquareController controller = new SourceSquareController(view, eventBus);

            ExitSourceSquareView exitView = new ExitSourceSquareView();
            ExitController exitController = new ExitController(exitView, eventBus);

            exitController.bind();
            controller.bind();
            controller.display();
        }

    }
}
