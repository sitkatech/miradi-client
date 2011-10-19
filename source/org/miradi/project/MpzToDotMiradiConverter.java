/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.project;

import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.martus.util.UnicodeStringWriter;
import org.miradi.database.ProjectServer;
import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.ORef;
import org.miradi.utils.EnhancedJsonObject;

public class MpzToDotMiradiConverter extends ProjectSaver
{
	public MpzToDotMiradiConverter(ZipInputStream zipInputStream, UnicodeStringWriter writerToUse) throws Exception
	{
		super(writerToUse);
		
		inputStream = zipInputStream;
		writer = writerToUse;
	}
	
	public static final void convert(ZipInputStream zipInputStream, UnicodeStringWriter writer) throws Exception
	{
		MpzToDotMiradiConverter conveter = new MpzToDotMiradiConverter(zipInputStream, writer);
		conveter.convert();
	}
	
	private void convert() throws Exception
	{
		try
		{
			while(true)
			{
				ZipEntry entry = getInputStream().getNextEntry();
				if(entry == null)
					break;
				
				if (!entry.isDirectory())
					writeOneFile(entry);
			}
		}
		finally
		{
		}
	}
	
	private void writeOneFile(ZipEntry entry) throws Exception
	{
		byte[] contents = convertToByteArray(entry);
		final String fileContent = new String(contents, "UTF-8");
		
		String relativeFilePath = entry.getName();
		int slashAt = findSlash(relativeFilePath);
		relativeFilePath = relativeFilePath.substring(slashAt + 1);
		
		
		if (relativeFilePath.equals("json/version"))
		{
			EnhancedJsonObject json = new EnhancedJsonObject(fileContent);
			writeTagValue(UPDATE_PROJECT_VERSION_CODE, ProjectServer.TAG_VERSION, json.optString(ProjectServer.TAG_VERSION));
			writeTagValue(UPDATE_PROJECT_VERSION_CODE, ProjectServer.TAG_VERSION, json.optString(ProjectServer.TAG_VERSION));
		}
		if (relativeFilePath.equals("json/project"))
		{
			EnhancedJsonObject json = new EnhancedJsonObject(fileContent);
			writeTagValue(UPDATE_PROJECT_INFO_CODE, ProjectInfo.TAG_HIGHEST_OBJECT_ID, json.optString(ProjectInfo.TAG_HIGHEST_OBJECT_ID));
			writeTagValue(UPDATE_PROJECT_INFO_CODE, ProjectInfo.TAG_PROJECT_METADATA_ID, json.optString(ProjectInfo.TAG_PROJECT_METADATA_ID));
		}
		if (relativeFilePath.equals(ProjectServer.LAST_MODIFIED_FILE_NAME))
		{
			writeTagValue(ProjectSaver.UPDATE_LAST_MODIFIED_TIME_CODE, ProjectSaver.LAST_MODIFIED_TAG, fileContent);
		}
		if (relativeFilePath.startsWith("json/objects") && !relativeFilePath.endsWith("manifest"))
		{
			writeObject(relativeFilePath, fileContent);
		}
	}
	
	
	private byte[] convertToByteArray(ZipEntry entry) throws Exception
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int got = -1;
		while( (got = getInputStream().read(buffer)) > 0)
		{
			out.write(buffer, 0, got);
		}
		out.close();

		return out.toByteArray();
	}
	
	private void writeObject(String relativeFilePath, String fileContent) throws Exception
	{
		String[] splittedPath = relativeFilePath.split("-");
		String[] typeId = splittedPath[1].split("/");
		ORef ref = new ORef(Integer.parseInt(typeId[0]), new BaseId(typeId[1]));
		System.out.println(relativeFilePath +  " = "+ fileContent);
		writeValue(CREATE_OBJECT_CODE, createSimpleRefString(ref));
		writeUpdateObjectLines(fileContent, ref);
	}

	public void writeUpdateObjectLines(String fileContent, ORef ref) throws Exception
	{
		try
		{
			EnhancedJsonObject json = new EnhancedJsonObject(fileContent);
			Iterator iterator = json.keys();
			while (iterator.hasNext())
			{
				String tag = (String)iterator.next();
				final String data = json.get(tag).toString();
				writeRefTagValue(UPDATE_OBJECT_CODE, ref, tag, data);
			}

		}
		catch (Exception e)
		{
			System.out.println("-------------------"+fileContent);
		}

	}

	private static int findSlash(String name)
	{
		return name.indexOf('/');
	}

	private ZipInputStream getInputStream()
	{
		return inputStream;
	}
	
	private UnicodeStringWriter getWriter()
	{
		return writer;
	}
	
	private void writeTagValue(final String actionCode, final String tag, final String value) throws Exception
	{
		write(actionCode);
		write(ProjectSaver.TAB);
		write(tag);
		write(ProjectSaver.EQUALS);
		write(value);
		getWriter().writeln();
	}

	private void write(final String data) throws Exception
	{
		getWriter().write(data);
	}
	
	private ZipInputStream inputStream;
	private UnicodeStringWriter writer;
}
