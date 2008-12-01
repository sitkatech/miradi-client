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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.StringTokenizer;
import java.util.Vector;

public class DelimitedFileLoader
{
	static String QUOTE = "\"";

	static String TAB = "\t";
	
	static String COMMENT = "#";

	public Vector<Vector<String>> getDelimitedContents(Reader rawReader)
			throws IOException
	{
		BufferedReader reader = new BufferedReader(rawReader);
		
		Vector<Vector<String>> lineVector = new Vector();
		while(reader.ready())
		{
			String line = reader.readLine();
			if(line == null)
				break;
			
			if (line.startsWith(COMMENT))
				continue;
			
			line = translateLine(line);
			StringTokenizer st = new StringTokenizer(line, TAB);
			Vector<String> thisLine = new Vector();
			while(st.hasMoreTokens())
			{
				String token = st.nextToken().trim();
				addElementToLine(thisLine, token);
			}
			if(thisLine.size() > 0)
				lineVector.add(thisLine);
		}
		reader.close();
		return lineVector;
	}


	protected String translateLine(String line)
	{
		return line;
	}


	private static void addElementToLine(Vector thisLine, String token)
	{
		if(token.startsWith(QUOTE))
			token = token.substring(1, token.length() - 1);
		thisLine.addElement(token);
	}

}
