/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.database;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.project.ProjectInfo;
import org.conservationmeasures.eam.project.ThreatRatingBundle;
import org.conservationmeasures.eam.project.ThreatRatingFramework;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;
import org.json.JSONObject;
import org.martus.util.DirectoryLock;
import org.martus.util.DirectoryLock.AlreadyLockedException;

public class ProjectServer
{
	public ProjectServer() throws IOException
	{
		lock = new DirectoryLock();
	}

	public void close() throws IOException
	{
		topDirectory = null;
		name = null;
		lock.close();
	}

	public static boolean doesProjectExist(File directoryToCheck)
	{
		return isExistingProject(directoryToCheck);
	}
	
	public boolean isOpen()
	{
		return lock.isLocked();
	}

	public String getName()
	{
		return name;
	}
	
	public File getTopDirectory()
	{
		if(topDirectory == null)
			throw new RuntimeException("ERROR: ProjectServer must be opened before use");
		return topDirectory;
	}
	
	public void create(File directory) throws Exception
	{
		if(!isEmpty(directory))
			throw new RuntimeException("Can't create project in non-empty directory");
		
		openNonDatabaseStore(directory);
		writeVersion();
	}

	public void open(File directory) throws IOException, AlreadyLockedException
	{
		if(!doesProjectExist(directory))
			throw new IOException("Can't open non-project, non-empty directory");

		openNonDatabaseStore(directory);
	}
	
	private boolean isEmpty(File directory)
	{
		String[] files = directory.list();
		if(files == null)
			return true;
		return (files.length == 0);
	}
	
	public static boolean isCurrentVersion(File projectDirectory) throws IOException, ParseException
	{
		int dataVersion = readDataVersion(projectDirectory);
		if(dataVersion != DATA_VERSION)
			return false;
		
		return true;
	}

	public static int readDataVersion(File projectDirectory) throws IOException, ParseException
	{
		File versionFile = getVersionFile(projectDirectory);
		if(!versionFile.exists())
			throw new RuntimeException("No version file");
		JSONObject version = JSONFile.read(versionFile);
		int dataVersion = version.getInt(TAG_VERSION);
		return dataVersion;
	}
	
	public void writeVersion() throws IOException
	{
		int versionToWrite = DATA_VERSION;
		writeVersion(versionToWrite);
	}

	void writeVersion(int versionToWrite) throws IOException
	{
		JSONObject version = new JSONObject();
		version.put(TAG_VERSION, versionToWrite);
		JSONFile.write(getVersionFile(), version);
	}

	protected void openNonDatabaseStore(File directory) throws IOException, AlreadyLockedException
	{
		directory.mkdirs();
		lock.lock(directory);
		setTopDirectory(directory);
		createJsonDirectories();
		name = topDirectory.getName();
	}

	void setTopDirectory(File directory)
	{
		topDirectory = directory;
	}

	private void createJsonDirectories()
	{
		getJsonDirectory().mkdirs();
	}

	public static boolean isExistingProject(File projectDirectory)
	{
		if(projectDirectory == null)
			return false;
		
		if(!projectDirectory.isDirectory())
			return false;
		
		return (getVersionFile(projectDirectory).exists());
	}
	
	
	
	
	public void writeProjectInfo(ProjectInfo info) throws IOException
	{
		JSONFile.write(getProjectInfoFile(), info.toJson());
	}
	
	public void readProjectInfo(ProjectInfo info) throws IOException, ParseException
	{
		File infoFile = getProjectInfoFile();
		info.clear();
		if(infoFile.exists())
			info.fillFrom(JSONFile.read(infoFile));
	}
	
	public void writeThreatRatingBundle(ThreatRatingBundle bundle) throws Exception
	{
		getThreatRatingsDirectory().mkdirs();
		JSONFile.write(getThreatBundleFile(bundle.getThreatId(), bundle.getTargetId()), bundle.toJson());
	}
	
	public ThreatRatingBundle readThreatRatingBundle(BaseId threatId, BaseId targetId) throws Exception
	{
		File threatBundleFile = getThreatBundleFile(threatId, targetId);
		if(!threatBundleFile.exists())
			return null;
		return new ThreatRatingBundle(JSONFile.read(threatBundleFile));
	}
	
	
	public void writeThreatRatingFramework(ThreatRatingFramework framework) throws IOException
	{
		getJsonDirectory().mkdirs();
		JSONFile.write(getThreatRatingFrameworkFile(), framework.toJson());
	}
	
	public EnhancedJsonObject readRawThreatRatingFramework() throws IOException, ParseException
	{
		if(!getThreatRatingFrameworkFile().exists())
			return null;
		
		return JSONFile.read(getThreatRatingFrameworkFile());
	}
	
	
	public BaseObject readObject(int type, BaseId id) throws Exception
	{
		return readObject(null, type, id);
	}
	
	public BaseObject readObject(ObjectManager objectManager, int type, BaseId id) throws Exception
	{
		return BaseObject.createFromJson(objectManager, type, JSONFile.read(getObjectFile(type, id)));
	}
	
	public void writeObject(BaseObject object) throws IOException, ParseException
	{
		getObjectDirectory(object.getType()).mkdirs();
		JSONFile.write(getObjectFile(object.getType(), object.getId()), object.toJson());	
		addToObjectManifest(object.getType(), object.getId());
	}
	
	public void deleteObject(int type, BaseId id) throws IOException, ParseException
	{
		removeFromObjectManifest(type, id);
		getObjectFile(type, id).delete();
	}
	
	public ObjectManifest readObjectManifest(int type) throws IOException, ParseException
	{
		File manifestFile = getObjectManifestFile(type);
		if(!manifestFile.exists())
			return new ObjectManifest();
		JSONObject rawManifest = JSONFile.read(manifestFile);
		return new ObjectManifest(rawManifest);
	}
	
	void addToObjectManifest(int type, BaseId idToAdd) throws IOException, ParseException
	{
		ObjectManifest manifest = readObjectManifest(type);
		manifest.put(idToAdd);
		writeObjectManifest(type, manifest);
	}
	
	private void removeFromObjectManifest(int type, BaseId idToRemove) throws IOException, ParseException
	{
		ObjectManifest manifest = readObjectManifest(type);
		manifest.remove(idToRemove);
		writeObjectManifest(type, manifest);
	}
	
	protected void writeObjectManifest(int type, ObjectManifest manifest) throws IOException
	{
		manifest.write(getObjectManifestFile(type));
	}
	

	private File getJsonDirectory()
	{
		return getJsonDirectory(getTopDirectory());
	}

	private static File getJsonDirectory(File topDirectory)
	{
		return new File(topDirectory, JSON_DIRECTORY);
	}
	
	private File getObjectDirectory(int type)
	{	
		return new File(getJsonDirectory(), "objects-" + possiblyConvertToGenericFactorType(type));
	}
	
	private int possiblyConvertToGenericFactorType(int specificFactorType)
	{
		if (specificFactorType == ObjectType.STRATEGY)
			return ObjectType.FACTOR;
		
		if (specificFactorType == ObjectType.CAUSE)
			return ObjectType.FACTOR;
		
		if (specificFactorType == ObjectType.TARGET)
			return ObjectType.FACTOR;
		
		return specificFactorType;
	}
	
	private File getThreatRatingsDirectory()
	{
		return new File(getJsonDirectory(), THREATRATINGS_DIRECTORY);
	}
	
	private File getVersionFile()
	{
		return getVersionFile(getTopDirectory());
	}
	
	private static File getVersionFile(File projectDirectory)
	{
		return new File(getJsonDirectory(projectDirectory), VERSION_FILE);
	}
	
	private File getProjectInfoFile()
	{
		return new File(getJsonDirectory(), PROJECTINFO_FILE);
	}
	
	private File getThreatRatingFrameworkFile()
	{
		return new File(getJsonDirectory(), THREATFRAMEWORK_FILE);
	}
	
	private File getThreatBundleFile(BaseId threatId, BaseId targetId)
	{
		String bundleKey = ThreatRatingFramework.getBundleKey(threatId, targetId);
		return new File(getThreatRatingsDirectory(), bundleKey);
	}
	
	protected File getObjectFile(int type, BaseId id)
	{
		return new File(getObjectDirectory(type), Integer.toString(id.asInt()));
	}
	
	protected File getObjectManifestFile(int type)
	{
	
		return new File(getObjectDirectory(type), MANIFEST_FILE);
	}
	
	static String JSON_DIRECTORY = "json";
	static String THREATRATINGS_DIRECTORY = "threatratings";
	static String VERSION_FILE = "version";
	static String PROJECTINFO_FILE = "project";
	static String THREATFRAMEWORK_FILE = "threatframework";
	static String MANIFEST_FILE = "manifest";

	static public String OBJECT_TYPE = "Type";
	static public String TAG_VERSION = "Version";
	static public String OBJECT_MANIFEST = "ObjectManifest";
	static public int DATA_VERSION = 18;

	File topDirectory;
	String name;
	DirectoryLock lock;
}
