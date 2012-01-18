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
package com.antelink.sourcesquare;

public class Color {

    public static final Color MAX = new Color("#1BA2FF");
    public static final Color MIN = new Color("#9C470E");

    private int red;
    private int green;
    private int blue;

    public Color(int red, int green, int blue) {
        super();
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public Color(String htmlColor) {
        String redVal = htmlColor.substring(1, 3);
        String greenVal = htmlColor.substring(3, 5);
        String blueVal = htmlColor.substring(5, 7);
        this.red = Integer.parseInt(redVal, 16);
        this.green = Integer.parseInt(greenVal, 16);
        this.blue = Integer.parseInt(blueVal, 16);
    }

    public Color minus(Color color) {
        return new Color(this.red - color.red, this.green - color.green, this.blue - color.blue);
    }

    public Color plus(Color color) {
        return new Color(this.red + color.red, this.green + color.green, this.blue + color.blue);
    }

    public Color times(double d) {
        return new Color((int) (this.red * d), (int) (this.green * d), (int) (this.blue * d));
    }

    public String encode() {
        String hexRed = Integer.toHexString(this.red);
        hexRed = hexRed.length() == 1 ? "0" + hexRed : hexRed;
        String hexGreen = Integer.toHexString(this.green);
        hexGreen = hexGreen.length() == 1 ? "0" + hexGreen : hexGreen;
        String hexBlue = Integer.toHexString(this.blue);
        hexBlue = hexBlue.length() == 1 ? "0" + hexBlue : hexBlue;
        return "#" + (hexRed + hexGreen + hexBlue).toUpperCase();
    }
}
