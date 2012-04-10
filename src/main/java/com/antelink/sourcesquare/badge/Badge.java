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
package com.antelink.sourcesquare.badge;

public enum Badge {
    OS_SAMURAI("Open Source Samurai", "badge-samurai.png", "More than 80% of your files are open source"),

    OS_BLACKBELT("Open Source BlackBelt", "badge-blackbelt.png", "More than 60% of your files are open source"),

    OS_TRAINEE("Open Source Trainee", "badge-trainee.png", "More than 40% of your files are open source"),

    OS_WHAT("Open What?", "badge-openwhat.png", "Less than 40% of your files are open source"),

    OS_CLEAR("It's All Clear Capitain", "badge-clearcaptain.png", "You have no open source files"),

    SVN("Subversive Activity", "badge-svn.png", "You use Subversion"),

    GIT("Rebase yourself", "badge-git.png", "You use Git"),

    CVS("CVS Nostalgia", "badge-cvs.png", "You use CVS"),

    JAVA("Coffee maker", "badge-java.png", "More than 20% of your files are java/scala files"),

    C("Malloc Friend", "badge-c.png", "More than 20% of your files are c/c++ language files"),

    PERL("Perl Monger", "badge-perl.png", "More than 20% of your files are perl files"),

    EXECUTOR("Executor", "badge-executor.png", "More than 25% of your files are executable files"),

    OLD("Old Timer", "badge-old.png", "More than 20% of your files are cobol or fortran files"),

    PICASSO("Picasso Wannabe", "badge-picasso.png", "More than 25% of your files are images"),

    SCRIPT("Scripting paradise", "badge-scripting.png", "More than 20% of your files are script files (sh, php, ruby, python, csh, bat)"),

    SHAKER("Language Shaker", "badge-shaker.png", "You use more than 3 programming languages");

    /**
     * image alt description
     */
    private String alt;
    /**
     * image system path
     */
    private String path;
    /**
     * image toolTip
     */
    private String toolTip;

    private Badge(String alt, String path, String toolTip) {
        this.alt = alt;
        this.path = path;
        this.toolTip = toolTip;
    }

    public String getAlt() {
        return this.alt;
    }

    public String getPath() {
        return this.path;
    }

    public String getToolTip() {
        return this.toolTip;
    }

    public void setToolTip(String toolTip) {
        this.toolTip = toolTip;
    }

}
