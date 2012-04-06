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
package com.antelink.sourcesquare.event.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Event bus reifying the publisher-subscriber architectural pattern. Basically,
 * it registers a set of handlers for particular events. A third actor fires
 * these events into the event bus, which dispatches them to the respective
 * handlers.
 * 
 * @author Freddy Munoz, PhD Antelink S.A.S
 * 
 */
public class EventBus {

    private static final Log logger = LogFactory.getLog(EventBus.class);

    HashMap<ClientEventType<?>, List<? extends ClientEventHandler>> eventMap = new HashMap<ClientEventType<?>, List<? extends ClientEventHandler>>();

    @SuppressWarnings("unchecked")
    public <T extends ClientEventHandler> void addHandler(ClientEventType<T> eventType, T handler) {
        if (!this.eventMap.containsKey(eventType)) {
            this.eventMap.put(eventType, new ArrayList<T>());
        }
        ((ArrayList<T>) this.eventMap.get(eventType)).add(0, handler);
    }

    @SuppressWarnings("unchecked")
    public <T extends ClientEventHandler> void fireEvent(final ClientEvent<T> event) {
        if (!this.eventMap.containsKey(event.getHandlerType())) {
            return;
        }
        for (final ClientEventHandler handler : this.eventMap.get(event.getHandlerType())) {
            try {
                Runnable runner = new Runnable() {

                    @Override
                    public void run() { 
                        event.dispatch((T) handler);

                    }
                };

                Thread thread = new Thread(runner);
                thread.start();

                logger.trace("event dispatched to the handler " + handler.getId());
            } catch (Exception e) {
                logger.error("An event could not be dispatched to the handler " + handler.getId(), e);
            }
        }
    }
}
