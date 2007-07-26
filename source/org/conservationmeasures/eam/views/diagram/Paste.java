/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.TransferableEamList;
import org.conservationmeasures.eam.main.TransferableMiradiList;
import org.conservationmeasures.eam.objecthelpers.CreateDiagramFactorLinkParameter;
import org.conservationmeasures.eam.objecthelpers.CreateDiagramFactorParameter;
import org.conservationmeasures.eam.objecthelpers.CreateFactorLinkParameter;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.DiagramLink;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.project.FactorCommandHelper;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class Paste extends LocationDoer
{
	public boolean isAvailable()
	{
		if(!getProject().isOpen())
			return false;
		
		if (! isDiagramView())
			return false;
		
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable contents = clipboard.getContents(null);
		if(contents == null)
			return false;
		return contents.isDataFlavorSupported(TransferableEamList.eamListDataFlavor);
	}

	public void doIt() throws CommandFailedException
	{
		DiagramClipboard clipboard = getProject().getDiagramClipboard();
		try 
		{
			pasteEAMDataFlavor(clipboard);
			pasteMiradiDataFlavor(clipboard);
		} 
		catch (Exception e) 
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		} 
	}

	private void pasteEAMDataFlavor(DiagramClipboard clipboard) throws UnsupportedFlavorException, IOException, Exception
	{
//		FIXME temp swith beween transitions of two flavors
		if (! TransferableEamList.IS_EAM_FLAVOR)
			return;

		Transferable contents = clipboard.getContents(null);
		if(!contents.isDataFlavorSupported(TransferableEamList.eamListDataFlavor))
			return;
		TransferableEamList list = (TransferableEamList)contents.getTransferData(TransferableEamList.eamListDataFlavor);
		if(!list.getProjectFileName().equals(getProject().getFilename()))
		{
			EAM.notifyDialog(EAM.text("Paste between different Miradi projects not yet supported"));
			return;
		}
		
		FactorCommandHelper factorCommandHelper = new FactorCommandHelper(getProject(), getDiagramView().getDiagramModel());
		if (! factorCommandHelper.canPaste(list))
		{
			EAM.notifyDialog(EAM.text("Contributing Factors and Direct Threats cannot be pasted into a Results Chain; " +
										"Intermediate Results and Threat Reduction Results cannot be pasted into a Conceptual Model."));
			return;
		}
		
		pasteCellsIntoProject(list, factorCommandHelper);
		clipboard.incrementPasteCount();
	}

	//FIXME Paste code is under construction but going in the right direction
	//lots of unsused code commented and will uncomment and reuse.  
	private void pasteMiradiDataFlavor(DiagramClipboard clipboard) throws Exception
	{
//		FIXME temp swith beween transitions of two flavors
		if (TransferableEamList.IS_EAM_FLAVOR)
			return;

		Transferable contents = clipboard.getContents(null);
		if(!contents.isDataFlavorSupported(TransferableEamList.miradiListDataFlavor))
			return;
	
		//TODO this transaction should be moved up to the doit method once transition is done.
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			TransferableMiradiList list = (TransferableMiradiList)contents.getTransferData(TransferableEamList.miradiListDataFlavor);
			Vector factorDeepCopies = list.getFactorDeepCopies();
			HashMap oldToNewFactorIdMap = createNewFactors(factorDeepCopies);
			
			Vector diagramFactorDeepCopies = list.getDiagramFactorDeepCopies();
			HashMap oldToNewDiagramFactorIdMap = createNewDiagramFactors(diagramFactorDeepCopies, oldToNewFactorIdMap);
			
			Vector factorLinkDeepCopies = list.getFactorLinkDeepCopies();
			HashMap oldToNewFactorLinkIdMap = createNewFactorLinks(factorLinkDeepCopies, oldToNewFactorIdMap);
			
			Vector diagramLinkDeepCopies = list.getDiagramLinkDeepCopies();
			createNewDiagramLinks(diagramLinkDeepCopies, oldToNewFactorLinkIdMap, oldToNewDiagramFactorIdMap);
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
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

	private HashMap createNewDiagramFactors(Vector diagramFactorDeepCopies, HashMap oldToNewMap) throws Exception
	{
		ORefList diagramFactorsToSelect = new ORefList();
		HashMap oldToNewDiagamFactorIdMap = new HashMap();
		for (int i = 0; i < diagramFactorDeepCopies.size(); ++i)
		{
			String jsonAsString = (String) diagramFactorDeepCopies.get(i);
			EnhancedJsonObject json = new EnhancedJsonObject(jsonAsString);
			ORef wrappedRef = json.getRef(DiagramFactor.TAG_WRAPPED_REF);
			BaseId newFactorId = (BaseId) oldToNewMap.get(wrappedRef.getObjectId());

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

	private void addDiagramFactorToSelection(ORefList diagramFactorRefsToSelect) throws Exception
	{
		DiagramModel model = getDiagramView().getDiagramModel();
		Vector factorCells = new Vector();
		for (int i = 0; i < diagramFactorRefsToSelect.size(); ++i)
		{
			ORef diagramFactorRefToSelect = diagramFactorRefsToSelect.get(i);
			DiagramFactorId diagramFactorId = new DiagramFactorId(diagramFactorRefToSelect.getObjectId().asInt());
			FactorCell cell = model.getFactorCellById(diagramFactorId);
			factorCells.add(cell);
		}
		
		FactorCell[] cells = (FactorCell[]) factorCells.toArray(new FactorCell[0]);
		getDiagramView().getDiagramComponent().addSelectionCells(cells);
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
		DiagramObject diagramObject = getDiagramView().getDiagramModel().getDiagramObject();
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
	
	private void createNewDiagramLinks(Vector diagramLinkDeepCopies, HashMap oldToNewFactorLinkIdMap, HashMap oldToNewDiagramFactorIdMap) throws Exception
	{
		for (int i = 0; i < diagramLinkDeepCopies.size(); ++i )
		{
			String jsonAsString = (String) diagramLinkDeepCopies.get(i);
			EnhancedJsonObject json = new EnhancedJsonObject(jsonAsString);

			BaseId oldWrappedFactorLinkId = json.getId(DiagramLink.TAG_WRAPPED_ID);
			BaseId newFatorLinkIdAsBaseId = (BaseId) oldToNewFactorLinkIdMap.get(oldWrappedFactorLinkId);
			FactorLinkId newFactorLinkId = new FactorLinkId(newFatorLinkIdAsBaseId.asInt());

			DiagramFactorId fromDiagramFactorId = getDiagramFactorId(oldToNewDiagramFactorIdMap, json, DiagramLink.TAG_FROM_DIAGRAM_FACTOR_ID);
			DiagramFactorId toDiagramFactorId = getDiagramFactorId(oldToNewDiagramFactorIdMap, json, DiagramLink.TAG_TO_DIAGRAM_FACTOR_ID);
			
			CreateDiagramFactorLinkParameter extraInfo = new CreateDiagramFactorLinkParameter(newFactorLinkId, fromDiagramFactorId, toDiagramFactorId);
			DiagramLink newDiagramLink = (DiagramLink) createObject(json, extraInfo);
			
			Command[]  commandsToLoadFromJson = newDiagramLink.createCommandsToLoadFromJson(json);
			getProject().executeCommands(commandsToLoadFromJson);
			
			addToCurrentDiagram(newDiagramLink.getRef(), DiagramObject.TAG_DIAGRAM_FACTOR_LINK_IDS);
		}
	}

	private DiagramFactorId getDiagramFactorId(HashMap oldToNewDiagramFactorIdMap, EnhancedJsonObject json, String tag)
	{
		BaseId oldId = json.getId(tag);
		BaseId newId = (BaseId) oldToNewDiagramFactorIdMap.get(oldId);
		if (newId == null)
			return new DiagramFactorId(oldId.asInt()); 
			 
		return new DiagramFactorId(newId.asInt());
	}
		
	public void pasteCellsIntoProject(TransferableEamList list, FactorCommandHelper factorCommandHelper) throws Exception 
	{
		factorCommandHelper.pasteFactorsAndLinksIntoProject(list, getLocation());
	}
}
