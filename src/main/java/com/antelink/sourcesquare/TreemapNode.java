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
import java.text.NumberFormat;
import java.util.HashSet;
import java.util.Set;

public class TreemapNode {

    public static class Data {
        private int $area;
        private String $color;
        private boolean isFileSet;

        public int get$area() {
            return this.$area;
        }

        public void set$area(int $area) {
            this.$area = $area;
        }

        public String get$color() {
            return this.$color;
        }

        public void set$color(String $color) {
            this.$color = $color;
        }

        public boolean isFileSet() {
            return this.isFileSet;
        }

        public void setFileSet(boolean isFileSet) {
            this.isFileSet = isFileSet;
        }
    }

    private Set<TreemapNode> children;
    private int nbFiles;
    private String name;
    private int nbOSFiles;
    private Data data;
    private String id;
    private int cumulatedOSFiles;
    private int cumulatedFiles;

    public TreemapNode(String name) {
        setName(name);
        this.data = new Data();
        this.nbFiles = 0;
        this.nbOSFiles = 0;
        this.cumulatedFiles = 0;
        this.cumulatedOSFiles = 0;
    }

    public Set<TreemapNode> getChildren() {
        if (this.children == null) {
            this.children = new HashSet<TreemapNode>();
        }
        return this.children;
    }

    public void addChild(TreemapNode child) {
        this.getChildren().add(child);
    }

    public int getNbFiles() {
        return this.nbFiles;
    }

    public void setNbFiles(int nbFiles) {
        this.nbFiles = nbFiles;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = isPath(name) ? extractFileName(name) : name;
    }

    private String extractFileName(String path) {
        return path.substring(path.lastIndexOf(File.separator) + 1);
    }

    private boolean isPath(String name) {
        return name.contains(File.separator) && name.length() > 1;
    }

    public int getNbOSFiles() {
        return this.nbOSFiles;
    }

    public void setNbOSFiles(int nbOSFiles) {
        this.nbOSFiles = nbOSFiles;
    }

    public void setChildren(Set<TreemapNode> children) {
        this.children = children;
    }

    public Data getData() {
        return this.data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void incrementOSFile() {
        this.nbOSFiles++;

    }

    public void setCumulatedOSFiles(int nbOSFiles) {
        this.cumulatedOSFiles = nbOSFiles;

    }

    public int getCumulatedOSFiles() {
        return this.cumulatedOSFiles;
    }

    public void setCumulatedOSFilesString(String nbOSFiles) {
        this.cumulatedOSFiles = Integer.parseInt(nbOSFiles);

    }

    public String getCumulatedOSFilesString() {
        return NumberFormat.getInstance().format(this.cumulatedOSFiles);
    }

    public void processColor() {
        double openSourcePercent = this.cumulatedFiles == 0.0 ? 0.0 : (double) this.cumulatedOSFiles / (double) this.cumulatedFiles;
        Color color = Color.MIN.plus(Color.MAX.minus(Color.MIN).times(openSourcePercent));
        getData().set$color(color.encode());

    }

    public void processArea() {
        this.data.$area = this.cumulatedFiles;

    }

    public int getCumulatedFiles() {
        return this.cumulatedFiles;
    }

    public void setCumulatedFiles(int cumulatedFiles) {
        this.cumulatedFiles = cumulatedFiles;
    }

    public String getCumulatedFilesString() {
        return NumberFormat.getInstance().format(this.cumulatedFiles);
    }

    public void setCumulatedFilesString(String cumulatedFiles) {
        this.cumulatedFiles = Integer.parseInt(cumulatedFiles);
    }

    public void removeChild(TreemapNode child) {
        this.children.remove(child);

    }


	public void clean() {
		this.name=name.substring(0, 1)+"...";
        for (TreemapNode tnode : this.getChildren()) {
            tnode.clean();
        }
	}
}
