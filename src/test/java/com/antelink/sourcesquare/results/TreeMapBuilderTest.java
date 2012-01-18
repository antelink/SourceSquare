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
package com.antelink.sourcesquare.results;

import static org.easymock.EasyMock.createMock;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.antelink.sourcesquare.TreemapNode;
import com.antelink.sourcesquare.event.base.EventBus;

public class TreeMapBuilderTest {

    private TreeMapBuilder builder;
    private EventBus eventBus;

    @Before
    public void init() {
        this.eventBus = createMock(EventBus.class);
        this.builder = new TreeMapBuilder(this.eventBus);
    }

    @Test
    public void testProcessOSFiles() {
        Set<TreemapNode> children = new HashSet<TreemapNode>();
        TreemapNode child1 = this.builder.createTreeMapNode("child1", null, 1);
        children.add(child1);
        TreemapNode child2 = this.builder.createTreeMapNode("child2", null, 2);
        children.add(child2);

        TreemapNode root = this.builder.createTreeMapNode("root", children, 3);

        HashMap<String, TreemapNode> childMap = new HashMap<String, TreemapNode>();

        for (TreemapNode childNode : root.getChildren()) {
            childMap.put(childNode.getName(), childNode);
        }

        Set<String> fileSet = new HashSet<String>();
        fileSet.add("child2" + File.separator + "toto");
        fileSet.add("child1" + File.separator + "toto");
        fileSet.add("root" + File.separator + "toto");

        this.builder.processOSFiles(fileSet);

        for (TreemapNode childNode : root.getChildren()) {
            childMap.put(childNode.getName(), childNode);
        }

        assertTrue(childMap.containsKey("child1"));
        assertTrue(childMap.containsKey("child2"));
        assertTrue(childMap.containsKey("root_"));

        assertThat(childMap.get("root_").getNbFiles(), is(3));
        assertThat(childMap.get("root_").getNbOSFiles(), is(1));

        assertThat(childMap.get("child1").getNbFiles(), is(1));
        assertThat(childMap.get("child1").getNbOSFiles(), is(1));

        assertThat(childMap.get("child2").getNbFiles(), is(2));
        assertThat(childMap.get("child2").getNbOSFiles(), is(1));

        this.builder.processFinalTreeMap(root);

        assertThat(root.getCumulatedOSFiles(), is(3));
        assertThat(root.getCumulatedFiles(), is(6));
        // assertThat(root.getData().get$color(), is("#71655E"));
    }

    @Test
    public void testCreateNode() {

        String path = "path";
        int nbFiles = 3;
        Set<TreemapNode> children = new HashSet<TreemapNode>();
        children.add(new TreemapNode("child1"));
        children.add(new TreemapNode("child2"));
        TreemapNode node = this.builder.createTreeMapNode(path, children, nbFiles);

        assertNotNull(node);
        assertThat(node.getName(), is("path"));
        assertThat(node.getNbFiles(), is(0));
        assertThat(node.getNbOSFiles(), is(0));
        assertThat(node.getChildren().size(), is(3));
        assertThat(node.getId(), is("0"));

        Iterator<TreemapNode> iterator = node.getChildren().iterator();
        int cpt = 0;
        while (iterator.hasNext()) {
            TreemapNode treemapNode = iterator.next();
            if (treemapNode.getName().equals("child1")) {
                assertThat(treemapNode.getNbFiles(), is(0));
                cpt++;
            }
            if (treemapNode.getName().equals("child2")) {
                assertThat(treemapNode.getNbFiles(), is(0));
                cpt++;
            }
            if (treemapNode.getName().equals("path_")) {
                assertThat(treemapNode.getNbFiles(), is(3));
                cpt++;
            }
        }
        assertThat(cpt, is(3));

        this.builder.processFinalTreeMap(node);

        assertThat(node.getData().get$area(), is(3));
        assertThat(node.getCumulatedFiles(), is(3));
    }

}
