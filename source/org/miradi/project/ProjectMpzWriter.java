/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.miradi.database.ObjectManifest;
import org.miradi.database.ProjectServer;
import org.miradi.ids.BaseId;
import org.miradi.ids.IdList;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.threatrating.SimpleThreatRatingFramework;
import org.miradi.project.threatrating.ThreatRatingBundle;
import org.miradi.utils.EnhancedJsonObject;

public class ProjectMpzWriter
{
	public static void createProjectZipFile(File projectDirectory, File destinationZip) throws FileNotFoundException, Exception, IOException
	{
		ProjectServer database = new ProjectServer();
		try
		{
			String projectName = projectDirectory.getName();
			database.setLocalDataLocation(projectDirectory.getParentFile());
			database.openProject(projectName);

			writeProjectZip(database, destinationZip);
		}
		finally
		{
			database.close();
		}
	}

	public static void writeProjectZip(ProjectServer database, File destinationZip) throws Exception
	{
		ByteArrayOutputStream outputBytes = new ByteArrayOutputStream();
		ZipOutputStream out = new ZipOutputStream(outputBytes);

		writeProjectZip(out, database);

		OutputStream blastOut = new FileOutputStream(destinationZip);
		blastOut.write(outputBytes.toByteArray());
		blastOut.close();
	}

	public static void writeProjectZip(ZipOutputStream out, Project project) throws Exception
	{
		ProjectServer database = project.getDatabase();
		
		writeProjectZip(out, database);
	}

	private static void writeProjectZip(ZipOutputStream out, ProjectServer database) throws Exception
	{
		String projectFilename = database.getCurrentProjectName();
		String exceptions = database.readFileContents(new File(EAM.EXCEPTIONS_LOG_FILE_NAME));
		writeZipEntry(out, projectFilename + "/" + EAM.EXCEPTIONS_LOG_FILE_NAME, exceptions);
		
		if(database.isLocalProject())
		{
			String lastModified = database.readLocalLastModifiedProjectTime(database.getCurrentLocalProjectDirectory());
			writeZipEntry(out, projectFilename + "/" + ProjectServer.LAST_MODIFIED_FILE_NAME, lastModified);
		}
		
		String quarantine = database.getQuarantineFileContents();
		writeZipEntry(out, projectFilename + "/" + ProjectServer.QUARANTINE_FILE_NAME, quarantine);

		EnhancedJsonObject versionJson = database.createVersionJson(database.readProjectDataVersion(projectFilename));
		writeZipEntry(out, buildPathForZipEntryInJsonDirectory(projectFilename, ProjectServer.VERSION_FILE), versionJson.toString());

		ProjectInfo projectInfo = new ProjectInfo();
		database.readProjectInfo(projectInfo);
		EnhancedJsonObject infoJson = projectInfo.toJson(); 
		writeZipEntry(out, buildPathForZipEntryInJsonDirectory(projectFilename, ProjectServer.PROJECTINFO_FILE), infoJson.toString());
		
		writeSimpleThreatFramework(out, database);
		
		writeBaseObjects(out, projectFilename, database);
		out.close();
	}

	private static void writeSimpleThreatFramework(ZipOutputStream out, ProjectServer database) throws Exception
	{
		String projectFilename = database.getCurrentProjectName();
		EnhancedJsonObject threatRatingJson = database.readRawThreatRatingFramework();
		writeZipEntry(out, buildPathForZipEntryInJsonDirectory(projectFilename, ProjectServer.THREATFRAMEWORK_FILE), threatRatingJson.toString());
		
		Collection<ThreatRatingBundle> allBundles = SimpleThreatRatingFramework.loadSimpleThreatRatingBundles(database);
		for(ThreatRatingBundle bundle : allBundles)
		{
			String contents = bundle.toJson().toString();
			String bundleName = SimpleThreatRatingFramework.getBundleKey(bundle.getThreatId(), bundle.getTargetId());
			writeZipEntry(out, projectFilename + "/" + ProjectServer.JSON_DIRECTORY + "/" + ProjectServer.THREATRATINGS_DIRECTORY + "/" + bundleName, contents);
		}
	}

	private static void writeBaseObjects(ZipOutputStream out, String projectFilename, ProjectServer database) throws Exception
	{
		for(int type = 0; type < ObjectType.OBJECT_TYPE_COUNT; ++type)
		{
			ObjectManifest manifest = database.readObjectManifest(type);
			if(manifest.size() == 0)
				continue;
			
			IdList ids = new IdList(type, manifest.getAllKeys());
			ORefSet refs = new ORefSet(new ORefList(type, ids));
			
			String path = buildZipEntryPath(projectFilename, type, ProjectServer.MANIFEST_FILE);
			writeZipEntry(out, path, manifest.toJson().toString());
			addBaseObjectFilesToZip(out, database, refs);
		}
	}

	private static void addBaseObjectFilesToZip(ZipOutputStream out, ProjectServer database, ORefSet refs) throws Exception
	{
		for(ORef ref : refs)
		{
			int objectType = ref.getObjectType();
			BaseId id = ref.getObjectId();
			EnhancedJsonObject json = database.readJsonObjectFile(database.getCurrentProjectName(), objectType, id);
			writeBaseObjectZipEntry(out, database.getCurrentProjectName(), ref, json.toString());
		}
	}

	private static void writeBaseObjectZipEntry(ZipOutputStream out, String projectFilename, ORef ref, String fileContents)
			throws UnsupportedEncodingException, IOException
	{
		int objectType = ref.getObjectType();
		String filename = Integer.toString(ref.getObjectId().asInt());
		String path = buildZipEntryPath(projectFilename, objectType, filename);
		writeZipEntry(out, path, fileContents);
	}

	private static String buildPathForZipEntryInJsonDirectory(String projectFilename, String zipEntryFilename)
	{
		return projectFilename + "/" + ProjectServer.JSON_DIRECTORY + "/" + zipEntryFilename;
	}

	private static String buildZipEntryPath(String projectFilename, int objectType, String zipEntryFilename)
	{
		String directory = projectFilename + "/" + ProjectServer.JSON_DIRECTORY + "/objects-" + objectType;
		String path = directory + "/" + zipEntryFilename;
		return path;
	}

	private static void writeZipEntry(ZipOutputStream out, String path,
			String fileContents) throws UnsupportedEncodingException, IOException
	{
		if(fileContents.length() == 0)
			return;
		
		byte[] bytes = fileContents.getBytes("UTF-8");
		ZipEntry entry = new ZipEntry(path);
		entry.setSize(bytes.length);
		out.putNextEntry(entry);
		out.write(bytes);
		out.closeEntry();
	}
}
