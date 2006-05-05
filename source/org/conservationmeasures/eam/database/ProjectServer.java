/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.database;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Vector;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.objects.ConceptualModelLinkage;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.ThreatRatingBundle;
import org.conservationmeasures.eam.project.ProjectInfo;
import org.conservationmeasures.eam.project.ThreatRatingFramework;
import org.json.JSONObject;
import org.martus.util.DirectoryLock;
import org.martus.util.DirectoryLock.AlreadyLockedException;

public class ProjectServer
{
	public ProjectServer() throws IOException
	{
		commands = new Vector();
		lock = new DirectoryLock();
	}

	public int getCommandCount()
	{
		return commands.size();
	}

	public Command getCommandAt(int i)
	{
		if(i < 0 || i >= getCommandCount())
			throw new RuntimeException("Command " + i + " not found");
		
		return (Command)commands.get(i);
	}

	protected void clear()
	{
		commands.clear();
	}
	
	public void addCommandWithoutSaving(Command command) throws IOException
	{
		addCommand(command);
	}
	
	public void appendCommand(Command command) throws IOException
	{
		if(!isOpen())
			throw new IOException("FileStorage: Can't append if no file open");
		
		addCommandWithoutSaving(command);
	}

	public void close() throws IOException
	{
		clear();
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
		clear();
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
	
	
	
	public void writeNode(ConceptualModelNode node) throws IOException, ParseException
	{
		getNodesDirectory().mkdirs();
		int id = node.getId();
		JSONFile.write(getNodeFile(id), node.toJson());
		addToNodeManifest(id);
	}
	
	public ConceptualModelNode readNode(int id) throws IOException, ParseException
	{
		return ConceptualModelNode.createFrom(JSONFile.read(getNodeFile(id)));
	}
	
	public void deleteNode(int id) throws IOException, ParseException
	{
		removeFromNodeManifest(id);
		getNodeFile(id).delete();
		
	}
	
	private void addToNodeManifest(int idToAdd) throws IOException, ParseException
	{
		NodeManifest manifest = readNodeManifest();
		manifest.put(idToAdd);
		writeNodeManifest(manifest);
	}
	
	private void removeFromNodeManifest(int idToRemove) throws IOException, ParseException
	{
		NodeManifest manifest = readNodeManifest();
		manifest.remove(idToRemove);
		writeNodeManifest(manifest);
	}
	
	public NodeManifest readNodeManifest() throws IOException, ParseException
	{
		File manifestFile = getNodeManifestFile();
		if(!manifestFile.exists())
			return new NodeManifest();
		JSONObject rawManifest = JSONFile.read(manifestFile);
		return new NodeManifest(rawManifest);
	}

	private void writeNodeManifest(NodeManifest manifest) throws IOException
	{
		manifest.write(getNodeManifestFile());
	}

	
	
	
	public void writeLinkage(ConceptualModelLinkage linkage) throws IOException, ParseException
	{
		getLinkagesDirectory().mkdirs();
		int id = linkage.getId();
		JSONFile.write(getLinkageFile(id), linkage.toJson());
		addToLinkageManifest(id);
	}

	public ConceptualModelLinkage readLinkage(int id) throws IOException, ParseException
	{
		return new ConceptualModelLinkage(JSONFile.read(getLinkageFile(id)));
	}
	
	public void deleteLinkage(int id) throws IOException, ParseException
	{
		removeFromLinkageManifest(id);
		getLinkageFile(id).delete();
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
	
	public void writeDiagram(DiagramModel model) throws IOException
	{
		getDiagramsDirectory().mkdirs();
		JSONFile.write(getDiagramFile(), model.toJson());
	}
	
	public void readDiagram(DiagramModel model) throws Exception
	{
		File diagramFile = getDiagramFile();
		model.clear();
		if(diagramFile.exists())
			model.fillFrom(JSONFile.read(diagramFile));
	}
	
	private void removeFromLinkageManifest(int idToRemove) throws IOException, ParseException
	{
		LinkageManifest manifest = readLinkageManifest();
		manifest.remove(idToRemove);
		writeLinkageManifest(manifest);
	}
	
	private void addToLinkageManifest(int idToAdd) throws IOException, ParseException
	{
		LinkageManifest manifest = readLinkageManifest();
		manifest.put(idToAdd);
		writeLinkageManifest(manifest);
	}
	
	public LinkageManifest readLinkageManifest() throws IOException, ParseException
	{
		File manifestFile = getLinkageManifestFile();
		if(!manifestFile.exists())
			return new LinkageManifest();
		JSONObject rawManifest = JSONFile.read(manifestFile);
		return new LinkageManifest(rawManifest);
	}
	
	private void writeLinkageManifest(LinkageManifest manifest) throws IOException
	{
		manifest.write(getLinkageManifestFile());
	}
	
	public void writeThreatRatingBundle(ThreatRatingBundle bundle) throws Exception
	{
		getThreatRatingsDirectory().mkdirs();
		JSONFile.write(getThreatBundleFile(bundle.getThreatId(), bundle.getTargetId()), bundle.toJson());
	}
	
	public ThreatRatingBundle readThreatRatingBundle(int threatId, int targetId) throws Exception
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
	
	public JSONObject readRawThreatRatingFramework() throws IOException, ParseException
	{
		if(!getThreatRatingFrameworkFile().exists())
			return null;
		
		return JSONFile.read(getThreatRatingFrameworkFile());
	}

	
	
	public EAMObject readObject(int type, int id) throws Exception
	{
		return EAMObject.createFromJson(type, JSONFile.read(getObjectFile(type, id)));
	}
	
	public void writeObject(EAMObject object) throws IOException, ParseException
	{
		getObjectDirectory(object.getType()).mkdirs();
		JSONFile.write(getObjectFile(object.getType(), object.getId()), object.toJson());	
		addToObjectManifest(object.getType(), object.getId());
	}
	
	public void deleteObject(int type, int id) throws IOException, ParseException
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
	
	void addToObjectManifest(int type, int idToAdd) throws IOException, ParseException
	{
		ObjectManifest manifest = readObjectManifest(type);
		manifest.put(idToAdd);
		writeObjectManifest(type, manifest);
	}
	
	private void removeFromObjectManifest(int type, int idToRemove) throws IOException, ParseException
	{
		ObjectManifest manifest = readObjectManifest(type);
		manifest.remove(idToRemove);
		writeObjectManifest(type, manifest);
	}
	
	private void writeObjectManifest(int type, ObjectManifest manifest) throws IOException
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
	
	private File getNodesDirectory()
	{
		return new File(getJsonDirectory(), NODES_DIRECTORY);
	}
	
	private File getLinkagesDirectory()
	{
		return new File(getJsonDirectory(), LINKAGES_DIRECTORY);
	}
	
	private File getDiagramsDirectory()
	{
		return new File(getJsonDirectory(), DIAGRAMS_DIRECTORY);
	}
	
	private File getObjectDirectory(int type)
	{
		return new File(getJsonDirectory(), "objects-" + Integer.toString(type));
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
	
	private File getThreatBundleFile(int threatId, int targetId)
	{
		String bundleKey = ThreatRatingFramework.getBundleKey(threatId, targetId);
		return new File(getThreatRatingsDirectory(), bundleKey);
	}
	
	private File getNodeManifestFile()
	{
		return new File(getNodesDirectory(), MANIFEST_FILE);
	}
	
	private File getNodeFile(int id)
	{
		return new File(new File(getJsonDirectory(getTopDirectory()), NODES_DIRECTORY), Integer.toString(id));
	}
	
	private File getLinkageManifestFile()
	{
		return new File(getLinkagesDirectory(), MANIFEST_FILE);
	}
	
	private File getLinkageFile(int id)
	{
		return new File(getLinkagesDirectory(), Integer.toString(id));
	}
	
	private File getDiagramFile()
	{
		return new File(getDiagramsDirectory(), DIAGRAM_FILE);
	}
	
	private File getObjectFile(int type, int id)
	{
		return new File(getObjectDirectory(type), Integer.toString(id));
	}
	
	private File getObjectManifestFile(int type)
	{
		return new File(getObjectDirectory(type), MANIFEST_FILE);
	}
	
	private void addCommand(Command command)
	{
		commands.add(command);
	}
	
	static String JSON_DIRECTORY = "json";
	static String NODES_DIRECTORY = "nodes";
	static String LINKAGES_DIRECTORY = "linkages";
	static String DIAGRAMS_DIRECTORY = "diagrams";
	static String THREATRATINGS_DIRECTORY = "threatratings";
	static String VERSION_FILE = "version";
	static String PROJECTINFO_FILE = "project";
	static String THREATFRAMEWORK_FILE = "threatframework";
	static String MANIFEST_FILE = "manifest";
	static String DIAGRAM_FILE = "main";

	static public String OBJECT_TYPE = "Type";
	static public String TAG_VERSION = "Version";
	static public String LINKAGE_MANIFEST = "LinkageManifest";
	static public String NODE_MANIFEST = "NodeManifest";
	static public String OBJECT_MANIFEST = "ObjectManifest";
	static public int DATA_VERSION = 3;

	protected Vector commands;
	File topDirectory;
	String name;
	DirectoryLock lock;
}
