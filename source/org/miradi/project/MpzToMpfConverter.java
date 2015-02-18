/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;
import java.util.zip.ZipEntry;

import org.martus.util.DirectoryUtils;
import org.martus.util.UnicodeStringWriter;
import org.martus.util.UnicodeWriter;
import org.miradi.exceptions.CorruptSimpleThreatRatingDataException;
import org.miradi.exceptions.FutureSchemaVersionException;
import org.miradi.exceptions.UserCanceledException;
import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.legacyprojects.DataUpgrader;
import org.miradi.legacyprojects.Manifest;
import org.miradi.main.EAM;
import org.miradi.migrations.Miradi40TypeToFieldSchemaTypesMap;
import org.miradi.migrations.RawObject;
import org.miradi.migrations.RawPool;
import org.miradi.migrations.RawProject;
import org.miradi.migrations.VersionRange;
import org.miradi.objecthelpers.CodeToUserStringMap;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.threatrating.RatingValueSet;
import org.miradi.project.threatrating.ThreatRatingBundle;
import org.miradi.schemas.RatingCriterionSchema;
import org.miradi.schemas.ValueOptionSchema;
import org.miradi.utils.EnhancedJsonArray;
import org.miradi.utils.EnhancedJsonObject;
import org.miradi.utils.FileUtilities;
import org.miradi.utils.HtmlUtilities;
import org.miradi.utils.MiradiZipFile;
import org.miradi.utils.MpfToMpzConverter;
import org.miradi.utils.NullProgressMeter;
import org.miradi.utils.ProgressInterface;
import org.miradi.utils.Translation;
import org.miradi.utils.Utility;
import org.miradi.utils.XmlUtilities2;
import org.miradi.utils.ZipUtilities;

public class MpzToMpfConverter extends AbstractConverter
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
		File migratedTempFile = null;
		MiradiZipFile originalZipFile = new MiradiZipFile(mpzFile);
		try
		{
			if(extractVersion(originalZipFile) > REQUIRED_VERSION)
				throw new FutureSchemaVersionException();
			
			if(needsMigration(originalZipFile))
				migratedTempFile = migrate(mpzFile, progressIndicator);
		}
		finally
		{
			originalZipFile.close();
		}

		File mpzToUse = mpzFile;
		if(migratedTempFile != null)
			mpzToUse = migratedTempFile;
		
		MiradiZipFile zip = new MiradiZipFile(mpzToUse);
		try
		{
			MpzToMpfConverter converter = new MpzToMpfConverter(zip);
			if (converter.hasCorruptSimpleThreatRatingData())
				throw new CorruptSimpleThreatRatingDataException();
				
			RawProject project = converter.convert(progressIndicator);
			project.setCurrentVersionRange(new VersionRange(FIRST_LOW_VERSION_OF_MPF, FIRST_HIGH_VERSION_OF_MPF));
			UnicodeStringWriter writer = UnicodeStringWriter.create();
			RawProjectSaver.saveProject(project, writer);

			return writer.toString();
		}
		finally
		{
			zip.close();
			if(migratedTempFile != null)
				FileUtilities.deleteExistingWithRetries(migratedTempFile);
		}
	}

	public static boolean needsMigration(MiradiZipFile originalZipFile) throws Exception
	{
		return extractVersion(originalZipFile) < REQUIRED_VERSION;
	}

	public static File migrate(File mpzFile, ProgressInterface progressIndicator) throws Exception
	{
		MiradiZipFile zipFile = new MiradiZipFile(mpzFile);
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
	
			File newMpzFile = ZipUtilities.createZipFromDirectory(projectDirectory);
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
			if(ZipUtilities.isMacResourceForkPath(file))
				continue;
			
			if(result != null)
				throw new RuntimeException("Two children: " + result.getName() + " and " + file.getName());
			result = file;
		}
		
		return result;
	}

	public static int extractVersion(MiradiZipFile mpzFileToUse) throws Exception
	{
		MpzToMpfConverter converter = new MpzToMpfConverter(mpzFileToUse);
		return converter.extractVersion();
	}
	
	private MpzToMpfConverter(MiradiZipFile mpzFileToUse) throws Exception
	{
		zipFile = mpzFileToUse;
		projectPrefix = extractProjectPrefix();
		
		project = new RawProject();
		project.setQuarantineValue("");
		project.setExceptionLog("");
	}
	
	private RawProject convert(ProgressInterface progressIndicator) throws Exception
	{
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
		
		return getProject();
	}

	private void confirmVersion() throws Exception
	{
		ZipEntry versionEntry = zipFile.getEntry(getVersionEntryPath());
		if(versionEntry == null)
			throw new Exception("Missing version file");

		
		int version = extractVersion();
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
		getProject().setProjectMetadataId(metadataId.toString());
		BaseId highestId = new BaseId(json.optString(ProjectInfo.TAG_HIGHEST_OBJECT_ID));
		getProject().setHighestAssignedId(highestId.toString());
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
		
		getProject().setLastModifiedTime(lastModifiedMillis);
	}

	private void convertBaseObjects(ProgressInterface progressIndicator) throws Exception, UserCanceledException
	{
		for(int index = 0; index < ObjectType.OBJECT_TYPE_COUNT; ++index)
		{
			if (!Miradi40TypeToFieldSchemaTypesMap.hasType(index))
				continue;

			RawPool pool = new RawPool();
			getProject().putTypeToNewPoolEntry(index, pool);
			convertObjectsOfType(index);
			
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
	
	public boolean hasCorruptSimpleThreatRatingData() throws Exception
	{
		if (getZipFile().getEntry(getThreatFrameworkEntryPath()) == null)
			return true;
		
		if (hasIncorrectItemCount(RatingCriterionSchema.getObjectType(), 3))
			return true;
		
		if (hasIncorrectItemCount(ValueOptionSchema.getObjectType(), 5))
			return true;
		
		return false;
	}

	private boolean hasIncorrectItemCount(final int type, final int maxItemCount) throws Exception
	{
		ZipEntry manifestEntry = getManifestEntryIfAny(type);
		if(manifestEntry == null)
			return true;
		
		EnhancedJsonObject json = readJson(manifestEntry);
		Manifest manifest = new Manifest(json);

		return manifest.getAllKeys().length > maxItemCount;
	}

	private void convertExceptionLog() throws Exception
	{
		String ExceptionsLogEntryName = FileUtilities.join(getProjectPrefix(), EAM.EXCEPTIONS_LOG_FILE_NAME);
		ZipEntry exceptionsEntry = zipFile.getEntry(ExceptionsLogEntryName);
		if(exceptionsEntry == null)
			return;

		convertExceptionLog(exceptionsEntry);
	}

	private void convertQuarantine() throws Exception
	{
		String quarantineEntryPath = FileUtilities.join(getProjectPrefix(), "DeletedOrphans.txt");
		ZipEntry quarantineEntry = zipFile.getEntry(quarantineEntryPath);
		if(quarantineEntry == null)
			return;

		String contents = readIntoString(quarantineEntry);
		contents = HtmlUtilities.convertPlainTextToHtmlText(contents);
		getProject().setQuarantineValue(contents);
	}

	private void convertObjectsOfType(int type) throws Exception
	{
		ZipEntry manifestEntry = getManifestEntryIfAny(type);
		if(manifestEntry == null)
			return;
		
		EnhancedJsonObject json = readJson(manifestEntry);
		Manifest manifest = new Manifest(json);
		BaseId[] ids = manifest.getAllKeys();
		Vector<BaseId> sortedIds = new Vector<BaseId>(Arrays.asList(ids));
		Collections.sort(sortedIds);
		for(BaseId id : ids)
		{
			convertObject(new ORef(type, id));
		}
	}

	private void convertObject(ORef ref) throws Exception
	{
		String objectEntryPath = getObjectEntryPath(ref);
		ZipEntry entry = zipFile.getEntry(objectEntryPath);
		EnhancedJsonObject json = readJson(entry);
		RawObject rawObject = getProject().createObjectAndReturnObject(ref);
		
		Vector<String> sortedKeys = json.getSortedKeys();
		for (String tag : sortedKeys)
		{
			String value = json.get(tag).toString();
			if (tag.equals("Id"))
				continue;
			
			if (tag.equals(MpfToMpzConverter.FACTOR_TYPE_TAG))
				continue;
		
			value = getFixedUpValue(ref, json, tag);
			if (value.length() == 0)
				continue;
			
			rawObject.put(tag, value);	
		}
	}
	
	private String getFixedUpValue(ORef ref, EnhancedJsonObject json, String tag) throws Exception
	{
		String value = json.optString(tag);
		if (Miradi40TypeToFieldSchemaTypesMap.isUserTextData(ref.getObjectType(), tag))
		{
			return HtmlUtilities.convertPlainTextToHtmlText(value);
		}
		if(Miradi40TypeToFieldSchemaTypesMap.isCodeToUserStringMapData(ref.getObjectType(), tag))
		{
			return encodeIndividualMapValues(value);
		}
		if(Miradi40TypeToFieldSchemaTypesMap.isNumericData(ref.getObjectType(), tag))
		{
			return getSafeNumericValue(value);
		}
		if (Miradi40TypeToFieldSchemaTypesMap.isIdField(ref.getObjectType(), tag))
		{
			return getSafeIdValue(value);
		}
		if (Miradi40TypeToFieldSchemaTypesMap.isRefField(ref.getObjectType(), tag))
		{
			return getSafeRefValue(value);
		}
		
		return value;
	}

	private String getSafeRefValue(String value)
	{
		ORef ref = ORef.createFromString(value);
		if (ref.isInvalid())
			return "";
		
		return value;
	}

	private String getSafeIdValue(String value)
	{
		if (value.equals("-1"))
			return "";
		
		return value;
	}

	public String getSafeNumericValue(String value)
	{
		if (value.equals("0"))
			return "";
		
		return value;
	}
	
	private String encodeIndividualMapValues(String mapAsString) throws ParseException
	{
		CodeToUserStringMap map = new CodeToUserStringMap();

		EnhancedJsonObject json = new EnhancedJsonObject(mapAsString);
		EnhancedJsonObject innerJson = json.optJson("StringMap");
		Iterator it = innerJson.keys();
		while(it.hasNext())
		{
			String key = (String)it.next();
			String value = innerJson.getString(key);
			String encoded = XmlUtilities2.getXmlEncoded(value);
			map.putUserString(key, encoded);
		}
		
		return map.toJsonString();
	}

	private String extractProjectPrefix()
	{
		Enumeration<? extends ZipEntry> enumeration = getZipFile().entries();
		if(!enumeration.hasMoreElements())
			throw new RuntimeException("MPZ file was empty");
		
		ZipEntry entry = enumeration.nextElement();
		return getNameOfTopLevelDirectory(entry);
	}

	public static String getNameOfTopLevelDirectory(ZipEntry entry)
	{
		String rawEntryName = entry.getName();
		String normalizedEntryName = ZipUtilities.getNormalizedWithoutLeadingSlash(rawEntryName);
		File file = new File(normalizedEntryName);
		while(file.getParentFile() != null && !file.getParentFile().getName().equals(""))
		{
			file = file.getParentFile();
		}
		
		return file.getName();
	}

	private String readIntoString(ZipEntry entry) throws Exception
	{
		byte[] contents = readIntoByteArray(entry);
		final String fileContent = new String(contents, "UTF-8");
		return fileContent;
	}

	private int extractVersion(final String fileContent) throws ParseException
	{
		EnhancedJsonObject json = new EnhancedJsonObject(fileContent);
		int version = json.getInt("Version");
		return version;
	}

	private void convertExceptionLog(ZipEntry entry) throws Exception
	{
		String exceptionLog = getExceptionsLog(getZipFile(), entry);
		exceptionLog = HtmlUtilities.convertPlainTextToHtmlText(exceptionLog);
		getProject().setExceptionLog(exceptionLog);
	}

	public static String getExceptionsLog(MiradiZipFile miradiZipFile, ZipEntry entry) throws Exception
	{
		long totalSize = entry.getSize();
		final int MAX_EXCEPTION_LOG_SIZE = 20000;
		long availableUpTo20k = Math.min(totalSize, MAX_EXCEPTION_LOG_SIZE);
		byte[] maximumBytesToRead = new byte[(int)availableUpTo20k];

		InputStream in = miradiZipFile.getInputStream(entry);
		try
		{
			in.skip(totalSize - availableUpTo20k);
			int totalReadCount = Utility.readAsMuchAsPossible(in, maximumBytesToRead);
			
			if(totalReadCount != availableUpTo20k)
				throw new IOException("convertExceptionLog Tried to read " + availableUpTo20k + " but got " + totalReadCount);
		}
		finally
		{
			in.close();
		}
		
		return safeConvertUtf8BytesToString(maximumBytesToRead);
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

	private void writeSimpleThreatRatingBundle(int threatIdAsInt, int targetIdAsInt) throws Exception
	{
		String bundleEntryPath = getBundleEntryPath(threatIdAsInt, targetIdAsInt);
		ZipEntry entry = zipFile.getEntry(bundleEntryPath);
		if(entry == null)
			throw new Exception("Missing simple threat rating bundle: " + bundleEntryPath);
		
		EnhancedJsonObject jsonBundle = readJson(entry);
		int defaultValueId = jsonBundle.getInt("DefaultValueId");
		EnhancedJsonObject jsonRatings = jsonBundle.getJson("Values");
		
		FactorId threatId = new FactorId(threatIdAsInt);
		FactorId targetId = new FactorId(targetIdAsInt);
		ThreatRatingBundle bundle = new ThreatRatingBundle(threatId, targetId, BaseId.INVALID);
		bundle.setDefaultValueId(new BaseId(defaultValueId));
		bundle.setRating(new RatingValueSet(jsonRatings));
		getProject().addThreatRatingBundle(bundle);
	}
	
	private int extractVersion() throws Exception
	{
		String versionEntryPath = getVersionEntryPath();
		String versionEntryName = ZipUtilities.getNormalizedWithoutLeadingSlash(versionEntryPath);
		ZipEntry versionEntry = getZipFile().getEntry(versionEntryName);
		if(versionEntry == null)
			throw new Exception("Version not found: " + versionEntryPath);
		
		String versionAsString = readIntoString(versionEntry);
		return extractVersion(versionAsString);
	}

	private String getLastModifiedEntryPath() throws Exception
	{
		return FileUtilities.join(getProjectPrefix(), "LastModifiedProjectTime.txt");
	}
	
	private String getBundleEntryPath(int threatId, int targetId) throws Exception
	{
		return FileUtilities.join(getThreatRatingsDirectoryEntryPath(), threatId + "-" + targetId);
	}
	
	@Override
	protected String getProjectPrefix()
	{
		return projectPrefix;
	}
	
	private String getObjectEntryPath(ORef ref) throws Exception
	{
		return FileUtilities.join(getObjectsDirectoryPrefix(ref.getObjectType()), ref.getObjectId().toString());
	}

	private EnhancedJsonObject readJson(ZipEntry entry) throws Exception
	{
		final String fileContent = readIntoString(entry);
		return new EnhancedJsonObject(fileContent);
	}

	private ZipEntry getManifestEntryIfAny(int objectType) throws Exception
	{
		String manifestEntryName = getManifestFileName(objectType);
		return zipFile.getEntry(manifestEntryName);
	}
	
	private MiradiZipFile getZipFile()
	{
		return zipFile;
	}
	
	private RawProject getProject()
	{
		return project;
	}

	public static final int FIRST_LOW_VERSION_OF_MPF = 3;
	public static final int FIRST_HIGH_VERSION_OF_MPF = 3;
	public static int REQUIRED_VERSION = 61;
	private MiradiZipFile zipFile;
	private String projectPrefix;
	private RawProject project;
	private int convertedProjectVersion;
}
