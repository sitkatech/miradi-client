/*
 * Copyright 2006, The Conservation Measures Partnership & Beneficent
 * Technology, Inc. (Benetech, at www.benetech.org)
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.Vector;

import org.conservationmeasures.eam.main.EAM;

public class DelimitedFileLoader
{
	static String QUOTE = "\"";

	static String TAB = "\t";

	public static Vector getDelimitedContents(String cvsResourceName)
			throws Exception
	{

		InputStream is = EAM.class.getResourceAsStream(cvsResourceName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		return getDelimitedContents(reader);
	}

	public static Vector getDelimitedContents(BufferedReader reader)
			throws IOException
	{
		Vector lineVec = new Vector();
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
				if(token.startsWith(QUOTE))
					token = token.substring(1, token.length() - 1);
				thisLine.addElement(token);
			}
			lineVec.add(thisLine);
		}
		reader.close();
		return lineVec;
	}

}
