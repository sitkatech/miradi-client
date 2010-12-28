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
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objectpools.EAMObjectPool;
import org.miradi.objects.BaseObject;
import org.miradi.project.threatrating.SimpleThreatRatingFramework;
import org.miradi.project.threatrating.ThreatRatingBundle;
import org.miradi.utils.EnhancedJsonObject;

public class ProjectMpzWriter
{
	public static void createProjectZipFile(File destination, File projectDirectory) throws FileNotFoundException, Exception, IOException
	{
		String projectName = projectDirectory.getName();
		createProjectZipFile(destination, projectName, projectDirectory);
	}

	public static void createProjectZipFile(File destination, String zipTopLevelDirectory, File projectDirectory) throws FileNotFoundException, Exception, IOException
	{
		Project project = new Project();
		project.setLocalDataLocation(projectDirectory.getParentFile());
		project.rawCreateorOpen(zipTopLevelDirectory);

		createProjectZipFile(destination, project);
	}

	public static void createProjectZipFile(File destination, Project project) throws Exception
	{
		ByteArrayOutputStream outputBytes = new ByteArrayOutputStream();
		ZipOutputStream out = new ZipOutputStream(outputBytes);
		writeProjectZip(out, project);
		
		OutputStream blastOut = new FileOutputStream(destination);
		blastOut.write(outputBytes.toByteArray());
		blastOut.close();
	}

	public static void writeProjectZip(ZipOutputStream out, Project project) throws Exception
	{
		String projectFilename = project.getFilename();
		ProjectServer database = project.getDatabase();
		
		String exceptions = database.readFileContents(new File(EAM.EXCEPTIONS_LOG_FILE_NAME));
		writeZipEntry(out, exceptions, projectFilename + "/" + EAM.EXCEPTIONS_LOG_FILE_NAME);
		
		String lastModified = database.readLocalLastModifiedProjectTime(database.getCurrentLocalProjectDirectory());
		writeZipEntry(out, lastModified, projectFilename + "/" + ProjectServer.LAST_MODIFIED_FILE_NAME);
		
		String quarantine = database.getQuarantineFileContents();
		writeZipEntry(out, quarantine, projectFilename + "/" + ProjectServer.QUARANTINE_FILE_NAME);

		EnhancedJsonObject infoJson = project.getProjectInfo().toJson();
		writeZipEntry(out, infoJson.toString(), projectFilename + "/" + ProjectServer.JSON_DIRECTORY + "/" + ProjectServer.PROJECTINFO_FILE);
		
		EnhancedJsonObject threatRatingJson = project.getSimpleThreatRatingFramework().toJson();
		writeZipEntry(out, threatRatingJson.toString(), projectFilename + "/" + ProjectServer.JSON_DIRECTORY + "/" + ProjectServer.THREATFRAMEWORK_FILE);
		
		EnhancedJsonObject versionJson = database.createVersionJson(database.readProjectDataVersion(projectFilename));
		writeZipEntry(out, versionJson.toString(), projectFilename + "/" + ProjectServer.JSON_DIRECTORY + "/" + ProjectServer.VERSION_FILE);

		writeThreatRatingBundles(out, projectFilename, database);
		
		writeBaseObjects(out, project);
		out.close();
	}

	private static void writeBaseObjects(ZipOutputStream out, Project project)
			throws UnsupportedEncodingException, IOException, Exception
	{
		ProjectServer database = project.getDatabase();
		ObjectManager objectManager = project.getObjectManager();
		for(int type = 0; type < ObjectType.OBJECT_TYPE_COUNT; ++type)
		{
			EAMObjectPool pool = objectManager.getPool(type);
			if(pool == null)
				continue;
			ORefSet refs = pool.getRefSet();
			if(refs.size() == 0)
				continue;

			ObjectManifest manifest = database.readObjectManifest(type);
			String projectFilename = project.getFilename();
			writeBaseObjectZipEntry(out, projectFilename, type,
					ProjectServer.MANIFEST_FILE, manifest.toJson().toString());
			addObjectFilesToZip(out, project, refs);
		}
	}

	private static void writeThreatRatingBundles(ZipOutputStream out, String projectFilename, ProjectServer db) throws Exception
	{
		Collection<ThreatRatingBundle> allBundles = SimpleThreatRatingFramework.loadSimpleThreatRatingBundles(db);
		for(ThreatRatingBundle bundle : allBundles)
		{
			String contents = bundle.toJson().toString();
			String bundleName = SimpleThreatRatingFramework.getBundleKey(bundle.getThreatId(), bundle.getTargetId());
			writeZipEntry(out, contents, projectFilename + "/" + ProjectServer.JSON_DIRECTORY + "/" + ProjectServer.THREATRATINGS_DIRECTORY + "/" + bundleName);
		}
	}

	private static void addObjectFilesToZip(ZipOutputStream out, Project project, ORefSet refs) throws Exception
	{
		for(ORef ref : refs)
		{
			BaseObject object = project.findObject(ref);
			String fileContents = object.toJson().toString();

			String projectFilename = project.getFilename();
			int objectType = ref.getObjectType();
			String filename = Integer.toString(ref.getObjectId().asInt());
			writeBaseObjectZipEntry(out, projectFilename, objectType, filename, fileContents);
		}
	}

	private static void writeBaseObjectZipEntry(ZipOutputStream out,
			String projectFilename, int objectType, String objectIdAsString,
			String fileContents) throws UnsupportedEncodingException,
			IOException
	{
		String directory = projectFilename + "/" + ProjectServer.JSON_DIRECTORY + "/objects-" + objectType;
		String path = directory + "/" + objectIdAsString;
		writeZipEntry(out, fileContents, path);
	}

	private static void writeZipEntry(ZipOutputStream out, String fileContents,
			String path) throws UnsupportedEncodingException, IOException
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
