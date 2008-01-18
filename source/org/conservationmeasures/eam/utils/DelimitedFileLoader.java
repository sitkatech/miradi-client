/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.StringTokenizer;
import java.util.Vector;

public class DelimitedFileLoader
{
	static String QUOTE = "\"";

	static String TAB = "\t";

	public static Vector getDelimitedContents(Reader rawReader)
			throws IOException
	{
		BufferedReader reader = new BufferedReader(rawReader);
		
		Vector lineVector = new Vector();
		while(reader.ready())
		{
			String line = reader.readLine();
			if(line == null)
				break;
			StringTokenizer st = new StringTokenizer(line, TAB);
			Vector thisLine = new Vector();
			while(st.hasMoreTokens())
			{
				String token = st.nextToken().trim();
				addElementToLine(thisLine, token);
			}
			lineVector.add(thisLine);
		}
		reader.close();
		return lineVector;
	}


	private static void addElementToLine(Vector thisLine, String token)
	{
		if(token.startsWith(QUOTE))
			token = token.substring(1, token.length() - 1);
		thisLine.addElement(token);
	}

}
