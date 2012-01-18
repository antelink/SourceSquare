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

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class SourceSquareView extends JFrame {

    /**
     * 
     */
    private static final long serialVersionUID = 4204275094395452904L;
    private final JPanel contentPane;
    private final JTextField textField;
    private JPanel mainPanel;
    private final JLabel scanButtonLabel;
    private final JLabel selectButtonLabel;

    /**
     * Create the frame.
     */
    public SourceSquareView() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(
                SourceSquareView.class.getResource("/antelink.png")));
        setTitle("SourceSquare");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 443, 272);
        this.contentPane = new JPanel();
        this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.contentPane.setBackground(Color.WHITE);
        setContentPane(this.contentPane);

        this.mainPanel = new JPanel();
        this.mainPanel.setBackground(Color.WHITE);
        GroupLayout gl_contentPane = new GroupLayout(this.contentPane);
        gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
                .addComponent(this.mainPanel, GroupLayout.DEFAULT_SIZE, 428, Short.MAX_VALUE));
        gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
                .addComponent(this.mainPanel, GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE));
        this.mainPanel.setLayout(new BoxLayout(this.mainPanel, BoxLayout.Y_AXIS));

        Image logo;
        JPanel imgPanel = new JPanel();
        imgPanel.setBackground(Color.WHITE);
        this.mainPanel.add(imgPanel, "2, 4, 3, 1, fill, fill");
        logo = Toolkit.getDefaultToolkit().getImage(
                SourceSquareView.class.getResource("/logo-SourceSquare-400-117.png"));
        JLabel picLabel = new JLabel(new ImageIcon(logo));
        imgPanel.add(picLabel, "2, 2, fill, fill");

        JPanel top = new JPanel();
        top.setBackground(Color.WHITE);
        this.mainPanel.add(top);
        top.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("default:grow"), FormFactory.RELATED_GAP_COLSPEC,
                FormFactory.DEFAULT_COLSPEC, }, new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                RowSpec.decode("default:grow"), }));

        this.textField = new JTextField();
        this.textField.setText("Select a Directory to scan");
        top.add(this.textField, "2, 2, fill, default");
        this.textField.setColumns(10);

        Image selectButtonImage = Toolkit.getDefaultToolkit().getImage(
                SourceSquareView.class.getResource("/SelectButton.png"));
        this.selectButtonLabel = new JLabel(new ImageIcon(selectButtonImage));
        this.selectButtonLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        top.add(this.selectButtonLabel, "4, 2");

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        top.add(panel, "2, 4, 3, 1, fill, fill");

        Image scanButtonImage = Toolkit.getDefaultToolkit().getImage(
                SourceSquareView.class.getResource("/ScanButton.png"));
        this.scanButtonLabel = new JLabel(new ImageIcon(scanButtonImage));
        this.scanButtonLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panel.add(this.scanButtonLabel);

        JPanel bottom = new CopyrightPanel();
        bottom.setBackground(Color.WHITE);
        this.mainPanel.add(bottom);
        this.contentPane.setLayout(gl_contentPane);

        setLocationRelativeTo(null);
    }

    public JTextField getTextField() {
        return this.textField;
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

    public JLabel getScanButtonLabel() {
        return this.scanButtonLabel;
    }

    public JLabel getSelectButtonLabel() {
        return this.selectButtonLabel;
    }

}
