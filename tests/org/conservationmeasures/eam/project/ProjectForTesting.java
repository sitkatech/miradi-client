/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import java.io.File;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandDiagramAddFactor;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.ids.AssignmentId;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.ids.TaskId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.CreateAssignmentParameter;
import org.conservationmeasures.eam.objecthelpers.CreateDiagramFactorParameter;
import org.conservationmeasures.eam.objecthelpers.CreateTaskParameter;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objectpools.ConceptualModelDiagramPool;
import org.conservationmeasures.eam.objects.ConceptualModelDiagram;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.DiagramObject;



public class ProjectForTesting extends Project
{
	public ProjectForTesting(String testName) throws Exception
	{
		super(new ProjectServerForTesting());
		
		diagramModel = new DiagramModel(this);
		getTestDatabase().openMemoryDatabase(testName);
		finishOpening();
		commandStack = new Vector();
	}
	
	public DiagramModel getDiagramModel()
	{
		return diagramModel;
	}
	
	public DiagramObject getDiagramObject()
	{
		return getDiagramModel().getDiagramObject();
	}
	
	protected void finishOpening() throws Exception
	{
		super.finishOpening();
		loadDiagram();
	}

	
	//TODO this is fragile, should do a true close
	public void closeAndReopen() throws Exception
	{
		File topDirectory = getDatabase().getTopDirectory();
		getTestDatabase().closeAndDontDelete();
		createOrOpen(topDirectory);
	}

	void fireCommandExecuted(Command command) 
	{
		super.fireCommandExecuted(command);
		if(commandStack != null)
			commandStack.add(command);
	}

	Command getLastCommand()
	{
		return (Command)commandStack.remove(commandStack.size()-1);
	}
	
	public ProjectServerForTesting getTestDatabase()
	{
		return (ProjectServerForTesting)getDatabase();
	}
	
	public FactorId createFactor(int objectType) throws Exception
	{
		FactorId factorId = (FactorId)createObject(objectType);
		return factorId;
	}
	
	public BaseId addItemToViewDataList(BaseId id, int type, String tag) throws Exception
	{
		return addItemToList(ObjectType.VIEW_DATA, id,  type,  tag);
	}
	
	public BaseId addItemToProjectMetaDataList(BaseId id, int type, String tag) throws Exception
	{
		return addItemToList(ObjectType.PROJECT_METADATA, id,  type,  tag);
	}
	
	public BaseId addItemToKeyEcologicalAttributeList(BaseId id, int type, String tag) throws Exception
	{
		return addItemToList(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, id,  type,  tag);
	}
	
	
	public BaseId addItemToIndicatorList(BaseId id, int type, String tag) throws Exception
	{
		return addItemToList(ObjectType.INDICATOR, id,  type,  tag);
	}
	
	
	public BaseId addItemToFactorList(BaseId id, int type, String tag) throws Exception
	{
		return addItemToList(ObjectType.FACTOR, id,  type,  tag);
	}
	
	public BaseId addItemToList(int sourceType, BaseId id, int type, String tag) throws Exception
	{
		BaseId baseId = createObject(type);
		IdList idList = new IdList(new BaseId[] {baseId});
		setObjectData(sourceType, id, tag, idList.toString());
		return baseId;
	}

	public TaskId createTask(ORef oref) throws Exception
	{
		CreateTaskParameter createTask = new CreateTaskParameter(oref);
		TaskId cmTaskId = (TaskId)createObject(ObjectType.TASK, BaseId.INVALID, createTask);
		return cmTaskId;
	}

	public AssignmentId createAssignment(ORef oref) throws Exception
	{
		CreateAssignmentParameter createAssignment = new CreateAssignmentParameter((TaskId)oref.getObjectId());
		AssignmentId cmAssignmentId = (AssignmentId)createObject(ObjectType.ASSIGNMENT, BaseId.INVALID, createAssignment);
		return cmAssignmentId;
	}
	
	public DiagramFactorId createAndAddFactorToDiagram(int nodeType) throws Exception
	{
		FactorId factorId = createFactor(nodeType);
		CreateDiagramFactorParameter extraDiagramFactorInfo = new CreateDiagramFactorParameter(factorId);
		CommandCreateObject createDiagramFactorCommand = new CommandCreateObject(ObjectType.DIAGRAM_FACTOR, extraDiagramFactorInfo);
		executeCommand(createDiagramFactorCommand);
		
		DiagramFactorId diagramFactorId = (DiagramFactorId) createDiagramFactorCommand.getCreatedId();
		CommandDiagramAddFactor addDiagramFactorCommand = new CommandDiagramAddFactor(diagramFactorId);
		executeCommand(addDiagramFactorCommand);
		
		return diagramFactorId;
	}
	
	//TODO fix method name (remove 2 and come up with better name)
	public DiagramFactor createNodeAndAddToDiagram2(int objectType) throws Exception
	{
		DiagramFactorId diagramFactorId = createAndAddFactorToDiagram(objectType);
		DiagramFactor diagramFactor = (DiagramFactor) findObject(new ORef(ObjectType.DIAGRAM_FACTOR, diagramFactorId));

		return diagramFactor;
	}

	public FactorId createNodeAndAddToDiagram(int objectType) throws Exception
	{
		DiagramFactorId diagramFactorId = createAndAddFactorToDiagram(objectType);
		DiagramFactor diagramFactor = (DiagramFactor) findObject(new ORef(ObjectType.DIAGRAM_FACTOR, diagramFactorId));
	
		return diagramFactor.getWrappedId();
	}

	public FactorCell createFactorCell(int objectType) throws Exception
	{
		FactorId insertedId = createNodeAndAddToDiagram(objectType);
		return getDiagramModel().getFactorCellByWrappedId(insertedId);
	}
	
	public void loadDiagram() throws Exception
	{
		ConceptualModelDiagramPool diagramContentsPool = (ConceptualModelDiagramPool) getPool(ObjectType.CONCEPTUAL_MODEL_DIAGRAM);
		ORefList oRefs = diagramContentsPool.getORefList();
		ConceptualModelDiagram diagramContentsObject = getDiagramContentsObject(oRefs);
		getDiagramModel().fillFrom(diagramContentsObject);
	}

	private ConceptualModelDiagram getDiagramContentsObject(ORefList oRefs) throws Exception
	{
		if (oRefs.size() == 0)
		{
			BaseId id = createObject(ObjectType.CONCEPTUAL_MODEL_DIAGRAM);
			return (ConceptualModelDiagram) findObject(new ORef(ObjectType.CONCEPTUAL_MODEL_DIAGRAM, id));
		}
		if (oRefs.size() > 1)
		{
			EAM.logVerbose("Found more than one diagram contents inside pool");
		}

		ORef oRef = oRefs.get(0);
		return (ConceptualModelDiagram) findObject(oRef);
	}
	
//	FIXME duplicate code exists inside DiagramPanel.  remove this copy when done
	public FactorCell[] getOnlySelectedFactorCells(Object[] allSelectedCells)
	{
		Vector nodes = new Vector();
		for(int i = 0; i < allSelectedCells.length; ++i)
		{
			if(((EAMGraphCell)allSelectedCells[i]).isFactor())
				nodes.add(allSelectedCells[i]);
		}
		return (FactorCell[])nodes.toArray(new FactorCell[0]);
	}
	
	//FIXME there is a duplicate of this method inside DiagraPanel
	public Vector getAllSelectedCellsWithRelatedLinkages(Object[] selectedCells) 
	{
		DiagramModel model = getDiagramModel();
		Vector selectedCellsWithLinkages = new Vector();
		for(int i=0; i < selectedCells.length; ++i)
		{
			EAMGraphCell cell = (EAMGraphCell)selectedCells[i];
			if(cell.isFactorLink())
			{
				if(!selectedCellsWithLinkages.contains(cell))
					selectedCellsWithLinkages.add(cell);
			}
			else if(cell.isFactor())
			{
				Set linkages = model.getFactorLinks((FactorCell)cell);
				for (Iterator iter = linkages.iterator(); iter.hasNext();) 
				{
					EAMGraphCell link = (EAMGraphCell) iter.next();
					if(!selectedCellsWithLinkages.contains(link))
						selectedCellsWithLinkages.add(link);
				}
				selectedCellsWithLinkages.add(cell);
			}
		}
		return selectedCellsWithLinkages;
	}
	
	Vector commandStack;
}
