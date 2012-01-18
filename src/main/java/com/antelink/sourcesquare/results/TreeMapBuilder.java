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

import java.io.File;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import com.antelink.sourcesquare.TreemapNode;
import com.antelink.sourcesquare.event.base.EventBus;
import com.antelink.sourcesquare.event.events.OSFilesFoundEvent;
import com.antelink.sourcesquare.event.handlers.OSFilesFoundEventHandler;

public class TreeMapBuilder {

    private final EventBus eventBus;
    private TreemapNode rootNode;
    private final Hashtable<String, TreemapNode> treeMap;

    private long countId = 0;

    public TreeMapBuilder(EventBus eventBus) {
        this.eventBus = eventBus;
        this.treeMap = new Hashtable<String, TreemapNode>();
    }

    public void bind() {

        this.eventBus.addHandler(OSFilesFoundEvent.TYPE, new OSFilesFoundEventHandler() {

            @Override
            public String getId() {
                return getClass().getCanonicalName() + ": " + OSFilesFoundEventHandler.class.getName();
            }

            @Override
            public void handle(Set<String> fileSet) {
                TreeMapBuilder.this.processOSFiles(fileSet);

            }
        });
    }

    protected void processOSFiles(Set<String> fileSet) {
        for (String path : fileSet) {
            getNodeForFile(path).incrementOSFile();
        }

    }

    private TreemapNode getNodeForFile(String path) {
        TreemapNode node = getNode(path.substring(0, path.lastIndexOf(File.separator)));
        if (node.getChildren().size() == 0) {
            return node;
        }
        return getNode(path.substring(0, path.lastIndexOf(File.separator)) + "_");
    }

    protected void processFinalTreeMap(TreemapNode node) {

        int cumulatedFiles = node.getNbFiles();
        int cumulatedOSFiles = node.getNbOSFiles();
        Iterator<TreemapNode> it = node.getChildren().iterator();
        while (it.hasNext()) {
            TreemapNode child = it.next();
            processFinalTreeMap(child);
            if (child.getCumulatedFiles() == 0) {
                it.remove();
                this.treeMap.remove(child.getName());
            }
            cumulatedFiles += child.getCumulatedFiles();
            cumulatedOSFiles += child.getCumulatedOSFiles();
        }
        node.setCumulatedFiles(cumulatedFiles);
        node.setCumulatedOSFiles(cumulatedOSFiles);
        node.processColor();
        node.processArea();

    }

    public TreemapNode createTreeMapNode(String path, Set<TreemapNode> children, int nbFiles) {
        TreemapNode node = getNode(path);
        if (children != null && !children.isEmpty()) {
            node.setChildren(children);
            TreemapNode fileSetNode = getNode(path + "_");
            fileSetNode.getData().setFileSet(true);
            fileSetNode.setNbFiles(nbFiles);
            node.addChild(fileSetNode);
        } else {
            node.setNbFiles(nbFiles);

        }
        return node;
    }

    private TreemapNode getNode(String path) {
        if (this.treeMap.keySet().contains(path)) {
            return this.treeMap.get(path);
        }

        TreemapNode node = new TreemapNode(path);
        node.setId(String.valueOf(getIdCount()));
        incrementIdCount();
        this.treeMap.put(path, node);
        return node;
    }

    private synchronized void incrementIdCount() {
        this.countId++;

    }

    private long getIdCount() {
        return this.countId;
    }

    public TreemapNode getRootNode() {
        return this.rootNode;
    }

    public void setRoot(TreemapNode root) {
        this.rootNode = root;

    }

    public Hashtable<String, TreemapNode> getTreeMap() {
        return this.treeMap;
    }

}
