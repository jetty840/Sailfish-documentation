// Main.java
// Copyright (c) 2014, Dan Newman
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// 1. Redistributions of source code must retain the above copyright notice,
//    this list of conditions and the following disclaimer. 
// 2. Redistributions in binary form must reproduce the above copyright notice,
//    this list of conditions and the following disclaimer in the documentation
//    and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Main {

    private static String in_file = null;
    private static String out_file = null;

    private static void usage(int err) {
	System.err.println("Usage: lcdgen tiny|small|medium|large <text-file> [<image-file>]");
	System.exit(err);
    }

    public static void main(String[] args) {

	LCDPane lcd = new LCDPane();

	if (args.length != 3) {
	    usage(1);
	}

	lcd.numColumns = 20;
	lcd.numRows    =  4;
	lcd.setBackground(LCDPane.GREEN_BACKGROUND);
	lcd.pixelOnColor  = LCDPane.GREEN_PIXEL_ON_COLOR;
	lcd.pixelOffColor = LCDPane.GREEN_PIXEL_OFF_COLOR;

	switch(args[0].charAt(0)) {
	case 't' :
	case 'T' :
	    lcd.border = 2;
	    lcd.borderEdgeRadius = 2;
	    lcd.scrHborder = 5;
	    lcd.scrVborder = 2;
	    lcd.pixelWidth = 1;
	    lcd.pixelHeight = 1;
	    lcd.pixelHspacing = 0;
	    lcd.pixelVspacing = 0;
	    lcd.charHspacing = 1;
	    lcd.charVspacing = 1;
	    break;

	case 's' :
	case 'S' :
	    lcd.border = 7;
	    lcd.borderEdgeRadius = 4;
	    lcd.scrHborder = 10;
	    lcd.scrVborder = 5;
	    lcd.pixelWidth = 2;
	    lcd.pixelHeight = 2;
	    lcd.pixelHspacing = 1;
	    lcd.pixelVspacing = 1;
	    lcd.charHspacing = 2;
	    lcd.charVspacing = 2;
	    break;

	case 'm' :
	case 'M' :
	    lcd.border = 10;
	    lcd.borderEdgeRadius = 6;
	    lcd.scrHborder = 15;
	    lcd.scrVborder = 7;
	    lcd.pixelWidth = 3;
	    lcd.pixelHeight = 3;
	    lcd.pixelHspacing = 1;
	    lcd.pixelVspacing = 1;
	    lcd.charHspacing = 4;
	    lcd.charVspacing = 4;
	    break;

	case 'l' :
	case 'L' :
	    lcd.border = 12;
	    lcd.borderEdgeRadius = 9;
	    lcd.scrHborder = 20;
	    lcd.scrVborder = 12;
	    lcd.pixelWidth = 4;
	    lcd.pixelHeight = 4;
	    lcd.pixelHspacing = 1;
	    lcd.pixelVspacing = 1;
	    lcd.charHspacing = 5;
	    lcd.charVspacing = 5;
	    break;

	default :
	    usage(0);
	    break;
	}

	lcd.invalidate();
	lcd.recalculateDimensions();
	// lcd.revalidate();
	lcd.repaint();

	in_file = new String(args[1]);
	out_file = new String(args[2]);

	byte[] data = null;
	try {
	    File file = new File(in_file);
	    FileInputStream fis = new FileInputStream(file);
	    data = new byte[(int)file.length()];
	    fis.read(data);
	    fis.close();
	}
	catch (FileNotFoundException ex) {
	    System.err.println("Unable to open the file \"" + in_file + "\", file not found");
	    System.exit(1);
	}
	catch (IOException ex) {
	    System.err.println("Error reading the file \"" + in_file + "\"");
	    System.exit(1);
	}

	String s = new String("");
	boolean literal = false;

	for (int i = 0; i < data.length; i++) {
	    char c = (char)data[i];
	    if (literal) {
		literal = false;
		switch(c) {
		case 'B' : c = (char)0x01; break;
		case 'r' : c = '\r'; break;
		case 'n' : c = '\n'; break;
		case 'f' : c = (char)0x04; break;
		case 'F' : c = (char)0x03; break;
		default : break;
		}
		s += c;
	    }
	    else if (c == '\\')
		literal = true;
	    else 
		s += c;
	}

	lcd.setText(s);

	Dimension dim = lcd.getPreferredSize();
	BufferedImage image = new BufferedImage(dim.width, dim.height,
						BufferedImage.TYPE_INT_RGB);
	lcd.paint(image.getGraphics());
	try {
	    File file = null;
	    if (out_file.indexOf(".") < 0)
		out_file = out_file + ".jpg";
	    file = new File(out_file);
	    ImageIO.write(image, "gif", file);
	}
	catch(Throwable t) {
	    t.printStackTrace();
	    System.exit(1);
	}
    }
}
