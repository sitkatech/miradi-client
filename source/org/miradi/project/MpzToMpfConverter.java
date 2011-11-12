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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.martus.util.UnicodeStringWriter;
import org.martus.util.UnicodeWriter;
import org.miradi.database.ProjectServer;
import org.miradi.exceptions.UserCanceledException;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Cause;
import org.miradi.objects.Target;
import org.miradi.project.threatrating.RatingValueSet;
import org.miradi.project.threatrating.SimpleThreatRatingFramework;
import org.miradi.project.threatrating.ThreatRatingBundle;
import org.miradi.utils.EnhancedJsonArray;
import org.miradi.utils.EnhancedJsonObject;
import org.miradi.utils.NullProgressMeter;
import org.miradi.utils.ProgressInterface;
import org.miradi.utils.Translation;

public class MpzToMpfConverter
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
	
	public static final String convert(File zipFile, ProgressInterface progressIndicator) throws Exception
	{
		ZipFile zip = new ZipFile(zipFile);
		try
		{
			return convert(zip, progressIndicator);
		}
		finally
		{
			zip.close();
		}
	}
	
	private static final String convert(ZipFile zipFileToUse, ProgressInterface progressIndicator) throws Exception
	{
		MpzToMpfConverter converter = new MpzToMpfConverter(zipFileToUse);
		Project project = converter.convert(progressIndicator);
		UnicodeStringWriter writer = UnicodeStringWriter.create();
		ProjectSaver.saveProject(project, writer);
		return writer.toString();
	}
	
	public static int extractVersion(ZipFile mpzFileToUse) throws Exception
	{
		MpzToMpfConverter converter = new MpzToMpfConverter(mpzFileToUse);
		return converter.extractVersion();
	}
	
	private MpzToMpfConverter(ZipFile mpzFileToUse) throws Exception
	{
		zipFile = mpzFileToUse;
		entries = extractZipEntries();
		
		project = new Project();
	}
	
	public static void extractFile(InputStream mpzInputStream, File temporaryMpz) throws Exception
	{
		FileOutputStream out = new FileOutputStream(temporaryMpz);
		try
		{
			copyStream(mpzInputStream, out);
		}
		finally
		{
			out.close();
		}
	}

	private Project convert(ProgressInterface progressIndicator) throws Exception
	{
		EAM.logWarning("MPZ converter is not yet handling quarantine");
		
		project.clear();
		
		progressIndicator.setStatusMessage(EAM.text("Scanning..."), 1);
		progressIndicator.setStatusMessage(EAM.text("Reading..."), entries.size());

		for(int i = 0; i < entries.size(); ++i)
		{
			ZipEntry entry = entries.get(i);
			if(entry == null)
				break;
			
			if (!entry.isDirectory())
				extractOneFile(entry);

			progressIndicator.incrementProgress();
			if(progressIndicator.shouldExit())
				throw new UserCanceledException();
		}

		if(convertedProjectVersion != REQUIRED_VERSION)
			throw new RuntimeException("Cannot convert MPZ without a version");
		
		return project;
	}
	
	private Vector<ZipEntry> extractZipEntries()
	{
		Vector<ZipEntry> zipEntries = new Vector<ZipEntry>();
		
		Enumeration<? extends ZipEntry> enumeration = getZipFile().entries();
		while(enumeration.hasMoreElements())
		{
			ZipEntry entry = enumeration.nextElement();
			zipEntries.add(entry);
		}
		
		return zipEntries;
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

		final String fileContent = readIntoString(entry);
		
		if (relativeFilePath.equals("json/version"))
		{
			if(convertedProjectVersion != 0)
				throw new RuntimeException("Cannot convert MPZ with two versions");
			int version = extractVersion(fileContent);
			convertedProjectVersion = version;
			if(convertedProjectVersion != REQUIRED_VERSION)
				throw new RuntimeException("Cannot convert MPZ version " + convertedProjectVersion);
		}
		if (relativeFilePath.equals("json/project"))
		{
			EnhancedJsonObject json = new EnhancedJsonObject(fileContent);
			BaseId metadataId = new BaseId(json.optString(ProjectInfo.TAG_PROJECT_METADATA_ID));
			project.getProjectInfo().setMetadataId(metadataId);
			BaseId highestId = new BaseId(json.optString(ProjectInfo.TAG_HIGHEST_OBJECT_ID));
			project.getProjectInfo().getNormalIdAssigner().idTaken(highestId);
		}
		if (relativeFilePath.equals(ProjectServer.LAST_MODIFIED_FILE_NAME))
		{
			long lastModifiedMillis = System.currentTimeMillis();

			String trimmed = fileContent.trim();
			try
			{
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
				Date lastModified = dateFormat.parse(trimmed);
				lastModifiedMillis = lastModified.getTime();
			}
			catch(Exception e)
			{
				EAM.logException(e);
			}
			
			project.setLastModified(lastModifiedMillis);
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

	private String readIntoString(ZipEntry entry) throws Exception,
			UnsupportedEncodingException
	{
		byte[] contents = readIntoByteArray(entry);
		final String fileContent = new String(contents, "UTF-8");
		return fileContent;
	}

	private int extractVersion(final String fileContent) throws ParseException
	{
		EnhancedJsonObject json = new EnhancedJsonObject(fileContent);
		int version = json.getInt(ProjectServer.TAG_VERSION);
		return version;
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
		
		project.appendToExceptionLog(exceptionLog);
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
		copyStream(inputStream, out);

		return out.toByteArray();
	}

	private static void copyStream(InputStream inputStream, OutputStream out) throws IOException
	{
		byte[] buffer = new byte[1024];
		int got = -1;
		while( (got = inputStream.read(buffer)) > 0)
		{
			out.write(buffer, 0, got);
		}
		out.close();
	}
	
	private void writeObject(String relativeFilePath, String fileContent) throws Exception
	{
		String[] splittedPath = relativeFilePath.split("-");
		String[] typeId = splittedPath[1].split("/");
		ORef ref = new ORef(Integer.parseInt(typeId[0]), new BaseId(typeId[1]));
		EnhancedJsonObject json = new EnhancedJsonObject(fileContent);
		BaseObject baseObject = BaseObject.createFromJson(project.getObjectManager(), ref.getObjectType(), json);
		String[] legalTags = baseObject.getFieldTags();
		for(int i = 0; i < legalTags.length; ++i)
		{
			String tag = legalTags[i];
			String data = json.optString(tag);
			if(data.length() == 0)
				continue;

			baseObject.setData(tag, data);
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
		SimpleThreatRatingFramework framework = project.getSimpleThreatRatingFramework();
		
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
			
			ORef threatRef = new ORef(Cause.getObjectType(), new BaseId(threatId));
			ORef targetRef = new ORef(Target.getObjectType(), new BaseId(targetId));
			ThreatRatingBundle bundle = framework.getBundle(threatRef, targetRef);
			bundle.setDefaultValueId(new BaseId(defaultValueId));
			bundle.setRating(new RatingValueSet(jsonRatings));
		}
	}
	
	private int extractVersion() throws Exception
	{
		String versionEntryPath = getJsonPrefix() + "version";
		ZipEntry versionEntry = getZipFile().getEntry(versionEntryPath);
		String versionAsString = readIntoString(versionEntry);
		return extractVersion(versionAsString);
	}
	
	private String getProjectPrefix()
	{
		return entries.get(0).getName();
	}
	
	private String getJsonPrefix()
	{
		return getProjectPrefix() + "json/";
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
	private Vector<ZipEntry> entries;
	private Project project;
	private int convertedProjectVersion;
}
