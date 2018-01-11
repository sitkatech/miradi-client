/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
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

package org.miradi.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.martus.util.UnicodeStringReader;
import org.miradi.ids.BaseId;
import org.miradi.legacyprojects.ObjectManifest;
import org.miradi.migrations.Miradi40TypeToFieldSchemaTypesMap;
import org.miradi.migrations.RawProject;
import org.miradi.migrations.VersionRange;
import org.miradi.migrations.forward.MigrationManager;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ThreatRatingBundleSorter;
import org.miradi.objects.Cause;
import org.miradi.objects.Strategy;
import org.miradi.objects.Target;
import org.miradi.project.AbstractConverter;
import org.miradi.project.AbstractProjectLoader;
import org.miradi.project.MpzToMpfConverter;
import org.miradi.project.ProjectInfo;
import org.miradi.project.ProjectInterface;
import org.miradi.project.ProjectLoader;
import org.miradi.project.MiradiProjectFileUtilities;
import org.miradi.project.RawProjectSaver;
import org.miradi.project.threatrating.SimpleThreatFrameworkJson;
import org.miradi.project.threatrating.SimpleThreatRatingFramework;
import org.miradi.project.threatrating.ThreatRatingBundle;
//FIXME medium: This class needs to use ZipUtilities to eliminate duplication of
// zipFile and zipEntry creation 
public class MpfToMpzConverter extends AbstractConverter
{
	public static void convertWithoutMigrating(RawProject rawProject, String projectFileNameToUse, File destinationFile) throws Exception
	{
		VersionRange versionRange = rawProject.getCurrentVersionRange();
		VersionRange oldestMpfVersion = new VersionRange(MigrationManager.OLDEST_VERSION_TO_HANDLE);
		if (versionRange.isEntirelyNewerThan(oldestMpfVersion))
			throw new Exception("Project is too new to convert");
		
		MpfToMpzConverter converter = new MpfToMpzConverter(rawProject, projectFileNameToUse);
		String mpfSnapShot = RawProjectSaver.saveProject(rawProject);
		final UnicodeStringReader reader = new UnicodeStringReader(mpfSnapShot);
		converter.load(reader);
		converter.createZipFile(destinationFile);
	}
	
	private MpfToMpzConverter(ProjectInterface projectToUse, String projectFileNameToUse)
	{
		project = projectToUse;
		projectFileName = projectFileNameToUse;
		refToJsonMap = new HashMap<ORef, EnhancedJsonObject>();
		projectInfoJson = new EnhancedJsonObject();
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

	private void writeZipStream(final FileOutputStream fileOutputStream) throws Exception
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
				final String objectDir = getObjectsDirectoryPrefix(refAsKey.getObjectType());
				final String zipEntryName = FileUtilities.join(objectDir, refAsKey.getObjectId().toString());
				EnhancedJsonObject value = refToJsonMap.get(refAsKey);
				writeZipEntry(zipOutputStream, zipEntryName, value.toString());
			}
			
			addManifestFiles(zipOutputStream);
			addSimpleThreatRatingFiles(zipOutputStream);
		}
		finally
		{
			zipOutputStream.flush();
			zipOutputStream.close();
		}
	}
	
	private void addSimpleThreatRatingFiles(ZipOutputStream zipOutputStream) throws Exception
	{
		writeSimpleThreatRatingFrameworkFile(zipOutputStream);
		writeSimpleThreatRatingBundleFiles(zipOutputStream);
	}
	
	private void writeSimpleThreatRatingFrameworkFile(ZipOutputStream zipOutputStream) throws Exception
	{
		EnhancedJsonObject simpleThreatRatingBundlesAsJson = SimpleThreatFrameworkJson.toJson(getProject());
		writeZipEntry(zipOutputStream, getThreatFrameworkEntryPath(), simpleThreatRatingBundlesAsJson.toString());
	}

	private void writeSimpleThreatRatingBundleFiles(ZipOutputStream zipOutputStream) throws Exception
	{
		Collection<ThreatRatingBundle> allBundles = getProject().getSimpleThreatRatingBundles();
		Vector<ThreatRatingBundle> sortedBundles = new Vector<ThreatRatingBundle>(allBundles);
		Collections.sort(sortedBundles, new ThreatRatingBundleSorter());
		for(ThreatRatingBundle bundle : sortedBundles)
		{
			String bundleName = SimpleThreatRatingFramework.getBundleKey(bundle.getThreatId(), bundle.getTargetId());
			writeZipEntry(zipOutputStream, getSimpleThreatRatingBundleEntryPath(bundleName), bundle.toString());
		}
	}

	private String getSimpleThreatRatingBundleEntryPath(String bundleName) throws Exception
	{
		return FileUtilities.join(getThreatRatingsDirectoryEntryPath(), bundleName);
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
			writeZipEntry(zipOutputStream, getManifestFileName(objectType), string);
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
		final String zipEntryValue = projectInfoJson.toString();
		writeZipEntry(zipOutputStream, getProjectInfoEntryPath(), zipEntryValue);
	}

	private void addVersionEntry(ZipOutputStream zipOutputStream) throws Exception
	{
		String versionString = "{\"Version\":" + Integer.toString(MpzToMpfConverter.REQUIRED_VERSION) + "}";
		writeZipEntry(zipOutputStream, getVersionEntryPath(), versionString);
	}

	public static void writeZipEntry(ZipOutputStream zipOutputStream, final String fileName, String zipContent) throws Exception
	{
		ZipEntry zipEntry = new ZipEntry(fileName);
		zipOutputStream.putNextEntry(zipEntry);
		zipOutputStream.write(StringUtilities.getUtf8EncodedBytes(zipContent));
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

			if (line.startsWith(MiradiProjectFileUtilities.STOP_MARKER))
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
		if (line.startsWith(MiradiProjectFileUtilities.CREATE_OBJECT_CODE))
		{
			ORef ref = AbstractProjectLoader.extractRefFromLine(line);
			EnhancedJsonObject jsonObject = createInitializedJson(ref.getObjectType());
			jsonObject.putId("Id", ref.getObjectId());
			refToJsonMap.put(ref, jsonObject);
		}
		else if (line.startsWith(MiradiProjectFileUtilities.UPDATE_OBJECT_CODE))
		{
			loadUpdateObjectline(line);
		}
		else if (line.startsWith(MiradiProjectFileUtilities.UPDATE_PROJECT_INFO_CODE))
		{
			loadProjectInformation(line);
		}
	}
	
	private EnhancedJsonObject createInitializedJson(int objectType)
	{
		EnhancedJsonObject json = new EnhancedJsonObject();
		Vector<String> tags = Miradi40TypeToFieldSchemaTypesMap.getFieldTags(objectType);	
		HashMap<String, String> tagToValueMap = new HashMap<String, String>();
		for(String tag : tags)
		{
			String defaultValue = "";
			if (Miradi40TypeToFieldSchemaTypesMap.isNumericData(objectType, tag))
				defaultValue = Integer.toString(0);
			
			if (Miradi40TypeToFieldSchemaTypesMap.isIdField(objectType, tag))
				defaultValue = BaseId.INVALID.toString();
			
			tagToValueMap.put(tag, defaultValue);
			tagToValueMap.putAll(addCustomTagValues(objectType));
		}
		
		Vector<String> sortedTags = new Vector<String>(tagToValueMap.keySet());
		Collections.sort(sortedTags);
		for(String tag : sortedTags)
		{
			String value = tagToValueMap.get(tag);
			json.put(tag, value);
		}
		
		return json;
	}

	private Map<String, String> addCustomTagValues(int objectType)
	{
		HashMap<String, String> tagToValueMap = new HashMap<String, String>();
		if (Target.is(objectType))
			tagToValueMap.put(FACTOR_TYPE_TAG, "Target");
		
		if (Cause.is(objectType))
			tagToValueMap.put(FACTOR_TYPE_TAG, "Factor");
		
		if (Strategy.is(objectType))
			tagToValueMap.put(FACTOR_TYPE_TAG, "Intervention");
		
		return tagToValueMap;
	}

	public void putDefaultValue(EnhancedJsonObject json, String defaultValue, final String tag)
	{
		json.put(tag, defaultValue);
	}

	private void loadProjectInformation(String line)
	{
		String[] splitLine = line.split(MiradiProjectFileUtilities.TAB);
		String[] tagValue = splitLine[1].split(MiradiProjectFileUtilities.EQUALS);
		String tag = tagValue[0];
		if (isProjectInformationTag(tag))
		{
			String value = tagValue[1];
			projectInfoJson.put(tag, value);
		}
	}

	private boolean isProjectInformationTag(String tag)
	{
		if (tag.equals(ProjectInfo.TAG_HIGHEST_OBJECT_ID))
			return true;
		
		if (tag.equals(ProjectInfo.TAG_PROJECT_METADATA_ID))
			return true;
		
		throw new RuntimeException("Incorrect tag found for project information, tag:" + tag);
	}

	private void loadUpdateObjectline(String line) throws Exception
	{
		StringTokenizer tokenizer = new StringTokenizer(line);
		/*String command =*/ tokenizer.nextToken();
		String refString = tokenizer.nextToken();
		ORef ref = AbstractProjectLoader.extractRef(refString);
		if(!getProject().containsObject(ref))
			throw new RuntimeException("Missing object " + ref.toString() + " from: " + line);
		
		String tag = tokenizer.nextToken(ProjectLoader.EQUALS_DELIMITER_TAB_PREFIXED);
		EnhancedJsonObject jsonObjects = refToJsonMap.get(ref);
		String json = StringUtilities.substringAfter(line, ProjectLoader.EQUALS_DELIMITER);

		if (isCodeToUserStringMapField(ref, tag))
			json = json.replaceAll("&quot;", "\\\\&quot;");
		
		json = HtmlUtilities.convertHtmlToPlainText(json);
		putDefaultValue(jsonObjects, json, tag);
		refToJsonMap.put(ref, jsonObjects);
	}

	private boolean isCodeToUserStringMapField(ORef ref, String tag)
	{
		return Miradi40TypeToFieldSchemaTypesMap.isCodeToUserStringMapData(ref.getObjectType(), tag);
	}
	
	private ProjectInterface getProject()
	{
		return project;
	}

	@Override
	protected String getProjectPrefix()
	{
		return projectFileName;
	}
	
	private ProjectInterface project;
	private HashMap<ORef, EnhancedJsonObject> refToJsonMap;
	private EnhancedJsonObject projectInfoJson;
	private String projectFileName;
	public static final String FACTOR_TYPE_TAG = "Type";
}