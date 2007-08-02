/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

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
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;
import org.conservationmeasures.eam.utils.PointList;

public class DiagramPaster
{
	public DiagramPaster(DiagramModel modelToUse, TransferableMiradiList transferableListToUse)
	{
		currentModel = modelToUse;
		project = currentModel.getProject();
		transferableList = transferableListToUse;
		
		factorDeepCopies = transferableList.getFactorDeepCopies();
		diagramFactorDeepCopies = transferableList.getDiagramFactorDeepCopies();
		factorLinkDeepCopies = transferableList.getFactorLinkDeepCopies();
		diagramLinkDeepCopies = transferableList.getDiagramLinkDeepCopies();
	}
	
	public void pasteMiradiDataFlavorsFactors(Point startPoint) throws Exception
	{	
		dataHelper = new FactorDataHelper(project.getAllDiagramFactorIds(), startPoint);
		createNewFactors();	
		createNewDiagramFactors();
	}
	
	public void pasteMiradiDataFlavorFactorsAndLinks(Point startPoint) throws Exception
	{	
		pasteMiradiDataFlavorsFactors(startPoint);
		createNewFactorLinks();
		createNewDiagramLinks();
	}
	
	private void createNewFactors() throws Exception
	{
		oldToNewFactorRefMap = new HashMap();
		for (int i = factorDeepCopies.size() - 1; i >= 0; --i)
		{			
			String jsonAsString = factorDeepCopies.get(i);
			EnhancedJsonObject json = new EnhancedJsonObject(jsonAsString);
			int type = json.getInt("Type");

			BaseObject newObject = createObject(type, json, null);
			loadNewObjectFromOldJson(newObject, json);
			
			String clipboardProjectFileName = transferableList.getProjectFileName();
			clearAssignmentFieldsForInBetweenProjectPastes(clipboardProjectFileName, newObject);
			
			BaseId oldId = json.getId(BaseObject.TAG_ID);
			ORef oldObjectRef = new ORef(type, oldId);
			oldToNewFactorRefMap.put(oldObjectRef, newObject.getRef());
			fixupRefs(newObject, oldToNewFactorRefMap);
		}
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

	private void createNewDiagramFactors() throws Exception
	{
		ORefList diagramFactorsToSelect = new ORefList();
		oldToNewDiagramFactorRefMap = new HashMap();
		for (int i = 0; i < diagramFactorDeepCopies.size(); ++i)
		{
			String jsonAsString = diagramFactorDeepCopies.get(i);
			EnhancedJsonObject json = new EnhancedJsonObject(jsonAsString);
			ORef wrappedRef = json.getRef(DiagramFactor.TAG_WRAPPED_REF);
			ORef newFactorRef = (ORef) oldToNewFactorRefMap.get(wrappedRef);
			DiagramFactorId diagramFactorId = new DiagramFactorId(json.getId(DiagramFactor.TAG_ID).asInt());

			String newLocationAsJsonString = offsetLocation(json, diagramFactorId);
			json.put(DiagramFactor.TAG_LOCATION, newLocationAsJsonString);
			
			ORef newDiagramFactorRef = createDiagramFactor(wrappedRef, newFactorRef);
			DiagramFactor newDiagramFactor = (DiagramFactor) getProject().findObject(newDiagramFactorRef);
			Command[]  commandsToLoadFromJson = newDiagramFactor.loadDataFromJson(json);
			getProject().executeCommands(commandsToLoadFromJson);

			addToCurrentDiagram(newDiagramFactorRef, DiagramObject.TAG_DIAGRAM_FACTOR_IDS);
			diagramFactorsToSelect.add(newDiagramFactorRef);
			
			BaseId oldDiagramFactorId = json.getId(DiagramFactor.TAG_ID);
			int type = json.getInt("Type");
			oldToNewDiagramFactorRefMap.put(new ORef(type, oldDiagramFactorId), newDiagramFactorRef);
		}
		addDiagramFactorToSelection(diagramFactorsToSelect);
	}

	private int getOffsetToAvoidOverlaying()
	{
		int NO_OFFSET = 0;
		if (diagramLinkDeepCopies.size() > 0)
			return getProject().getDiagramClipboard().getPasteOffset();
		
		return NO_OFFSET;
	}
	
	private String offsetLocation(EnhancedJsonObject json, DiagramFactorId diagramFactorId) throws Exception
	{
		Point originalLocation = json.getPoint(DiagramFactor.TAG_LOCATION);
		dataHelper.setOriginalLocation(diagramFactorId, originalLocation);
		int offsetToAvoidOverlaying = dataHelper.getOffset(getProject());
		Point transLatedPoint = dataHelper.getSnappedTranslatedPoint(getProject(), originalLocation, offsetToAvoidOverlaying);
		
		return EnhancedJsonObject.convertFromPoint(transLatedPoint);
	}

	private String movePoints(PointList originalBendPoints, int offsetToAvoidOverlaying) throws Exception
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
	
	private void createNewFactorLinks() throws Exception
	{
		oldToNewFactorLinkRefMap = new HashMap();
		for (int i = 0; i < factorLinkDeepCopies.size(); ++i)
		{
			String jsonAsString = factorLinkDeepCopies.get(i);
			EnhancedJsonObject json = new EnhancedJsonObject(jsonAsString);
			BaseId oldFactorLinkId = json.getId(FactorLink.TAG_ID);

			String clipboardProjectFileName = transferableList.getProjectFileName();
			if (cannotCreateNewFactorLinkFromAnotherProject(clipboardProjectFileName, json))
				continue;
			
			ORef newFromRef = getFactor(json, FactorLink.TAG_FROM_REF);
			ORef newToRef = getFactor(json, FactorLink.TAG_TO_REF);	
			
			int type = json.getInt("Type");
			CreateFactorLinkParameter extraInfo = new CreateFactorLinkParameter(newFromRef, newToRef);
			FactorLink newFactorLink = (FactorLink) createObject(type, json, extraInfo);
			
			Command[]  commandsToLoadFromJson = newFactorLink.createCommandsToLoadFromJson(json);
			getProject().executeCommands(commandsToLoadFromJson);
			
			oldToNewFactorLinkRefMap.put(new ORef(type, oldFactorLinkId), newFactorLink.getRef());
		}
	}

	private boolean isInBetweenProjectPaste(String clipboardProjectFileName)
	{
		return ! getProject().getFilename().equals(clipboardProjectFileName);
	}
	
	private boolean cannotCreateNewFactorLinkFromAnotherProject(String clipboardProjectFileName, EnhancedJsonObject json)
	{
		ORef oldFromRef = json.getRef(FactorLink.TAG_FROM_REF);
		ORef oldToRef = json.getRef(FactorLink.TAG_TO_REF);
		boolean haveBothFactorsBeenCopied = haveBothFactorsBeenCopied(oldFromRef, oldToRef);
		boolean isInBetweenProjectPaste = isInBetweenProjectPaste(clipboardProjectFileName);
		
		return (haveBothFactorsBeenCopied && isInBetweenProjectPaste);
	}

	private boolean haveBothFactorsBeenCopied(ORef oldFromRef, ORef oldToRef)
	{
		return (oldToNewFactorRefMap.get(oldFromRef) == null || oldToNewFactorRefMap.get(oldToRef) == null);
	}
	
	public boolean wasAnyDataLost() throws Exception
	{
		if (! isInBetweenProjectPaste(transferableList.getProjectFileName()))
			return false;
		
		for (int i = 0; i < factorDeepCopies.size(); ++i)
		{
			String jsonAsString = factorDeepCopies.get(i);
			EnhancedJsonObject json = new EnhancedJsonObject(jsonAsString);
			int type = json.getInt("Type");
			if (Assignment.getObjectType() == type)
				return true;
		}
		
		return false;
	}
	
	private ORef getFactor(EnhancedJsonObject json, String tag)
	{
		ORef oldRef = json.getRef(tag);
		ORef newRef = (ORef) oldToNewFactorRefMap.get(oldRef);
		if (newRef == null)
			return oldRef;
		 
		return newRef;
	}
	
	private void createNewDiagramLinks() throws Exception
	{	
		int offsetToAvoidOverlaying = getOffsetToAvoidOverlaying();
		for (int i = 0; i < diagramLinkDeepCopies.size(); ++i )
		{
			String jsonAsString = diagramLinkDeepCopies.get(i);
			EnhancedJsonObject json = new EnhancedJsonObject(jsonAsString);
			
			PointList originalBendPoints = new PointList(json.getString(DiagramLink.TAG_BEND_POINTS));
			String movedBendPointsAsString = movePoints(originalBendPoints, offsetToAvoidOverlaying);
			json.put(DiagramLink.TAG_BEND_POINTS, movedBendPointsAsString);
			
			ORef oldWrappedFactorLinkRef = new ORef(FactorLink.getObjectType(), json.getId(DiagramLink.TAG_WRAPPED_ID));
			ORef newFactorLinkRef = (ORef) oldToNewFactorLinkRefMap.get(oldWrappedFactorLinkRef);
			if (newFactorLinkRef == null)
				continue;
			
			FactorLinkId newFactorLinkId = new FactorLinkId(newFactorLinkRef.getObjectId().asInt());
			DiagramFactorId fromDiagramFactorId = getDiagramFactorId(json, DiagramLink.TAG_FROM_DIAGRAM_FACTOR_ID);
			DiagramFactorId toDiagramFactorId = getDiagramFactorId(json, DiagramLink.TAG_TO_DIAGRAM_FACTOR_ID);
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

	private DiagramFactorId getDiagramFactorId(EnhancedJsonObject json, String tag)
	{
		BaseId oldId = json.getId(tag);
		ORef newRef = (ORef) oldToNewDiagramFactorRefMap.get(new ORef(ObjectType.DIAGRAM_FACTOR, oldId));
		if (newRef == null)
			return new DiagramFactorId(oldId.asInt()); 
			 
		return new DiagramFactorId(newRef.getObjectId().asInt());
	}
	
	public boolean canPaste() throws Exception
	{
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
	
	private Project getProject()
	{
		return project;
	}
	
	Project project;
	DiagramModel currentModel;
	
	Vector<String> factorDeepCopies;
	Vector<String> diagramFactorDeepCopies;
	Vector<String> factorLinkDeepCopies;
	Vector<String> diagramLinkDeepCopies;
	
	HashMap oldToNewFactorRefMap;
	HashMap oldToNewDiagramFactorRefMap;
	HashMap oldToNewFactorLinkRefMap;
	
	FactorDataHelper dataHelper;
	TransferableMiradiList transferableList;
}
