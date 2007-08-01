/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.project;

import java.awt.Dimension;
import java.awt.Point;
import java.util.HashMap;
import java.util.Vector;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.diagram.cells.FactorDataHelper;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.TransferableMiradiList;
import org.conservationmeasures.eam.objecthelpers.CreateDiagramFactorLinkParameter;
import org.conservationmeasures.eam.objecthelpers.CreateDiagramFactorParameter;
import org.conservationmeasures.eam.objecthelpers.CreateFactorLinkParameter;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Assignment;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.DiagramLink;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;
import org.conservationmeasures.eam.utils.PointList;
import org.conservationmeasures.eam.views.diagram.LinkCreator;

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
			{
				EAM.logDebug("Cannot paste type " + type);
				return false;
			}
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
				ObjectType.TEXT_BOX, 
				ObjectType.INDICATOR,
				ObjectType.OBJECTIVE,
				ObjectType.TASK,
				ObjectType.GOAL,
				ObjectType.KEY_ECOLOGICAL_ATTRIBUTE,
				ObjectType.ASSIGNMENT,
				ObjectType.ACCOUNTING_CODE,
				ObjectType.FUNDING_SOURCE,
				};
	}
	
	private int[] getConceptualDiagramPastableTypes()
	{
		return new int[] {
				ObjectType.CAUSE, 
				ObjectType.STRATEGY, 
				ObjectType.TARGET,
				ObjectType.TEXT_BOX,
				ObjectType.INDICATOR,
				ObjectType.OBJECTIVE,
				ObjectType.TASK,
				ObjectType.GOAL,
				ObjectType.KEY_ECOLOGICAL_ATTRIBUTE,
				ObjectType.ASSIGNMENT,
				ObjectType.ACCOUNTING_CODE,
				ObjectType.FUNDING_SOURCE,
				};
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
	

	public void pasteMiradiDataFlavorWithoutLinks(TransferableMiradiList list, Point startPoint) throws Exception
	{	
		FactorDataHelper dataHelper = new FactorDataHelper(project.getAllDiagramFactorIds(), startPoint);
		HashMap oldToNewFactorRefMap = createNewFactors(list);

		Vector diagramFactorDeepCopies = list.getDiagramFactorDeepCopies();
		createNewDiagramFactors(diagramFactorDeepCopies, oldToNewFactorRefMap, dataHelper);
	}
	
	public void pasteMiradiDataFlavor(TransferableMiradiList list, Point startPoint) throws Exception
	{	
		FactorDataHelper dataHelper = new FactorDataHelper(project.getAllDiagramFactorIds(), startPoint);
		HashMap oldToNewFactorRefMap = createNewFactors(list);

		Vector diagramFactorDeepCopies = list.getDiagramFactorDeepCopies();
		HashMap oldToNewDiagramFactorRefMap = createNewDiagramFactors(diagramFactorDeepCopies, oldToNewFactorRefMap, dataHelper);

		HashMap oldToNewFactorLinkRefMap = createNewFactorLinks(oldToNewFactorRefMap, list);

		Vector diagramLinkDeepCopies = list.getDiagramLinkDeepCopies();
		createNewDiagramLinks(diagramLinkDeepCopies, oldToNewFactorLinkRefMap, oldToNewDiagramFactorRefMap, dataHelper);
	}
	
	private HashMap createNewFactors(TransferableMiradiList list) throws Exception
	{
		Vector factorDeepCopies = list.getFactorDeepCopies();
		HashMap oldToNewRefMap = new HashMap();
		for (int i = factorDeepCopies.size() - 1; i >= 0; --i)
		{			
			String jsonAsString = (String) factorDeepCopies.get(i);
			EnhancedJsonObject json = new EnhancedJsonObject(jsonAsString);
			int type = json.getInt("Type");
			String clipboardProjectFileName = list.getProjectFileName();
			//FIXME assignments should be pastable amongst projects,
			//assignments refer to items that are not deep copied. such as accounting code, resource, funding source
			//if (! isPastable(clipboardProjectFileName, type))
			//	continue;
			
			//TODO no longer create assignments with parent task
			//CreateObjectParameter extraInfo = createExtraInfo(json, type);
			BaseObject newObject = createObject(type, json, null);
			loadNewObjectFromOldJson(newObject, json);
			clearAssignmentFieldsForInBetweenProjectPastes(clipboardProjectFileName, newObject);
			
			BaseId oldId = json.getId(BaseObject.TAG_ID);
			ORef oldObjectRef = new ORef(type, oldId);
			oldToNewRefMap.put(oldObjectRef, newObject.getRef());
			fixupRefs(newObject, oldToNewRefMap);
		}
		
		return oldToNewRefMap;
	}

	private void clearAssignmentFieldsForInBetweenProjectPastes(String clipboardProjectFileName, BaseObject newObject) throws Exception
	{
		if (Assignment.getObjectType() != newObject.getType())
			return;
		
		if (! isInBetweenProjectPaste(clipboardProjectFileName))
			return;
		
		Assignment assignment = (Assignment) newObject;
		Command[] commandsToClearSomeFields = assignment.getCommandsToClearSomeFields();
		getProject().executeCommands(commandsToClearSomeFields);
	}

//TODO remove code after done removing assignemtn tasksid
//	private CreateObjectParameter createExtraInfo(EnhancedJsonObject json, int type)
//	{
//		if (type == Assignment.getObjectType())
//		{
//			BaseId taskId = json.getId(Assignment.TAG_ASSIGNMENT_TASK_ID);
//			return new CreateAssignmentParameter(new TaskId(taskId.asInt()));
//		}
//			
//		return null;
//	}
//TODO remove code after done deleting assignemtn.taskId
//	private boolean isPastable(String clipboardProjectFileName, int type)
//	{
//	 	if (type == Assignment.getObjectType() && !isInBetweenProjectPaste(clipboardProjectFileName)) 
//	 		return false;
//	 	
//	 	return true;
//	}

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

	private BaseObject createObject(int type, EnhancedJsonObject json, CreateObjectParameter extraInfo) throws CommandFailedException
	{
		CommandCreateObject createObject = new CommandCreateObject(type, extraInfo);
		getProject().executeCommand(createObject);
		
		ORef newObjectRef = createObject.getObjectRef();
		BaseObject newObject = getProject().findObject(newObjectRef);
		
		return newObject;
	}

	private HashMap createNewDiagramFactors(Vector diagramFactorDeepCopies, HashMap oldToNewMap, FactorDataHelper dataHelper) throws Exception
	{
		ORefList diagramFactorsToSelect = new ORefList();
		HashMap oldToNewDiagamFactorRefMap = new HashMap();
		for (int i = 0; i < diagramFactorDeepCopies.size(); ++i)
		{
			String jsonAsString = (String) diagramFactorDeepCopies.get(i);
			EnhancedJsonObject json = new EnhancedJsonObject(jsonAsString);
			ORef wrappedRef = json.getRef(DiagramFactor.TAG_WRAPPED_REF);
			ORef newFactorRef = (ORef) oldToNewMap.get(wrappedRef);
			DiagramFactorId diagramFactorId = new DiagramFactorId(json.getId(DiagramFactor.TAG_ID).asInt());

			String newLocationAsJsonString = offsetLocation(dataHelper, json, diagramFactorId);
			json.put(DiagramFactor.TAG_LOCATION, newLocationAsJsonString);
			
			ORef newDiagramFactorRef = createDiagramFactor(wrappedRef, newFactorRef);
			DiagramFactor newDiagramFactor = (DiagramFactor) getProject().findObject(newDiagramFactorRef);
			Command[]  commandsToLoadFromJson = newDiagramFactor.loadDataFromJson(json);
			getProject().executeCommands(commandsToLoadFromJson);

			addToCurrentDiagram(newDiagramFactorRef, DiagramObject.TAG_DIAGRAM_FACTOR_IDS);
			diagramFactorsToSelect.add(newDiagramFactorRef);
			
			BaseId oldDiagramFactorId = json.getId(DiagramFactor.TAG_ID);
			int type = json.getInt("Type");
			oldToNewDiagamFactorRefMap.put(new ORef(type, oldDiagramFactorId), newDiagramFactorRef);
		}
		addDiagramFactorToSelection(diagramFactorsToSelect);
		
		return oldToNewDiagamFactorRefMap;
	}

	private String offsetLocation(FactorDataHelper dataHelper, EnhancedJsonObject json, DiagramFactorId diagramFactorId) throws Exception
	{
		Point originalLocation = json.getPoint(DiagramFactor.TAG_LOCATION);
		dataHelper.setOriginalLocation(diagramFactorId, originalLocation);
		int offsetToAvoidOverlaying = dataHelper.getOffset(getProject());
		Point transLatedPoint = dataHelper.getSnappedTranslatedPoint(getProject(), originalLocation, offsetToAvoidOverlaying);
		
		return EnhancedJsonObject.convertFromPoint(transLatedPoint);
	}

	private String movePoints(FactorDataHelper dataHelper, PointList originalBendPoints, int offsetToAvoidOverlaying) throws Exception
	{
		PointList movedPoints = new PointList();
		for (int i = 0; i < originalBendPoints.size(); ++i)
		{
			Point originalPoint = originalBendPoints.get(i);
			Point translatedPoint = dataHelper.getSnappedTranslatedPoint(getProject(), originalPoint, offsetToAvoidOverlaying);			
			movedPoints.add(translatedPoint);
		}
		
		return movedPoints.toString();
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

	private ORef createDiagramFactor(ORef wrappedRef, ORef newRef) throws CommandFailedException
	{
		CreateDiagramFactorParameter extraInfo = new CreateDiagramFactorParameter(newRef);
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
	
	private HashMap createNewFactorLinks(HashMap oldToNewFactorRefMap, TransferableMiradiList list) throws Exception
	{
		Vector factorLinkDeepCopies = list.getFactorLinkDeepCopies();
		HashMap oldToNewFactorLinkRefMap = new HashMap();
		for (int i = 0; i < factorLinkDeepCopies.size(); ++i)
		{
			String jsonAsString = (String) factorLinkDeepCopies.get(i);
			EnhancedJsonObject json = new EnhancedJsonObject(jsonAsString);
			BaseId oldFactorLinkId = json.getId(FactorLink.TAG_ID);

			String clipboardProjectFileName = list.getProjectFileName();
			if (canCreateNewFactorLinkFromAnotherProject(oldToNewFactorRefMap, clipboardProjectFileName, json))
				continue;
			
			ORef newFromRef = getFactor(oldToNewFactorRefMap, json, FactorLink.TAG_FROM_REF);
			ORef newToRef = getFactor(oldToNewFactorRefMap, json, FactorLink.TAG_TO_REF);	
			
			int type = json.getInt("Type");
			CreateFactorLinkParameter extraInfo = new CreateFactorLinkParameter(newFromRef, newToRef);
			FactorLink newFactorLink = (FactorLink) createObject(type, json, extraInfo);
			
			Command[]  commandsToLoadFromJson = newFactorLink.createCommandsToLoadFromJson(json);
			getProject().executeCommands(commandsToLoadFromJson);
			
			oldToNewFactorLinkRefMap.put(new ORef(type, oldFactorLinkId), newFactorLink.getRef());
		}
		
		return oldToNewFactorLinkRefMap;
	}

	private boolean isInBetweenProjectPaste(String clipboardProjectFileName)
	{
		return ! getProject().getFilename().equals(clipboardProjectFileName);
	}
	
	private boolean canCreateNewFactorLinkFromAnotherProject(HashMap oldToNewFactorRefMap, String clipboardProjectFileName, EnhancedJsonObject json)
	{
		ORef oldFromRef = json.getRef(FactorLink.TAG_FROM_REF);
		ORef oldToRef = json.getRef(FactorLink.TAG_TO_REF);
		
		return (haveBothFactorsBeenCopied(oldToNewFactorRefMap, oldFromRef, oldToRef) && isInBetweenProjectPaste(clipboardProjectFileName));
	}

	private boolean haveBothFactorsBeenCopied(HashMap oldToNewFactorRefMap, ORef oldFromRef, ORef oldToRef)
	{
		return (oldToNewFactorRefMap.get(oldFromRef) == null || oldToNewFactorRefMap.get(oldToRef) == null);
	}
	
	private ORef getFactor(HashMap oldToNewFactorRefMap, EnhancedJsonObject json, String tag)
	{
		ORef oldRef = json.getRef(tag);
		ORef newRef = (ORef) oldToNewFactorRefMap.get(oldRef);
		if (newRef == null)
			return oldRef;
		 
		return newRef;
	}
	
	private void createNewDiagramLinks(Vector diagramLinkDeepCopies, HashMap oldToNewFactorLinkRefMap, HashMap oldToNewDiagramFactorRefMap, FactorDataHelper dataHelper) throws Exception
	{	
		int offsetToAvoidOverlaying = getOffsetToAvoidOverlaying(diagramLinkDeepCopies);
		for (int i = 0; i < diagramLinkDeepCopies.size(); ++i )
		{
			String jsonAsString = (String) diagramLinkDeepCopies.get(i);
			EnhancedJsonObject json = new EnhancedJsonObject(jsonAsString);
			
			PointList originalBendPoints = new PointList(json.getString(DiagramLink.TAG_BEND_POINTS));
			String movedBendPointsAsString = movePoints(dataHelper, originalBendPoints, offsetToAvoidOverlaying);
			json.put(DiagramLink.TAG_BEND_POINTS, movedBendPointsAsString);
			
			ORef oldWrappedFactorLinkRef = new ORef(FactorLink.getObjectType(), json.getId(DiagramLink.TAG_WRAPPED_ID));
			ORef newFactorLinkRef = (ORef) oldToNewFactorLinkRefMap.get(oldWrappedFactorLinkRef);
			if (newFactorLinkRef == null)
				continue;
			
			FactorLinkId newFactorLinkId = new FactorLinkId(newFactorLinkRef.getObjectId().asInt());
			DiagramFactorId fromDiagramFactorId = getDiagramFactorId(oldToNewDiagramFactorRefMap, json, DiagramLink.TAG_FROM_DIAGRAM_FACTOR_ID);
			DiagramFactorId toDiagramFactorId = getDiagramFactorId(oldToNewDiagramFactorRefMap, json, DiagramLink.TAG_TO_DIAGRAM_FACTOR_ID);
			//FIXME shouldnt this also be inside the factorLink creation
			LinkCreator linkCreator = new LinkCreator(project);
			if (linkCreator.linkWasRejected(currentModel, fromDiagramFactorId, toDiagramFactorId))
				continue;
			
			int type = json.getInt("Type");
			CreateDiagramFactorLinkParameter extraInfo = new CreateDiagramFactorLinkParameter(newFactorLinkId, fromDiagramFactorId, toDiagramFactorId);
			DiagramLink newDiagramLink = (DiagramLink) createObject(type, json, extraInfo);
			
			Command[]  commandsToLoadFromJson = newDiagramLink.createCommandsToLoadFromJson(json);
			getProject().executeCommands(commandsToLoadFromJson);
			
			addToCurrentDiagram(newDiagramLink.getRef(), DiagramObject.TAG_DIAGRAM_FACTOR_LINK_IDS);
		}
	}

	private DiagramFactorId getDiagramFactorId(HashMap oldToNewDiagramFactorRefMap, EnhancedJsonObject json, String tag)
	{
		BaseId oldId = json.getId(tag);
		ORef newRef = (ORef) oldToNewDiagramFactorRefMap.get(new ORef(ObjectType.DIAGRAM_FACTOR, oldId));
		if (newRef == null)
			return new DiagramFactorId(oldId.asInt()); 
			 
		return new DiagramFactorId(newRef.getObjectId().asInt());
	}
		
	Project project;
	DiagramModel currentModel;
}
