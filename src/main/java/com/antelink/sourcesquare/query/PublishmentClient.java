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
package com.antelink.sourcesquare.query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.client.RestTemplate;

import com.antelink.sourcesquare.SourceSquareResults;
import com.antelink.sourcesquare.TreemapNode;
import com.antelink.sourcesquare.server.servlet.Feedback;
import com.google.gson.Gson;

public class PublishmentClient extends RestClient {

    private final static Log logger = LogFactory.getLog(PublishmentClient.class);

    private static final String SOURCESQUARE_SERVER_DOMAIN = System.getProperty(
        "sourcesquare.domain", "sourcesquare.antepedia.com");

    Gson gson = new Gson();

    public void publish(Feedback feedback) {
        RestTemplate template = getTemplate(SOURCESQUARE_SERVER_DOMAIN);
        String url = "https://" + SOURCESQUARE_SERVER_DOMAIN + "/service/store/feedback";
        logger.info(url);
        try{
        template.postForObject(url, feedback, Boolean.class);
        }
        catch(Throwable t){
            logger.error("Error sending feedback",t);
        }
    }

    public String publish(SourceSquareResults results) {
        RestTemplate template = getTemplate(SOURCESQUARE_SERVER_DOMAIN);

        String request = this.gson.toJson(cleanResults(results));
        String url = "https://" + SOURCESQUARE_SERVER_DOMAIN + "/publish";
        logger.info(url);
        return template.postForObject(url, request, String.class);
    }

    private SourceSquareResults cleanResults(SourceSquareResults results) {
        SourceSquareResults nresults = new SourceSquareResults();
        nresults.setBadges(results.getBadges());
        String tree = this.gson.toJson(results.getRootNode());
        TreemapNode copy = this.gson.fromJson(tree, TreemapNode.class);
        copy.clean();
        nresults.setRootNode(copy);
        nresults.setNodeLevel(results.getNodeLevel());
        return nresults;
    }

}
