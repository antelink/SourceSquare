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
package com.antelink.sourcesquare.event.events;

import java.util.Set;

import com.antelink.sourcesquare.event.base.ClientEvent;
import com.antelink.sourcesquare.event.base.ClientEventType;
import com.antelink.sourcesquare.event.handlers.CreateTreeMapNodeEventHandler;

public class CreateTreeMapNodeEvent implements ClientEvent<CreateTreeMapNodeEventHandler> {

    public static final ClientEventType<CreateTreeMapNodeEventHandler> TYPE = new ClientEventType<CreateTreeMapNodeEventHandler>();

    private final String path;
    private final Set<String> children;
    private final int nbFiles;

    public CreateTreeMapNodeEvent(String path, Set<String> children, int nbFiles) {
        super();
        this.path = path;
        this.children = children;
        this.nbFiles = nbFiles;
    }

    @Override
    public ClientEventType<CreateTreeMapNodeEventHandler> getHandlerType() {
        return TYPE;
    }

    @Override
    public void dispatch(CreateTreeMapNodeEventHandler handler) {
        handler.handle(this.path, this.children, this.nbFiles);
    }

    public String getPath() {
        return this.path;
    }

    public Set<String> getChildren() {
        return this.children;
    }

    public int getNbFiles() {
        return this.nbFiles;
    }

}
