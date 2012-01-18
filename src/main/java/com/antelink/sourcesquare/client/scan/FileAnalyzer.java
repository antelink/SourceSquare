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
package com.antelink.sourcesquare.client.scan;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Utility class to compute the byte hash of a given file or byte stream.
 * 
 * @author Freddy Munoz, PhD Antelink S.A.S
 * 
 */
public class FileAnalyzer {

    private final static Log logger = LogFactory.getLog(FileAnalyzer.class);

    /**
     * Computes the hash of a given file.
     * 
     * @param hashType
     *            Hash that you want to compute. Usually SHA-1 or MD5 (or any
     *            other kind supported by java.security.MessageDigest).
     * @param fileToAnalyze
     *            Descriptor of the file to analyze. Must be a file otherwise
     *            you will get an exception.
     * @return the String hexadecimal representation of the file hash.
     * @throws NoSuchAlgorithmException
     *             if hashType is not a supported hash algorithm
     * @throws IOException
     *             in case of an I/O error while reading file
     */
    public static String calculateHash(String hashType, File fileToAnalyze) throws NoSuchAlgorithmException,
            IOException {
        FileInputStream fis = null;
        BufferedInputStream inputStream = null;
        try {
            logger.debug("Calculating sha1 for file " + fileToAnalyze.getName());
            fis = new FileInputStream(fileToAnalyze);
            inputStream = new BufferedInputStream(fis);
            MessageDigest digest = MessageDigest.getInstance(hashType);
            byte[] buffer = new byte[1024 * 1024];
            int length = -1;
            while ((length = inputStream.read(buffer)) != -1) {
                digest.update(buffer, 0, length);
            }

            String hash = new String(Hex.encodeHex(digest.digest()));
            logger.debug("Sha1 calculated for file " + fileToAnalyze.getName());
            return hash;
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(fis);
        }
    }

}
