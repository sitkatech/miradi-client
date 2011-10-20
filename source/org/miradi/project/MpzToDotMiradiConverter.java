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
import java.io.InputStream;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.martus.util.UnicodeStringWriter;
import org.miradi.database.ProjectServer;
import org.miradi.ids.BaseId;
import org.miradi.objectdata.ObjectData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.utils.EnhancedJsonArray;
import org.miradi.utils.EnhancedJsonObject;

public class MpzToDotMiradiConverter extends AbstractMiradiProjectSaver
{
	public MpzToDotMiradiConverter(InputStream inputStream, UnicodeStringWriter writerToUse) throws Exception
	{
		super(writerToUse);
		
		zipInputStream = new ZipInputStream(inputStream);
	}
	
	public static final void convert(InputStream inputStream, UnicodeStringWriter writer) throws Exception
	{
		MpzToDotMiradiConverter conveter = new MpzToDotMiradiConverter(inputStream, writer);
		conveter.convert();
	}
	
	private void convert() throws Exception
	{
		while(true)
		{
			ZipEntry entry = getInputStream().getNextEntry();
			if(entry == null)
				break;
			
			if (!entry.isDirectory())
				extractOneFile(entry);
		}
		getWriter().flush();
	}
	
	private void extractOneFile(ZipEntry entry) throws Exception
	{
		byte[] contents = readIntoByteArray(entry);
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
		if (relativeFilePath.equals("json/threatframework"))
		{
			writeSimpleThreatFramework(fileContent);
		}
	}
	
	
	private byte[] readIntoByteArray(ZipEntry entry) throws Exception
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
		writeValue(CREATE_OBJECT_CODE, createSimpleRefString(ref));
		writeUpdateObjectLines(fileContent, ref);
	}

	public void writeUpdateObjectLines(String fileContent, ORef ref) throws Exception
	{
		final Project tempProject = new Project();
		EnhancedJsonObject json = new EnhancedJsonObject(fileContent);
		BaseObject baseObject = BaseObject.createFromJson(tempProject.getObjectManager(), ref.getObjectType(), json);
		Iterator iterator = json.keys();
		while (iterator.hasNext())
		{
			String tag = (String)iterator.next();
			if (!tag.equals(BaseObject.TAG_TIME_STAMP_MODIFIED))
			{
				String data = json.get(tag).toString();	
				ObjectData dataField = baseObject.getField(tag);
				if (dataField != null && dataField.isUserText())
				{
					data = xmlNewLineEncode(data);
				}

				writeRefTagValue(UPDATE_OBJECT_CODE, ref, tag, data);
			}
		}
	}

	private void writeSimpleThreatFramework(String jsonContent) throws Exception
	{
		EnhancedJsonObject json = new EnhancedJsonObject(jsonContent);
		Iterator iterator = json.keys();
		while (iterator.hasNext())
		{
			String tag = (String)iterator.next();
			if(tag.equals("BundleKeys"))
			{
				writeSimpleThreatRatingBundles(json.getJsonArray(tag));
			}
		}
	}

	private void writeSimpleThreatRatingBundles(EnhancedJsonArray jsonForAllBundles) throws Exception
	{
		for(int i = 0; i < jsonForAllBundles.length(); ++i)
		{
			EnhancedJsonObject jsonBundle = jsonForAllBundles.getJson(i);
			int threatId = jsonBundle.getInt("BundleThreatId");
			int targetId = jsonBundle.getInt("BundleTargetId");
			if(threatId < 0 || targetId < 0)
				continue;
			
			int defaultValueId = jsonBundle.optInt("DefaultValueId", -1);
			EnhancedJsonObject jsonRatings = jsonBundle.optJson("Values");
			String ratings = jsonRatings.toString();
			writeSimpleThreatRatingBundle(threatId, targetId, defaultValueId, ratings);
		}
	}

	private static int findSlash(String name)
	{
		return name.indexOf('/');
	}

	private ZipInputStream getInputStream()
	{
		return zipInputStream;
	}
	
	private ZipInputStream zipInputStream;
}
