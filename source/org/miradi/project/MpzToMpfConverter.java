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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.martus.util.DirectoryUtils;
import org.martus.util.UnicodeStringWriter;
import org.martus.util.UnicodeWriter;
import org.miradi.database.DataUpgrader;
import org.miradi.database.Manifest;
import org.miradi.database.ProjectServer;
import org.miradi.exceptions.UserCanceledException;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Cause;
import org.miradi.objects.Target;
import org.miradi.project.threatrating.RatingValueSet;
import org.miradi.project.threatrating.SimpleThreatRatingFramework;
import org.miradi.project.threatrating.ThreatRatingBundle;
import org.miradi.utils.EnhancedJsonArray;
import org.miradi.utils.EnhancedJsonObject;
import org.miradi.utils.FileUtilities;
import org.miradi.utils.NullProgressMeter;
import org.miradi.utils.ProgressInterface;
import org.miradi.utils.Translation;
import org.miradi.utils.ZipUtilities;

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
		
		String converted = convert(mpzFile, new NullProgressMeter());
		UnicodeWriter writer = new UnicodeWriter(destination);
		writer.write(converted);
		writer.close();
		System.out.println("Converted");
	}
	
	public static final String convert(File mpzFile, ProgressInterface progressIndicator) throws Exception
	{
		File migratedFile = null;
		ZipFile originalZipFile = new ZipFile(mpzFile);
		try
		{
			if(extractVersion(originalZipFile) != REQUIRED_VERSION)
				migratedFile = migrate(mpzFile, progressIndicator);
		}
		finally
		{
			originalZipFile.close();
		}

		File mpzToUse = mpzFile;
		if(migratedFile != null)
			mpzToUse = migratedFile;
		
		ZipFile zip = new ZipFile(mpzToUse);
		try
		{
			MpzToMpfConverter converter = new MpzToMpfConverter(zip);
			Project project = converter.convert(progressIndicator);
			UnicodeStringWriter writer = UnicodeStringWriter.create();
			ProjectSaver.saveProject(project, writer);

			if(migratedFile != null)
				migratedFile.delete();

			return writer.toString();
		}
		finally
		{
			zip.close();
		}
		
	}

	public static File migrate(File mpzFile, ProgressInterface progressIndicator) throws Exception
	{
		ZipFile zipFile = new ZipFile(mpzFile);
		try
		{
			File tempDirectory = FileUtilities.createTempDirectory("MigrateMPZ");
			ZipUtilities.extractAll(zipFile, tempDirectory);

			File projectDirectory = getTheOneChildDirectory(tempDirectory);
			DataUpgrader.initializeStaticDirectory(projectDirectory);
			DataUpgrader.upgrade(progressIndicator);
			int versionAfterUpgrading = DataUpgrader.readDataVersion(projectDirectory);
			if(versionAfterUpgrading != REQUIRED_VERSION)
				throw new RuntimeException("Migration failed");
	
			File newMpzFile = ZipUtilities.createZipFromDirectory(tempDirectory);
			newMpzFile.deleteOnExit();
			DirectoryUtils.deleteEntireDirectoryTree(tempDirectory);
			return newMpzFile;
		}
		finally
		{
			zipFile.close();
		}
	}

	private static File getTheOneChildDirectory(File directory)
	{
		File[] children = directory.listFiles();
		File result = null;
		for(File file : children)
		{
			if(file.getName().startsWith("__MACOSX"))
				continue;
			
			if(result != null)
				throw new RuntimeException("Two children: " + result.getName() + " and " + file.getName());
			result = file;
		}
		
		return result;
	}

	public static int extractVersion(ZipFile mpzFileToUse) throws Exception
	{
		MpzToMpfConverter converter = new MpzToMpfConverter(mpzFileToUse);
		return converter.extractVersion();
	}
	
	private MpzToMpfConverter(ZipFile mpzFileToUse) throws Exception
	{
		zipFile = mpzFileToUse;
		projectPrefix = extractProjectPrefix();
		
		project = new Project();
	}
	
	private Project convert(ProgressInterface progressIndicator) throws Exception
	{
		project.clear();
		
		int steps = ObjectType.OBJECT_TYPE_COUNT + 6;
		progressIndicator.setStatusMessage(EAM.text("Reading..."), steps);

		confirmVersion();
		progressIndicator.incrementProgress();
		
		convertProjectInfo();
		progressIndicator.incrementProgress();
		
		convertLastModified();
		progressIndicator.incrementProgress();
		
		convertBaseObjects(progressIndicator);

		convertSimpleThreatRatings();
		progressIndicator.incrementProgress();

		convertExceptionLog();
		progressIndicator.incrementProgress();
		
		convertQuarantine();
		progressIndicator.incrementProgress();
		
		return project;
	}

	private void confirmVersion() throws Exception
	{
		ZipEntry versionEntry = zipFile.getEntry(getVersionEntryPath());
		if(versionEntry == null)
			throw new Exception("Missing version file");

		EnhancedJsonObject json = readJson(versionEntry);
		int version = json.getInt(ProjectServer.TAG_VERSION);
		convertedProjectVersion = version;
		if(convertedProjectVersion != REQUIRED_VERSION)
			throw new Exception("Cannot convert MPZ version " + convertedProjectVersion);
	}

	private void convertProjectInfo() throws Exception
	{
		ZipEntry projectInfoEntry = zipFile.getEntry(getProjectInfoEntryPath());
		if(projectInfoEntry == null)
			throw new Exception("Missing project info file");

		EnhancedJsonObject json = readJson(projectInfoEntry);
		BaseId metadataId = new BaseId(json.optString(ProjectInfo.TAG_PROJECT_METADATA_ID));
		project.getProjectInfo().setMetadataId(metadataId);
		BaseId highestId = new BaseId(json.optString(ProjectInfo.TAG_HIGHEST_OBJECT_ID));
		project.getProjectInfo().getNormalIdAssigner().idTaken(highestId);
	}

	private void convertLastModified() throws Exception
	{
		long lastModifiedMillis = System.currentTimeMillis();

		ZipEntry lastModifiedEntry = zipFile.getEntry(getLastModifiedEntryPath());
		if(lastModifiedEntry != null)
		{
			String trimmed = readIntoString(lastModifiedEntry).trim();
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
		}
		
		project.setLastModified(lastModifiedMillis);
		
	}

	private void convertBaseObjects(ProgressInterface progressIndicator)
			throws Exception, UserCanceledException
	{
		for(int i = 0; i < ObjectType.OBJECT_TYPE_COUNT; ++i)
		{
			convertObjectsOfType(i);
			
			progressIndicator.incrementProgress();
			if(progressIndicator.shouldExit())
				throw new UserCanceledException();
		}
	}
	
	private void convertSimpleThreatRatings() throws Exception
	{
		ZipEntry frameworkEntry = zipFile.getEntry(getThreatFrameworkEntryPath());
		if(frameworkEntry != null)
		{
			EnhancedJsonObject json = readJson(frameworkEntry);
			writeSimpleThreatFramework(json);
		}
	}

	private void convertExceptionLog() throws Exception
	{
		String ExceptionsLogEntryName = getProjectPrefix() + EAM.EXCEPTIONS_LOG_FILE_NAME;
		ZipEntry exceptionsEntry = zipFile.getEntry(ExceptionsLogEntryName);
		if(exceptionsEntry == null)
			return;
		
		convertExceptionLog(exceptionsEntry);
	}

	private void convertQuarantine()
	{
		// FIXME: Mpz convertQuarantine not implemented yet
		EAM.logWarning("MPZ converter is not yet handling quarantine");
	}

	private void convertObjectsOfType(int type) throws Exception
	{
		ZipEntry manifestEntry = getManifestEntryIfAny(type);
		if(manifestEntry == null)
			return;
		
		EnhancedJsonObject json = readJson(manifestEntry);
		Manifest manifest = new Manifest(json);
		BaseId[] ids = manifest.getAllKeys();
		for(int i = 0; i < ids.length; ++i)
			convertObject(new ORef(type, ids[i]));
	}

	private void convertObject(ORef ref) throws Exception
	{
		String objectEntryPath = getObjectEntryPath(ref);
		ZipEntry entry = zipFile.getEntry(objectEntryPath);
		EnhancedJsonObject json = readJson(entry);
		project.createObject(ref);
		BaseObject object = BaseObject.find(project, ref);
		object.loadFromJson(json);
	}

	private String extractProjectPrefix()
	{
		Enumeration<? extends ZipEntry> enumeration = getZipFile().entries();
		if(!enumeration.hasMoreElements())
			throw new RuntimeException("MPZ file was empty");
		
		ZipEntry entry = enumeration.nextElement();
		File file = new File(entry.getName());
		while(file.getParentFile() != null && !file.getParentFile().getName().equals(""))
			file = file.getParentFile();
		String path = file.getPath();
		if(!path.endsWith("/"))
			path = path + "/";
		return path;
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
		FileUtilities.copyStream(inputStream, out);

		return out.toByteArray();
	}

	private void writeSimpleThreatFramework(EnhancedJsonObject json) throws Exception
	{
		EnhancedJsonArray bundleKeysArray = json.getJsonArray("BundleKeys");
		for(int i = 0; i < bundleKeysArray.length(); ++i)
		{
			EnhancedJsonObject jsonKeyBundle = bundleKeysArray.getJson(i);
			
			int threatId = jsonKeyBundle.getInt("BundleThreatId");
			int targetId = jsonKeyBundle.getInt("BundleTargetId");
			if(threatId < 0 || targetId < 0)
				continue;
			
			writeSimpleThreatRatingBundle(threatId, targetId);
		}
	}

	private void writeSimpleThreatRatingBundle(int threatId, int targetId) throws Exception
	{
		SimpleThreatRatingFramework framework = project.getSimpleThreatRatingFramework();
		
		String bundleEntryPath = getBundleEntryPath(threatId, targetId);
		ZipEntry entry = zipFile.getEntry(bundleEntryPath);
		if(entry == null)
			throw new Exception("Missing simple threat rating bundle: " + bundleEntryPath);
		
		EnhancedJsonObject jsonBundle = readJson(entry);
		int defaultValueId = jsonBundle.getInt("DefaultValueId");
		EnhancedJsonObject jsonRatings = jsonBundle.getJson("Values");
		
		ORef threatRef = new ORef(Cause.getObjectType(), new BaseId(threatId));
		ORef targetRef = new ORef(Target.getObjectType(), new BaseId(targetId));
		ThreatRatingBundle bundle = framework.getBundle(threatRef, targetRef);
		bundle.setDefaultValueId(new BaseId(defaultValueId));
		bundle.setRating(new RatingValueSet(jsonRatings));
	}
	
	private int extractVersion() throws Exception
	{
		String versionEntryPath = getVersionEntryPath();
		ZipEntry versionEntry = getZipFile().getEntry(versionEntryPath);
		if(versionEntry == null)
			throw new RuntimeException("Version not found: " + versionEntryPath);
		String versionAsString = readIntoString(versionEntry);
		return extractVersion(versionAsString);
	}

	private String getVersionEntryPath()
	{
		return getJsonPrefix() + "version";
	}
	
	private String getProjectInfoEntryPath()
	{
		return getJsonPrefix() + "project";
	}
	
	private String getLastModifiedEntryPath()
	{
		return getProjectPrefix() + ProjectServer.LAST_MODIFIED_FILE_NAME;
	}
	
	private String getThreatFrameworkEntryPath()
	{
		return getJsonPrefix() + "threatframework";
	}
	
	private String getBundleEntryPath(int threatId, int targetId)
	{
		return getThreatRatingsDirectoryEntryPath() + threatId + "-" + targetId;
	}
	
	private String getThreatRatingsDirectoryEntryPath()
	{
		return getJsonPrefix() + "threatratings/";
	}
	
	private String getProjectPrefix()
	{
		return projectPrefix;
	}
	
	private String getJsonPrefix()
	{
		return getProjectPrefix() + "json/";
	}

	private String getObjectEntryPath(ORef ref)
	{
		return getObjectsDirectoryPrefix(ref.getObjectType()) + ref.getObjectId().asInt();
	}

	private EnhancedJsonObject readJson(ZipEntry entry) throws Exception,
			UnsupportedEncodingException, ParseException
	{
		final String fileContent = readIntoString(entry);
		return new EnhancedJsonObject(fileContent);
	}

	private ZipEntry getManifestEntryIfAny(int i)
	{
		String manifestEntryName = getObjectsDirectoryPrefix(i) + "manifest";
		return zipFile.getEntry(manifestEntryName);
	}
	
	private String getObjectsDirectoryPrefix(int objectType)
	{
		return getJsonPrefix() + "objects-" + objectType + "/";
	}

	private ZipFile getZipFile()
	{
		return zipFile;
	}
	
	private static int REQUIRED_VERSION = 61;
	private ZipFile zipFile;
	private String projectPrefix;
	private Project project;
	private int convertedProjectVersion;
}
