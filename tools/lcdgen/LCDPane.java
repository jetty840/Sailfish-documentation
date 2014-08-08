// LCDPane.java
// Copyright (c) 2014, Alexander Avtanski
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JWindow;

public class LCDPane extends JWindow
{
    private static final long serialVersionUID = 1L;

    public final static Color GREEN_BACKGROUND = new Color(0xA0, 0xFF, 0xA0);
    public final static Color GREEN_PIXEL_ON_COLOR = new Color(0x10, 0x20, 0x10);
    public final static Color GREEN_PIXEL_OFF_COLOR = new Color(0x98, 0xFF, 0x98); // new Color(0x90, 0xF0, 0x90);

    public final static Color GREEN_INV_BACKGROUND = new Color(0x10, 0x20, 0x10);
    public final static Color GREEN_INV_PIXEL_ON_COLOR = new Color(0xA0, 0xFF, 0xA0);
    public final static Color GREEN_INV_PIXEL_OFF_COLOR = new Color(0x18, 0x38, 0x18);

    public final static Color YELLOW_BACKGROUND = new Color(0xFF, 0xFF, 0x60);
    public final static Color YELLOW_PIXEL_ON_COLOR = new Color(0x20, 0x20, 0x08);
    public final static Color YELLOW_PIXEL_OFF_COLOR = new Color(0xF0, 0xF0, 0x50);

    public final static Color YELLOW_INV_BACKGROUND = new Color(0x20, 0x20, 0x08);
    public final static Color YELLOW_INV_PIXEL_ON_COLOR = new Color(0xFF, 0xFF, 0x60);
    public final static Color YELLOW_INV_PIXEL_OFF_COLOR = new Color(0x38, 0x38, 0x08);

    public final static Color RED_BACKGROUND = new Color(0xFF, 0x60, 0x60);
    public final static Color RED_PIXEL_ON_COLOR = new Color(0x20, 0x10, 0x10);
    public final static Color RED_PIXEL_OFF_COLOR = new Color(0xF0, 0x50, 0x50);

    public final static Color RED_INV_BACKGROUND = new Color(0x20, 0x10, 0x10);
    public final static Color RED_INV_PIXEL_ON_COLOR = new Color(0xFF, 0x30, 0x30);
    public final static Color RED_INV_PIXEL_OFF_COLOR = new Color(0x38, 0x18, 0x18);

    public final static Color BLUE_BACKGROUND = new Color(0x60, 0x60, 0xFF);
    public final static Color BLUE_PIXEL_ON_COLOR = new Color(0x10, 0x10, 0x20);
    public final static Color BLUE_PIXEL_OFF_COLOR = new Color(0x50, 0x50, 0xF0);

    public final static Color BLUE_INV_BACKGROUND = new Color(0x10, 0x10, 0x20);
    public final static Color BLUE_INV_PIXEL_ON_COLOR = new Color(0x30, 0x30, 0xFF);
    public final static Color BLUE_INV_PIXEL_OFF_COLOR = new Color(0x18, 0x18, 0x38);

    public final static Color BW_BACKGROUND = new Color(0xF8, 0xF8, 0xF8);
    public final static Color BW_PIXEL_ON_COLOR = new Color(0x10, 0x10, 0x10);
    public final static Color BW_PIXEL_OFF_COLOR = new Color(0xF0, 0xF0, 0xF0);

    public final static Color BW_INV_BACKGROUND = new Color(0x20, 0x20, 0x20);
    public final static Color BW_INV_PIXEL_ON_COLOR = new Color(0xF0, 0xF0, 0xF0);
    public final static Color BW_INV_PIXEL_OFF_COLOR = new Color(0x30, 0x30, 0x30);

    public final static Color DEFAULT_FRAME_COLOR = Color.BLACK;
    public final static Color DEFAULT_PIXEL_ON_COLOR = new Color(0x10, 0x20, 0x10);
    public final static Color DEFAULT_PIXEL_OFF_COLOR = new Color(0xA0, 0xFF, 0xA0); //new Color(0x90, 0xF0, 0x90);

    public final static int CURSOR_OFF = 0;
    public final static int CURSOR_BAR = 1;
    public final static int CURSOR_LINE = 2;

    public Color pixelOnColor = DEFAULT_PIXEL_ON_COLOR;
    public Color pixelOffColor = DEFAULT_PIXEL_OFF_COLOR;

    public int border = 12;
    public int borderEdgeRadius = 8;
    public int scrHborder = 12;
    public int scrVborder = 12;
    public int pixelWidth = 4;
    public int pixelHeight = 4;
    public int pixelHspacing = 1;
    public int pixelVspacing = 1;
    public int charHspacing = 5;
    public int charVspacing = 5;
    public int numColumns = 16;
    public int numRows = 2;
    public int cursor = CURSOR_OFF;
    public int cursorRow = 0;
    public int cursorColumn = 0;
    public int cw = 5; // 5 pixels char width
    public int ch = 8; // 7 pixels char height

    int charWidth = 0;
    int charHeight = 0;
    int scrWidth = 0;
    int scrHeight = 0;

    private Map<Character, int[]> charset = null;
    private List<String> rows = null;

    public LCDPane() {
	super();
	setFocusableWindowState(false);
	setVisible(false);
	recalculateDimensions();
    }

    private Map<Character, int[]> getCharset() {
	if (charset == null) {
	    charset = new HashMap<Character, int[]>();
	    // --- Begin Sailfish additions
	    // Four special characters added for Sailfish, July 2014
	    // BLOCK
	    charset.put((char)0x01, new int[] { 0xFF, 0xFF, 0xFF, 0xFF, 0xFF });
	    // DOWN
	    charset.put((char)0x02, new int[] { 0xFF, 0xFF, 0xFF, 0xFF, 0xFF });
	    // IN
	    charset.put((char)0x03, new int[] { 0x00, 0xFE, 0x7C, 0x38, 0x10 });
	    // OUT
	    charset.put((char)0x04, new int[] { 0x20, 0x70, 0xFA, 0x22, 0x3C });
	    // --- END Sailfish additions

	    charset.put('!', new int[] { 0x00, 0x00, 0xF2, 0x00, 0x00 });
	    charset.put('"', new int[] { 0x00, 0xE0, 0x00, 0xE0, 0x00 });
	    charset.put('#', new int[] { 0x28, 0xFE, 0x28, 0xFE, 0x28 });
	    charset.put('$', new int[] { 0x24, 0x54, 0xFE, 0x54, 0x48 });
	    charset.put('%', new int[] { 0xC4, 0xC8, 0x10, 0x26, 0x46 });
	    charset.put('&', new int[] { 0x6C, 0x92, 0xAA, 0x44, 0x0A });
	    charset.put('\'', new int[] { 0x00, 0xA0, 0xC0, 0x00, 0x00 });
	    charset.put('(', new int[] { 0x00, 0x38, 0x44, 0x82, 0x00 });
	    charset.put(')', new int[] { 0x00, 0x82, 0x44, 0x38, 0x00 });
	    charset.put('*', new int[] { 0x28, 0x10, 0x7C, 0x10, 0x28 });
	    charset.put('+', new int[] { 0x10, 0x10, 0x7C, 0x10, 0x10 });
	    charset.put(',', new int[] { 0x00, 0x0A, 0x0C, 0x00, 0x00 });
	    charset.put('-', new int[] { 0x10, 0x10, 0x10, 0x10, 0x10 });
	    charset.put('.', new int[] { 0x00, 0x06, 0x06, 0x00, 0x00 });
	    charset.put('/', new int[] { 0x04, 0x08, 0x10, 0x20, 0x40 });

	    charset.put('0', new int[] { 0x7C, 0x8A, 0x92, 0xA2, 0x7C });
	    charset.put('1', new int[] { 0x00, 0x42, 0xFE, 0x02, 0x00 });
	    charset.put('2', new int[] { 0x42, 0x86, 0x8A, 0x92, 0x62 });
	    charset.put('3', new int[] { 0x84, 0x82, 0xA2, 0xD2, 0x8C });
	    charset.put('4', new int[] { 0x18, 0x28, 0x48, 0xFE, 0x08 });
	    charset.put('5', new int[] { 0xE4, 0xA2, 0xA2, 0xA2, 0x9C });
	    charset.put('6', new int[] { 0x3C, 0x52, 0x92, 0x92, 0x0C });
	    charset.put('7', new int[] { 0x80, 0x8E, 0x90, 0xA0, 0xC0 });
	    charset.put('8', new int[] { 0x6C, 0x92, 0x92, 0x92, 0x6C });
	    charset.put('9', new int[] { 0x60, 0x92, 0x92, 0x94, 0x78 });
	    charset.put(':', new int[] { 0x00, 0x6C, 0x6C, 0x00, 0x00 });
	    charset.put(';', new int[] { 0x00, 0x6A, 0x6C, 0x00, 0x00 });
	    charset.put('<', new int[] { 0x10, 0x28, 0x44, 0x82, 0x00 });
	    charset.put('=', new int[] { 0x28, 0x28, 0x28, 0x28, 0x28 });
	    charset.put('>', new int[] { 0x00, 0x82, 0x44, 0x28, 0x10 });
	    charset.put('?', new int[] { 0x40, 0x80, 0x8A, 0x90, 0x60 });

	    charset.put('@', new int[] { 0x4C, 0x92, 0x9E, 0x82, 0x7C });
	    charset.put('A', new int[] { 0x7E, 0x88, 0x88, 0x88, 0x7E });
	    charset.put('B', new int[] { 0xFE, 0x92, 0x92, 0x92, 0x6C });
	    charset.put('C', new int[] { 0x7C, 0x82, 0x82, 0x82, 0x44 });
	    charset.put('D', new int[] { 0xFE, 0x82, 0x82, 0x44, 0x38 });
	    charset.put('E', new int[] { 0xFE, 0x92, 0x92, 0x92, 0x82 });
	    charset.put('F', new int[] { 0xFE, 0x90, 0x90, 0x90, 0x80 });
	    charset.put('G', new int[] { 0x7C, 0x82, 0x92, 0x92, 0x5E });
	    charset.put('H', new int[] { 0xFE, 0x10, 0x10, 0x10, 0xFE });
	    charset.put('I', new int[] { 0x00, 0x82, 0xFE, 0x82, 0x00 });
	    charset.put('J', new int[] { 0x04, 0x02, 0x82, 0xFC, 0x80 });
	    charset.put('K', new int[] { 0xFE, 0x10, 0x28, 0x44, 0x82 });
	    charset.put('L', new int[] { 0xFE, 0x02, 0x02, 0x02, 0x02 });
	    charset.put('M', new int[] { 0xFE, 0x40, 0x30, 0x40, 0xFE });
	    charset.put('N', new int[] { 0xFE, 0x20, 0x10, 0x08, 0xFE });
	    charset.put('O', new int[] { 0x7C, 0x82, 0x82, 0x82, 0x7C });

	    charset.put('P', new int[] { 0xFE, 0x90, 0x90, 0x90, 0x60 });
	    charset.put('Q', new int[] { 0x7C, 0x82, 0x8A, 0x84, 0x7A });
	    charset.put('R', new int[] { 0xFE, 0x90, 0x98, 0x94, 0x62 });
	    charset.put('S', new int[] { 0x62, 0x92, 0x92, 0x92, 0x8C });
	    charset.put('T', new int[] { 0x80, 0x80, 0xFE, 0x80, 0x80 });
	    charset.put('U', new int[] { 0xFC, 0x02, 0x02, 0x02, 0xFC });
	    charset.put('V', new int[] { 0xF8, 0x04, 0x02, 0x04, 0xF8 });
	    charset.put('W', new int[] { 0xFC, 0x02, 0x1C, 0x02, 0xFC });
	    charset.put('X', new int[] { 0xC6, 0x28, 0x10, 0x28, 0xC6 });
	    charset.put('Y', new int[] { 0xE0, 0x10, 0x0E, 0x10, 0xE0 });
	    charset.put('Z', new int[] { 0x86, 0x8A, 0x92, 0xA2, 0xC2 });
	    charset.put('[', new int[] { 0x00, 0xFE, 0x82, 0x82, 0x00 });
	    charset.put('\\', new int[] { 0xA8, 0x68, 0x3E, 0x68, 0xA8 });
	    charset.put(']', new int[] { 0x00, 0x82, 0x82, 0xFE, 0x00 });
	    charset.put('^', new int[] { 0x20, 0x40, 0x80, 0x40, 0x20 });
	    charset.put('_', new int[] { 0x02, 0x02, 0x02, 0x02, 0x02 });

	    charset.put('`', new int[] { 0x00, 0x80, 0x40, 0x20, 0x00 });
	    charset.put('a', new int[] { 0x04, 0x2A, 0x2A, 0x2A, 0x1E });
	    charset.put('b', new int[] { 0xFE, 0x12, 0x22, 0x22, 0x1C });
	    charset.put('c', new int[] { 0x1C, 0x22, 0x22, 0x22, 0x04 });
	    charset.put('d', new int[] { 0x1C, 0x22, 0x22, 0x12, 0xFE });
	    charset.put('e', new int[] { 0x1C, 0x2A, 0x2A, 0x2A, 0x18 });
	    charset.put('f', new int[] { 0x10, 0x7E, 0x90, 0x80, 0x40 });
	    charset.put('g', new int[] { 0x30, 0x4A, 0x4A, 0x4A, 0x7C });
	    charset.put('h', new int[] { 0xFE, 0x10, 0x20, 0x20, 0x1E });
	    charset.put('i', new int[] { 0x00, 0x22, 0xBE, 0x02, 0x00 });
	    charset.put('j', new int[] { 0x04, 0x02, 0x22, 0xBC, 0x00 });
	    charset.put('k', new int[] { 0xFE, 0x08, 0x14, 0x22, 0x00 });
	    charset.put('l', new int[] { 0x00, 0x82, 0xFE, 0x02, 0x00 });
	    charset.put('m', new int[] { 0x3E, 0x20, 0x18, 0x20, 0x1E });
	    charset.put('n', new int[] { 0x3E, 0x10, 0x20, 0x20, 0x1E });
	    charset.put('o', new int[] { 0x1C, 0x22, 0x22, 0x22, 0x1C });

	    charset.put('p', new int[] { 0x3E, 0x28, 0x28, 0x28, 0x10 });
	    charset.put('q', new int[] { 0x10, 0x28, 0x28, 0x18, 0x3E });
	    charset.put('r', new int[] { 0x3E, 0x10, 0x20, 0x20, 0x10 });
	    charset.put('s', new int[] { 0x12, 0x2A, 0x2A, 0x2A, 0x04 });
	    charset.put('t', new int[] { 0x20, 0xFC, 0x22, 0x02, 0x04 });
	    charset.put('u', new int[] { 0x3C, 0x02, 0x02, 0x04, 0x3E });
	    charset.put('v', new int[] { 0x38, 0x04, 0x02, 0x04, 0x38 });
	    charset.put('w', new int[] { 0x3C, 0x02, 0x0C, 0x02, 0x3C });
	    charset.put('x', new int[] { 0x22, 0x14, 0x08, 0x14, 0x22 });
	    charset.put('y', new int[] { 0x30, 0x0A, 0x0A, 0x0A, 0x3C });
	    charset.put('z', new int[] { 0x22, 0x26, 0x2A, 0x32, 0x22 });
	    charset.put('{', new int[] { 0x00, 0x10, 0x6C, 0x82, 0x00 });
	    charset.put('|', new int[] { 0x00, 0x00, 0xFE, 0x00, 0x00 });
	    charset.put('}', new int[] { 0x00, 0x82, 0x6C, 0x10, 0x00 });
	    charset.put('~', new int[] { 0x08, 0x08, 0x2A, 0x1C, 0x08 });
	}
	return charset;
    }

    public void setText(String text)
    {
	if (text == null)
	    text = "";
	getRows().clear();
	text.replaceAll("\r", "n");
	String[] lines = text.split("\n");
	for (int i = 0; i < lines.length; i++)
	    getRows().add(lines[i]);
	repaint();
    }

    private List<String> getRows()
    {
	if (rows == null)
	    rows = new ArrayList<String>();
	return rows;
    }

    private String getRow(int i)
    {
	String row = null;
	if (getRows().size() > i)
	    row = getRows().get(i);
	if (row == null)
	    row = "";
	return row;
    }

    public char getChar(String text, int index)
    {
	return (text == null || text.length() <= index) ? ' ' : text.charAt(index);
    }

    public Color getFrameColor() 
    {
	return DEFAULT_FRAME_COLOR;
    }

    public void recalculateDimensions()
    {
	charWidth = cw * pixelWidth + (cw - 1) * pixelHspacing;
	charHeight = ch * pixelHeight + (ch - 1) * pixelVspacing;
	scrWidth = 2 * scrHborder + numColumns * charWidth + (numColumns - 1) * charHspacing;
	scrHeight = 2 * scrVborder + numRows * charHeight + (numRows - 1) * charVspacing;
    }

    @Override
    public Dimension getPreferredSize()
    {
	recalculateDimensions();
	return new Dimension(2 * border + scrWidth, 2 * border + scrHeight);
    }

    @Override
    public Dimension getMinimumSize()
    {
	return getPreferredSize();
    }

    @Override
    public void paint(Graphics g)
    {
	recalculateDimensions();
	Graphics2D g2 = (Graphics2D)g;
	RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
					       RenderingHints.VALUE_ANTIALIAS_OFF);
	rh.put(RenderingHints.KEY_RENDERING,
	       RenderingHints.VALUE_RENDER_QUALITY);
	g2.setRenderingHints(rh);
	g2.setColor(getFrameColor());
	g2.fillRect(0, 0, getSize().width, getSize().height);
	g2.setColor(getBackground());
	g2.fillRect(border + borderEdgeRadius, border + 1,
		    scrWidth - 2 * borderEdgeRadius - 2, scrHeight - 2);
	g2.fillRect(border + 1, border + borderEdgeRadius, scrWidth - 2,
		    scrHeight - 2 * borderEdgeRadius - 2);
	g2.fillOval(border, border, borderEdgeRadius * 2 + 1,
		    borderEdgeRadius * 2 + 1);
	g2.fillOval(border + scrWidth - 2 * borderEdgeRadius - 2,
		    border, borderEdgeRadius * 2 + 1, borderEdgeRadius * 2 + 1);
	g2.fillOval(border, border + scrHeight - 2 * borderEdgeRadius - 2,
		    borderEdgeRadius * 2 + 1, borderEdgeRadius * 2 + 1);
	g2.fillOval(border + scrWidth - 2 * borderEdgeRadius - 2,
		    border + scrHeight - 2 * borderEdgeRadius - 2,
		    borderEdgeRadius * 2 + 1, borderEdgeRadius * 2 + 1);
	for(int row = 0; row < numRows; row++)
	{
	    for(int col = 0; col < numColumns; col++)
            {
		int cursorModifier = CURSOR_OFF;
		if (row == cursorRow && col == cursorColumn)
		    cursorModifier = cursor;
		paintChar(getChar(getRow(row), col), g2,
			  border + scrHborder + col * (charWidth + charHspacing) - 1,
			  border + scrVborder + row * (charHeight + charVspacing), cursorModifier);
	    }
	}
    }

    private void paintChar(char c, Graphics g2, int x, int y, int cursorModifier)
    {
	int[] charData = getCharset().get(c);
	if (charData == null)
	    charData = new int[] { 0x00, 0x00, 0x00, 0x00, 0x00 };
	for(int i = 0; i < cw; i++)
	{
	    int pixelData = 0x00;
	    if (i < charData.length)
		pixelData = charData[i];
	    for(int j = 0; j < ch; j++)
	    {
		boolean pixelOn = false;
		pixelOn = (pixelData & (0x1 << 7 - j)) != 0;
		switch(cursorModifier) {
		case CURSOR_BAR:
		    pixelOn = true;
		    break;
		case CURSOR_LINE:
		    if (j == ch - 1)
			pixelOn = true;
		    break;
		}
		g2.setColor(pixelOn ? pixelOnColor : pixelOffColor);
		g2.fillRect(x + i * (pixelWidth + pixelHspacing),
			    y + j * (pixelHeight + pixelVspacing),
			    pixelWidth, pixelHeight);
	    }
	}
    }
}
