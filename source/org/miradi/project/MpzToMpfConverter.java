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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.martus.util.UnicodeStringWriter;
import org.martus.util.UnicodeWriter;
import org.miradi.database.ProjectServer;
import org.miradi.exceptions.UserCanceledException;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objectdata.ObjectData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.utils.EnhancedJsonArray;
import org.miradi.utils.EnhancedJsonObject;
import org.miradi.utils.NullProgressMeter;
import org.miradi.utils.ProgressInterface;
import org.miradi.utils.Translation;

public class MpzToMpfConverter extends AbstractMiradiProjectSaver
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
		
		String converted = convert(new ZipFile(mpzFile), new NullProgressMeter());
		UnicodeWriter writer = new UnicodeWriter(destination);
		writer.write(converted);
		writer.close();
		System.out.println("Converted");
	}
	
	public MpzToMpfConverter(ZipFile mpzFileToUse, UnicodeStringWriter writerToUse) throws Exception
	{
		super(writerToUse);
		
		zipFile = mpzFileToUse;
	}
	
	public static final String convert(ZipFile zipFileToUse, ProgressInterface progressIndicator) throws Exception
	{
		UnicodeStringWriter writer = UnicodeStringWriter.create();
		convert(zipFileToUse, writer, progressIndicator);
		return writer.toString();
	}
	
	public static final void convert(ZipFile zipFileToUse, UnicodeStringWriter writer, ProgressInterface progressIndicator) throws Exception
	{
		MpzToMpfConverter conveter = new MpzToMpfConverter(zipFileToUse, writer);
		conveter.convert(progressIndicator);
	}
	
	private void convert(ProgressInterface progressIndicator) throws Exception
	{
		EAM.logWarning("MPZ converter is not yet handling quarantine");
		
		writeFileHeader();
		
		progressIndicator.setStatusMessage(EAM.text("Scanning..."), 1);
		int zipEntryCount = getZipEntryCount(getZipFile());
		progressIndicator.setStatusMessage(EAM.text("Reading..."), zipEntryCount);

		Enumeration<? extends ZipEntry> zipEntries = getZipFile().entries();
		while(zipEntries.hasMoreElements())
		{
			progressIndicator.incrementProgress();
			if(progressIndicator.shouldExit())
				throw new UserCanceledException();
			
			ZipEntry entry = zipEntries.nextElement();
			if(entry == null)
				break;
			
			if (!entry.isDirectory())
				extractOneFile(entry);
		}
		writeStopMarker(lastModifiedMillis);
		getWriter().flush();
		if(convertedProjectVersion != REQUIRED_VERSION)
			throw new RuntimeException("Cannot convert MPZ without a version");
	}
	
	private int getZipEntryCount(ZipFile zipFile2)
	{
		int count = 0;
		
		Enumeration<? extends ZipEntry> zipEntries = getZipFile().entries();
		while(zipEntries.hasMoreElements())
		{
			zipEntries.nextElement();
			++count;
		}
		
		return count;
	}

	private void extractOneFile(ZipEntry entry) throws Exception
	{
		String relativeFilePath = entry.getName();
		int slashAt = findSlash(relativeFilePath);
		relativeFilePath = relativeFilePath.substring(slashAt + 1);

		if (relativeFilePath.startsWith(EAM.EXCEPTIONS_LOG_FILE_NAME))
		{
			convertExceptionLog(entry);
			return;
		}

		byte[] contents = readIntoByteArray(entry);
		final String fileContent = new String(contents, "UTF-8");
		
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
			String trimmed = fileContent.trim();
			try
			{
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
				Date lastModified = dateFormat.parse(trimmed);
				lastModifiedMillis = lastModified.getTime();
			}
			catch(Exception e)
			{
				lastModifiedMillis = System.currentTimeMillis();
			}
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

	private void convertExceptionLog(ZipEntry entry) throws Exception
	{
		long totalSize = entry.getSize();
		final int MAX_EXCEPTION_LOG_SIZE = 20000;
		long availableUpTo20k = Math.min(totalSize, MAX_EXCEPTION_LOG_SIZE);
		byte[] exceptionLogBytes = new byte[(int)availableUpTo20k];

		InputStream in = getZipFile().getInputStream(entry);
		try
		{
			in.skip(totalSize - availableUpTo20k);
			int got = in.read(exceptionLogBytes);
			if(got != availableUpTo20k)
				throw new IOException("convertExceptionLog Tried to read " + availableUpTo20k + " but got " + got);
		}
		finally
		{
			in.close();
		}
		
		String exceptionLog = safeConvertUtf8BytesToString(exceptionLogBytes);
		
		final String xmlNewLineEncode = xmlNewLineEncode(exceptionLog);
		writeTagValue(UPDATE_EXCEPTIONS_CODE, EXCEPTIONS_DATA_TAG, xmlNewLineEncode);
	}

	public static String safeConvertUtf8BytesToString(byte[] exceptionLogBytes) throws UnsupportedEncodingException
	{
		int startOfUtf8Character = findStartOfFirstValueUtf8Character(exceptionLogBytes);
		int length = exceptionLogBytes.length - startOfUtf8Character;
		String exceptionLog = new String(exceptionLogBytes, startOfUtf8Character, length, "UTF-8");
		return exceptionLog;
	}

	private static int findStartOfFirstValueUtf8Character(byte[] exceptionLogBytes)
	{
		int startOfUtf8Character = 0;
		while(startOfUtf8Character < exceptionLogBytes.length && exceptionLogBytes[startOfUtf8Character] < 0)
			++startOfUtf8Character;
		return startOfUtf8Character;
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
		String[] legalTags = baseObject.getFieldTags();
		for(int i = 0; i < legalTags.length; ++i)
		{
			String tag = legalTags[i];
			String data = json.optString(tag);
			if(data.length() == 0)
				continue;

			ObjectData dataField = baseObject.getField(tag);
			if (dataField.isUserText())
				data = xmlNewLineEncode(data);

			writeRefTagValue(UPDATE_OBJECT_CODE, ref, tag, data);
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
	private long lastModifiedMillis;
}
