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
		String image = "{\\pict\\piccropl0\\piccropr0\\piccropt0\\piccropb0\\picw"+bufferedImage.getWidth()+"\\pich"+bufferedImage.getHeight()+"\\jpegblip ";
		getWriter().writeln(image);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		SaveImageJPEGDoer.saveJpeg(baos, bufferedImage);	
		byte[] imageAsByte = baos.toByteArray();
		baos.close();
		for (int i = 0; i < imageAsByte.length; ++i)
		{
			byte b = imageAsByte[i];
			getWriter().write(toHex(b));
			if(i % 32 == 0)
				  getWriter().writeln();
		}
		
		getWriter().writeln("}");
	}

	static public String toHex(byte b)
	{
		Integer integer = new Integer((b << 24) >>> 24);
		int i = integer.intValue();
		if ( i < (byte)16 )
			return "0"+Integer.toString(i, 16);
		
		return Integer.toString(i, 16);
	}
	
	public void writeRtfTable(ExportableTableInterface exportableTable) throws Exception
	{
		String rowHeaderContent = "{";
		String rowHeaderFormatting = "{\\trowd \\trgaph \\trhdr \\intbl ";
		for (int headerIndex = 0; headerIndex < exportableTable.getColumnCount(); ++headerIndex)
		{
			rowHeaderContent += exportableTable.getHeaderFor(headerIndex) + " \\cell ";
			rowHeaderFormatting += " \\cellx3000 ";
		}
		rowHeaderContent += "}";
		rowHeaderFormatting += " \\row }";
		
		getWriter().writeln(rowHeaderContent);
		getWriter().writeln(rowHeaderFormatting);
		
		for (int row = 0; row < exportableTable.getRowCount(); ++row)
		{
			String rowContent = "{";
			String rowFormating = "{\\trowd \\trgaph \\intbl ";
			for (int column = 0; column < exportableTable.getColumnCount(); ++column)
			{
				rowContent += exportableTable.getValueAt(row, column) +" \\cell ";
				rowFormating += " \\cellx3000 ";	
			}
			rowContent += "}";
			rowFormating += " \\row }";
			
			getWriter().writeln(rowContent);
			getWriter().writeln(rowFormating);
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
