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
package org.miradi.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.swing.Icon;

import org.martus.util.UnicodeWriter;
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
	
	public void writeln(String textToWrite) throws Exception
	{
		getWriter().writeln(textToWrite);
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

	private void startBlock() throws IOException
	{
		getWriter().writeln("{");
	}
	
	private void endBlock() throws IOException
	{
		getWriter().writeln("}");
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
		StringBuffer rowHeaderContent = new StringBuffer("{");
		StringBuffer rowHeaderFormatting = new StringBuffer("{\\trowd \\trhdr \\trautofit1 \\intbl ");
		for (int headerIndex = 0; headerIndex < exportableTable.getColumnCount(); ++headerIndex)
		{
			String header = exportableTable.getHeaderFor(headerIndex);
			rowHeaderContent.append(header + " \\cell ");
			rowHeaderFormatting.append(" \\cellx" + headerIndex + " ");
		}
		rowHeaderContent.append("}");
		rowHeaderFormatting.append(" \\row }");
		
		getWriter().writeln(rowHeaderContent.toString());
		getWriter().writeln(rowHeaderFormatting.toString());
		
		for (int row = 0; row < exportableTable.getRowCount(); ++row)
		{
			StringBuffer rowContent = new StringBuffer("{");
			StringBuffer rowFormating = new StringBuffer("{\\trowd \\trautofit1 \\intbl ");
			for (int column = 0; column < exportableTable.getColumnCount(); ++column)
			{
				//FIXME this is temporaraly and under construction.  
				if (column == 0)
				{
					int paddingCount = exportableTable.getDepth(row);
					rowContent.append(exportableTable.pad(paddingCount, column));
					
					Icon cellIcon = exportableTable.getIconAt(row, column);
					if (cellIcon != null)
						writeImage(BufferedImageFactory.getImage(cellIcon));
					
					
					rowContent.append(exportableTable.getTextAt(row, column) + " \\cell ");
				}
				else
				{
					rowContent.append(exportableTable.getTextAt(row, column) + " \\cell ");
				}
				
				rowFormating.append("\\cellx" + column + " ");	
			}
			rowContent.append("}");
			rowFormating.append(" \\row }");
			
			getWriter().writeln(rowContent.toString());
			getWriter().writeln(rowFormating.toString());
		}
	}
	
	public void startRtf() throws Exception
	{
		getWriter().write("{\\rtf ");
	}
	
	public void endRtf() throws Exception
	{
		getWriter().write("}");
	}

	public UnicodeWriter getWriter()
	{
		return writer;
	}
	
	private UnicodeWriter writer;
}
