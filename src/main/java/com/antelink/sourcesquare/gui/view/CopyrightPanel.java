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
package com.antelink.sourcesquare.gui.view;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CopyrightPanel extends JPanel {

    private final static Log logger = LogFactory.getLog(CopyrightPanel.class);

    /**
     * 
     */
    private static final long serialVersionUID = -2789554911632948675L;
    private final JLabel copyright;
    private final JLabel agreement;

    public CopyrightPanel() {
        super();
        this.copyright = new JLabel(
                "<html>Powered by <a href=\"\" style=\"cursor:pointer;\">Antepedia<a/>, an <a href=\"\" style=\"cursor:pointer;\">Antelink</a> product - </html>");
        this.copyright.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
        this.copyright.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.copyright.addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent arg0) {}

            @Override
            public void mousePressed(MouseEvent arg0) {}

            @Override
            public void mouseExited(MouseEvent arg0) {}

            @Override
            public void mouseEntered(MouseEvent arg0) {}

            @Override
            public void mouseClicked(MouseEvent arg0) {
                try {
                    Desktop.getDesktop().browse(new URI("http://www.antelink.com/"));
                } catch (IOException e) {
                    logger.error("Error opening the browser", e);
                } catch (URISyntaxException e) {
                    logger.error("Error opening the browser", e);
                }
            }
        });
        this.agreement = new JLabel("<html><a href=\"\">About SourceSquare</a></html>");
        this.agreement.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
        this.agreement.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.agreement.addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("https://sourcesquare.antepedia.com/about.html"));
                } catch (IOException ex) {
                    logger.error("Error opening the browser", ex);
                } catch (URISyntaxException ex) {
                    logger.error("Error opening the browser", ex);
                }
            }
        });
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        this.add(this.copyright);
        this.add(this.agreement);
        this.setSize(360, 25);
    }

    public JLabel getCopyright() {
        return this.copyright;
    }

    public JLabel getAgreement() {
        return this.agreement;
    }

}
