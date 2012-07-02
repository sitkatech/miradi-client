/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.martus.util.UnicodeReader;
import org.martus.util.UnicodeStringReader;
import org.miradi.legacyprojects.ObjectManifest;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.project.AbstractMiradiProjectSaver;
import org.miradi.project.MpzToMpfConverter;
import org.miradi.project.ProjectLoader;

public class MpfToMpzConverter
{
	public MpfToMpzConverter()
	{
		refToJsonMap = new HashMap<ORef, EnhancedJsonObject>();
	}
	
	public void convert(File mpfFile, File mpzFileToSaveTo, String projectNameToUse) throws Exception
	{
		projectName = projectNameToUse;
		String contents = UnicodeReader.getFileContents(mpfFile);
		final UnicodeStringReader reader = new UnicodeStringReader(contents);
		load(reader);
		createZipFile(mpzFileToSaveTo);
	}

	private void createZipFile(File mpzFileToSaveTo) throws Exception
	{
		final FileOutputStream fileOutputStream = new FileOutputStream(mpzFileToSaveTo);
		try
		{
			writeZipStream(fileOutputStream);
		}
		finally
		{
			fileOutputStream.flush();
			fileOutputStream.close();
		}
	}

	public void writeZipStream(final FileOutputStream fileOutputStream) throws Exception
	{
		ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
		try
		{
			addVersionEntry(zipOutputStream);
			addProjectEntry(zipOutputStream);

			Set<ORef> keys = refToJsonMap.keySet();
			Vector<ORef> sortedKeys = new Vector<ORef>(keys);
			Collections.sort(sortedKeys);
			for (ORef refAsKey : sortedKeys)
			{
				final String objectDir = "/json/objects-" + refAsKey.getObjectType() + "/";
				final String zipEntryName = objectDir + refAsKey.getObjectId();
				EnhancedJsonObject value = refToJsonMap.get(refAsKey);
				writeZipEntry(zipOutputStream, zipEntryName, value.toString());
			}
			
			addManifestFiles(zipOutputStream);
		}
		finally
		{
			zipOutputStream.flush();
			zipOutputStream.close();
		}
	}
	
	private void addManifestFiles(ZipOutputStream zipOutputStream) throws Exception
	{
		HashMap<Integer, ORefList> typeToReflistMap = new HashMap<Integer, ORefList>();
		Set<ORef> refs = refToJsonMap.keySet();
		for (ORef ref : refs)
		{
			ORefList refsForType = typeToReflistMap.get(ref.getObjectType());
			if (refsForType == null)
				refsForType = new ORefList();
			
			refsForType.add(ref);
			typeToReflistMap.put(ref.getObjectType(), refsForType);
		}
		
		Set<Integer> objectTypes = typeToReflistMap.keySet();
		Vector<Integer> sortedObjectTypes = new Vector<Integer>(objectTypes);
		Collections.sort(sortedObjectTypes);
		for (Integer objectType : objectTypes)
		{
			
			ORefList ids = typeToReflistMap.get(objectType);
			ObjectManifest objectManifest = createObjectManifest(ids);
			final String string = objectManifest.toJson().toString();
			writeZipEntry(zipOutputStream, "/json/objects-" + objectType + "/manifest", string);
		}
	}

	private ObjectManifest createObjectManifest(ORefList ids)
	{
		ObjectManifest objectManifest = new ObjectManifest();
		for (int index = 0; index < ids.size(); ++index)
		{
			objectManifest.put(ids.get(index).getObjectId());
		}
		
		return objectManifest;
	}

	private void addProjectEntry(ZipOutputStream zipOutputStream) throws Exception
	{
		final String zipEntryValue = new String("{\"ProjectMetadataId\":0,\"HighestUsedNodeId\":" + findHighestId() + "}");
		writeZipEntry(zipOutputStream, "/json/project", zipEntryValue);
	}

	private String findHighestId()
	{
		int highestId = -1;
		Set<ORef> refs = refToJsonMap.keySet();
		for (ORef ref : refs)
		{
			highestId = Math.max(highestId, ref.getObjectId().asInt());
		}
		
		return Integer.toString(highestId);
	}

	private void addVersionEntry(ZipOutputStream zipOutputStream) throws Exception
	{
		String versionString = "{\"Version\":" + Integer.toString(MpzToMpfConverter.REQUIRED_VERSION) + "}";
		writeZipEntry(zipOutputStream, "/json/version", versionString);
	}

	private void writeZipEntry(ZipOutputStream zipOutputStream, final String fileName, String zipContent) throws Exception
	{
		ZipEntry versionEntry = new ZipEntry(projectName + fileName);
		zipOutputStream.putNextEntry(versionEntry);
		zipOutputStream.write(getUtf8Bytes(zipContent));
		zipOutputStream.closeEntry();
	}

	private void load(UnicodeStringReader reader) throws Exception
	{
		boolean foundEnd = false;
		while(true)
		{
			String line = reader.readLine();
			if(line == null)
				break;

			if (line.startsWith(AbstractMiradiProjectSaver.STOP_MARKER))
			{
				foundEnd = true;
				continue;
			}
			else if(foundEnd)
			{
				throw new IOException("Project file is corrupted (data after end marker)");
			}

			processLine(line);
		}

		if(!foundEnd)
			throw new IOException("Project file is corrupted (no end marker found)");
	}
	
	private void processLine(String line) throws Exception
	{
		if (line.startsWith(AbstractMiradiProjectSaver.CREATE_OBJECT_CODE))
		{
			ORef ref = ProjectLoader.extractRefFromLine(line);
			EnhancedJsonObject jsonObjects = new EnhancedJsonObject();
			jsonObjects.putId("Id", ref.getObjectId());
			refToJsonMap.put(ref, jsonObjects);
		}
		else if (line.startsWith(AbstractMiradiProjectSaver.UPDATE_OBJECT_CODE))
		{
			loadUpdateObjectline(line);
		}
	}
	
	private void loadUpdateObjectline(String line) throws Exception
	{
		StringTokenizer tokenizer = new StringTokenizer(line);
		/*String command =*/ tokenizer.nextToken();
		String refString = tokenizer.nextToken();
		ORef ref = ProjectLoader.extractRef(refString);
		String tag = tokenizer.nextToken(ProjectLoader.EQUALS_DELIMITER_TAB_PREFIXED);
		EnhancedJsonObject jsonObjects = refToJsonMap.get(ref);
		String json = StringUtilities.substringAfter(line, ProjectLoader.EQUALS_DELIMITER);
		json = HtmlUtilities.convertHtmlToPlainText(json);
		jsonObjects.put(tag, json);
		refToJsonMap.put(ref, jsonObjects);
	}
	
	private byte[] getUtf8Bytes(final String string) throws Exception
	{
		return string.getBytes("UTF-8");
	}
	
	private String projectName;
	private HashMap<ORef, EnhancedJsonObject> refToJsonMap;
}