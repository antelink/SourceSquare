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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Ignore;
import org.junit.Test;

import com.antelink.sourcesquare.TreemapNode;

@Ignore
public class TreemapNodeTest {
    @Test
    public void testProcessColor_100() {
        TreemapNode treemapNode = new TreemapNode("path");
        treemapNode.getData().set$area(100);
        treemapNode.setCumulatedOSFiles(100);
        treemapNode.processColor();
        assertThat(treemapNode.getData().get$color(), is("#FFFFFF"));
    }

    @Test
    public void testProcessColor_0() {
        TreemapNode treemapNode = new TreemapNode("path");
        treemapNode.getData().set$area(100);
        treemapNode.setCumulatedOSFiles(0);
        treemapNode.processColor();
        assertThat(treemapNode.getData().get$color(), is("#000000"));
    }

    @Test
    public void testProcessColor_50() {
        TreemapNode treemapNode = new TreemapNode("path");
        treemapNode.getData().set$area(19);
        treemapNode.setCumulatedOSFiles(14);
        treemapNode.processColor();
        assertThat(treemapNode.getData().get$color(), is("#7F7F7F"));
    }
}
