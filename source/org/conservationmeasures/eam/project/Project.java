/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.project;

import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Vector;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.database.DataUpgrader;
import org.conservationmeasures.eam.database.FileBasedProjectServer;
import org.conservationmeasures.eam.database.ProjectServer;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.exceptions.FutureVersionException;
import org.conservationmeasures.eam.exceptions.OldVersionException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.DiagramFactorLinkId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.VersionConstants;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objectpools.AssignmentPool;
import org.conservationmeasures.eam.objectpools.CausePool;
import org.conservationmeasures.eam.objectpools.ConceptualModelDiagramPool;
import org.conservationmeasures.eam.objectpools.DiagramFactorLinkPool;
import org.conservationmeasures.eam.objectpools.DiagramFactorPool;
import org.conservationmeasures.eam.objectpools.EAMObjectPool;
import org.conservationmeasures.eam.objectpools.FactorLinkPool;
import org.conservationmeasures.eam.objectpools.GoalPool;
import org.conservationmeasures.eam.objectpools.GroupBoxPool;
import org.conservationmeasures.eam.objectpools.IndicatorPool;
import org.conservationmeasures.eam.objectpools.IntermediateResultPool;
import org.conservationmeasures.eam.objectpools.KeyEcologicalAttributePool;
import org.conservationmeasures.eam.objectpools.ObjectivePool;
import org.conservationmeasures.eam.objectpools.PlanningViewConfigurationPool;
import org.conservationmeasures.eam.objectpools.RareProjectDataPool;
import org.conservationmeasures.eam.objectpools.ResourcePool;
import org.conservationmeasures.eam.objectpools.ResultsChainDiagramPool;
import org.conservationmeasures.eam.objectpools.StrategyPool;
import org.conservationmeasures.eam.objectpools.TargetPool;
import org.conservationmeasures.eam.objectpools.TaskPool;
import org.conservationmeasures.eam.objectpools.TextBoxPool;
import org.conservationmeasures.eam.objectpools.ThreatReductionResultPool;
import org.conservationmeasures.eam.objectpools.ViewPool;
import org.conservationmeasures.eam.objectpools.WcsProjectDataPool;
import org.conservationmeasures.eam.objectpools.WwfProjectDataPool;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.ConceptualModelDiagram;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.DiagramLink;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.FosProjectData;
import org.conservationmeasures.eam.objects.PlanningViewConfiguration;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.objects.RareProjectData;
import org.conservationmeasures.eam.objects.TncProjectData;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.objects.WcsProjectData;
import org.conservationmeasures.eam.objects.WwfProjectData;
import org.conservationmeasures.eam.questions.BudgetTimePeriodQuestion;
import org.conservationmeasures.eam.questions.ThreatRatingModeChoiceQuestion;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;
import org.conservationmeasures.eam.views.diagram.DiagramClipboard;
import org.conservationmeasures.eam.views.diagram.DiagramPageList;
import org.conservationmeasures.eam.views.diagram.DiagramView;
import org.conservationmeasures.eam.views.diagram.LayerManager;
import org.conservationmeasures.eam.views.planning.PlanningView;
import org.conservationmeasures.eam.views.planning.doers.CreatePlanningViewConfigurationDoer;
import org.conservationmeasures.eam.views.summary.SummaryView;
import org.martus.util.MultiCalendar;
import org.martus.util.UnicodeWriter;
import org.martus.util.xml.XmlUtilities;


public class Project
{
	public Project() throws Exception
	{
		this(new FileBasedProjectServer());
	}
	
	public Project(ProjectServer databaseToUse) throws Exception
	{
		database = databaseToUse;
		commandExecutedListeners = new Vector();
		projectCalendar = new ProjectCalendar(this);

		clear();
	}

	protected void clear() throws Exception
	{
		projectInfo = new ProjectInfo();
		objectManager = new ObjectManager(this);
		undoRedoState = new UndoRedoState();
		
		diagramClipboard = new DiagramClipboard(this);
		layerManager = new LayerManager();
		simpleThreatFramework = new SimpleThreatRatingFramework(this);
		stressBasedThreatFramework = new StressBasedThreatRatingFramework(this);
		
		currentViewName = SummaryView.getViewName();
		
		projectCalendar.clearDateRanges();
	}
	
	static public void validateNewProject(String newName) throws Exception
	{
		File newFile = new File(EAM.getHomeDirectory(),newName);
		if(ProjectServer.isExistingProject(newFile))
			throw new Exception(EAM.text(" A project by this name already exists: ") + newName);
		
		if (!EAM.getMainWindow().getProject().isValidProjectFilename(newName))
			throw new Exception(EAM.text("Invalid project name:") + newName);
		
		if(newFile.exists())
			throw new Exception(EAM.text("A file or directory exist by the same name:") + newName);
		
	}
	/////////////////////////////////////////////////////////////////////////////////
	// simple getters
	
	public IdAssigner getNodeIdAssigner()
	{
		return projectInfo.getFactorAndLinkIdAssigner();
	}
	
	public IdAssigner getAnnotationIdAssigner()
	{
		return projectInfo.getNormalIdAssigner();
	}
	
	public ProjectServer getDatabase()
	{
		return database;
	}
	
	public ObjectManager getObjectManager()
	{
		return objectManager;
	}
	
	public EAMObjectPool getPool(int objectType)
	{
		return objectManager.getPool(objectType);
	}
	
	public ORefList getAllDiagramObjectRefs()
	{
		return objectManager.getAllDiagramObjectRefs();
	}
	
	public WwfProjectDataPool getWwfProjectDataPool()
	{
		return (WwfProjectDataPool) getPool(WwfProjectData.getObjectType());
	}
	
	public RareProjectDataPool getRareProjectDataPool()
	{
		return (RareProjectDataPool) getPool(RareProjectData.getObjectType());
	}
	
	public WcsProjectDataPool getWcsProjectDataPool()
	{
		return (WcsProjectDataPool) getPool(WcsProjectData.getObjectType());
	}
	
	public ConceptualModelDiagramPool getConceptualModelDiagramPool()
	{
		return (ConceptualModelDiagramPool) getPool(ObjectType.CONCEPTUAL_MODEL_DIAGRAM);
	}
	
	public ResultsChainDiagramPool getResultsChainDiagramPool()
	{
		return (ResultsChainDiagramPool) getPool(ObjectType.RESULTS_CHAIN_DIAGRAM);
	}
	
	public TextBoxPool getTextBoxPool()
	{
		return (TextBoxPool) getPool(ObjectType.TEXT_BOX);
	}
	
	public CausePool getCausePool()
	{
		return (CausePool) getPool(ObjectType.CAUSE);
	}
	
	public IntermediateResultPool getIntermediateResultPool()
	{
		return (IntermediateResultPool) getPool(ObjectType.INTERMEDIATE_RESULT);
	}
	
	public ThreatReductionResultPool getThreatReductionResultPool()
	{
		 return (ThreatReductionResultPool) getPool(ObjectType.THREAT_REDUCTION_RESULT);
	}
	
	public StrategyPool getStrategyPool()
	{
		return (StrategyPool) getPool(ObjectType.STRATEGY);
	}
	
	public TargetPool getTargetPool()
	{
		return (TargetPool) getPool(ObjectType.TARGET);
	}
	
	public DiagramFactorPool getDiagramFactorPool()
	{
		return objectManager.getDiagramFactorPool();
	}
	
	public DiagramFactorLinkPool getDiagramFactorLinkPool()
	{
		return objectManager.getDiagramFactorLinkPool();
	}
	
	public FactorLinkPool getFactorLinkPool()
	{
		return objectManager.getLinkagePool();
	}
	
	public TaskPool getTaskPool()
	{
		return objectManager.getTaskPool();
	}
	
	public KeyEcologicalAttributePool getKeyEcologicalAttributePool()
	{
		return objectManager.getKeyEcologicalAttributePool();
	}
	
	public ViewPool getViewPool()
	{
		return objectManager.getViewPool();
	}
	
	public ResourcePool getResourcePool()
	{
		return objectManager.getResourcePool();
	}
	
	public IndicatorPool getIndicatorPool()
	{
		return objectManager.getIndicatorPool();
	}

	public ObjectivePool getObjectivePool()
	{
		return objectManager.getObjectivePool();
	}
	
	public GoalPool getGoalPool()
	{
		return objectManager.getGoalPool();
	}
	
	public AssignmentPool getAssignmentPool()
	{
		return objectManager.getAssignmentPool();
	}
	
	public PlanningViewConfigurationPool getPlanningViewConfigurationPool()
	{
		return objectManager.getPlanningConfigurationPool();
	}
	
	public GroupBoxPool getGroupBoxPool()
	{
		return (GroupBoxPool) getPool(ObjectType.GROUP_BOX);
	}
	
	public ORef getSingletonObjectRef(int objectType)
	{
		EAMObjectPool pool = getPool(objectType);
		ORefList objectRefs = pool.getORefList();
		if (objectRefs.size() == 1)
			return objectRefs.get(0);
		
		throw new RuntimeException("Wrong object count (count = " + objectRefs.size() + ") in pool for type:" + objectType);
	}
	
	public LayerManager getLayerManager()
	{
		return layerManager;
	}
	
	public String getCurrentView()
	{
		if(!isOpen())
			return NO_PROJECT_VIEW_NAME;
		
		return currentViewName;
	}
	
	public ViewData getDiagramViewData() throws Exception
	{
		return getViewData(DiagramView.getViewName());
	}
	
	public ViewData getCurrentViewData() throws Exception
	{
		return getViewData(getCurrentView());
	}
	
	public ViewData getViewData(String viewName) throws Exception
	{
		ViewData found = getViewPool().findByLabel(viewName);
		if(found != null)
			return found;
		
		BaseId createdId = createObjectAndReturnId(ObjectType.VIEW_DATA);
		setObjectData(ObjectType.VIEW_DATA, createdId, ViewData.TAG_LABEL, viewName);
		return getViewPool().find(createdId);
	}
	
	public SimpleThreatRatingFramework getSimpleThreatRatingFramework()
	{
		return simpleThreatFramework;
	}
	
	public ThreatRatingFramework getThreatRatingFramework()
	{
		if (getMetadata().getThreatRatingMode().equals(ThreatRatingModeChoiceQuestion.STRESS_BASED_CODE))
			return  stressBasedThreatFramework;
		
		return simpleThreatFramework;
	}
	
	public SimpleThreatFormula getSimpleThreatFormula()
	{
		return getSimpleThreatRatingFramework().getSimpleThreatFormula();
	}
	
	public StressBasedThreatFormula getStressBasedThreatFormula()
	{
		return stressBasedThreatFramework.getStressBasedThreatFormula();
	}
	
	public ProjectCalendar getProjectCalendar()
	{
		return projectCalendar;
	}

	public BaseObject findObject(ORef ref)
	{
		return findObject(ref.getObjectType(), ref.getObjectId());
	}

	public BaseObject findObject(int objectType, BaseId objectId)
	{
		return objectManager.findObject(new ORef(objectType, objectId));
	}
	
	public Factor findNode(FactorId nodeId)
	{
		return objectManager.findNode(nodeId);
	}
	
	public Factor findFactor(ORef factorRef)
	{
		return objectManager.findFactor(factorRef);
	}

	
	public ProjectInfo getProjectInfo()
	{
		return projectInfo;
	}
	
	public ProjectMetadata getMetadata()
	{
		return (ProjectMetadata)findObject(ObjectType.PROJECT_METADATA, getMetadataId());
	}

	private BaseId getMetadataId()
	{
		return projectInfo.getMetadataId();
	}
	
	/////////////////////////////////////////////////////////////////////////////////
	// objects
	
	public void setMetadata(String tag, String value) throws Exception
	{
		setObjectData(ObjectType.PROJECT_METADATA, getMetadataId(), tag, value);
	}
	
	public FactorLinkId obtainRealLinkageId(BaseId proposedId)
	{
		return projectInfo.obtainRealLinkId(proposedId);
	}
	
	public BaseId obtainRealNodeId(BaseId proposedId)
	{
		return projectInfo.obtainRealFactorId(proposedId);
	}
	
	public ORef createObject(int objectType) throws Exception
	{
		BaseId createdId = createObjectAndReturnId(objectType);
		return new ORef(objectType, createdId);
	}
	
	public BaseId createObjectAndReturnId(int objectType) throws Exception
	{
		return createObject(objectType, BaseId.INVALID);
	}
	
	public BaseId createObject(int objectType, BaseId objectId) throws Exception
	{
		return createObject(objectType, objectId, null);
	}
	
	public ORef createObjectAndReturnRef(int objectType, CreateObjectParameter extraInfo) throws Exception
	{
		return new ORef(objectType, createObject(objectType, extraInfo));
	}
	
	public BaseId createObject(int objectType, CreateObjectParameter extraInfo) throws Exception
	{
		return createObject(objectType, BaseId.INVALID, extraInfo);
	}
	
	public BaseId createObject(int objectType, BaseId objectId, CreateObjectParameter extraInfo) throws Exception
	{
		BaseId createdId = objectManager.createObject(objectType, objectId, extraInfo);
		saveProjectInfo();
		return createdId;
	}
	
	public void deleteObject(BaseObject object) throws IOException, ParseException
	{
		objectManager.deleteObject(object);
	}
	
	public void setObjectData(int objectType, BaseId objectId, String fieldTag, String dataValue) throws Exception
	{
		setObjectData(new ORef(objectType, objectId), fieldTag, dataValue);
	}
	
	public void setObjectData(ORef objectRef, String fieldTag, String dataValue) throws Exception
	{
		objectManager.setObjectData(objectRef, fieldTag, dataValue);
	}
	
	public String getObjectData(int objectType, BaseId objectId, String fieldTag)
	{
		return objectManager.getObjectData(objectType, objectId, fieldTag);
	}
	
	public String getObjectData(ORef ref, String fieldTag)
	{
		return objectManager.getObjectData(ref.getObjectType(), ref.getObjectId(), fieldTag);
	}
	
	/////////////////////////////////////////////////////////////////////////////////
	// database
	
	public void createOrOpen(File projectDirectory) throws Exception
	{
		clear();
			
		if(ProjectServer.isExistingProject(projectDirectory))
			openProject(projectDirectory);
		else
			createProject(projectDirectory);
		
		writeStartingLogEntry();
		
		finishOpening();
	}

	//TODO: need to remvoe duplicate code after test code fixed as to not need to be tested for
	// between writeStartingLogEntry and writeLogLine...the two new File() lines
	private void writeStartingLogEntry() throws IOException
	{
		File thisProjectDirectory = new File(EAM.getHomeDirectory(), getFilename());
		File commandLogFile = new File(thisProjectDirectory, COMMAND_LOG_FILE_NAME);
		if (commandLogFile.exists())
			commandLogFile.delete();
		writeLogLine("Project Opened by Miradi " + VersionConstants.VERSION_STRING);
	}

	public void writeLogLine(String logLine) throws IOException
	{
		File thisProjectDirectory = new File(EAM.getHomeDirectory(), getFilename());
		
		//TODO: this line is here to support test code
		if (!thisProjectDirectory.exists())
			return;
		
		File commandLogFile = new File(thisProjectDirectory, COMMAND_LOG_FILE_NAME);
		FileOutputStream os = new FileOutputStream(commandLogFile, true);
		PrintStream logPrintStream = new PrintStream(os);
		logPrintStream.println(logLine);
		EAM.logVerbose("Command Executed: " +logLine);
		os.close();
	}
	
	private void applyDefaultBehavior() throws Exception
	{
		simpleThreatFramework.createDefaultObjectsIfNeeded();
		createDefaultConceptualModel();
		createDefaultPlanningCustomization();
		selectDefaultPlanningCustomization();
		selectPlanningViewStrategicRadioButton();
		createDefaultProjectDataObject(WwfProjectData.getObjectType());
		createDefaultProjectDataObject(RareProjectData.getObjectType());
		createDefaultProjectDataObject(WcsProjectData.getObjectType());
		createDefaultProjectDataObject(TncProjectData.getObjectType());
		createDefaultProjectDataObject(FosProjectData.getObjectType());
		eliminateBlankConceptualModelPages();
		ensureAllDiagramFactorsAreVisible();
	}

	public void ensureAllDiagramFactorsAreVisible() throws Exception
	{
		ORefList allDiagramFactorRefs = getDiagramFactorPool().getORefList();
		for (int i = 0; i < allDiagramFactorRefs.size(); ++i)
		{
			DiagramFactor diagramFactor = DiagramFactor.find(this, allDiagramFactorRefs.get(i));
			Point location = (Point) diagramFactor.getLocation().clone();
			if (isOffScreenLocation(location))
			{
				CommandSetObjectData moveToOnScreen = new CommandSetObjectData(diagramFactor.getRef(), DiagramFactor.TAG_LOCATION, EnhancedJsonObject.convertFromPoint(new Point(0, 0)));
				executeWithoutRecording(moveToOnScreen);
			}	
		}
	}
	
	private boolean isOffScreenLocation(Point location)
	{
		if (location.x < 0)
			return true;
		
		if (location.y < 0)
			return true;
		
		return false;
	}

	private void createDefaultPlanningCustomization() throws Exception
	{
		if(getPlanningViewConfigurationPool().getORefList().size() > 0)
			return;
		
		ORef createPlanningConfiguration = createObject(PlanningViewConfiguration.getObjectType());
		setObjectData(createPlanningConfiguration, PlanningViewConfiguration.TAG_LABEL, CreatePlanningViewConfigurationDoer.getConfigurationDefaultLabel(this));
		
		ViewData planningViewData = getViewData(PlanningView.getViewName());
		setObjectData(planningViewData.getRef(), ViewData.TAG_PLANNING_CUSTOM_PLAN_REF, createPlanningConfiguration.toString());
	}
	
	private void selectDefaultPlanningCustomization() throws Exception
	{
		ORef currentCustomizationRef = getViewData(PlanningView.getViewName()).getORef(ViewData.TAG_PLANNING_CUSTOM_PLAN_REF);
		if (! currentCustomizationRef.isInvalid())
			return;
		
		ORefList contomizationRefs = getPlanningViewConfigurationPool().getORefList();
		ViewData planningViewData = getViewData(PlanningView.getViewName());
		setObjectData(planningViewData.getRef(), ViewData.TAG_PLANNING_CUSTOM_PLAN_REF, contomizationRefs.get(0).toString());
	}

	private void createDefaultConceptualModel() throws Exception
	{
		if (getConceptualModelDiagramPool().getORefList().size() > 0)
			return;
		
		createObject(ObjectType.CONCEPTUAL_MODEL_DIAGRAM);
	}
	
	private void createProjectMetadata() throws Exception
	{
		BaseId createdId = createObjectAndReturnId(ObjectType.PROJECT_METADATA);
		projectInfo.setMetadataId(createdId);
		setObjectData(getMetadata().getRef(), ProjectMetadata.TAG_CURRENCY_SYMBOL, "$");
		setObjectData(getMetadata().getRef(), ProjectMetadata.TAG_WORKPLAN_TIME_UNIT, BudgetTimePeriodQuestion.BUDGET_BY_YEAR_CODE);

		getDatabase().writeProjectInfo(projectInfo);
	}
	
	private void selectPlanningViewStrategicRadioButton() throws Exception
	{
		ViewData planningViewData = getViewData(PlanningView.getViewName());
		if (planningViewData.getData(ViewData.TAG_PLANNING_STYLE_CHOICE).length() == 0)
			setObjectData(planningViewData.getRef(), ViewData.TAG_PLANNING_STYLE_CHOICE, PlanningView.STRATEGIC_PLAN_RADIO_CHOICE);
	}
	
	private void createDefaultProjectDataObject(int objectType) throws Exception
	{
		EAMObjectPool pool = getPool(objectType);
		if (pool.getORefList().size() > 0)
			return;
		
		createObject(objectType);
	}
	
	private void eliminateBlankConceptualModelPages() throws Exception
	{
		ORefList diagramPageRefs = getConceptualModelDiagramPool().getORefList();
		String defaultDiagramPageLabel = getDefaultDiagramPageName(diagramPageRefs);		
		for (int i = 0; i < diagramPageRefs.size(); ++i)
		{
			ORef diagramPageRef = diagramPageRefs.get(i);
			ConceptualModelDiagram diagramPage = (ConceptualModelDiagram) findObject(diagramPageRef);
			if (diagramPage.toString().length() != 0)
				continue;
			
			setObjectData(diagramPageRef, ConceptualModelDiagram.TAG_LABEL, defaultDiagramPageLabel);
		}
	}

	private String getDefaultDiagramPageName(ORefList diagramPageRefs)
	{
		if (diagramPageRefs.size() > 1)
			return ConceptualModelDiagram.DEFAULT_BLANK_NAME;
		
		return ConceptualModelDiagram.DEFAULT_MAIN_NAME;
	}
	
	private void openProject(File projectDirectory) throws Exception
	{
		if(getDatabase().readDataVersion(projectDirectory) > ProjectServer.DATA_VERSION)
			throw new FutureVersionException();

		if(getDatabase().readDataVersion(projectDirectory) < ProjectServer.DATA_VERSION)
			DataUpgrader.attemptUpgrade(projectDirectory);
		
		if(getDatabase().readDataVersion(projectDirectory) < ProjectServer.DATA_VERSION)
			throw new OldVersionException();

		ProjectServer db = getDatabase();
		db.open(projectDirectory);
		try
		{
			loadProjectInfo();
			objectManager.loadFromDatabase();
			EAM.logDebug("Highest Factor Id: " + getNodeIdAssigner().getHighestAssignedId());
			EAM.logDebug("Highest Normal Id: " + getAnnotationIdAssigner().getHighestAssignedId());
		}
		catch(Exception e)
		{
			close();
			throw e;
		}
	}
	
	private void createProject(File projectDirectory) throws Exception
	{
		getDatabase().create(projectDirectory);
	}
	
	private void loadProjectInfo() throws IOException, ParseException
	{
		getDatabase().readProjectInfo(projectInfo);
	}
	
	private void saveProjectInfo() throws IOException
	{
		getDatabase().writeProjectInfo(projectInfo);
	}

	private void loadThreatRatingFramework() throws Exception
	{
		getSimpleThreatRatingFramework().load();
	}
	
	protected void finishOpening() throws Exception
	{
		if(getMetadataId().isInvalid())
			createProjectMetadata();
		
		loadThreatRatingFramework();
		
		applyDefaultBehavior();
		setDefaultDiagramPage(ObjectType.CONCEPTUAL_MODEL_DIAGRAM);
		setDefaultDiagramPage(ObjectType.RESULTS_CHAIN_DIAGRAM);
		database.writeVersion();

	}

	protected void setDefaultDiagramPage(int objectType) throws Exception
	{
		EAMObjectPool pool = getPool(objectType);
		if (pool.size() == 0)
			return;
	
		ViewData viewData = getCurrentViewData();
		ORef currentDiagramObjectRef = DiagramPageList.getCurrentDiagramViewDataRef(viewData, objectType);
		if (!currentDiagramObjectRef.isInvalid())
			return;

		ORef firstPoolItemRef = pool.getORefList().get(0);
		String currentDiagramViewDataTag = DiagramPageList.getCurrentDiagramViewDataTag(objectType);
		CommandSetObjectData setCurrentDiagramObject = new CommandSetObjectData(viewData.getRef(), currentDiagramViewDataTag, firstPoolItemRef);
		executeCommand(setCurrentDiagramObject);		
	}
	
	public String getFilename()
	{
		if(isOpen())
			return getDatabase().getName();
		return EAM.text("[No Project]");
	}

	public boolean isOpen()
	{
		return getDatabase().isOpen();
	}
	
	public void close() throws Exception
	{
		if(!isOpen())
			return;
		
		EAM.logWarning("Memory statistics:\n" + 
				"  Undos: " + undoRedoState.size() + "\n" + 
				"  Max:   " + Runtime.getRuntime().maxMemory() + "\n" +
				"  Total: " + Runtime.getRuntime().totalMemory() + "\n" +
				"  Free:  " + Runtime.getRuntime().freeMemory() + "\n" +
				"");
		try
		{
			getDatabase().close();
			clear();
		}
		catch (IOException e)
		{
			EAM.logException(e);
		}
		
	}
	
	static public boolean isValidProjectFilename(String candidate)
	{
		return candidate.equals(makeProjectFilenameLegal(candidate));
	}
	
	static public String makeProjectFilenameLegal(String candidate)
	{
		if(candidate.length() < 1)
			return "-";
		
		if(candidate.length() > MAX_PROJECT_FILENAME_LENGTH)
			candidate = candidate.substring(0, MAX_PROJECT_FILENAME_LENGTH);
		
		char[] asArray = candidate.toCharArray();
		for(int i = 0; i < candidate.length(); ++i)
		{
			char c = asArray[i];
			if(c >= 128)
				continue;
			if(Character.isLetterOrDigit(c))
				continue;
			if(c == ' ' || c == '.' || c == '-')
				continue;
			
			asArray[i] = '-';
		}

		return new String(asArray);
	}
	
	public void toXml(UnicodeWriter out) throws Exception
	{
		out.writeln("<MiradiProject>");
		out.writeln("<FileName>" + XmlUtilities.getXmlEncoded(getFilename()) + "</FileName>");
		out.writeln("<ExportDate>" + new MultiCalendar().toIsoDateString() + "</ExportDate>");
		objectManager.toXml(out);
		out.writeln("</MiradiProject>");
	}

	/////////////////////////////////////////////////////////////////////////////////
	// command execution

	public void executeCommand(Command command) throws CommandFailedException
	{
		if(command.isDoNothingCommand(this))
			return;
		
		isExecuting = true;
		try
		{
			executeWithoutRecording(command);
			recordCommand(command);
		}
		finally
		{
			isExecuting = false;
		}
	}
	
	public void executeCommandsWithoutTransaction(Command[] commands) throws CommandFailedException
	{
		for(int i = 0; i < commands.length; ++i)
		{
			executeCommand(commands[i]);
		}
	}

	public void executeCommandsWithoutTransaction(Vector commands) throws CommandFailedException
	{
		executeCommandsWithoutTransaction((Command[]) commands.toArray(new Command[0]));
	}
	
	public void executeCommandsAsTransaction(Command[] commands) throws CommandFailedException
	{
		executeCommand(new CommandBeginTransaction());
		try
		{
			executeCommandsWithoutTransaction(commands);
		}
		finally
		{
			executeCommand(new CommandEndTransaction());
		}
	}
	
	public Command undo() throws CommandFailedException
	{
		Command cmd = undoRedoState.popCommandToUndo();
		try
		{
			isExecuting = true;
			executeWithoutRecording(cmd.getReverseCommand());
			fireCommandExecuted(cmd.getReverseCommand());
			return cmd;
		}
		finally
		{
			isExecuting = false;
		}
	}
	
	public Command redo() throws CommandFailedException
	{
		Command cmd = undoRedoState.popCommandToRedo();
		try
		{
			EAM.logVerbose("Redoing: " + cmd.toString());
			isExecuting = true;
			executeWithoutRecording(cmd);
			fireCommandExecuted(cmd);
			return cmd;
		}
		finally
		{
			isExecuting = false;
		}
	}

	private void executeWithoutRecording(Command command) throws CommandFailedException
	{
		try 
		{
			EAM.logVerbose("Executing: " + command.toString());
			command.executeAndLog(this);
			EAM.logVerbose("Finished : " + command.toString());
		} 
		catch (CommandFailedException e) 
		{
			throw(e);
		}
	}
	
	public void executeInsideListener(Command command) throws CommandFailedException
	{
		if(!firingCommandExecutedEvents)
		{
			EAM.internalError(EAM.text("Attempt to execute command from outside command listener"));
		}
		
		executeWithoutRecording(command);
	}
	
	public void recordCommand(Command command)
	{
		if(firingCommandExecutedEvents)
		{
			EAM.internalError(EAM.text("Attempt to execute command from command listener"));
		}
		
		try
		{
			undoRedoState.pushUndoableCommand(command);
			fireCommandExecuted(command);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}
	
	public boolean isExecutingACommand()
	{
		return isExecuting;
	}

	public void addCommandExecutedListener(CommandExecutedListener listener)
	{
		if(commandExecutedListeners.contains(listener))
			throw new RuntimeException("Attempted to add listener twice: " + listener.getClass());
		EAM.logVerbose("addCommandExecutedListener: " + listener.getClass());
		commandExecutedListeners.add(listener);
	}
	
	public void removeCommandExecutedListener(CommandExecutedListener listener)
	{
		EAM.logVerbose("removeCommandExecutedListener: " + listener.getClass());
		if(!commandExecutedListeners.contains(listener))
			EAM.logWarning("removeCommandExecutedListener not in list: " + listener.getClass());
		commandExecutedListeners.remove(listener);
	}

	void fireCommandExecuted(Command command)
	{
		if(command.isBeginTransaction() || command.isEndTransaction())
			return;
		
		EAM.logVerbose("fireCommandExecuted: " + command.toString());
		firingCommandExecutedEvents = true;
		try
		{
			CommandExecutedEvent event = new CommandExecutedEvent(command);
			for(int i=0; i < getCommandListenerCount(); ++i)
			{
				CommandExecutedListener listener = (CommandExecutedListener)commandExecutedListeners.get(i);
				listener.commandExecuted(event);
			}
		}
		finally
		{
			firingCommandExecutedEvents = false;
		}
	}
	
	public int getCommandListenerCount()
	{
		return commandExecutedListeners.size();
	}
	
	public void logCommandListeners(PrintStream out)
	{
		for(int i=0; i < getCommandListenerCount(); ++i)
		{
			CommandExecutedListener listener = (CommandExecutedListener)commandExecutedListeners.get(i);
			out.println(listener.getClass());
		}
	}

	public boolean canUndo()
	{
		if(!isOpen())
			return false;
		
		return undoRedoState.canUndo();
	}
	
	public boolean canRedo()
	{
		if(!isOpen())
			return false;
		
		return undoRedoState.canRedo();
	}
	
	public void beginTransaction() throws CommandFailedException
	{
		if(inTransaction)
			throw new CommandFailedException("Attempted to nest transactions");
		inTransaction = true;
	}
	
	public void endTransaction()
	{
		inTransaction = false;
	}
	
	public boolean isInTransaction()
	{
		return inTransaction;
	}
	
	/////////////////////////////////////////////////////////////////////////////////
	// views
	
	public void switchToView(String viewName) throws CommandFailedException
	{
		currentViewName = viewName;
	}

	public boolean isLegalViewName(String viewName)
	{
		return Arrays.asList(getLegalViewNames()).contains(viewName);
	}
	
	public String[] getLegalViewNames()
	{
		return new String[] {
			SUMMARY_VIEW_NAME,
			DIAGRAM_VIEW_NAME,
			NO_PROJECT_VIEW_NAME,
			THREAT_MATRIX_VIEW_NAME,
			BUDGET_VIEW_NAME,
			WORK_PLAN_VIEW_NAME,
			MAP_VIEW_NAME,
			SCHEDULE_VIEW_NAME,
			LIBRARY_VIEW_NAME,
			STRATEGIC_PLAN_VIEW_NAME,
			MONITORING_VIEW_NAME,
			TARGET_VIABILITY_NAME,
			
		};
	}
	
	/////////////////////////////////////////////////////////////////////////////////
	// diagram view

	public ORefList findConceptualModelThatContainsBothFactors(FactorId fromFactorId, FactorId toFactorId)
	{
		ORefList conceptualModels = new ORefList();
		ConceptualModelDiagramPool diagramPool = getConceptualModelDiagramPool();
		ORefList diagramORefs = diagramPool.getORefList();
		for (int i = 0; i < diagramORefs.size(); ++i)
		{
			ORef thisDiagramRef = diagramORefs.get(i);
			ConceptualModelDiagram diagram =  (ConceptualModelDiagram) findObject(thisDiagramRef);
			if (diagram.containsWrappedFactor(fromFactorId) && diagram.containsWrappedFactor(toFactorId))
				conceptualModels.add(thisDiagramRef); 		
		}
		
		return conceptualModels;
	}
	
	public DiagramLink[] getToAndFromLinks(DiagramFactorId diagramFactorId)
	{
		DiagramFactorLinkId[] allLinkIds = getDiagramFactorLinkPool().getallDiagramFactorLinkIds();
		Vector fromAndToLinksForFactor = new Vector();
		for (int i = 0; i < allLinkIds.length; i++)
		{
			DiagramLink link = (DiagramLink) findObject(new ORef(ObjectType.DIAGRAM_LINK, allLinkIds[i]));
			if ((link.getFromDiagramFactorId().equals(diagramFactorId) || (link.getToDiagramFactorId().equals(diagramFactorId))))
				fromAndToLinksForFactor.add(link);
		}
		
		return (DiagramLink[]) fromAndToLinksForFactor.toArray(new DiagramLink[0]);
	}
	
	public DiagramFactorId[] getAllDiagramFactorIds()
	{
		return getDiagramFactorPool().getDiagramFactorIds();
	}
	
	public DiagramFactor[] getAllDiagramFactors()
	{
		DiagramFactorId[] diagramFactorIds = getAllDiagramFactorIds();
		DiagramFactor[] diagramFactors = new DiagramFactor[diagramFactorIds.length];
		
		for (int i = 0; i < diagramFactorIds.length; i++)
		{
			diagramFactors[i] = (DiagramFactor) findObject(new ORef(ObjectType.DIAGRAM_FACTOR, diagramFactorIds[i]));
		}
		
		return diagramFactors;
	}

	protected void writeFactor(FactorId factorId) throws IOException, ParseException
	{
		Factor cmNode = findNode(factorId);
		database.writeObject(cmNode);
	}

	public boolean areLinked(ORef factorRef1, ORef factorRef2)
	{
		Factor factor1 = (Factor)findObject(factorRef1);
		Factor factor2 = (Factor)findObject(factorRef2);
		return areLinked(factor1, factor2);
	}

	public boolean areLinked(Factor factor1, Factor factor2)
	{
		return getFactorLinkPool().areLinked(factor1, factor2);
	}
	
	public int forceNonZeroEvenSnap(int value)
	{
		//TODO this null check is here for test code
		if (getObjectManager() == null)
			return value;
		
		int gridSize = getGridSize();
		int newValue = (value + gridSize) - (value + gridSize) % (gridSize * 2);
		
		if (newValue != 0)
			return newValue;
		
		return gridSize * 2;
	}
		
	public int getGridSize()
	{
		return DEFAULT_GRID_SIZE;
	}
	
	public Point getSnapped(int x, int y)
	{
		return getSnapped(new Point(x, y));
	}
	
	public Dimension getSnapped(Dimension dimension)
	{
		int gridSize = getGridSize();
		return new Dimension(roundTo(dimension.width, gridSize), roundTo(dimension.height, gridSize));
	}
	
	public Point getSnapped(Point point)
	{
		int gridSize = getGridSize();
		return new Point(roundTo(point.x, gridSize), roundTo(point.y, gridSize));
	}
	
	int roundTo(int valueToRound, int incrementToRoundTo)
	{
		int sign = 1;
		if(valueToRound < 0)
			sign = -1;
		valueToRound = Math.abs(valueToRound);
		
		int half = incrementToRoundTo / 2;
		valueToRound += half;
		valueToRound -= (valueToRound % incrementToRoundTo);
		return valueToRound * sign;
	}
	
	public ProjectResource[] getAllProjectResources()
	{
		IdList allResourceIds = getResourcePool().getIdList();
		return getResources(allResourceIds);
	}

	public ProjectResource[] getResources(IdList resourceIds)
	{
		ProjectResource[] availableResources = new ProjectResource[resourceIds.size()];
		for(int i = 0; i < availableResources.length; ++i)
			availableResources[i] = getResourcePool().find(resourceIds.get(i));
		return availableResources;
	}
	
	public DiagramClipboard getDiagramClipboard()
	{
		return diagramClipboard;
	}

	public DecimalFormat getDecimalFormatter()
	{
		DecimalFormat formatter = new DecimalFormat("##0.##");
		formatter.setDecimalSeparatorAlwaysShown(false);
		
		return formatter;
	}
	
	public DecimalFormat getCurrencyFormatter()
	{
		int currencyDecimalPlaces = getMetadata().getCurrencyDecimalPlaces();
		DecimalFormat formatter = new DecimalFormat();
		formatter.setMinimumFractionDigits(currencyDecimalPlaces);
		formatter.setMaximumFractionDigits(currencyDecimalPlaces);
		
		return formatter;
	}
	
	public int getDiagramFontSize()
	{
		int size = getMetadata().getDiagramFontSize();
		if(size == 0)
			return DEFAULT_DIAGRAM_FONT_SIZE;
		
		return size;

	}


	public static final String MONITORING_VIEW_NAME = "Monitoring Plan";
	public static final String STRATEGIC_PLAN_VIEW_NAME = "Strategic Plan";
	public static final String LIBRARY_VIEW_NAME = "Library";
	public static final String SCHEDULE_VIEW_NAME = "Schedule";
	public static final String MAP_VIEW_NAME = "Map";
	public static final String WORK_PLAN_VIEW_NAME = "Work Plan";
	public static final String BUDGET_VIEW_NAME = "Financial";
	public static final String THREAT_MATRIX_VIEW_NAME = "ThreatMatrix";
	public static final String NO_PROJECT_VIEW_NAME = "NoProject";
	public static final String DIAGRAM_VIEW_NAME = "Diagram";
	public static final String SUMMARY_VIEW_NAME = "Summary";
	public static final String TARGET_VIABILITY_NAME = "Target Viability";
	public static final String PLANNING_VIEW_NAME = "Planning";
	
	public static final String DEFAULT_VIEW_NAME = SUMMARY_VIEW_NAME;
	
	public static final int DEFAULT_GRID_SIZE = 15;
	public static final int DEFAULT_DIAGRAM_FONT_SIZE = 11;
	

	private static final int MAX_PROJECT_FILENAME_LENGTH = 32;
	
	private static final String COMMAND_LOG_FILE_NAME = "command.log";
	
	ProjectInfo projectInfo;
	ObjectManager objectManager;
	UndoRedoState undoRedoState;
	boolean isExecuting;
	boolean firingCommandExecutedEvents;

	SimpleThreatRatingFramework simpleThreatFramework;
	StressBasedThreatRatingFramework stressBasedThreatFramework;
	
	ProjectServer database;
	DiagramClipboard diagramClipboard;
	private ProjectCalendar projectCalendar;

	Vector commandExecutedListeners;
	
	LayerManager layerManager;
	boolean inTransaction;
	
	// FIXME: This should go away, but it's difficult
	String currentViewName;

}

