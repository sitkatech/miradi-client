/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.rtf;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.swing.Icon;

import org.martus.util.UnicodeWriter;
import org.miradi.utils.AbstractTableExporter;
import org.miradi.utils.BufferedImageFactory;
import org.miradi.views.umbrella.SaveImageJPEGDoer;

public class RtfWriter
{
	public RtfWriter(File file) throws IOException
	{
		writer = new UnicodeWriter(file);
		rtfStyleManager = new RtfStyleManager();
	}
	
	public void close() throws Exception
	{
		getWriter().close();
	}
	
	public void writeEncoded(String data) throws Exception
	{
		writeln(encode(data));
	}
	
	public void writeRtfCommand(String rtfComand) throws Exception
	{
		writeln(rtfComand);
	}
	
	public void writeln(String textToWrite) throws Exception
	{
		getWriter().writeln(textToWrite);
	}
	
	public void write(String textToWrite) throws Exception
	{
		getWriter().write(textToWrite);
	}
	
	public void writeImage(BufferedImage bufferedImage) throws Exception
	{
		startBlock();

		int imageWidth = bufferedImage.getWidth();
		int imageHeight = bufferedImage.getHeight();
		String scaleRetainingAspectRatio = getScale(imageWidth, imageHeight);
		String jpegHeader = "\\pict\\picscalex" + scaleRetainingAspectRatio + "\\picscaley" + scaleRetainingAspectRatio + "\\piccropl0\\piccropr0\\piccropt0\\piccropb0\\picw" + imageWidth + "\\pich" + imageHeight + "\\jpegblip ";
		getWriter().writeln(jpegHeader);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		SaveImageJPEGDoer.saveJpeg(baos, bufferedImage);	
		byte[] imageAsBytes = baos.toByteArray();
		baos.close();
		final int BYTES_PER_LINE = 32;
		for (int i = 0; i < imageAsBytes.length; ++i)
		{
			byte b = imageAsBytes[i];
			getWriter().write(toHex(b));
			
			if(i % BYTES_PER_LINE == 0)
				  getWriter().writeln();
		}
		
		endBlock();
	}

	private String getScale(int imageWidth, int imageHeight)
	{
		return Integer.toString(calculateScale(imageWidth, imageHeight));
	}
	
	public static int calculateScale(int imageWidth, int imageHeight)
	{
		// NOTE: RTF inches are (pixels * 1.04) / 100 
		// so 11x8 inches minus margins and diagram title is about 800x600
		final int MAX_WIDTH = 800;
		final int MAX_HEIGHT = 600;
		double rawXScale = calculateSingleLenghtScale(imageWidth, MAX_WIDTH);
		double rawYScale = calculateSingleLenghtScale(imageWidth, MAX_HEIGHT);
		
		double scalePreservingAspectRatio = Math.min(rawXScale, rawYScale);
		
		return (int)scalePreservingAspectRatio;
	}

	private static double calculateSingleLenghtScale(int lenght, int maxLength)
	{
		// NOTE: RTF pre-scales images to fit the page, so we just have to scale 
		// images from page-size down to small enough to allow for margins and 
		// the diagram title. 72% happens to work well.
		final int DEFUALT_SCALE = 72;
		final int FULL_SCALE = 100;
		
		if (lenght < maxLength)
			return FULL_SCALE;
		
		return DEFUALT_SCALE;
	}

	void startBlock() throws Exception
	{
		write(START_BLOCK);
	}
	
	public void endBlock() throws Exception
	{
		writeln(END_BLOCK);
	}

	static public String toHex(byte b)
	{
		int i = b & 0xFF;
		int firstNibble = i/16;
		String firstDigit = Integer.toHexString(firstNibble);
		
		int secondNibble = i%16;
		String secondDigit = Integer.toHexString(secondNibble);
		
		return firstDigit + secondDigit;
	}
	
	public void writeRtfTable(AbstractTableExporter exportableTable) throws Exception
	{
		writeTableHeader(exportableTable);
		writeTableBody(exportableTable);
	}

	private void writeTableBody(AbstractTableExporter exportableTable) throws Exception
	{
		for (int row = 0; row < exportableTable.getRowCount(); ++row)
		{
			writeTableRow(exportableTable, row);
		}
	}

	private void writeTableRow(AbstractTableExporter exportableTable, int row) throws Exception
	{
		writeRowData(exportableTable, row);
	}

	private void writeRowData(AbstractTableExporter exportableTable, int row) throws Exception
	{
		final int COLUMN_TO_PAD = 0;
		writeRtfCommand(ROW_HEADER);
		writeCellCommands(exportableTable);
		for (int column = 0; column < exportableTable.getColumnCount(); ++column)
		{
			writeRtfCommand(PRE_CELL_COMMAND);
			Icon cellIcon = exportableTable.getIconAt(row, column);
			if (cellIcon != null)
				writeImage(BufferedImageFactory.getImage(cellIcon));

			String cellStyleTag = exportableTable.getStyleTagAt(row, column);
			String styleFormattingCommand = getRtfStyleManager().getStyleFormatingCommand(cellStyleTag);
			writeln(styleFormattingCommand);
			
			int paddingCount = exportableTable.getDepth(row);
			if (column == COLUMN_TO_PAD)
				createPadding(paddingCount);
			writeEncoded(exportableTable.getTextAt(row, column));
			
			write(CELL_COMMAND);
			newLine();
		}
		
		write(ROW_COMMAND);
		newLine();
	}

	private void writeCellCommands(AbstractTableExporter exportableTable) throws Exception
	{
		for (int column = 0; column < exportableTable.getColumnCount(); ++column)
		{
			write(createCellxCommand(column));	
		}
		
		newLine();
	}

	public String createCellxCommand(final int column)
	{
		return CELL_BORDER + CELL_X_COMMAND + ((column  + 1) * ONE_INCH_IN_TWIPS );
	}

	private void writeTableHeader(AbstractTableExporter exportableTable) throws Exception
	{
		writeln(TABLE_ROW_HEADER);
		writeCellCommands(exportableTable);
		for (int columnIndex = 0; columnIndex < exportableTable.getColumnCount(); ++columnIndex)
		{
			String header = exportableTable.getHeaderFor(columnIndex);
			write(PRE_TABLE_HEADER_CELL_COMMAND);
			startBlock();
			write(PRE_TABLE_HEADER_CELL_DATA_COMMAND);
			writeEncoded(header);
			endBlock();
			
			write(CELL_COMMAND);
			newLine();
		}
		
		write(ROW_COMMAND);
		newLine();
	}
	
	public static String encode(String stringToEncode)
	{
		String encodedString = stringToEncode.replaceAll("\\\\", "\\\\\\\\");
		encodedString = encodedString.replaceAll("\\}", "\\\\}");
		encodedString = encodedString.replaceAll("\\{", "\\\\{");
		
		StringBuffer buffer = new StringBuffer(encodedString);
		for(int i = 0; i < buffer.length(); ++i)
		{
			char c = buffer.charAt(i);
			if (c >= 128)
			{
				String hexValue = toHex(c);
				buffer.replace(i, i+1, "\\u" + hexValue.toUpperCase());
			}
		}
		
		return buffer.toString();
	}
	
	static public String toHex(char c)
	{
		int i = c & 0xFFFF;
		
		int firstNibble = (i & 0xF000) / 0x1000;
		String firstDigit = Integer.toHexString(firstNibble);
		
		int secondNibble = (i & 0x0F00) / 0x0100;
		String secondDigit = Integer.toHexString(secondNibble);
		
		int thirdNibble = (i & 0x00F0) / 0x0010;
		String thirdDigit = Integer.toHexString(thirdNibble);
		
		int forthNibble = (i & 0x000F) / 0x001;
		String forthDigit = Integer.toHexString(forthNibble);
		
		return firstDigit + secondDigit + thirdDigit + forthDigit;
	}
	
	public void createPadding(int padCount) throws Exception
	{
		for (int i = 0; i < padCount; ++i)
		{
			insertTab();
		}
	}
	
	public void startRtf() throws Exception
	{
		writeRtfHeader();
		writeFontTable();
		writeColorTable();
		writeInfo();
		getRtfStyleManager().exportRtfStyleTable(this);
	}

	private void writeRtfHeader() throws Exception
	{
		writeln("{\\rtf1\\ansi\\deff0\\adeflang1025");
	}
	
	private void writeFontTable() throws Exception
	{
		newLine();
		writeln("{\\fonttbl");
			writeln("{\\f0\\froman\\fcharset0\\fprq2{\\*\\panose 02020603050405020304}Times New Roman;}");
			writeln("{\\f1\\fswiss\\fcharset0\\fprq2{\\*\\panose 020b0604020202020204}Arial;}");
			writeln("{\\f37\\froman\\fcharset238\\fprq2 Times New Roman CE;}");
			writeln("{\\f38\\froman\\fcharset204\\fprq2 Times New Roman Cyr;}");
			writeln("{\\f40\\froman\\fcharset161\\fprq2 Times New Roman Greek;}");
			writeln("{\\f41\\froman\\fcharset162\\fprq2 Times New Roman Tur;}");
			writeln("{\\f42\\fbidi \\froman\\fcharset177\\fprq2 Times New Roman (Hebrew);}");
			writeln("{\\f43\\fbidi \\froman\\fcharset178\\fprq2 Times New Roman (Arabic);}");
			writeln("{\\f44\\froman\\fcharset186\\fprq2 Times New Roman Baltic;}");
			writeln("{\\f45\\froman\\fcharset163\\fprq2 Times New Roman (Vietnamese);}");
			writeln("{\\f47\\fswiss\\fcharset238\\fprq2 Arial CE;}");
			writeln("{\\f48\\fswiss\\fcharset204\\fprq2 Arial Cyr;}");
			writeln("{\\f50\\fswiss\\fcharset161\\fprq2 Arial Greek;}");
			writeln("{\\f51\\fswiss\\fcharset162\\fprq2 Arial Tur;}");
			writeln("{\\f52\\fbidi \\fswiss\\fcharset177\\fprq2 Arial (Hebrew);}");
			writeln("{\\f53\\fbidi \\fswiss\\fcharset178\\fprq2 Arial (Arabic);}");
			writeln("{\\f54\\fswiss\\fcharset186\\fprq2 Arial Baltic;}");
			writeln("{\\f55\\fswiss\\fcharset163\\fprq2 Arial (Vietnamese);}");
		writeln("}");
	}

	private void writeColorTable() throws Exception
	{
		newLine();
		writeln("{\\colortbl;" +
				"\\red0\\green0\\blue0;" +
				"\\red0\\green0\\blue255;" +
				"\\red0\\green255\\blue255;" +
				"\\red0\\green255\\blue0;" +
				"\\red255\\green0\\blue255;" +
				"\\red255\\green0\\blue0;" +
				"\\red255\\green255\\blue0;" +
				"\\red255\\green255\\blue255;" +
				"\\red0\\green0\\blue128;" +
				"\\red0\\green128\\blue128;" +
				"\\red0\\green128\\blue0;" +
				"\\red128\\green0\\blue128;" +
				"\\red128\\green0\\blue0;" +
				"\\red128\\green128\\blue0;" +
				"\\red128\\green128\\blue128;" +
				"\\red192\\green192\\blue192;}");
	}

	private void writeInfo() throws Exception
	{
		newLine();
		writeln("{\\info");
			writeln("{\\title Miradi}");
			writeln("{\\author Miradi}");
			writeln("{\\operator Miradi}");
			writeln("{\\creatim\\yr2008\\mo8\\dy26\\hr15}");
			writeln("{\\revtim\\yr2008\\mo8\\dy27\\hr10\\min24}");
			writeln("{\\edmins140}");
			writeln("{\\nofpages1}");
			writeln("{\\nofwords4}");
			writeln("{\\nofchars23}");
			writeln("{\\nofcharsws26}");
			writeln("{\\vern24613}");
		writeln("}");
	}

	public void endRtf() throws Exception
	{
		endBlock();
	}	

	public void newParagraph() throws Exception
	{
		writeSingleCommand(PARAGRAPH_COMMAND);	
	}
	
	public void insertTab() throws Exception
	{
		write(TAB_COMMAND);	
	}
	
	public void pageBreak() throws Exception
	{
		write(PAGE_BREAK_COMMAND);
	}
	
	public void landscapeMode() throws Exception
	{
		writeRtfCommand(LANDSCAPE_COMMAND);
	}
	
	public void writeSingleCommand(String command) throws Exception
	{
		startBlock();
		writeRtfCommand(command);
		endBlock();
	}
	
	public void newLine() throws Exception
	{
		writeln("");
	}
	
	public UnicodeWriter getWriter()
	{
		return writer;
	}
	
	public RtfStyleManager getRtfStyleManager()
	{
		return rtfStyleManager;
	}
	
	private UnicodeWriter writer;
	private RtfStyleManager rtfStyleManager;

	public static final int ONE_INCH_IN_TWIPS = 1440;
	public static final String START_BLOCK = "{";
	public static final String END_BLOCK = "}";
	public static final String CELL_X_COMMAND = "\\cellx";
	public static final String CELL_COMMAND = " \\cell ";
	public static final String ROW_COMMAND = "\\row ";
	public static final String PARAGRAPH_COMMAND = "\\pard\\par ";
	public static final String ROW_HEADER = "\\pard \\trowd\\trql\\trpaddft3\\trpaddt55\\trpaddfl3\\trpaddl55\\trpaddfb3\\trpaddb55\\trpaddfr3\\trpaddr55 ";
	public static final String TABLE_ROW_HEADER = "\\trowd\\trql\\trhdr\\trpaddft3\\trpaddt55\\trpaddfl3\\trpaddl55\\trpaddfb3\\trpaddb55\\trpaddfr3\\trpaddr55 ";
	public static final String LANDSCAPE_COMMAND = "\\landscape\\paperh12240\\paperw15840\\margl1134\\margr1134\\margt1134\\margb1134\\sectd\\sbknone\\lndscpsxn ";
	public static final String PAGE_BREAK_COMMAND = "\\par \\page\\pard\\plain \\ltrpar{\\*\\hyphen2\\hyphlead2\\hyphtrail2\\hyphmax0}";
	public static final String BOLD_DIAGRAM_HEADER_FONT_COMMAND = "\\rtlch \\ltrch\\lang1033 ";
	public static final String TAB_COMMAND = "\\tab ";
	
	public static final String PRE_TABLE_HEADER_CELL_COMMAND = " \\intbl\\qc\\b ";
	public static final String PRE_CELL_COMMAND  = "\\pard\\intbl\\plain ";
	public static final String PRE_TABLE_HEADER_CELL_DATA_COMMAND = " ";
	public static final String CELL_BORDER = "\\clbrdrt\\brdrs\\brdrw1\\brdrcf1\\clbrdrl\\brdrs\\brdrw1\\brdrcf1\\clbrdrb\\brdrs\\brdrw1\\brdrcf1\\clbrdrr\\brdrs\\brdrw1\\brdrcf1 ";										  																	
}
