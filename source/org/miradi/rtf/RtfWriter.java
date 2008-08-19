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
		
		String jpegHeader = "\\pict\\piccropl0\\piccropr0\\piccropt0\\piccropb0\\picw"+bufferedImage.getWidth()+"\\pich"+bufferedImage.getHeight()+"\\jpegblip ";
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

	private void startBlock() throws Exception
	{
		writeRtfCommand("{");
	}
	
	private void endBlock() throws Exception
	{
		writeRtfCommand("}");
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
		writeRtfCommand("\\pard \\trowd\\trql\\trpaddft3\\trpaddt55\\trpaddfl3\\trpaddl55\\trpaddfb3\\trpaddb55\\trpaddfr3\\trpaddr55");
		writeCellCommands(exportableTable);
		for (int column = 0; column < exportableTable.getColumnCount(); ++column)
		{
			
			writeRtfCommand("\\pard\\plain \\intbl\\ltrpar\\s7\\cf0{\\*\\hyphen2\\hyphlead2\\hyphtrail2\\hyphmax0}\\rtlch\\af5\\afs24\\lang255\\ltrch\\dbch\\af3\\langfe255\\hich\\f0\\fs24\\lang1033\\loch\\f0\\fs24\\lang1033 {\\rtlch \\ltrch\\loch\\f0\\fs24\\lang1033\\i0\\b0 ");
			int paddingCount = exportableTable.getDepth(row);
			if (column == COLUMN_TO_PAD)
				writeEncoded(createPadding(paddingCount, column));

			Icon cellIcon = exportableTable.getIconAt(row, column);
			if (cellIcon != null)
				writeImage(BufferedImageFactory.getImage(cellIcon));

			writeEncoded(exportableTable.getTextAt(row, column));
			write("}\\cell ");	
		}
		
		write("\\row");
		newLine();
	}

	private void writeCellCommands(AbstractTableExporter exportableTable) throws Exception
	{
		for (int column = 0; column < exportableTable.getColumnCount(); ++column)
		{
			write("\\cellx" + ((column  + 1) * 1440 )+ " ");	
		}
		
		newLine();
	}

	private void writeTableHeader(AbstractTableExporter exportableTable) throws Exception
	{
		writeln("\\trowd\\trql\\trhdr\\trpaddft3\\trpaddt55\\trpaddfl3\\trpaddl55\\trpaddfb3\\trpaddb55\\trpaddfr3\\trpaddr55");
		writeCellCommands(exportableTable);
		for (int columnIndex = 0; columnIndex < exportableTable.getColumnCount(); ++columnIndex)
		{
			String header = exportableTable.getHeaderFor(columnIndex);
			write("\\pard\\plain \\intbl\\ltrpar\\s8\\cf0\\qc{\\*\\hyphen2\\hyphlead2\\hyphtrail2\\hyphmax0}\\rtlch\\af5\\afs24\\lang255\\ab\\ltrch\\dbch\\af3\\langfe255\\hich\\f0\\fs24\\lang1033\\b\\loch\\f0\\fs24\\lang1033\\b {\\rtlch \\ltrch\\loch\\f0\\fs24\\lang1033\\i0\\b");
			writeEncoded(header);
			write(" }\\cell ");
			newLine();
		}
		
		write("\\row");
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
	
	public String createPadding(int padCount, int column) throws IOException
	{
		final String FIVE_SPACES = "     ";
		String padding = "";
		for (int i = 0; i < padCount; ++i)
		{
			padding += FIVE_SPACES;
		}
		
		return padding;
	}
	
	public void startRtf() throws Exception
	{
		write(
		"{\\rtf1\\ansi\\deff0\\adeflang1025" +
		"{\\fonttbl{\\f0\\froman\\fprq2\\fcharset0 Times New Roman;}{\\f1\\froman\\fprq2\\fcharset0 Times New Roman;}{\\f2\\fswiss\\fprq2\\fcharset0 Arial;}{\\f3\\fnil\\fprq2\\fcharset0 Arial Unicode MS;}{\\f4\\fnil\\fprq2\\fcharset0 MS Mincho;}{\\f5\\fnil\\fprq2\\fcharset0 Tahoma;}{\\f6\\fnil\\fprq0\\fcharset0 Tahoma;}}" +
		"{\\colortbl;\\red0\\green0\\blue0;\\red128\\green128\\blue128;}" +
		"{\\stylesheet{\\s1\\cf0{\\*\\hyphen2\\hyphlead2\\hyphtrail2\\hyphmax0}\\rtlch\\af5\\afs24\\lang255\\ltrch\\dbch\\af3\\langfe255\\hich\\f0\\fs24\\lang1033\\loch\\f0\\fs24\\lang1033\\snext1 Normal;}" +
		"{\\s2\\sb240\\sa120\\keepn\\cf0{\\*\\hyphen2\\hyphlead2\\hyphtrail2\\hyphmax0}\\rtlch\\afs28\\lang255\\ltrch\\dbch\\af4\\langfe255\\hich\\f2\\fs28\\lang1033\\loch\\f2\\fs28\\lang1033\\sbasedon1\\snext3 Heading;}" +
		"{\\s3\\sa120\\cf0{\\*\\hyphen2\\hyphlead2\\hyphtrail2\\hyphmax0}\\rtlch\\af5\\afs24\\lang255\\ltrch\\dbch\\af3\\langfe255\\hich\\f0\\fs24\\lang1033\\loch\\f0\\fs24\\lang1033\\sbasedon1\\snext3 Body Text;}" +
		"{\\s4\\sa120\\cf0{\\*\\hyphen2\\hyphlead2\\hyphtrail2\\hyphmax0}\\rtlch\\af6\\afs24\\lang255\\ltrch\\dbch\\af3\\langfe255\\hich\\f0\\fs24\\lang1033\\loch\\f0\\fs24\\lang1033\\sbasedon3\\snext4 List;}" +
		"{\\s5\\sb120\\sa120\\cf0{\\*\\hyphen2\\hyphlead2\\hyphtrail2\\hyphmax0}\\rtlch\\af6\\afs24\\lang255\\ai\\ltrch\\dbch\\af3\\langfe255\\hich\\f0\\fs24\\lang1033\\i\\loch\\f0\\fs24\\lang1033\\i\\sbasedon1\\snext5 caption;}" +
		"{\\s6\\cf0{\\*\\hyphen2\\hyphlead2\\hyphtrail2\\hyphmax0}\\rtlch\\af6\\afs24\\lang255\\ltrch\\dbch\\af3\\langfe255\\hich\\f0\\fs24\\lang1033\\loch\\f0\\fs24\\lang1033\\sbasedon1\\snext6 Index;}" +
		"{\\s7\\cf0{\\*\\hyphen2\\hyphlead2\\hyphtrail2\\hyphmax0}\\rtlch\\af5\\afs24\\lang255\\ltrch\\dbch\\af3\\langfe255\\hich\\f0\\fs24\\lang1033\\loch\\f0\\fs24\\lang1033\\sbasedon1\\snext7 Table Contents;}" +
		"{\\s8\\cf0\\qc{\\*\\hyphen2\\hyphlead2\\hyphtrail2\\hyphmax0}\\rtlch\\af5\\afs24\\lang255\\ab\\ltrch\\dbch\\af3\\langfe255\\hich\\f0\\fs24\\lang1033\\b\\loch\\f0\\fs24\\lang1033\\b\\sbasedon7\\snext8 Table Heading;}}" +
		"{\\info{\\author Miradi}{\\revtim\\yr0\\mo0\\dy0\\hr0\\min0}{\\printim\\yr0\\mo0\\dy0\\hr0\\min0}{\\comment StarWriter}{\\vern6800}}\\deftab709" +
		"{\\*\\pgdsctbl " +
		"{\\pgdsc0\\pgdscuse195\\pgwsxn12240\\pghsxn15840\\marglsxn1134\\margrsxn1134\\margtsxn1134\\margbsxn1134\\pgdscnxt0 Standard;}}" +
		"\\paperh15840\\paperw12240\\margl1134\\margr1134\\margt1134\\margb1134\\sectd\\sbknone\\pgwsxn12240\\pghsxn15840\\marglsxn1134\\margrsxn1134\\margtsxn1134\\margbsxn1134\\ftnbj\\ftnstart1\\ftnrstcont\\ftnnar\\aenddoc\\aftnrstcont\\aftnstart1\\aftnnrlc");

	}
	
	public void endRtf() throws Exception
	{
		endBlock();
	}	

	public void newParagraph() throws Exception
	{
		writeSingleCommand("\\par ");	
	}
	
	public void landscapeMode() throws Exception
	{
		writeRtfCommand("\\landscape\\paperh12240\\paperw15840\\margl1134\\margr1134\\margt1134\\margb1134\\sectd\\sbknone\\lndscpsxn ");
	}
	
	public void writeSingleCommand(String command) throws Exception
	{
		startBlock();
		writeRtfCommand(command);
		endBlock();
	}
	
	private void newLine() throws Exception
	{
		write("");
	}
	
	public UnicodeWriter getWriter()
	{
		return writer;
	}
	
	private UnicodeWriter writer;
}
