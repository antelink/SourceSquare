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
import java.util.ArrayList;

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

    public CopyrightPanel() {
        super();

        ArrayList<JLabel> sentence = new ArrayList<JLabel>();
        sentence.add(new JLabel("Powered by"));
        sentence.add(createJLabelWithHyperlink("Antepedia", "http://www.antepedia.com"));
        sentence.add(new JLabel(", an "));
        sentence.add(createJLabelWithHyperlink("Antelink", "http://www.antelink.com"));
        sentence.add(new JLabel(" product - "));
        sentence.add(createJLabelWithHyperlink("About SourceSquare",
                "https://sourcesquare.antepedia.com/about.html"));

        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        for (JLabel jLabel : sentence) {
            jLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
            this.add(jLabel);
        }
        this.setSize(360, 25);
    }

    private JLabel createJLabelWithHyperlink(String text, final String href) {
        JLabel label = new JLabel("<html><a href=\"\">" + text + "<a/></html>");
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label.addMouseListener(new MouseListener() {

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
                    Desktop.getDesktop().browse(new URI(href));
                } catch (IOException e) {
                    logger.error("Error opening the browser", e);
                } catch (URISyntaxException e) {
                    logger.error("Error opening the browser", e);
                }
            }
        });
        return label;
    }

}
