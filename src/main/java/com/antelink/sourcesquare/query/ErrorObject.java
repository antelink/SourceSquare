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

public class ErrorObject {

    private static final int INVALID_SHA1 = 1;
    private static final int INVALID_HASHLIST = 2;
    private static final int INTERNAL_ERROR = 3;
    private static final int ACCESS_DENIED = 4;

    private int errorCode;
    private String errorMessage;

    public ErrorObject() {

    }

    public ErrorObject(int errorCode, String errorMessage) {
        super();
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public static ErrorObject invalidSha1Error(String hash) {
        return new ErrorObject(INVALID_SHA1, "invalid SHA1: [" + hash + "]");
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.errorCode;
        result = prime * result + ((this.errorMessage == null) ? 0 : this.errorMessage.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ErrorObject other = (ErrorObject) obj;
        if (this.errorCode != other.errorCode) {
            return false;
        }
        if (this.errorMessage == null) {
            if (other.errorMessage != null) {
                return false;
            }
        } else if (!this.errorMessage.equals(other.errorMessage)) {
            return false;
        }
        return true;
    }

    public static ErrorObject invalidHashListFormat(String hashList) {
        return new ErrorObject(INVALID_HASHLIST, "invalid hash list format: " + hashList);
    }

    public static ErrorObject internalError() {
        return new ErrorObject(INTERNAL_ERROR, "internal error");
    }

    public static ErrorObject authorizationDenied() {
        return new ErrorObject(ACCESS_DENIED, "internal error");
    }

}
