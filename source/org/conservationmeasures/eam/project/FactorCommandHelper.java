/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.project;

import java.awt.Dimension;
import java.awt.Point;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Vector;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandDeleteObject;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.diagram.cells.FactorDataHelper;
import org.conservationmeasures.eam.diagram.cells.FactorDataMap;
import org.conservationmeasures.eam.diagram.factortypes.FactorType;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.TransferableEamList;
import org.conservationmeasures.eam.main.TransferableMiradiList;
import org.conservationmeasures.eam.objecthelpers.CreateDiagramFactorLinkParameter;
import org.conservationmeasures.eam.objecthelpers.CreateDiagramFactorParameter;
import org.conservationmeasures.eam.objecthelpers.CreateFactorLinkParameter;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objectpools.EAMObjectPool;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.DiagramLink;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.objects.KeyEcologicalAttribute;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.objects.ThreatReductionResult;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;
import org.conservationmeasures.eam.utils.PointList;
import org.conservationmeasures.eam.views.diagram.DeleteAnnotationDoer;
import org.conservationmeasures.eam.views.diagram.DeleteKeyEcologicalAttributeDoer;
import org.conservationmeasures.eam.views.diagram.LinkCreator;
import org.conservationmeasures.eam.views.umbrella.DeleteActivity;

public class FactorCommandHelper
{
	public FactorCommandHelper(Project projectToUse, DiagramModel modelToUse)
	{
		project = projectToUse;
		currentModel = modelToUse;
	}

	private void addCreatedFactorToConceptualModel(int objectType, FactorId factorId, Point insertionLocation, Dimension size, String label) throws Exception
	{
    	if (!currentModel.getDiagramObject().isResultsChain())
    		return;
    	
    	final int ONLY_CONCEPTUAL_MODEL_INDEX = 0;
    	ORefList refList = getProject().getConceptualModelDiagramPool().getORefList();
    	DiagramObject diagramObject = (DiagramObject) getProject().findObject(refList.get(ONLY_CONCEPTUAL_MODEL_INDEX));
		

    	DiagramFactorId createdDiagramFactorId = (DiagramFactorId) createDiagramFactor(diagramObject, objectType, factorId).getCreatedId();
    	DiagramFactor createdDiagramFactor = (DiagramFactor) project.findObject(new ORef(ObjectType.DIAGRAM_FACTOR, createdDiagramFactorId));
    	setLocationSizeLabel(createdDiagramFactor, insertionLocation, size, label);
	}
	
	public CommandCreateObject createFactorAndDiagramFactor(int objectType, Point insertionLocation, Dimension size, String label) throws Exception
	{
		CommandCreateObject createObjectCommand = createFactorAndDiagramFactor(objectType);
		DiagramFactorId diagramFactorId = (DiagramFactorId) createObjectCommand.getCreatedId();
		DiagramFactor diagramFactor = (DiagramFactor) project.findObject(new ORef(ObjectType.DIAGRAM_FACTOR, diagramFactorId));
		setLocationSizeLabel(diagramFactor, insertionLocation, size, label);
		
		if (shouldTypeBeCopiedToConceptualModel(objectType))
			addCreatedFactorToConceptualModel(objectType, diagramFactor.getWrappedId(), insertionLocation, size, label);
		
		return createObjectCommand;
	}

	public CommandCreateObject createFactorAndDiagramFactor(int objectType) throws Exception
	{
		FactorId factorId = createFactor(objectType);
		return createDiagramFactor(currentModel.getDiagramObject(), objectType, factorId);
	}
	
	public CommandCreateObject createDiagramFactor(DiagramObject diagramObject, int objectType, FactorId factorId) throws Exception
	{
		CreateDiagramFactorParameter extraDiagramFactorInfo = new CreateDiagramFactorParameter(new ORef(objectType, factorId));
		CommandCreateObject createDiagramFactor = new CommandCreateObject(ObjectType.DIAGRAM_FACTOR, extraDiagramFactorInfo);
		executeCommand(createDiagramFactor);
		
		DiagramFactorId diagramFactorId = (DiagramFactorId) createDiagramFactor.getCreatedId();
		CommandSetObjectData addDiagramFactor = CommandSetObjectData.createAppendIdCommand(diagramObject, DiagramObject.TAG_DIAGRAM_FACTOR_IDS, diagramFactorId);
		executeCommand(addDiagramFactor);
		
		Factor factor = project.findNode(factorId);
		Command[] commandsToAddToView = getProject().getCurrentViewData().buildCommandsToAddNode(factor.getRef());
		for(int i = 0; i < commandsToAddToView.length; ++i)
			executeCommand(commandsToAddToView[i]);
		
		return createDiagramFactor;
	}

	private FactorId createFactor(int objectType) throws CommandFailedException
	{
		CommandCreateObject createFactorCommand = new CommandCreateObject(objectType);
		executeCommand(createFactorCommand);
		
		return (FactorId) createFactorCommand.getCreatedId();
	}

	private boolean shouldTypeBeCopiedToConceptualModel(int objectType)
	{
		return ObjectType.STRATEGY == objectType;
	}
	
	private void setLocationSizeLabel(DiagramFactor diagramFactor, Point insertionLocation, Dimension size, String label) throws CommandFailedException, Exception
	{
		setDiagramFactorSize(diagramFactor.getDiagramFactorId(), size);
		setDiagramFactorLocation(diagramFactor.getDiagramFactorId(), insertionLocation);
		setDiagramFactorLabel(diagramFactor.getWrappedId(), label);
	}
	
	public void pasteFactorsOnlyIntoProject(TransferableEamList list, Point startPoint) throws Exception
	{
		executeCommand(new CommandBeginTransaction());
		FactorDataHelper dataHelper = new FactorDataHelper(project.getAllDiagramFactorIds());
		pasteFactorsIntoProject(list, startPoint, dataHelper);
		executeCommand(new CommandEndTransaction());
	}

	private void pasteFactorsIntoProject(TransferableEamList list, Point startPoint, FactorDataHelper dataHelper) throws Exception 
	{	
		FactorDataMap[] nodes = list.getArrayOfFactorDataMaps();
		for (int i = 0; i < nodes.length; i++) 
		{
			FactorDataMap nodeData = nodes[i];
			String label = nodeData.getLabel();
			Dimension originalSize = nodeData.getDimension(DiagramFactor.TAG_SIZE);
			DiagramFactorId originalDiagramFactorId = new DiagramFactorId(nodeData.getId(DiagramFactor.TAG_ID).asInt());
			
			Point point = nodeData.getPoint(DiagramFactor.TAG_LOCATION);
			int offsetToAvoidOverlaying = getProject().getDiagramClipboard().getPasteOffset();
			point.setLocation(point.x, point.y);
			dataHelper.setOriginalLocation(originalDiagramFactorId, point);
			Point newLocation = dataHelper.getNewLocation(originalDiagramFactorId, startPoint);
			newLocation.setLocation(newLocation.x + offsetToAvoidOverlaying, newLocation.y + offsetToAvoidOverlaying);
			newLocation = getProject().getSnapped(newLocation);
			
			int type = FactorType.getFactorTypeFromString(nodeData.getString(Factor.TAG_NODE_TYPE));
			CommandCreateObject addCommand = createFactorAndDiagramFactor(type, newLocation, originalSize, label);
			DiagramFactorId newDiagramFactorId = (DiagramFactorId) addCommand.getCreatedId();
			dataHelper.setNewId(originalDiagramFactorId, newDiagramFactorId);
			EAM.logDebug("Paste Node: " + newDiagramFactorId);
			
			FactorCell newFactorCell = getDiagramFactorById(newDiagramFactorId);
			
			setLocationSizeLabel(newFactorCell.getDiagramFactor(), newLocation, originalSize, label);
		}
	}

	private void setDiagramFactorSize(DiagramFactorId diagramFactorId, Dimension originalSize) throws CommandFailedException
	{
		String currentSize = EnhancedJsonObject.convertFromDimension(originalSize);
		CommandSetObjectData setSizeCommand = new CommandSetObjectData(ObjectType.DIAGRAM_FACTOR, diagramFactorId, DiagramFactor.TAG_SIZE, currentSize);
		executeCommand(setSizeCommand);
	}

	private void setDiagramFactorLabel(FactorId factorId, String label) throws CommandFailedException
	{
		CommandSetObjectData setLabel = new CommandSetObjectData(ObjectType.FACTOR, factorId, Factor.TAG_LABEL, label); 
		executeCommand(setLabel);
	}
	
	private void setDiagramFactorLocation(DiagramFactorId diagramFactorId, Point newNodeLocation) throws Exception
	{
		String newMoveLocation = EnhancedJsonObject.convertFromPoint(new Point(newNodeLocation.x, newNodeLocation.y));
		CommandSetObjectData moveCommand = new CommandSetObjectData(ObjectType.DIAGRAM_FACTOR, diagramFactorId, DiagramFactor.TAG_LOCATION, newMoveLocation);
		executeCommand(moveCommand);
	}

	public boolean canPaste(TransferableMiradiList list) throws Exception
	{
		Vector<String> factorDeepCopies  = list.getFactorDeepCopies();
		for (int i = 0; i < factorDeepCopies.size(); i++) 
		{
			String jsonAsString = factorDeepCopies.get(i);
			EnhancedJsonObject json = new EnhancedJsonObject(jsonAsString);
			int type = json.getInt("Type");
			if (! canPasteTypeInCurrentTab(type))
				return false;
		}
		
		return true;
	}

	private boolean canPasteTypeInCurrentTab(int type)
	{
		if (isResultsChain() && containsType(getResultsChainPastableTypes(), type))
			return true;
		
		if (! isResultsChain() && containsType(getConceptualDiagramPastableTypes(), type))
			return true;
		
		return false;
	}

	private boolean isResultsChain()
	{
		DiagramObject diagramObject = currentModel.getDiagramObject();
		if (diagramObject.getType() == ObjectType.RESULTS_CHAIN_DIAGRAM)
			return true;
		
		return false;
	}

	private boolean containsType(int[] listOfTypes, int type)
	{
		for (int i = 0 ; i < listOfTypes.length; ++i)
		{
			if (listOfTypes[i] == type)
				return true;
		}
		
		return false;
	}
	
	private int[] getResultsChainPastableTypes()
	{
		return new int[] {
				ObjectType.INTERMEDIATE_RESULT, 
				ObjectType.STRATEGY, 
				ObjectType.TARGET, 
				ObjectType.TEXT_BOX, };
	}
	
	private int[] getConceptualDiagramPastableTypes()
	{
		return new int[] {
				ObjectType.CAUSE, 
				ObjectType.STRATEGY, 
				ObjectType.TARGET,
				ObjectType.TEXT_BOX, };
	}

	private FactorCell getDiagramFactorById(DiagramFactorId newNodeId) throws Exception
	{
		return getDiagramModel().getFactorCellById(newNodeId);
	}	
	
	private PointList movePoints(int offsetToAvoidOverlaying, FactorDataHelper dataHelper, Point startPoint, PointList originalBendPoints)
	{
		PointList movedPoints = new PointList();
		for (int i = 0; i < originalBendPoints.size(); ++i)
		{
			Point originalPoint = originalBendPoints.get(i);
			Point movedPoint = dataHelper.getNewLocation(originalPoint, startPoint);
			movedPoint.setLocation(movedPoint.x + offsetToAvoidOverlaying, movedPoint.y + offsetToAvoidOverlaying);
			movedPoint = getProject().getSnapped(movedPoint);
			movedPoints.add(movedPoint);
		}
		
		return movedPoints;
	}

	private int getOffsetToAvoidOverlaying(Vector diagramLinkDeepCopies)
	{
		int NO_OFFSET = 0;
		if (diagramLinkDeepCopies.size() > 0)
			return getProject().getDiagramClipboard().getPasteOffset();
		
		return NO_OFFSET;
	}
	
	public static CommandSetObjectData createSetLabelCommand(ORef ref, String newLabel)
	{
		return new CommandSetObjectData(ref, Factor.TAG_LABEL, newLabel);
	}

	private Project getProject()
	{
		return project;
	}
	
	private void executeCommand(Command cmd) throws CommandFailedException
	{
		getProject().executeCommand(cmd);
	}
	
	private DiagramModel getDiagramModel()
	{
		return currentModel;
	}
	
	// TODO: This method should have unit tests
	public void deleteFactor(FactorCell factorToDelete, DiagramObject diagramObject) throws Exception
	{
		removeFromThreatReductionResults(factorToDelete);
		removeFromView(factorToDelete.getWrappedId());
		removeNodeFromDiagram(factorToDelete, diagramObject);
		deleteDiagramFactor(factorToDelete.getDiagramFactorId());
	
		Factor underlyingNode = factorToDelete.getUnderlyingObject();
		if (! canDeleteFactor(underlyingNode))
			return;

		deleteAnnotations(underlyingNode);
		deleteUnderlyingNode(underlyingNode);
	}

	private void removeFromThreatReductionResults(FactorCell factorToDelete) throws CommandFailedException
	{
		Factor factor = factorToDelete.getUnderlyingObject();
		EAMObjectPool pool = project.getPool(ObjectType.THREAT_REDUCTION_RESULT);
		ORefList orefList = pool.getORefList();
		for (int i = 0; i < orefList.size(); ++i)
		{
			ThreatReductionResult threatReductionResult = (ThreatReductionResult) project.findObject(orefList.get(i));
			ORef directThreatRef = ORef.createFromString(threatReductionResult.getRelatedDirectThreatRefAsString());
			if (! directThreatRef.equals(factor.getRef()))
				continue;
			
			CommandSetObjectData setDirectThreat = new CommandSetObjectData(threatReductionResult.getRef(), ThreatReductionResult.TAG_RELATED_DIRECT_THREAT_REF, ORef.INVALID.toString());
			project.executeCommand(setDirectThreat);
		}
	}

	private boolean canDeleteFactor(Factor factorToDelete)
	{
		ObjectManager objectManager = project.getObjectManager();
		ORefList referrers = factorToDelete.findObjectsThatReferToUs(objectManager, ObjectType.DIAGRAM_FACTOR, factorToDelete.getRef());
		if (referrers.size() > 0)
			return false;
		
		return true;
	}

	private void deleteDiagramFactor(DiagramFactorId diagramFactorId) throws CommandFailedException
	{
		CommandDeleteObject deleteDiagramFactorCommand = new CommandDeleteObject(ObjectType.DIAGRAM_FACTOR, diagramFactorId);
		getProject().executeCommand(deleteDiagramFactorCommand);
	}

	private void removeFromView(FactorId id) throws ParseException, Exception, CommandFailedException
	{
		Factor factor = getProject().findNode(id);
		Command[] commandsToRemoveFromView = getProject().getCurrentViewData().buildCommandsToRemoveNode(factor.getRef());
		for(int i = 0; i < commandsToRemoveFromView.length; ++i)
			getProject().executeCommand(commandsToRemoveFromView[i]);
	}

	private void removeNodeFromDiagram(FactorCell factorToDelete, DiagramObject diagramObject) throws CommandFailedException, ParseException
	{
		DiagramFactorId idToDelete = factorToDelete.getDiagramFactorId();
		CommandSetObjectData removeDiagramFactor = CommandSetObjectData.createRemoveIdCommand(diagramObject, DiagramObject.TAG_DIAGRAM_FACTOR_IDS, idToDelete);
		getProject().executeCommand(removeDiagramFactor);
		
		Command[] commandsToClear = factorToDelete.getDiagramFactor().createCommandsToClear();
		getProject().executeCommands(commandsToClear);
	}

	private void deleteUnderlyingNode(Factor factorToDelete) throws CommandFailedException
	{
		Command[] commandsToClear = factorToDelete.createCommandsToClear();
		getProject().executeCommands(commandsToClear);
		getProject().executeCommand(new CommandDeleteObject(factorToDelete.getType(), factorToDelete.getFactorId()));
	}
	
	private void deleteAnnotations(Factor factorToDelete) throws Exception
	{
		deleteAnnotations(factorToDelete, ObjectType.GOAL, factorToDelete.TAG_GOAL_IDS);
		deleteAnnotations(factorToDelete, ObjectType.OBJECTIVE, factorToDelete.TAG_OBJECTIVE_IDS);
		deleteAnnotations(factorToDelete, ObjectType.INDICATOR, factorToDelete.TAG_INDICATOR_IDS);
		//TODO: there is much common code between DeleteAnnotationDoer and DeleteActivity classes and this class; 
		// for example DeleteActivity.deleteTaskTree( is general and and good not just for activities
		// I am thinking that each object Task should be able to handle its own deletion so when you call it it would delete all its own 
		// children inforceing referencial integrity as a cascade, instead of having the the code here.
		if (factorToDelete.isStrategy())
			removeAndDeleteTasksInList(factorToDelete, Strategy.TAG_ACTIVITY_IDS);
		
		if (factorToDelete.isTarget())
			removeAndDeleteKeyEcologicalAttributesInList(factorToDelete, Target.TAG_KEY_ECOLOGICAL_ATTRIBUTE_IDS);
	}

	private void deleteAnnotations(Factor factorToDelete, int annotationType, String annotationListTag) throws Exception
	{
		IdList ids = new IdList(factorToDelete.getData(annotationListTag));
		for(int annotationIndex = 0; annotationIndex < ids.size(); ++annotationIndex)
		{
			BaseObject thisAnnotation = getProject().findObject(annotationType, ids.get(annotationIndex));
			Command[] commands = DeleteAnnotationDoer.buildCommandsToDeleteAnnotation(getProject(), factorToDelete, annotationListTag, thisAnnotation);
			getProject().executeCommands(commands);
		}
	}
	
	private void removeAndDeleteTasksInList(BaseObject objectToDelete, String annotationListTag) throws Exception
	{
		IdList ids = new IdList(objectToDelete.getData(annotationListTag));
		for(int annotationIndex = 0; annotationIndex < ids.size(); ++annotationIndex)
		{
			Task childTask = (Task)getProject().findObject(ObjectType.TASK, ids.get(annotationIndex));
			DeleteActivity.deleteTaskTree(getProject(), childTask);
		}
	}
	
	private void removeAndDeleteKeyEcologicalAttributesInList(Factor objectToDelete, String annotationListTag) throws Exception
	{
		IdList ids = new IdList(objectToDelete.getData(annotationListTag));
		for(int annotationIndex = 0; annotationIndex < ids.size(); ++annotationIndex)
		{
			KeyEcologicalAttribute kea = (KeyEcologicalAttribute)getProject().findObject(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, ids.get(annotationIndex));
			Command[] commands = DeleteKeyEcologicalAttributeDoer.buildCommandsToDeleteAnnotation(getProject(), objectToDelete, annotationListTag, kea);
			getProject().executeCommands(commands);
		}
	}
	
	//FIXME Paste code is under construction but going in the right direction
	//lots of unsused code commented and will uncomment and reuse.  
	public void pasteMiradiDataFlavor(TransferableMiradiList list, Point startPoint) throws Exception
	{	
		try
		{
			FactorDataHelper dataHelper = new FactorDataHelper(project.getAllDiagramFactorIds());
			Vector factorDeepCopies = list.getFactorDeepCopies();
			HashMap oldToNewFactorIdMap = createNewFactors(factorDeepCopies);
			
			Vector diagramFactorDeepCopies = list.getDiagramFactorDeepCopies();
			HashMap oldToNewDiagramFactorIdMap = createNewDiagramFactors(diagramFactorDeepCopies, oldToNewFactorIdMap, dataHelper, startPoint);
			
			Vector factorLinkDeepCopies = list.getFactorLinkDeepCopies();
			HashMap oldToNewFactorLinkIdMap = createNewFactorLinks(factorLinkDeepCopies, oldToNewFactorIdMap);
			
			Vector diagramLinkDeepCopies = list.getDiagramLinkDeepCopies();
			createNewDiagramLinks(diagramLinkDeepCopies, oldToNewFactorLinkIdMap, oldToNewDiagramFactorIdMap, dataHelper, startPoint);
		}
		catch (Exception e)
		{
			throw e;
		}
	}
	
	private HashMap createNewFactors(Vector factorDeepCopies) throws Exception
	{
		HashMap oldToNewRefMap = new HashMap();
		for (int i = factorDeepCopies.size() - 1; i >= 0; --i)
		{			
			String jsonAsString = (String) factorDeepCopies.get(i);
			EnhancedJsonObject json = new EnhancedJsonObject(jsonAsString);
			
			BaseObject newObject = createObject(json);
			loadNewObjectFromOldJson(newObject, json);
			
			BaseId oldId = json.getId(BaseObject.TAG_ID);
			oldToNewRefMap.put(oldId, newObject.getId());
			fixupRefs(newObject, oldToNewRefMap);
		}
		
		return oldToNewRefMap;
	}

	private void fixupRefs(BaseObject newObject, HashMap oldToNewRefMap) throws Exception
	{
		Command[] commandsToFixRefs = newObject.createCommandToFixupRefLists(oldToNewRefMap);
		getProject().executeCommands(commandsToFixRefs);
	}

	private void loadNewObjectFromOldJson(BaseObject newObject, EnhancedJsonObject json) throws Exception, CommandFailedException
	{
		Command[] commandsToLoadFromJson = newObject.createCommandsToLoadFromJson(json);
		getProject().executeCommands(commandsToLoadFromJson);
	}

	private BaseObject createObject(EnhancedJsonObject json) throws CommandFailedException
	{
		return createObject(json, null);
	}
	
	private BaseObject createObject(EnhancedJsonObject json, CreateObjectParameter extraInfo) throws CommandFailedException
	{
		int type = json.getInt("Type");
		CommandCreateObject createObject = new CommandCreateObject(type, extraInfo);
		getProject().executeCommand(createObject);
		
		ORef newObjectRef = createObject.getObjectRef();
		BaseObject newObject = getProject().findObject(newObjectRef);
		
		return newObject;
	}

	private HashMap createNewDiagramFactors(Vector diagramFactorDeepCopies, HashMap oldToNewMap, FactorDataHelper dataHelper, Point startPoint) throws Exception
	{
		ORefList diagramFactorsToSelect = new ORefList();
		HashMap oldToNewDiagamFactorIdMap = new HashMap();
		for (int i = 0; i < diagramFactorDeepCopies.size(); ++i)
		{
			String jsonAsString = (String) diagramFactorDeepCopies.get(i);
			EnhancedJsonObject json = new EnhancedJsonObject(jsonAsString);
			ORef wrappedRef = json.getRef(DiagramFactor.TAG_WRAPPED_REF);
			BaseId newFactorId = (BaseId) oldToNewMap.get(wrappedRef.getObjectId());
			DiagramFactorId diagramFactorId = new DiagramFactorId(json.getId(DiagramFactor.TAG_ID).asInt());
			
			String newLocationAsJsonString = offSetLocation(startPoint, dataHelper, json, diagramFactorId);
			json.put(DiagramFactor.TAG_LOCATION, newLocationAsJsonString);
			
			ORef newDiagramFactorRef = createDiagramFactor(wrappedRef, newFactorId);
			DiagramFactor newDiagramFactor = (DiagramFactor) getProject().findObject(newDiagramFactorRef);
			Command[]  commandsToLoadFromJson = newDiagramFactor.loadDataFromJson(json);
			getProject().executeCommands(commandsToLoadFromJson);

			addToCurrentDiagram(newDiagramFactorRef, DiagramObject.TAG_DIAGRAM_FACTOR_IDS);
			diagramFactorsToSelect.add(newDiagramFactorRef);
			
			BaseId oldDiagramFactorId = json.getId(DiagramFactor.TAG_ID);
			oldToNewDiagamFactorIdMap.put(oldDiagramFactorId, newDiagramFactorRef.getObjectId());
		}
		addDiagramFactorToSelection(diagramFactorsToSelect);
		
		return oldToNewDiagamFactorIdMap;
	}

	private String offSetLocation(Point startPoint, FactorDataHelper dataHelper, EnhancedJsonObject json, DiagramFactorId diagramFactorId) throws Exception
	{
		Point point = json.getPoint(DiagramFactor.TAG_LOCATION);
		int offsetToAvoidOverlaying = getProject().getDiagramClipboard().getPasteOffset();
		point.setLocation(point.x, point.y);
		dataHelper.setOriginalLocation(diagramFactorId, point);
		Point newLocation = dataHelper.getNewLocation(diagramFactorId, startPoint);
		newLocation.setLocation(newLocation.x + offsetToAvoidOverlaying, newLocation.y + offsetToAvoidOverlaying);
		newLocation = getProject().getSnapped(newLocation);
		
		return EnhancedJsonObject.convertFromPoint(newLocation);
	}

	private void addDiagramFactorToSelection(ORefList diagramFactorRefsToSelect) throws Exception
	{
		//FIXME select cells after paste
		//DiagramModel model = getDiagramView().getDiagramModel();
		Vector factorCells = new Vector();
		for (int i = 0; i < diagramFactorRefsToSelect.size(); ++i)
		{
			ORef diagramFactorRefToSelect = diagramFactorRefsToSelect.get(i);
			DiagramFactorId diagramFactorId = new DiagramFactorId(diagramFactorRefToSelect.getObjectId().asInt());
			FactorCell cell = currentModel.getFactorCellById(diagramFactorId);
			factorCells.add(cell);
		}
		
		//FactorCell[] cells = (FactorCell[]) factorCells.toArray(new FactorCell[0]);
		//getDiagramView().getDiagramComponent().addSelectionCells(cells);
	}

	private ORef createDiagramFactor(ORef wrappedRef, BaseId newId) throws CommandFailedException
	{
		CreateDiagramFactorParameter extraInfo = new CreateDiagramFactorParameter(new ORef(wrappedRef.getObjectType(), newId));
		CommandCreateObject createDiagramFactor = new CommandCreateObject(DiagramFactor.getObjectType(), extraInfo);
		getProject().executeCommand(createDiagramFactor);
		
		return createDiagramFactor.getObjectRef();
	}

	private void addToCurrentDiagram(ORef newDiagramFactorRef, String tag) throws Exception
	{
		DiagramObject diagramObject = currentModel.getDiagramObject();
		CommandSetObjectData addDiagramFactor = CommandSetObjectData.createAppendIdCommand(diagramObject, tag, newDiagramFactorRef.getObjectId());
		getProject().executeCommand(addDiagramFactor);
	}
	
	private HashMap createNewFactorLinks(Vector factorLinkDeepCopies, HashMap oldToNewFactorIdMap) throws Exception
	{
		HashMap oldToNewFactorLinkIdMap = new HashMap();
		for (int i = 0; i < factorLinkDeepCopies.size(); ++i)
		{
			String jsonAsString = (String) factorLinkDeepCopies.get(i);
			EnhancedJsonObject json = new EnhancedJsonObject(jsonAsString);
			BaseId oldFactorLinkId = json.getId(FactorLink.TAG_ID);
			
			ORef newFromRef = getFactor(oldToNewFactorIdMap, json, FactorLink.TAG_FROM_REF);
			ORef newToRef = getFactor(oldToNewFactorIdMap, json, FactorLink.TAG_TO_REF);
			
			CreateFactorLinkParameter extraInfo = new CreateFactorLinkParameter(newFromRef, newToRef);
			FactorLink newFactorLink = (FactorLink) createObject(json, extraInfo);
			
			Command[]  commandsToLoadFromJson = newFactorLink.createCommandsToLoadFromJson(json);
			getProject().executeCommands(commandsToLoadFromJson);
			
			oldToNewFactorLinkIdMap.put(oldFactorLinkId, newFactorLink.getId());
		}
		
		return oldToNewFactorLinkIdMap;
	}

	private ORef getFactor(HashMap oldToNewFactorIdMap, EnhancedJsonObject json, String tag)
	{
		ORef oldRef = json.getRef(tag);
		BaseId newId = (BaseId) oldToNewFactorIdMap.get(oldRef.getObjectId());
		if (newId == null)
			return oldRef;
		 
		return new ORef(oldRef.getObjectType(), newId);
	}
	
	private void createNewDiagramLinks(Vector diagramLinkDeepCopies, HashMap oldToNewFactorLinkIdMap, HashMap oldToNewDiagramFactorIdMap, FactorDataHelper dataHelper, Point startPoint) throws Exception
	{	
		int offsetToAvoidOverlaying = getOffsetToAvoidOverlaying(diagramLinkDeepCopies);
		for (int i = 0; i < diagramLinkDeepCopies.size(); ++i )
		{
			String jsonAsString = (String) diagramLinkDeepCopies.get(i);
			EnhancedJsonObject json = new EnhancedJsonObject(jsonAsString);

			String movedBendPointsAsString = offSetBendPoints(startPoint, dataHelper, offsetToAvoidOverlaying, json);
			json.put(DiagramLink.TAG_BEND_POINTS, movedBendPointsAsString);
			
			BaseId oldWrappedFactorLinkId = json.getId(DiagramLink.TAG_WRAPPED_ID);
			BaseId newFatorLinkIdAsBaseId = (BaseId) oldToNewFactorLinkIdMap.get(oldWrappedFactorLinkId);
			FactorLinkId newFactorLinkId = new FactorLinkId(newFatorLinkIdAsBaseId.asInt());

			DiagramFactorId fromDiagramFactorId = getDiagramFactorId(oldToNewDiagramFactorIdMap, json, DiagramLink.TAG_FROM_DIAGRAM_FACTOR_ID);
			DiagramFactorId toDiagramFactorId = getDiagramFactorId(oldToNewDiagramFactorIdMap, json, DiagramLink.TAG_TO_DIAGRAM_FACTOR_ID);
			//FIXME shouldnt this also be inside the factorLink creation
			LinkCreator linkCreator = new LinkCreator(project);
			if (linkCreator.linkWasRejected(currentModel, fromDiagramFactorId, toDiagramFactorId))
				continue;
			
			CreateDiagramFactorLinkParameter extraInfo = new CreateDiagramFactorLinkParameter(newFactorLinkId, fromDiagramFactorId, toDiagramFactorId);
			DiagramLink newDiagramLink = (DiagramLink) createObject(json, extraInfo);
			
			Command[]  commandsToLoadFromJson = newDiagramLink.createCommandsToLoadFromJson(json);
			getProject().executeCommands(commandsToLoadFromJson);
			
			addToCurrentDiagram(newDiagramLink.getRef(), DiagramObject.TAG_DIAGRAM_FACTOR_LINK_IDS);
		}
	}

	private String offSetBendPoints(Point startPoint, FactorDataHelper dataHelper, int offsetToAvoidOverlaying, EnhancedJsonObject json) throws Exception
	{
		PointList originalBendPoints = new PointList(json.getString(DiagramLink.TAG_BEND_POINTS));
		PointList movedBendPoints = movePoints(offsetToAvoidOverlaying, dataHelper, startPoint, originalBendPoints);
		return movedBendPoints.toString();
	}

	private DiagramFactorId getDiagramFactorId(HashMap oldToNewDiagramFactorIdMap, EnhancedJsonObject json, String tag)
	{
		BaseId oldId = json.getId(tag);
		BaseId newId = (BaseId) oldToNewDiagramFactorIdMap.get(oldId);
		if (newId == null)
			return new DiagramFactorId(oldId.asInt()); 
			 
		return new DiagramFactorId(newId.asInt());
	}
		
	Project project;
	DiagramModel currentModel;
}
