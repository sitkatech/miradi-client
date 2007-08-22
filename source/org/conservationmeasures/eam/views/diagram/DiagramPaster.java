/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import java.awt.Point;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.dialogs.DiagramPanel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.TransferableMiradiList;
import org.conservationmeasures.eam.objecthelpers.CreateDiagramFactorLinkParameter;
import org.conservationmeasures.eam.objecthelpers.CreateDiagramFactorParameter;
import org.conservationmeasures.eam.objecthelpers.CreateFactorLinkParameter;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.AccountingCode;
import org.conservationmeasures.eam.objects.Assignment;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.DiagramLink;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.objects.FundingSource;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;
import org.conservationmeasures.eam.utils.PointList;

public class DiagramPaster
{
	public DiagramPaster(DiagramPanel diagramPanelToUse, DiagramModel modelToUse, TransferableMiradiList transferableListToUse)
	{
		diagramPanel = diagramPanelToUse;
		currentModel = modelToUse;
		project = currentModel.getProject();
		transferableList = transferableListToUse;
		
		factorDeepCopies = transferableList.getFactorDeepCopies();
		diagramFactorDeepCopies = transferableList.getDiagramFactorDeepCopies();
		factorLinkDeepCopies = transferableList.getFactorLinkDeepCopies();
		diagramLinkDeepCopies = transferableList.getDiagramLinkDeepCopies();
	}
	
	public void pasteFactors(Point startPoint) throws Exception
	{	
		dataHelper = new PointManipulater(startPoint, transferableList.getUpperMostLeftMostCorner());
		createNewFactors();	
		createNewDiagramFactors();
	}
	
	public void pasteFactorsAndLinks(Point startPoint) throws Exception
	{	
		pasteFactors(startPoint);
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

			BaseObject newObject = createObject(type, json);
			loadNewObjectFromOldJson(newObject, json);
			
			BaseId oldId = json.getId(BaseObject.TAG_ID);
			ORef oldObjectRef = new ORef(type, oldId);
			oldToNewFactorRefMap.put(oldObjectRef, newObject.getRef());
			fixupRefs(newObject);
		}
	}

	private String getClipboardProjectFileName()
	{
		return transferableList.getProjectFileName();
	}

	private void fixupRefs(BaseObject newObject) throws Exception
	{
		Command[] commandsToFixRefs = createCommandToFixupRefLists(newObject);
		getProject().executeCommands(commandsToFixRefs);
	}
	
	public Command[] createCommandToFixupRefLists(BaseObject newObject) throws Exception
	{
		Vector commands = new Vector();
		String[] fields = newObject.getFieldTags();
		for (int i = 0; i < fields.length; ++i)
		{
			String tag = fields[i];
			commands.addAll(Arrays.asList(getCommandsToFixUpIdListRefs(newObject, tag)));
			commands.addAll(Arrays.asList(getCommandToFixUpIdRefs(newObject, tag)));
		}
		
		return (Command[]) commands.toArray(new Command[0]);
	}
	
	private Command[] getCommandsToFixUpIdListRefs(BaseObject newObject, String tag) throws Exception
	{
		if (!newObject.isIdListTag(tag))
			return new Command[0];
		
		Command commandToFixRefs = fixUpIdList(newObject, tag, newObject.getAnnotationType(tag));
		return new Command[] {commandToFixRefs};
	}
	
	private Command[] getCommandToFixUpIdRefs(BaseObject newObject, String tag) throws Exception
	{
		if (Assignment.getObjectType() != newObject.getType())
			return new Command[0];

		if (Assignment.TAG_ACCOUNTING_CODE.equals(tag))
			return getCommandToFixId(newObject, AccountingCode.getObjectType(), tag);
		
		if (Assignment.TAG_FUNDING_SOURCE.equals(tag))
			return getCommandToFixId(newObject, FundingSource.getObjectType(), tag);

		if (Assignment.TAG_ASSIGNMENT_RESOURCE_ID.equals(tag))
			return getCommandToFixId(newObject, ProjectResource.getObjectType(), tag);
		
		return new Command[0];
	}

	private Command[] getCommandToFixId(BaseObject newObject, int annotationType, String tag) throws Exception
	{
		BaseId baseId = new BaseId(newObject.getData(tag));
		ORef refToFix = new ORef(annotationType, baseId);
		ORef fixedRef = fixupSingleRef(refToFix);
		
		return new Command[] {new CommandSetObjectData(newObject.getRef(), tag, fixedRef.getObjectId().toString())};
	}

	private Command fixUpIdList(BaseObject newObject, String annotationTag, int annotationType) throws Exception
	{
		//FIXME currently items ids found in list but not in map are not added to new list
		IdList oldList = new IdList(annotationType, newObject.getData(annotationTag));
		IdList newList = new IdList(annotationType);
		for (int i = 0; i < oldList.size(); ++i)
		{
			ORef oldRef = oldList.getRef(i);
			ORef refToAdd = fixupSingleRef(oldRef);
			if (!refToAdd.isInvalid())
				newList.addRef(refToAdd);
		}
		
		return new CommandSetObjectData(newObject.getRef(), annotationTag, newList.toString());
	}

	private ORef fixupSingleRef(ORef oldRef) throws Exception
	{
		if (oldToNewFactorRefMap.containsKey(oldRef))
			return  (ORef) oldToNewFactorRefMap.get(oldRef);
		
		if (!isInBetweenProjectPaste())
			return oldRef;
		
		return ORef.INVALID;
	}

	private ORef getFixedupFactorRef(EnhancedJsonObject json, String tag) throws Exception
	{
		ORef oldRef = json.getRef(tag);
		return fixupSingleRef(oldRef);
	}	
	
	private void loadNewObjectFromOldJson(BaseObject newObject, EnhancedJsonObject json) throws Exception, CommandFailedException
	{
		Command[] commandsToLoadFromJson = newObject.createCommandsToLoadFromJson(json);
		getProject().executeCommands(commandsToLoadFromJson);
	}
	
	private BaseObject createObject(int type, EnhancedJsonObject json) throws Exception
	{
		return createObject(type, json, null);
	}
	
	private BaseObject createObject(int type, EnhancedJsonObject json, CreateObjectParameter extraInfo) throws CommandFailedException
	{
		CommandCreateObject createObject = new CommandCreateObject(type, extraInfo);
		getProject().executeCommand(createObject);
		
		ORef newObjectRef = createObject.getObjectRef();
		BaseObject newObject = getProject().findObject(newObjectRef);
		
		return newObject;
	}

	protected void createNewDiagramFactors() throws Exception
	{
		ORefList diagramFactorsToSelect = new ORefList();
		oldToNewDiagramFactorRefMap = new HashMap();
		for (int i = 0; i < diagramFactorDeepCopies.size(); ++i)
		{
			String jsonAsString = diagramFactorDeepCopies.get(i);
			EnhancedJsonObject json = new EnhancedJsonObject(jsonAsString);
			ORef oldWrappedRef = json.getRef(DiagramFactor.TAG_WRAPPED_REF);
			ORef newWrappedRef = getDiagramFactorWrappedRef(oldWrappedRef);
			DiagramFactorId diagramFactorId = new DiagramFactorId(json.getId(DiagramFactor.TAG_ID).asInt());

			String newLocationAsJsonString = offsetLocation(json, diagramFactorId);
			json.put(DiagramFactor.TAG_LOCATION, newLocationAsJsonString);
			
			ORef newDiagramFactorRef = createDiagramFactor(oldWrappedRef, newWrappedRef);
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

	public ORef getDiagramFactorWrappedRef(ORef oldWrappedRef) throws Exception
	{
		if (! containsTargetsThatMustBePastedAsAlias())
			return (ORef) oldToNewFactorRefMap.get(oldWrappedRef);
		
		return oldWrappedRef;
	}
	
	private String offsetLocation(EnhancedJsonObject json, DiagramFactorId diagramFactorId) throws Exception
	{
		Point originalLocation = json.getPoint(DiagramFactor.TAG_LOCATION);
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
		//FIXME refactor this code after commiting (get rid of forlopp and just select new DF as added)
		//DiagramModel model = getDiagramView().getDiagramModel();
		for (int i = 0; i < diagramFactorRefsToSelect.size(); ++i)
		{
			ORef diagramFactorRefToSelect = diagramFactorRefsToSelect.get(i);
			DiagramFactorId diagramFactorId = new DiagramFactorId(diagramFactorRefToSelect.getObjectId().asInt());
			FactorCell cell = currentModel.getFactorCellById(diagramFactorId);
			//NOTE test only exists for tests
			if (diagramPanel == null)
				continue;
			
			
			diagramPanel.addFactorToSelection(cell);
		}	
	}

	private ORef createDiagramFactor(ORef oldWrappedRef, ORef newWrappedRef) throws CommandFailedException
	{
		CreateDiagramFactorParameter extraInfo = new CreateDiagramFactorParameter(newWrappedRef);
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
		
			if (cannotCreateNewFactorLinkFromAnotherProject(json))
				continue;
			
			ORef newFromRef = getFixedupFactorRef(json, FactorLink.TAG_FROM_REF);
			ORef newToRef = getFixedupFactorRef(json, FactorLink.TAG_TO_REF);	
			
			LinkCreator linkCreator = new LinkCreator(project);
			if (linkCreator.linkWasRejected(currentModel, newFromRef, newToRef))
				continue;
			
			int type = json.getInt("Type");
			CreateFactorLinkParameter extraInfo = new CreateFactorLinkParameter(newFromRef, newToRef);
			FactorLink newFactorLink = (FactorLink) createObject(type, json, extraInfo);
			
			Command[]  commandsToLoadFromJson = newFactorLink.createCommandsToLoadFromJson(json);
			getProject().executeCommands(commandsToLoadFromJson);
			
			oldToNewFactorLinkRefMap.put(new ORef(type, oldFactorLinkId), newFactorLink.getRef());
		}
	}

	private boolean isInBetweenProjectPaste()
	{
		return ! getProject().getFilename().equals(getClipboardProjectFileName());
	}
	
	private boolean cannotCreateNewFactorLinkFromAnotherProject(EnhancedJsonObject json)
	{
		ORef oldFromRef = json.getRef(FactorLink.TAG_FROM_REF);
		ORef oldToRef = json.getRef(FactorLink.TAG_TO_REF);
		boolean haveBothFactorsBeenCopied = haveBothFactorsBeenCopied(oldFromRef, oldToRef);
		boolean isInBetweenProjectPaste = isInBetweenProjectPaste();
		
		return (haveBothFactorsBeenCopied && isInBetweenProjectPaste);
	}

	private boolean haveBothFactorsBeenCopied(ORef oldFromRef, ORef oldToRef)
	{
		return (oldToNewFactorRefMap.get(oldFromRef) == null || oldToNewFactorRefMap.get(oldToRef) == null);
	}
	
	public boolean wasAnyDataLost() throws Exception
	{
		if (! isInBetweenProjectPaste())
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
	
	protected void createNewDiagramLinks() throws Exception
	{	
		int offsetToAvoidOverlaying = dataHelper.getOffset(getProject());
		for (int i = 0; i < diagramLinkDeepCopies.size(); ++i )
		{
			String jsonAsString = diagramLinkDeepCopies.get(i);
			EnhancedJsonObject json = new EnhancedJsonObject(jsonAsString);
			
			PointList originalBendPoints = new PointList(json.getString(DiagramLink.TAG_BEND_POINTS));
			String movedBendPointsAsString = movePoints(originalBendPoints, offsetToAvoidOverlaying);
			json.put(DiagramLink.TAG_BEND_POINTS, movedBendPointsAsString);
			
			ORef oldWrappedFactorLinkRef = new ORef(FactorLink.getObjectType(), json.getId(DiagramLink.TAG_WRAPPED_ID));
			ORef newFactorLinkRef = getFactorLinkRef(oldWrappedFactorLinkRef);
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

	public ORef getFactorLinkRef(ORef oldWrappedFactorLinkRef)
	{
		return (ORef) oldToNewFactorLinkRefMap.get(oldWrappedFactorLinkRef);
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
	
	public boolean containsTargetsThatMustBePastedAsAlias() throws Exception
	{
		if (! currentModel.isResultsChain())
			return false;
		
		for (int i = 0; i < factorDeepCopies.size(); ++i)
		{
			String jsonAsString = factorDeepCopies.get(i);
			EnhancedJsonObject json = new EnhancedJsonObject(jsonAsString);
			int type = json.getInt("Type");
			if (Target.getObjectType() == type)
				return true;
		}
		
		return false;
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

	public HashMap getOldToNewFactorRefMap()
	{
		return oldToNewFactorRefMap;
	}
	
	Project project;
	DiagramModel currentModel;
	DiagramPanel diagramPanel;
	
	Vector<String> factorDeepCopies;
	Vector<String> diagramFactorDeepCopies;
	Vector<String> factorLinkDeepCopies;
	Vector<String> diagramLinkDeepCopies;
	
	HashMap oldToNewFactorRefMap;
	HashMap oldToNewDiagramFactorRefMap;
	HashMap oldToNewFactorLinkRefMap;
	
	PointManipulater dataHelper;
	TransferableMiradiList transferableList;
}
