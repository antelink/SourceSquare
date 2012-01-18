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
package com.antelink.sourcesquare.server.servlet;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.antelink.sourcesquare.SourceSquareResults;
import com.antelink.sourcesquare.event.base.EventBus;
import com.antelink.sourcesquare.event.events.SourceSquareResultsReadyEvent;
import com.antelink.sourcesquare.event.handlers.SourceSquareResultsReadyEventHandler;
import com.antelink.sourcesquare.query.PublishmentClient;
import com.google.gson.Gson;

public class PublishServlet extends HttpServlet {

	private static final Log logger = LogFactory.getLog(PublishServlet.class);

	private EventBus eventBus;

	private SourceSquareResults sourceSquareResult;

	private String publishUrl;

	private boolean published;

	private PublishmentClient server = new PublishmentClient();

	public PublishServlet(EventBus eventBus) {
		this.eventBus = eventBus;
		eventBus.addHandler(SourceSquareResultsReadyEvent.TYPE, new SourceSquareResultsReadyEventHandler() {
			@Override
			public String getId() {
				return "Embedded server result handler";
			}

			@Override
			public void handle(SourceSquareResults results) {
				PublishServlet.this.sourceSquareResult = results;
			}
		});
	}

	private static final long serialVersionUID = 1445253079298703388L;

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("publishing results");
		try {
			response.setContentType("text/html;charset=utf-8");
			//single publication
			if (!published) {
				publishUrl = this.server.publish(this.sourceSquareResult);
				published=true;
			}
			response.getWriter().append(URLEncoder.encode(publishUrl, "UTF-8"));
		} catch (IOException e) {
			logger.error("error publishing results to the server", e);
		}
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json;charset=utf-8");
		Gson gson = new Gson();
		try {
			response.getWriter().append(gson.toJson(this.sourceSquareResult != null));
		} catch (IOException e) {
			logger.error("error getting the server state", e);
		}
	}

	public EventBus getEventBus() {
		return this.eventBus;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public SourceSquareResults getSourceSquareResult() {
		return this.sourceSquareResult;
	}

	public void setSourceSquareResult(SourceSquareResults sourceSquareResult) {
		this.sourceSquareResult = sourceSquareResult;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
