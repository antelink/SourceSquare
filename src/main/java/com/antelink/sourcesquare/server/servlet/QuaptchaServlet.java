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

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.antelink.sourcesquare.server.JCaptchaBean;
import com.google.gson.Gson;

public class QuaptchaServlet extends HttpServlet {

    private static final Log logger = LogFactory.getLog(QuaptchaServlet.class);

    private static final long serialVersionUID = 1445253079298703388L;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        String key = request.getParameter("qaptcha_key");
        String action = request.getParameter("action");
        response.setContentType("text/html;charset=utf-8");
        JCaptchaBean qap = null;
        if ("qaptcha".equals(action)) {
            qap = new JCaptchaBean(false);
        } else {
            qap = new JCaptchaBean(true);
        }
        Gson gson = new Gson();
        try {
            response.getWriter().append(gson.toJson(qap));
        } catch (IOException e) {
            logger.error("error sending quaptcha response", e);
        }
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}
