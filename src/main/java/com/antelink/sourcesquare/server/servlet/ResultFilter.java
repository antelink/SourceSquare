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

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.antelink.sourcesquare.SourceSquareResults;
import com.antelink.sourcesquare.event.base.EventBus;
import com.antelink.sourcesquare.event.events.SourceSquareResultsReadyEvent;
import com.antelink.sourcesquare.event.handlers.SourceSquareResultsReadyEventHandler;

public class ResultFilter implements Filter {

    private static final Log logger = LogFactory.getLog(ResultFilter.class);

    private SourceSquareResults sourceSquareResult;

    public ResultFilter(EventBus eventBus) {
        eventBus.addHandler(SourceSquareResultsReadyEvent.TYPE,
                new SourceSquareResultsReadyEventHandler() {
                    @Override
                    public String getId() {
                        return "Embedded server result filter";
                    }

                    @Override
                    public void handle(SourceSquareResults results) {
                        ResultFilter.this.sourceSquareResult = results;
                    }
                });
    }

    @Override
    public void destroy() {}

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        if (this.sourceSquareResult == null) {
            logger.debug("filter results");
            ((HttpServletResponse) resp).sendRedirect("index.jsp");
        } else {
            logger.debug("show results");
            chain.doFilter(req, resp);
        }
    }

    @Override
    public void init(FilterConfig config) throws ServletException {}

}
