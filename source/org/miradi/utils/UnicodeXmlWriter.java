/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.martus.util.UnicodeWriter;

public class UnicodeXmlWriter extends UnicodeWriter
{
	protected UnicodeXmlWriter(ByteArrayOutputStream outBufferToUse) throws IOException
	{
		super(outBufferToUse);
		
		outputBuffer = outBufferToUse;
	}
	
	public UnicodeXmlWriter(File destination) throws Exception
	{
		super(destination);
	}

	public static UnicodeXmlWriter create() throws IOException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		return new UnicodeXmlWriter(out);
	}
	
	@Override
	public void write(String value) throws IOException
	{
		value = StringUtilities.removeIllegalCharacters(value);
		super.write(value);
	}
	
	@Override
	public String toString()
	{
		try 
		{
			return outputBuffer.toString("UTF-8");
		} 
		catch (UnsupportedEncodingException e) 
		{
			e.printStackTrace();
			return "unavailable";
		}
	}
	
	private ByteArrayOutputStream outputBuffer;
}
