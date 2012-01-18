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

import java.awt.EventQueue;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.antelink.sourcesquare.event.base.EventBus;
import com.antelink.sourcesquare.event.events.StartScanEvent;
import com.antelink.sourcesquare.gui.view.SourceSquareView;

public class SourceSquareController {
    private final SourceSquareView view;
    private final EventBus eventBus;
    private static final Log logger = LogFactory.getLog(SourceSquareController.class);

    public SourceSquareController(SourceSquareView view, EventBus eventBus) {
        super();
        this.view = view;
        this.eventBus = eventBus;
    }

    public void bind() {

        this.view.getSelectButtonLabel().addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mousePressed(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseExited(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseClicked(MouseEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileHidingEnabled(false);
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int showDialog = fileChooser.showDialog(SourceSquareController.this.view, "Select directory");
                if (showDialog == JFileChooser.APPROVE_OPTION) {
                    SourceSquareController.this.view.getTextField().setText(
                            fileChooser.getSelectedFile().getAbsolutePath());
                }

            }
        });

        this.view.getScanButtonLabel().addMouseListener(new MouseListener() {

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
                File toScan = new File(SourceSquareController.this.view.getTextField().getText());
                if (!toScan.exists() || toScan.list().length == 0) {
                    JOptionPane.showMessageDialog(SourceSquareController.this.view,
                            "Choose a valid and not empty directory", null, JOptionPane.ERROR_MESSAGE);
                    return;
                }
                SourceSquareController.this.view.setVisible(false);
                SourceSquareController.this.eventBus.fireEvent(new StartScanEvent(toScan));

            }
        });

    }

    public void display() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    SourceSquareController.this.view.setVisible(true);
                } catch (Exception e) {
                    logger.error("Error launching the UI", e);
                }
            }
        });
    }

}
