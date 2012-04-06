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
package com.antelink.sourcesquare.server;

import com.antelink.sourcesquare.server.servlet.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.ContextHandlerCollection;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.FilterHolder;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.jetty.webapp.WebAppContext;

import com.antelink.sourcesquare.SourceSquareResults;
import com.antelink.sourcesquare.client.scan.ScanStatus;
import com.antelink.sourcesquare.event.base.EventBus;
import com.antelink.sourcesquare.event.events.SourceSquareResultsReadyEvent;
import com.antelink.sourcesquare.event.handlers.SourceSquareResultsReadyEventHandler;
import com.google.gson.Gson;

public class EmbeddedServer {

    private static final int SERVER_PORT = 9524;
    private EventBus eventBus;
    private Server jetty;
    private WebAppContext webContext;
    private PublishServlet publishServlet;
    private Context servletContext;
    private ContextHandlerCollection contexts;
    private final ShutdownServlet shutdownServlet;

    private static final Log logger = LogFactory.getLog(EmbeddedServer.class);

    public EmbeddedServer(EventBus eventBus) {
        this.eventBus = eventBus;
        this.jetty = new Server(SERVER_PORT);
        this.publishServlet = new PublishServlet(eventBus);
        this.shutdownServlet = new ShutdownServlet();
        this.contexts = new ContextHandlerCollection();
        this.servletContext = new Context(this.contexts, "/service");
    }

    public void startServer() throws Exception {

        logger.debug("adding webhandler");
        getClass().getClassLoader();
        this.webContext = new WebAppContext(ClassLoader.getSystemResource("webapp/")
                .toExternalForm(), "/");
        FilterHolder resultFilter = new FilterHolder(new ResultFilter(this.eventBus));
        this.webContext.addFilter(resultFilter, "/result.jsp", 1);

        logger.debug("loading: " + ClassLoader.getSystemResource("webapp/").toExternalForm());
        this.contexts.addHandler(this.webContext);

        logger.debug("adding status servlet");
        ServletHolder statusService = new ServletHolder(new StatusServlet());
        this.servletContext.addServlet(statusService, "/status");

        logger.debug("adding publish servlet");
        ServletHolder publishService = new ServletHolder(this.publishServlet);
        this.servletContext.addServlet(publishService, "/publish");

        logger.debug("adding shutdown servlet");
        ServletHolder shutdownService = new ServletHolder(this.shutdownServlet);
        this.servletContext.addServlet(shutdownService, "/shutdown");

        logger.debug("adding time servlet");
        ServletHolder timeService = new ServletHolder(new TimeServlet());
        this.servletContext.addServlet(timeService, "/time");

        logger.debug("adding quapcha servlet");
        ServletHolder quaptchaService = new ServletHolder(new QuaptchaServlet());
        this.servletContext.addServlet(quaptchaService, "/check/captcha");

        logger.debug("adding quapcha servlet");
        ServletHolder feedbackService = new ServletHolder(new FeedbackServlet());
        this.servletContext.addServlet(feedbackService, "/add/feedback");

        this.contexts.addHandler(this.servletContext);

        ErrorHandler errorHandler = new ErrorHandler();

        this.webContext.setErrorHandler(errorHandler);
        this.servletContext.setErrorHandler(errorHandler);

        this.jetty.setHandler(this.contexts);

        bind();

        this.jetty.start();
    }

    private void bind() {
        this.eventBus.addHandler(SourceSquareResultsReadyEvent.TYPE,
                new SourceSquareResultsReadyEventHandler() {
                    @Override
                    public String getId() {
                        return "Embedded server result handler";
                    }

                    @Override
                    public void handle(SourceSquareResults results) {
                        bindWebContextData(results);
                        ScanStatus.INSTANCE.setComplete();
                    }
                });
    }

    public Context getServletContext() {
        return this.servletContext;
    }

    public void setServletContext(Context servletContext) {
        this.servletContext = servletContext;
    }

    public ContextHandlerCollection getContexts() {
        return this.contexts;
    }

    public void setContexts(ContextHandlerCollection contexts) {
        this.contexts = contexts;
    }

    private void bindWebContextData(SourceSquareResults resultElement) {
        Gson gson = new Gson();
        String jsonTreemapData = gson.toJson(resultElement.getRootNode());
        this.webContext.setAttribute("treemapData", jsonTreemapData);
        this.webContext.setAttribute("modelData", resultElement);
    }

    public EventBus getEventBus() {
        return this.eventBus;
    }

    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public Server getJetty() {
        return this.jetty;
    }

    public void setJetty(Server jetty) {
        this.jetty = jetty;
    }

    public WebAppContext getWebContext() {
        return this.webContext;
    }

    public void setWebContext(WebAppContext webContext) {
        this.webContext = webContext;
    }

    public PublishServlet getPublishServlet() {
        return this.publishServlet;
    }

    public void setPublishServlet(PublishServlet publishServlet) {
        this.publishServlet = publishServlet;
    }

    public static int getPort() {
        return SERVER_PORT;
    }

}
