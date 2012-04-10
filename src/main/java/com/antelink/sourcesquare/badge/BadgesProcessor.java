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

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.antelink.sourcesquare.event.base.EventBus;
import com.antelink.sourcesquare.event.events.FilesIdentifiedEvent;
import com.antelink.sourcesquare.event.events.HiddenFileFoundEvent;
import com.antelink.sourcesquare.event.events.OSFilesFoundEvent;
import com.antelink.sourcesquare.event.handlers.FilesIdentifiedEventHandler;
import com.antelink.sourcesquare.event.handlers.HiddenFileFoundEventHandler;
import com.antelink.sourcesquare.event.handlers.OSFilesFoundEventHandler;

public class BadgesProcessor {
    private static final int MAX_BADGES = 8;
    private static final double PROGRAMMING_LANGUAGE_THRESHOLD = 0.2;
    private static final double OTHERS_THRESHOLD = 0.25;
    private EventBus eventBus;
    private Set<String> hiddenFiles = new HashSet<String>();
    private int openSourceFiles;
    private Set<File> files;
    private Map<String, Integer> fileTypes = new HashMap<String, Integer>();
    private List<String> scriptExtensions = new LinkedList<String>() {
        private static final long serialVersionUID = 3768380499500306267L;

        {
            add("sh");
            add("php");
            add("csh");
            add("py");
            add("bat");
            add("rb");
            add("lua");
            add("ac");
            add("as");
            add("awk");
            add("esh");
            add("pyc");
            add("pyo");
            add("rgs");
            add("scar");
            add("scr");
            add("vbe");
            add("wsf");
            add("obs");
            add("js");
            add("asp");
            add("jsp");
            add("vbs");
        }
    };
    private List<String> cobolExtensions = new LinkedList<String>() {
        private static final long serialVersionUID = -7092349123506647331L;

        {
            add("cob");
            add("cbl");
            add("cpi");
        }
    };
    private List<String> fortranExtensions = new LinkedList<String>() {
        private static final long serialVersionUID = -5926165218478822175L;

        {
            add("f");
            add("f90");
            add("f03");
            add("f95");
            add("f97");
            add("for");
            add("F");
            add("F90");
        }
    };
    private List<String> executableExtensions = new LinkedList<String>() {
        private static final long serialVersionUID = -2193101699205657182L;
        {
            add("exe");
            add("bin");
            add("msi");
            add("app");
            add("com");
            add("mst");
        }
    };
    
    private List<String> imageExt = new LinkedList<String>() {
        private static final long serialVersionUID = -2193101699205237182L;
        {
            add("png");
            add("jpeg");
            add("jpg");
            add("bmp");
            add("gif");
            add("psd");
            add("tif");
            add("pcd");
            add("pct");
            add("pcx");
            add("pdf");
            add("pds");
            add("pic");
            add("pict");
            add("ps");
            add("psid");
            add("psp");
            add("pub");
            add("pif");
            add("pit");
            add("pnt");
        }
    };

    public BadgesProcessor(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void bind() {
        this.eventBus.addHandler(FilesIdentifiedEvent.TYPE, new FilesIdentifiedEventHandler() {

            @Override
            public String getId() {
                return getClass().getCanonicalName() + ": " + FilesIdentifiedEventHandler.class.getName();
            }

            @Override
            public void handle(TreeSet<File> fileSet) {
                BadgesProcessor.this.files = fileSet;
                for (File file : fileSet) {
                    String extension = extractExtention(file);
                    if (extension != null) {
                        Integer number = BadgesProcessor.this.fileTypes.get(extension);
                        BadgesProcessor.this.fileTypes.put(extension, number == null ? 1 : number + 1);
                    }
                }
            }

            private String extractExtention(File file) {
                String name = file.getName();
                int lastIndexOf = name.lastIndexOf('.');
                return lastIndexOf < 0 ? null : name.substring(lastIndexOf + 1);
            }
        });
        this.eventBus.addHandler(HiddenFileFoundEvent.TYPE, new HiddenFileFoundEventHandler() {

            @Override
            public String getId() {
                return getClass().getCanonicalName() + ": " + HiddenFileFoundEventHandler.class.getName();
            }

            @Override
            public void handle(File file) {
                BadgesProcessor.this.hiddenFiles.add(file.getName());
            }
        });
        this.eventBus.addHandler(OSFilesFoundEvent.TYPE, new OSFilesFoundEventHandler() {

            @Override
            public String getId() {
                return getClass().getCanonicalName() + ": " + OSFilesFoundEventHandler.class.getName();
            }

            @Override
            public void handle(Set<String> fileSet) {
                BadgesProcessor.this.openSourceFiles += fileSet.size();
            }
        });
    }

    public Collection<Badge> process() {
        List<Badge> badges = new ArrayList<Badge>();
        badges.add(getOpenSourceBadge());
        badges.addAll(getProgrammingLanguageBadges());
        badges.addAll(getScmBadges());
        badges.addAll(getOtherBadges());
        while (badges.size() > MAX_BADGES) {
            badges.remove((int) Math.random() * (badges.size() - 1) + 1);
        }
        return badges;
    }

    private Collection<? extends Badge> getOtherBadges() {
        Collection<Badge> badges = new LinkedHashSet<Badge>();

        // picaso badge
        double img = 0;
        for (String imgExt : this.imageExt) {
            img = img + (this.fileTypes.get(imgExt) == null ? 0 : this.fileTypes.get(imgExt));
        }
        if ((img / this.files.size()) > OTHERS_THRESHOLD) {
            badges.add(Badge.PICASSO);
        }

        // executor badge
        double exec = 0;
        for (String execExt : this.executableExtensions) {
            exec = exec + (this.fileTypes.get(execExt) == null ? 0 : this.fileTypes.get(execExt));
        }

        if ((exec / this.files.size()) > OTHERS_THRESHOLD) {
            badges.add(Badge.EXECUTOR);
        }
        return badges;
    }

    private Collection<? extends Badge> getScmBadges() {
        Collection<Badge> badges = new LinkedHashSet<Badge>();
        for (String hidden : this.hiddenFiles) {
            if (hidden.equals(".svn")) {
                badges.add(Badge.SVN);
            }
            if (hidden.equals(".git")) {
                badges.add(Badge.GIT);
            }
            if (hidden.equals(".cvs")) {
                badges.add(Badge.CVS);
            }
        }
        return badges;
    }

    private Collection<? extends Badge> getProgrammingLanguageBadges() {
        Collection<Badge> badges = new LinkedHashSet<Badge>();
        // java .java || scala .scala
        int langcounter = 0;
        double java = this.fileTypes.get("java") == null ? 0 : this.fileTypes.get("java");
        double scala = (this.fileTypes.get("scala") == null ? 0 : this.fileTypes.get("scala"));
        langcounter = langcounter + (java == 0 ? 0 : 1);
        langcounter = langcounter + (scala == 0 ? 0 : 1);

        checkAdd(Badge.JAVA, java + scala, badges);
        // c || cpp
        double c = this.fileTypes.get("c") == null ? 0 : this.fileTypes.get("c");
        double cpp = (this.fileTypes.get("cpp") == null ? 0 : this.fileTypes.get("cpp"));
        langcounter = langcounter + (c == 0 ? 0 : 1);
        langcounter = langcounter + (cpp == 0 ? 0 : 1);
        checkAdd(Badge.C, c + cpp, badges);
        // perl
        double perl = this.fileTypes.get("perl") == null ? 0 : this.fileTypes.get("perl");
        langcounter = langcounter + (perl == 0 ? 0 : 1);
        checkAdd(Badge.PERL, perl, badges);

        // scripting sh php ruby python csh bat
        double script = 0;
        for (String scriptExt : this.scriptExtensions) {
            script = script + (this.fileTypes.get(scriptExt) == null ? 0 : this.fileTypes.get(scriptExt));
        }
        langcounter = langcounter + (script == 0 ? 0 : 1);
        checkAdd(Badge.SCRIPT, perl + script, badges);

        // cobol cbl,cob,cpy
        double cobol = 0;
        for (String cobExt : this.cobolExtensions) {
            cobol = cobol + (this.fileTypes.get(cobExt) == null ? 0 : this.fileTypes.get(cobExt));
        }
        // fortran .f .f90 .F .F90 .f95 .f03 .for
        double fortran = 0;
        for (String forExt : this.fortranExtensions) {
            fortran = fortran + (this.fileTypes.get(forExt) == null ? 0 : this.fileTypes.get(forExt));
        }

        langcounter = langcounter + (fortran == 0 ? 0 : 1);
        langcounter = langcounter + (cobol == 0 ? 0 : 1);
        checkAdd(Badge.OLD, fortran + cobol, badges);
        if (langcounter > 3) {
            badges.add(Badge.SHAKER);
        }

        return badges;

    }

    private void checkAdd(Badge badge, double count, Collection<Badge> badges) {
        if ((count / this.files.size()) > PROGRAMMING_LANGUAGE_THRESHOLD) {
            badges.add(badge);
        }
    }

    private Badge getOpenSourceBadge() {
        double percentual = this.files.size() == 0 ? 0.0 : (double) this.openSourceFiles / (double) this.files.size();
        if (percentual >= 0.8) {
            return Badge.OS_SAMURAI;
        }
        if (percentual >= 0.6) {
            return Badge.OS_BLACKBELT;
        }
        if (percentual >= 0.4) {
            return Badge.OS_TRAINEE;
        }
        if (percentual > 0) {
            return Badge.OS_WHAT;
        }
        return Badge.OS_CLEAR;
    }
}
