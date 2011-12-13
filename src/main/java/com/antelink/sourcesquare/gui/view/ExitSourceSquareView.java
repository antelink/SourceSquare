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
package com.antelink.sourcesquare.gui.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.antelink.sourcesquare.server.ServerController;

public class ExitSourceSquareView extends JFrame {

    private final static Log logger = LogFactory.getLog(ExitSourceSquareView.class);

    /**
     * 
     */
    private static final long serialVersionUID = 4204275094395452904L;
    private final JPanel contentPane;
    private JPanel mainPanel;
    private final JLabel exitButtonLabel;

    /**
     * Create the frame.
     */
    public ExitSourceSquareView() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(SourceSquareView.class.getResource("/antelink.png")));
        setTitle("SourceSquare");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 500, 170);
        this.contentPane = new JPanel();
        this.contentPane.setBackground(Color.WHITE);
        this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(this.contentPane);

        this.mainPanel = new JPanel();
        this.mainPanel.setBackground(Color.WHITE);
        GroupLayout gl_contentPane = new GroupLayout(this.contentPane);
        gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addComponent(
                this.mainPanel, GroupLayout.DEFAULT_SIZE, 428, Short.MAX_VALUE));
        gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addComponent(
                this.mainPanel, GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE));
        this.mainPanel.setLayout(new BoxLayout(this.mainPanel, BoxLayout.Y_AXIS));

        JPanel panel_1 = new JPanel();
        panel_1.setBackground(Color.WHITE);
        this.mainPanel.add(panel_1);
        panel_1.setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel_1.add(panel, BorderLayout.SOUTH);

        Image exitButtonImage = Toolkit.getDefaultToolkit().getImage(
                SourceSquareView.class.getResource("/ExitButton.png"));
        this.exitButtonLabel = new JLabel(new ImageIcon(exitButtonImage));
        this.exitButtonLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panel.add(this.exitButtonLabel);

        JLabel exitLabel = new JLabel(
                "<html><b>Close this window or click \"Exit\" to quit the application.</b>\n</html>");
        exitLabel.setFont(new Font("Helvetica", Font.PLAIN, 13));

        JLabel browserLabel = new JLabel("<html>To see the results go to <a href=\"" + ServerController.URL + "\">"
                + ServerController.URL + "</a><br><br>\n</html>");
        browserLabel.setFont(new Font("Helvetica", Font.PLAIN, 13));
        browserLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        browserLabel.addMouseListener(new MouseListener() {

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
                    Desktop.getDesktop().browse(new URI("http://localhost:9524/"));
                } catch (IOException e) {
                    logger.error("Error opening the browser", e);
                } catch (URISyntaxException e) {
                    logger.error("Error opening the browser", e);
                }
            }
        });

        JPanel textPanel = new JPanel();
        textPanel.setBackground(Color.WHITE);
        textPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        textPanel.add(browserLabel, BorderLayout.CENTER);
        textPanel.add(exitLabel, BorderLayout.CENTER);

        panel_1.add(textPanel, BorderLayout.CENTER);

        CopyrightPanel copyrightPanel = new CopyrightPanel();
        copyrightPanel.setBackground(Color.WHITE);
        this.mainPanel.add(copyrightPanel);
        this.contentPane.setLayout(gl_contentPane);

        setLocationRelativeTo(null);
    }

    @Override
    public JPanel getContentPane() {
        return this.contentPane;
    }

    public JPanel getMainPanel() {
        return this.mainPanel;
    }

    public void setMainPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public JLabel getExitButtonLabel() {
        return this.exitButtonLabel;
    }

}
