/*
 * Copyright 2006, The Conservation Measures Partnership & Beneficent
 * Technology, Inc. (Benetech, at www.benetech.org)
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;

public class DelimitedFileLoader
{
	static String QUOTE = "\"";

	static String TAB = "\t";

	public static Vector getDelimitedContents(BufferedReader reader)
			throws IOException
	{
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
