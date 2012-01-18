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
import java.net.URI;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.CommonsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public abstract class RestClient {
    protected RestTemplate getTemplate(String baseDomain) {
        HttpClient client = new HttpClient();

        // Managing HTTP proxy - if any
        String proxyHost = System.getProperty("http.proxyHost");
        String proxyPort = System.getProperty("http.proxyPort");
        if (proxyHost != null && proxyPort != null) {
            client.getHostConfiguration().setProxy(proxyHost, Integer.parseInt(proxyPort));
        }

        // Managing HTTP proxy authentication - if any
        String proxyUser = System.getProperty("http.proxyUser");
        String proxyPassword = System.getProperty("http.proxyPassword");
        AuthScope auth;
        if (proxyHost != null && proxyUser != null && proxyPassword != null) {
            auth = new AuthScope(proxyHost, Integer.parseInt(proxyPort));
            client.getState().setProxyCredentials(auth, new UsernamePasswordCredentials(proxyUser, proxyPassword));
        } else {
            auth = new AuthScope(baseDomain, AuthScope.ANY_PORT);
            client.getState().setCredentials(auth, null);
        }

        CommonsClientHttpRequestFactory commons = new CommonsClientHttpRequestFactory(client) {
            @Override
            public ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) throws IOException {
                ClientHttpRequest createRequest = super.createRequest(uri, httpMethod);
                createRequest.getHeaders().add("User-Agent", "SourceSquare");
                return createRequest;
            }
        };
        return new RestTemplate(commons);
    }
}
