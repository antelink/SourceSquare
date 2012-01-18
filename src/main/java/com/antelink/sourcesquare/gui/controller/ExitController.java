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
package com.antelink.sourcesquare.gui.controller;

import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.antelink.sourcesquare.event.base.EventBus;
import com.antelink.sourcesquare.event.events.ErrorEvent;
import com.antelink.sourcesquare.event.events.StartScanEvent;
import com.antelink.sourcesquare.event.handlers.ErrorEventHandler;
import com.antelink.sourcesquare.event.handlers.StartScanEventHandler;
import com.antelink.sourcesquare.gui.view.ExitSourceSquareView;

public class ExitController {

    private final ExitSourceSquareView view;
    private EventBus eventBus;
    private static final Log logger = LogFactory.getLog(ExitController.class);

    public ExitController(ExitSourceSquareView view, EventBus eventBus) {
        super();
        this.view = view;
        this.setEventBus(eventBus);
    }

    public void bind() {

        this.view.getOpenButtonLabel().addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mousePressed(MouseEvent arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseExited(MouseEvent arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseEntered(MouseEvent arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseClicked(MouseEvent arg0) {
                try {
                    Desktop.getDesktop().browse(new URI("http://localhost:9524/"));
                } catch (IOException e) {
                    logger.error("Error opening the browser", e);
                } catch (URISyntaxException e) {
                    logger.error("Error opening the browser", e);
                }
            }
        });

        this.eventBus.addHandler(StartScanEvent.TYPE, new StartScanEventHandler() {

            @Override
            public String getId() {
                return "Exit Controller is now Handling";
            }

            @Override
            public void handle(File toScan) {
                display();
            }
        });
        this.eventBus.addHandler(ErrorEvent.TYPE, new ErrorEventHandler() {

            @Override
            public String getId() {
                return "Handling error";
            }

            @Override
            public void handle(String error) {
                JOptionPane.showMessageDialog(ExitController.this.view, error, null,
                        JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        });

    }

    public void display() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    ExitController.this.view.setVisible(true);
                } catch (Exception e) {
                    logger.error("Error launching the exit UI", e);
                }
            }
        });
    }

    public EventBus getEventBus() {
        return this.eventBus;
    }

    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }
}
