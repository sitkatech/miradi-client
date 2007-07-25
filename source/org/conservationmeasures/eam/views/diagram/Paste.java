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
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.TransferableEamList;
import org.conservationmeasures.eam.main.TransferableMiradiList;
import org.conservationmeasures.eam.objecthelpers.CreateDiagramFactorParameter;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.objects.Factor;
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
			//FIXME nima copy/paste now add deep copies of selected objects
			//FIXME new pasted objects should be selected
			TransferableMiradiList list = (TransferableMiradiList)contents.getTransferData(TransferableEamList.miradiListDataFlavor);
			Vector factorDeepCopies = list.getFactorDeepCopies();
			HashMap oldToNewFactorIdMap = createNewFactors(factorDeepCopies);
			
			Vector diagramFactorDeepCopies = list.getDiagramFactorDeepCopies();
			createNewDiagramFactors(diagramFactorDeepCopies, oldToNewFactorIdMap);
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
			fixupFactorRefs(newObject, oldToNewRefMap);
		}
		
		return oldToNewRefMap;
	}

	private void fixupFactorRefs(BaseObject newObject, HashMap oldToNewRefMap) throws Exception
	{
		if (! Factor.isFactor(newObject.getType()))
			return;
		
		Command[] commandsToFixRefs = newObject.fixupAllRefs(oldToNewRefMap);
		getProject().executeCommands(commandsToFixRefs);
	}

	private void loadNewObjectFromOldJson(BaseObject newObject, EnhancedJsonObject json) throws Exception, CommandFailedException
	{
		Command[] commandsToLoadFromJson = newObject.createCommandsToLoadFromJson(json);
		getProject().executeCommands(commandsToLoadFromJson);
	}

	private BaseObject createObject(EnhancedJsonObject json) throws CommandFailedException
	{
		int type = json.getInt("Type");
		CommandCreateObject createObject = new CommandCreateObject(type);
		getProject().executeCommand(createObject);
		
		ORef newObjectRef = createObject.getObjectRef();
		BaseObject newObject = getProject().findObject(newObjectRef);
		
		return newObject;
	}

	private void createNewDiagramFactors(Vector diagramFactorDeepCopies, HashMap oldToNewMap) throws Exception
	{
		for (int i = 0; i < diagramFactorDeepCopies.size(); ++i)
		{
			String jsonAsString = (String) diagramFactorDeepCopies.get(i);
			EnhancedJsonObject json = new EnhancedJsonObject(jsonAsString);
			String wrappedRefAsString = json.getString(DiagramFactor.TAG_WRAPPED_REF);
			ORef wrappedRef = ORef.createFromString(wrappedRefAsString);
			BaseId newId = (BaseId) oldToNewMap.get(wrappedRef.getObjectId());

			CreateDiagramFactorParameter extraInfo = new CreateDiagramFactorParameter(new ORef(wrappedRef.getObjectType(), newId));
			CommandCreateObject createDiagramFactor = new CommandCreateObject(DiagramFactor.getObjectType(), extraInfo);
			getProject().executeCommand(createDiagramFactor);

			ORef newDiagramFactorRef = createDiagramFactor.getObjectRef();
			DiagramFactor newDiagramFactor = (DiagramFactor) getProject().findObject(newDiagramFactorRef);
			Command[]  commandsToLoadFromJson = newDiagramFactor.loadDataFromJson(json);
			getProject().executeCommands(commandsToLoadFromJson);

			addDiagramFactorToCurrentDiagram(newDiagramFactorRef);
		}
	}

	private void addDiagramFactorToCurrentDiagram(ORef newDiagramFactorRef) throws Exception
	{
		DiagramObject diagramObject = getDiagramView().getDiagramModel().getDiagramObject();
		CommandSetObjectData addDiagramFactor = CommandSetObjectData.createAppendIdCommand(diagramObject, DiagramObject.TAG_DIAGRAM_FACTOR_IDS, newDiagramFactorRef.getObjectId());
		getProject().executeCommand(addDiagramFactor);
	}
		
	public void pasteCellsIntoProject(TransferableEamList list, FactorCommandHelper factorCommandHelper) throws Exception 
	{
		factorCommandHelper.pasteFactorsAndLinksIntoProject(list, getLocation());
	}
}
