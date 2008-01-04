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
import org.conservationmeasures.eam.project.SimpleThreatRatingFramework;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;
import org.json.JSONObject;
import org.martus.util.DirectoryLock;
import org.martus.util.DirectoryLock.AlreadyLockedException;

abstract public class ProjectServer
{
	public ProjectServer() throws IOException
	{
	}

	abstract public void create(File directory) throws Exception;
	abstract public void open(File directory) throws IOException, AlreadyLockedException;
	abstract public void close() throws IOException;
	abstract public boolean isOpen();
	abstract public boolean doesFileExist(File infoFile);

	abstract void writeJsonFile(File file, JSONObject json) throws IOException;
	abstract EnhancedJsonObject readJsonFile(File file) throws IOException, ParseException;
	abstract boolean deleteJsonFile(File objectFile);
	

	public int readDataVersion(File projectDirectory) throws IOException, ParseException
	{
		File versionFile = getVersionFile(projectDirectory);
		if(!doesFileExist(versionFile))
			throw new RuntimeException("No version file");
		JSONObject version = JSONFile.read(versionFile);
		int dataVersion = version.getInt(TAG_VERSION);
		return dataVersion;
	}
	
	public static boolean isExistingProject(File projectDirectory)
	{
		if(projectDirectory == null)
			return false;
		
		if(!projectDirectory.isDirectory())
			return false;
		
		return (getVersionFile(projectDirectory).exists());
	}
	

	
	public String getName()
	{
		return name;
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
		writeJsonFile(getVersionFile(), version);
	}

	abstract void openNonDatabaseStore(File directory) throws IOException, AlreadyLockedException;

	public File getTopDirectory()
	{
		if(topDirectory == null)
			throw new RuntimeException("ERROR: ProjectServer must be opened before use");
		return topDirectory;
	}
	
	void setTopDirectory(File directory)
	{
		topDirectory = directory;
	}

	
	
	
	public void writeProjectInfo(ProjectInfo info) throws IOException
	{
		writeJsonFile(getProjectInfoFile(), info.toJson());
	}

	public void readProjectInfo(ProjectInfo info) throws IOException, ParseException
	{
		File infoFile = getProjectInfoFile();
		info.clear();
		if(doesFileExist(infoFile))
			info.fillFrom(readJsonFile(infoFile));
	}

	public void writeThreatRatingBundle(ThreatRatingBundle bundle) throws Exception
	{
		writeJsonFile(getThreatBundleFile(bundle.getThreatId(), bundle.getTargetId()), bundle.toJson());
	}
	
	public ThreatRatingBundle readThreatRatingBundle(BaseId threatId, BaseId targetId) throws Exception
	{
		File threatBundleFile = getThreatBundleFile(threatId, targetId);
		if(!doesFileExist(threatBundleFile))
			return null;
		return new ThreatRatingBundle(readJsonFile(threatBundleFile));
	}
	
	
	public void writeThreatRatingFramework(SimpleThreatRatingFramework framework) throws IOException
	{
		writeJsonFile(getThreatRatingFrameworkFile(), framework.toJson());
	}
	
	public EnhancedJsonObject readRawThreatRatingFramework() throws IOException, ParseException
	{
		if(!doesFileExist(getThreatRatingFrameworkFile()))
			return null;
		
		return readJsonFile(getThreatRatingFrameworkFile());
	}
	
	public BaseObject readObject(ObjectManager objectManager, int type, BaseId id) throws Exception
	{
		return BaseObject.createFromJson(objectManager, type, readJsonFile(getObjectFile(type, id)));
	}
	
	public void writeObject(BaseObject object) throws IOException, ParseException
	{
		writeJsonFile(getObjectFile(object.getType(), object.getId()), object.toJson());	
		addToObjectManifest(object.getType(), object.getId());
	}
	
	public void deleteObject(int type, BaseId id) throws IOException, ParseException
	{
		removeFromObjectManifest(type, id);
		File objectFile = getObjectFile(type, id);
		deleteJsonFile(objectFile);
	}

	public ObjectManifest readObjectManifest(int type) throws IOException, ParseException
	{
		File manifestFile = getObjectManifestFile(type);
		if(!doesFileExist(manifestFile))
			return new ObjectManifest();
		JSONObject rawManifest = readJsonFile(manifestFile);
		return new ObjectManifest(rawManifest);
	}
	
	void addToObjectManifest(int type, BaseId idToAdd) throws IOException, ParseException
	{
		ObjectManifest manifest = readObjectManifest(type);
		if (manifest.has(idToAdd))
			return;
		
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
		writeJsonFile(getObjectManifestFile(type), manifest.toJson());
	}
	

	protected File getJsonDirectory()
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
		String bundleKey = SimpleThreatRatingFramework.getBundleKey(threatId, targetId);
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
	static public int DATA_VERSION = 31;

	protected File topDirectory;
	protected String name;
	protected DirectoryLock lock;
}
