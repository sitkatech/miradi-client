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


public class ProcessDelimitedFile
{
	static String CONST_QOUTE = "\"";
	static String CONST_TAB = "\t";
	
	public static Vector getDelimitedContents(String cvsResourceName) throws Exception {

		InputStream is = EAM.class.getResourceAsStream(cvsResourceName);
        BufferedReader reader = new BufferedReader ( new InputStreamReader ( is ) );
		return getDelimitedContents(reader);
	}
	

	static Vector getDelimitedContents(BufferedReader reader) throws IOException
	{
		Vector lineVec = new Vector();
		while (reader.ready()) {
	            String line = reader.readLine();
	            if (line==null) return lineVec;
	            StringTokenizer st = new StringTokenizer(line, CONST_TAB);
	            lineVec.addElement(new Vector());
	            while(st.hasMoreTokens()) {
	            	Vector thisLine = (Vector)lineVec.lastElement();
	            	String token = st.nextToken().trim();
	            	if (token.startsWith(CONST_QOUTE)) token = token.substring(1, token.length()-1);
	            	thisLine.add(token);
	            }
		}
		return lineVec;
	}


}
