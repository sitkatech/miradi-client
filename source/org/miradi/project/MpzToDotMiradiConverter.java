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

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.martus.util.UnicodeStringWriter;
import org.martus.util.UnicodeWriter;
import org.miradi.database.ProjectServer;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objectdata.ObjectData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Factor;
import org.miradi.utils.EnhancedJsonArray;
import org.miradi.utils.EnhancedJsonObject;
import org.miradi.utils.Translation;

public class MpzToDotMiradiConverter extends AbstractMiradiProjectSaver
{
	public static void main(String[] args) throws Exception
	{
		Translation.initialize();
		
		File homeDirectory = EAM.getHomeDirectory();
		String mpzPath = "";

		System.out.println("Converts a .MPZ file to a .Miradi file");
		System.out.println(" The .Miradi file will be created in " + homeDirectory);
		if(args.length == 0)
		{
			System.out.print("Enter the path to the MPZ file: ");
			System.out.flush();
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			mpzPath = reader.readLine();
		}
		else if(args.length == 1)
		{
			mpzPath = args[0];
		}
		else
		{
			System.out.println("Must specify an MPZ file to convert.");
			System.exit(1);
		}
		System.out.println("Converting " + mpzPath);
		
		if(!mpzPath.endsWith(".mpz"))
			throw new RuntimeException("Not an MPZ file: " + mpzPath);

		File mpzFile = new File(mpzPath);
		if(!mpzFile.exists())
			throw new RuntimeException("MPZ doesn't exist: " + mpzFile.getAbsolutePath());

		String newName = mpzFile.getName().replaceAll(".mpz", ".Miradi");
		File destination = new File(homeDirectory, newName);
		if(destination.exists())
			throw new RuntimeException(".Miradi file already exists: " + destination.getAbsolutePath());
		
		String converted = convert(new ZipFile(mpzFile));
		UnicodeWriter writer = new UnicodeWriter(destination);
		writer.write(converted);
		writer.close();
		System.out.println("Converted");
	}
	
	public MpzToDotMiradiConverter(ZipFile mpzFileToUse, UnicodeStringWriter writerToUse) throws Exception
	{
		super(writerToUse);
		
		zipFile = mpzFileToUse;
	}
	
	public static final String convert(ZipFile zipFileToUse) throws Exception
	{
		UnicodeStringWriter writer = UnicodeStringWriter.create();
		convert(zipFileToUse, writer);
		return writer.toString();
	}
	
	public static final void convert(ZipFile zipFileToUse, UnicodeStringWriter writer) throws Exception
	{
		MpzToDotMiradiConverter conveter = new MpzToDotMiradiConverter(zipFileToUse, writer);
		conveter.convert();
	}
	
	private void convert() throws Exception
	{
		Enumeration<? extends ZipEntry> zipEntries = getZipFile().entries();
		while(zipEntries.hasMoreElements())
		{
			ZipEntry entry = zipEntries.nextElement();
			if(entry == null)
				break;
			
			if (!entry.isDirectory())
				extractOneFile(entry);
		}
		getWriter().flush();
		if(convertedProjectVersion != REQUIRED_VERSION)
			throw new RuntimeException("Cannot convert MPZ without a version");
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
			if(convertedProjectVersion != 0)
				throw new RuntimeException("Cannot convert MPZ with two versions");
			EnhancedJsonObject json = new EnhancedJsonObject(fileContent);
			convertedProjectVersion = json.getInt(ProjectServer.TAG_VERSION);
			if(convertedProjectVersion != REQUIRED_VERSION)
				throw new RuntimeException("Cannot convert MPZ version " + convertedProjectVersion);
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
		InputStream inputStream = getZipFile().getInputStream(entry);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int got = -1;
		while( (got = inputStream.read(buffer)) > 0)
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
		final Project tempProject = new Project();
		EnhancedJsonObject json = new EnhancedJsonObject(fileContent);
		BaseObject baseObject = BaseObject.createFromJson(tempProject.getObjectManager(), ref.getObjectType(), json);
		Iterator iterator = json.keys();
		while (iterator.hasNext())
		{
			String tag = (String)iterator.next();
			if (!shouldSkipTag(ref, tag))
			{
				String data = json.get(tag).toString();	
				ObjectData dataField = baseObject.getField(tag);
				if (dataField != null && dataField.isUserText())
					data = xmlNewLineEncode(data);
		
				if(data.length() > 0)
					writeRefTagValue(UPDATE_OBJECT_CODE, ref, tag, data);
			}
		}
	}

	private boolean shouldSkipTag(ORef ref, String tag)
	{
		if(tag.equals(BaseObject.TAG_TIME_STAMP_MODIFIED))
			return true;
		if(tag.equals("BudgetCostOverride"))
			return true;
		if(tag.equals("WhoOverrideRefs"))
			return true;
		if(tag.equals("WhenOverride"))
			return true;
		if(tag.equals("CostUnit"))
			return true;
		
		if(Factor.isFactor(ref) && tag.equals("Type"))
			return true;

		return false;
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
			if(defaultValueId == -1 && ratings.equals("{}"))
				continue;
			
			writeSimpleThreatRatingBundle(threatId, targetId, defaultValueId, ratings);
		}
	}

	private static int findSlash(String name)
	{
		return name.indexOf('/');
	}

	private ZipFile getZipFile()
	{
		return zipFile;
	}

	private static int REQUIRED_VERSION = 61;
	private ZipFile zipFile;
	private int convertedProjectVersion;
}
