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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.web.client.RestClientException;

public class AntepediaQuery extends RestClient {

    private final static Log logger = LogFactory.getLog(AntepediaQuery.class);

    private final AntepediaLocations locations = new AntepediaLocations();

    public List<ResultEntry> getResults(Map<String, String> files) throws RestClientException,
            JsonGenerationException, JsonMappingException, IOException {

        ResponseObject response = getResponseObjects(files.values());

        if (response.hasError()) {
            response.getError().getErrorCode();
            logger.error("Error getting the results from the antepedia server : "
                    + response.getError().getErrorMessage());
            logger.error("Remote service returned error code: "
                    + response.getError().getErrorCode());
        }

        return response.getResults();
    }

    private ResponseObject getResponseObjects(Collection<String> hashes)
            throws JsonGenerationException, JsonMappingException, IOException {
        try {
            String queryUrl = this.locations.getBinaryBatchQueryUrl();
            ObjectMapper mapper = new ObjectMapper();
            String map = mapper.writeValueAsString(hashes);
            logger.info("contacting server " + queryUrl + " with " + hashes.size() + " hashs");
            ResponseObject postForObject = getTemplate(this.locations.getBaseDomain())
                    .postForObject(queryUrl, map, ResponseObject.class);
            logger.info("response received");
            return postForObject;
        } catch (UnsupportedEncodingException e) {
            logger.error("Error contacting the server", e);
            return null;
        }
    }
}
