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
package org.miradi.database;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONObject;
import org.martus.util.DirectoryLock;
import org.martus.util.DirectoryLock.AlreadyLockedException;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.network.MiradiFileSystem;
import org.miradi.network.MiradiLocalFileSystem;
import org.miradi.objects.BaseObject;
import org.miradi.project.ObjectManager;
import org.miradi.project.ProjectInfo;
import org.miradi.project.threatrating.SimpleThreatRatingFramework;
import org.miradi.project.threatrating.ThreatRatingBundle;
import org.miradi.utils.EnhancedJsonObject;

public class ProjectServer
{
	public ProjectServer() throws IOException
	{
		localFileSystem = new MiradiLocalFileSystem();
		lock = new DirectoryLock();
	}

	public void close() throws IOException
	{
		topDirectory = null;
		name = null;
		lock.close();
		localFileSystem.setDataDirectory(null);
		currentFileSystem = null;
	}
	
	public void createLocalProject(File directory) throws Exception
	{
		File dataDirectory = directory.getParentFile();
		String projectName = directory.getName();
		
		localFileSystem.setDataDirectory(dataDirectory);
		if(localFileSystem.doesProjectDirectoryExist(projectName))
			throw new RuntimeException("Can't create project if directory already exists");
		
		localFileSystem.createProject(projectName);
		currentFileSystem = localFileSystem;
		openJsonDatabase(directory);
		writeVersion();
	}
		
	public void deleteObject(int type, BaseId id) throws Exception
	{
		removeFromObjectManifest(getCurrentProjectName(), type, id);
		currentFileSystem.deleteFile(getCurrentProjectName(), getRelativeObjectFile(type, id));
	}
	
	public String getCurrentProjectName()
	{
		return name;
	}

	public File getTopDirectory()
	{
		if(topDirectory == null)
			throw new RuntimeException("ERROR: ProjectServer must be opened before use");
		return topDirectory;
	}
	
	public boolean isOpen()
	{
		return lock.isLocked();
	}
	
	public void openLocalProject(File directory) throws Exception
	{
		File dataDirectory = directory.getParentFile();
		String projectName = directory.getName();

		localFileSystem.setDataDirectory(dataDirectory);
		if(!localFileSystem.doesProjectDirectoryExist(projectName))
			throw new IOException("Can't open project that doesn't exist");
		if(!isExistingLocalProject(directory))
			throw new IOException("Can't open non-project, non-empty directory");
	
		currentFileSystem = localFileSystem;
		openJsonDatabase(directory);
	}

	public int readDataVersion(File projectDirectory) throws Exception
	{
		File dataDirectory = projectDirectory.getParentFile();
		String projectName = projectDirectory.getName();

		return readLocalProjectVersion(dataDirectory, projectName);
	}

	public int readLocalProjectVersion(File dataDirectory, String projectName) throws Exception
	{
		MiradiLocalFileSystem fileSystem = new MiradiLocalFileSystem();
		fileSystem.setDataDirectory(dataDirectory);
		
		File versionFile = getRelativeVersionFile();
		if(!fileSystem.doesFileExist(projectName, versionFile))
			throw new RuntimeException("No version file");
		JSONObject version = readRelativeJsonFile(fileSystem, projectName, versionFile);
		int dataVersion = version.getInt(TAG_VERSION);
		return dataVersion;
	}

	public BaseObject readObject(ObjectManager objectManager, int type, BaseId id) throws Exception
	{
		EnhancedJsonObject json = readJsonObjectFile(getCurrentProjectName(), type, id);
		return BaseObject.createFromJson(objectManager, type, json);
	}

	public ObjectManifest readObjectManifest(int type) throws Exception
	{
		return readObjectManifest(getCurrentProjectName(), type);
	}
	
	public void readProjectInfo(ProjectInfo info) throws Exception
	{
		info.clear();
		File infoFile = getRelativeProjectInfoFile();
		if(currentFileSystem.doesFileExist(getCurrentProjectName(), infoFile));
			info.fillFrom(readRelativeJsonFile(getCurrentProjectName(), infoFile));
	}

	public EnhancedJsonObject readRawThreatRatingFramework() throws Exception
	{
		if(!currentFileSystem.doesFileExist(getCurrentProjectName(), getRelativeThreatRatingFrameworkFile()))
			return null;
		
		return readRelativeJsonFile(getCurrentProjectName(), getRelativeThreatRatingFrameworkFile());
	}

	public ThreatRatingBundle readThreatRatingBundle(BaseId threatId, BaseId targetId) throws Exception
	{
		File threatBundleFile = getRelativeThreatBundleFile(threatId, targetId);
		if(!currentFileSystem.doesFileExist(getCurrentProjectName(), threatBundleFile))
			return null;
		return new ThreatRatingBundle(readRelativeJsonFile(getCurrentProjectName(), threatBundleFile));
	}

	public void updateLastModifiedTime() throws Exception
	{
		String currentTime = timestampToString(Calendar.getInstance().getTimeInMillis());
		File lastModifiedTimeFile = getRelativeLastModifiedTimeFile();
		currentFileSystem.writeFile(getCurrentProjectName(), lastModifiedTimeFile, currentTime);
	}

	public void writeObject(BaseObject object) throws Exception
	{
		File relativeObjectFile = getRelativeObjectFile(object.getType(), object.getId());
		writeRelativeJsonFile(getCurrentProjectName(), relativeObjectFile, object.toJson());

		addToObjectManifest(getCurrentProjectName(), object.getType(), object.getId());
	}
	
	public void writeProjectInfo(ProjectInfo info) throws Exception
	{
		writeRelativeJsonFile(getCurrentProjectName(), getRelativeProjectInfoFile(), info.toJson());
	}

	public void writeThreatRatingBundle(ThreatRatingBundle bundle) throws Exception
	{
		writeRelativeJsonFile(getCurrentProjectName(), getRelativeThreatBundleFile(bundle.getThreatId(), bundle.getTargetId()), bundle.toJson());
	}
	
	public void writeThreatRatingFramework(SimpleThreatRatingFramework framework) throws Exception
	{
		writeRelativeJsonFile(getCurrentProjectName(), getRelativeThreatRatingFrameworkFile(), framework.toJson());
	}

	public void writeVersion() throws Exception
	{
		int versionToWrite = DATA_VERSION;
		writeVersion(versionToWrite);
	}
	
	private void openJsonDatabase(File directory) throws IOException,
			AlreadyLockedException
	{
		directory.mkdirs();
		lock.lock(directory);
		setTopDirectory(directory);
		name = topDirectory.getName();
	}

	protected static void writeLocalVersion(File projectDirectory, int versionToWrite) throws Exception
	{
		File dataDirectory = projectDirectory.getParentFile();
		String projectName = projectDirectory.getName();
		
		MiradiLocalFileSystem fileSystem = new MiradiLocalFileSystem();
		fileSystem.setDataDirectory(dataDirectory);
		
		EnhancedJsonObject version = createVersionJson(versionToWrite);
		writeRelativeJsonFile(fileSystem, projectName, getRelativeVersionFile(), version);
	}
	
	private void writeObjectManifest(String projectName, int type, ObjectManifest manifest) throws Exception
	{
		File manifestFile = getRelativeObjectManifestFile(type);
		writeRelativeJsonFile(projectName, manifestFile, manifest.toJson());
	}
	
	void setTopDirectory(File directory)
	{
		topDirectory = directory;
	}

	private void addToObjectManifest(String projectName, int type, BaseId idToAdd) throws Exception
	{
		ObjectManifest manifest = readObjectManifest(projectName, type);
		if (manifest.has(idToAdd))
			return;
		
		manifest.put(idToAdd);
		writeObjectManifest(projectName, type, manifest);
	}

	private static EnhancedJsonObject createVersionJson(int versionToWrite)
	{
		EnhancedJsonObject version = new EnhancedJsonObject();
		version.put(TAG_VERSION, versionToWrite);
		return version;
	}
	
	private File getRelativeObjectFile(int type, BaseId id)
	{
		File objectDirectory = getRelativeObjectsDirectory(type);
		return new File(objectDirectory, Integer.toString(id.asInt()));
	}
	
	private File getRelativeObjectManifestFile(int type)
	{
		File objectsDirectory = getRelativeObjectsDirectory(type);
		return new File(objectsDirectory, MANIFEST_FILE);
	}
	
	private File getRelativeProjectInfoFile()
	{
		return new File(getRelativeJsonDirectory(), PROJECTINFO_FILE);
	}

	private EnhancedJsonObject readJsonObjectFile(String projectName, int type, BaseId id) throws Exception
	{
		File relativeFile = getRelativeObjectFile(type, id);
		return readRelativeJsonFile(projectName, relativeFile);
	}
	
	private ObjectManifest readObjectManifest(String projectName, int type) throws Exception
	{
		File manifestFile = getRelativeObjectManifestFile(type);
		if(!currentFileSystem.doesFileExist(projectName, manifestFile))
			return new ObjectManifest();
		JSONObject rawManifest = readRelativeJsonFile(projectName, manifestFile);
		return new ObjectManifest(rawManifest);
	}
	
	private EnhancedJsonObject readRelativeJsonFile(String projectName, File relativeFile)
			throws Exception, ParseException
	{
		return readRelativeJsonFile(currentFileSystem, projectName, relativeFile);
	}
	
	private static EnhancedJsonObject readRelativeJsonFile(MiradiFileSystem fileSystem, String projectName, File relativeFile)
	throws Exception, ParseException
	{
		String contents = fileSystem.readFile(projectName, relativeFile);
		return new EnhancedJsonObject(contents);
	}

	private void removeFromObjectManifest(String projectName, int type, BaseId idToRemove) throws Exception
	{
		ObjectManifest manifest = readObjectManifest(projectName, type);
		manifest.remove(idToRemove);
		writeObjectManifest(projectName, type, manifest);
	}

	private static void writeRelativeJsonFile(MiradiFileSystem fileSystem, String projectName, File relativeObjectFile,
			EnhancedJsonObject json) throws Exception
	{
		String contents = json.toString();
		fileSystem.writeFile(projectName, relativeObjectFile, contents);
	}
	
	private void writeRelativeJsonFile(String projectName, File relativeObjectFile,
			EnhancedJsonObject json) throws Exception
	{
		writeRelativeJsonFile(currentFileSystem, projectName, relativeObjectFile, json);
	}
	
	private void writeVersion(int versionToWrite) throws Exception
	{
		EnhancedJsonObject version = createVersionJson(versionToWrite);
		writeRelativeJsonFile(getCurrentProjectName(), getRelativeVersionFile(), version);
	}
	
	public static boolean isExistingLocalProject(File projectDirectory)
	{
		if(projectDirectory == null)
			return false;
		
		if(!projectDirectory.isDirectory())
			return false;

		try
		{
			File dataDirectory = projectDirectory.getParentFile();
			String projectName = projectDirectory.getName();
	
			MiradiLocalFileSystem fileSystem = new MiradiLocalFileSystem();
			fileSystem.setDataDirectory(dataDirectory);
			return fileSystem.doesFileExist(projectName, getRelativeVersionFile());
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return false;
		}
	}

	public static String readLocalLastModifiedProjectTime(File projectDirectory)
	{
		File dataDirectory = projectDirectory.getParentFile();
		String projectName = projectDirectory.getName();
		
		MiradiLocalFileSystem fileSystem = new MiradiLocalFileSystem();
		fileSystem.setDataDirectory(dataDirectory);
		
		try
		{
			File lastModifiedTimeFile = getRelativeLastModifiedTimeFile();
			if (fileSystem.doesFileExist(projectName, lastModifiedTimeFile))
				return fileSystem.readFile(projectName, lastModifiedTimeFile);
			
			long lastModifiedMillisFromOperatingSystem = projectDirectory.lastModified();
			String lastModifiedTimeFromOperatingSystem = ProjectServer.timestampToString(lastModifiedMillisFromOperatingSystem);
			fileSystem.writeFile(projectName, lastModifiedTimeFile, lastModifiedTimeFromOperatingSystem);
			return lastModifiedTimeFromOperatingSystem;
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return EAM.text("Unknown");
		}
	}
	
	public static String timestampToString(long lastModifiedMillis)
	{
		Date date = new Date(lastModifiedMillis);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return format.format(date);
	}

	private static File getRelativeLastModifiedTimeFile()
	{
		return new File(LAST_MODIFIED_FILE_NAME);
	}

	private static File getRelativeJsonDirectory()
	{
		return new File(JSON_DIRECTORY);
	}

	private static File getRelativeObjectsDirectory(int type)
	{
		return new File(getRelativeJsonDirectory(), "objects-" + Integer.toString(type));
	}

	private static File getRelativeThreatBundleFile(BaseId threatId, BaseId targetId)
	{
		String bundleKey = SimpleThreatRatingFramework.getBundleKey(threatId, targetId);
		return new File(getRelativeThreatRatingsDirectory(), bundleKey);
	}

	private static File getRelativeThreatRatingFrameworkFile()
	{
		return new File(getRelativeJsonDirectory(), THREATFRAMEWORK_FILE);
	}

	private static File getRelativeThreatRatingsDirectory()
	{
		return new File(getRelativeJsonDirectory(), THREATRATINGS_DIRECTORY);
	}

	private static File getRelativeVersionFile()
	{
		return new File(getRelativeJsonDirectory(), VERSION_FILE);
	}

	static public int DATA_VERSION = 37;
	public static final String LAST_MODIFIED_FILE_NAME = "LastModifiedProjectTime.txt";
	static public String OBJECT_MANIFEST = "ObjectManifest";
	static public String OBJECT_TYPE = "Type";
	static public String TAG_VERSION = "Version";
	static String JSON_DIRECTORY = "json";

	static String MANIFEST_FILE = "manifest";
	static String PROJECTINFO_FILE = "project";
	static String THREATFRAMEWORK_FILE = "threatframework";
	static String THREATRATINGS_DIRECTORY = "threatratings";
	static String VERSION_FILE = "version";

	protected File topDirectory;
	private MiradiFileSystem currentFileSystem;
	private MiradiLocalFileSystem localFileSystem;
	private DirectoryLock lock;
	private String name;
}
