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

import java.util.ArrayList;
import java.util.List;

public class ResponseObject {

    private ErrorObject error;
    private List<ResultEntry> results;

    public ResponseObject() {}

    public ResponseObject(ErrorObject error, List<ResultEntry> results) {
        super();
        this.error = error;
        this.results = results;
    }

    public Boolean hasError() {
        return this.error != null;
    }

    public ErrorObject getError() {
        return this.error;
    }

    public void setError(ErrorObject error) {
        this.error = error;
    }

    public List<ResultEntry> getResults() {
        if (this.results == null) {
            this.results = new ArrayList<ResultEntry>();
        }
        return this.results;
    }

    public void setResults(List<ResultEntry> results) {
        this.results = results;
    }

}
