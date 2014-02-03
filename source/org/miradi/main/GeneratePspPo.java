/* 
Copyright 2005-2013, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.main;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

import org.martus.common.i18n.PoLoader;
import org.martus.common.i18n.TranslationEntry;
import org.martus.common.i18n.TranslationFileContents;
import org.martus.common.i18n.TranslationFileHeader;
import org.martus.util.UnicodeReader;

public class GeneratePspPo
{

	public static void main(String[] args) throws Exception
	{
		File originalPoFile = new File(args[0]);
		UnicodeReader reader = new UnicodeReader(originalPoFile);
		PoLoader loader = new PoLoader();
		TranslationFileContents contents = loader.read(reader, "psp");
		TranslationFileHeader header = contents.getHeader();
		
		output("# Language PSP translations for PACKAGE package.");
		output("# Copyright (C) 2013 Benetech");
		output("# This file is distributed under the same license as the PACKAGE package.");
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String formatted = dateFormat.format(new Date());
		output("# Automatically generated, " + formatted);
		
		HashSet<String> ids = new HashSet<String>();
		
		for(TranslationEntry entry : contents.getEntries())
		{
			String msgid = entry.getMsgid();
			if(ids.contains(msgid))
				throw new RuntimeException("Duplicate ids: " + msgid);
			ids.add(msgid);
			
			if(msgid.length() == 0)
				throw new RuntimeException("Wasn't expecting empty msgid");
			
			String msgstr = entry.getMsgstr();
		
			if(msgid.startsWith("html|"))
			{
				msgstr = getConvertedPrefixedString(msgid);
			}
			else if(msgid.startsWith("FieldLabel|"))
			{
				msgstr = getConvertedPrefixedString(msgid);
			}
			else if(msgid.startsWith("choice|"))
			{
				msgstr = getConvertedPrefixedString(msgid);
			}
			else
			{
				msgstr = getConvertedString(msgid);
			}
			
			String originalMsgstr = entry.getMsgstr();
			if(!isCompatible(msgstr, originalMsgstr))
			{
				System.err.println("# NOTE: Replacing different translation");
				System.err.println("# " + msgstr);
				System.err.println("# " + originalMsgstr);
			}


			output("msgid" + " \"" + msgid + "\"");
			output("msgstr" + " \"" + msgstr + "\"");
			output("");
		}
	}
	
	private static boolean isCompatible(String converted, String original)
	{
		if(original.length() == 0)
			return true;
		
		if(converted.equals(original))
			return true;
		
		return false;
	}

	private static String getConvertedPrefixedString(String msgid)
	{
		int lastBar = msgid.lastIndexOf('|');
		String prefix = msgid.substring(0, lastBar);
		String remainder = msgid.substring(lastBar);
		
		String convertedRemainder = getConvertedString(remainder);
		return prefix + convertedRemainder;
	}

	private static String getConvertedString(String msgid)
	{
		String msgstr;
		msgstr = msgid;
		msgstr = msgstr.replaceAll("Target", "Component");
		msgstr = msgstr.replaceAll("target", "component");

		msgstr = msgstr.replaceAll("Direct Threat", "Pressure");
		msgstr = msgstr.replaceAll("Direct threat", "Pressure");
		msgstr = msgstr.replaceAll("direct threat", "pressure");
		msgstr = msgstr.replaceAll("Threat", "Pressure");
		msgstr = msgstr.replaceAll("threat", "pressure");
		
		msgstr = msgstr.replaceAll("pressureened", "threatened");

		return msgstr;
	}

	public static void output(String text)
	{
		System.out.println(text);
	}
}
