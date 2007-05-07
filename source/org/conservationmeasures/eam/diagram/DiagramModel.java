/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.conservationmeasures.eam.diagram.cells.DiagramCauseCell;
import org.conservationmeasures.eam.diagram.cells.DiagramIntermediateResultCell;
import org.conservationmeasures.eam.diagram.cells.DiagramStrategyCell;
import org.conservationmeasures.eam.diagram.cells.DiagramTargetCell;
import org.conservationmeasures.eam.diagram.cells.DiagramThreatReductionResultCell;
import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.diagram.cells.LinkCell;
import org.conservationmeasures.eam.diagram.cells.ProjectScopeBox;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.DiagramFactorLinkId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.CreateDiagramFactorLinkParameter;
import org.conservationmeasures.eam.objecthelpers.FactorSet;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objectpools.DiagramFactorLinkPool;
import org.conservationmeasures.eam.objectpools.DiagramFactorPool;
import org.conservationmeasures.eam.objectpools.FactorLinkPool;
import org.conservationmeasures.eam.objectpools.GoalPool;
import org.conservationmeasures.eam.objectpools.ObjectivePool;
import org.conservationmeasures.eam.objects.Cause;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.DiagramFactorLink;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.IntermediateResult;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.objects.ThreatReductionResult;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ThreatRatingFramework;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;
import org.conservationmeasures.eam.views.diagram.LayerManager;
import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.GraphLayoutCache;

public class DiagramModel extends DefaultGraphModel
{
	public DiagramModel(Project projectToUse)
	{
		project = projectToUse;
		clear();
	}
		
	public void clear()
	{
		while(getRootCount() > 0)
			remove(new Object[] {getRootAt(0)});

		cellInventory = new CellInventory();
		projectScopeBox = new ProjectScopeBox(this);
		graphLayoutCache = new PartialGraphLayoutCache(this);
		insertCellIntoGraph(projectScopeBox);
		
		factorsToDiagramFactors = new HashMap();
	}

	public ProjectScopeBox getProjectScopeBox()
	{
		return projectScopeBox;
	}
	
	public Project getProject()
	{
		return project;
	}
	
	public ThreatRatingFramework getThreatRatingFramework()
	{
		return project.getThreatRatingFramework();
	}
	
	public void addDiagramFactor(DiagramFactor diagramFactor) throws Exception
	{
		Factor factor = project.findNode(diagramFactor.getWrappedId());
		FactorCell factorCell = createFactorCell(diagramFactor, factor);
		addFactorCellToModel(factorCell);
		factorsToDiagramFactors.put(diagramFactor.getWrappedId(), diagramFactor.getDiagramFactorId());
	}

	private FactorCell createFactorCell(DiagramFactor diagramFactor, Factor factor)
	{
		int factorType = factor.getType();
		if (factorType == ObjectType.CAUSE)
			return new DiagramCauseCell((Cause) factor, diagramFactor);
	
		if (factorType == ObjectType.STRATEGY)
			return new DiagramStrategyCell((Strategy) factor, diagramFactor);
		
		if (factorType == ObjectType.TARGET)
			return new DiagramTargetCell((Target) factor, diagramFactor);
	
		if (factorType == ObjectType.INTERMEDIATE_RESULT)
			return new DiagramIntermediateResultCell((IntermediateResult) factor, diagramFactor);
		
		if (factorType == ObjectType.THREAT_REDUCTION_RESULT)
			return new DiagramThreatReductionResultCell((ThreatReductionResult) factor, diagramFactor);
		
		throw new RuntimeException("Unknown factor type "+factorType);
	}

	private void addFactorCellToModel(FactorCell factor) throws Exception
	{
		insertCellIntoGraph(factor);
		cellInventory.addFactor(factor);
		notifyListeners(createDiagramModelEvent(factor), new ModelEventNotifierFactorAdded());
	}

	private void insertCellIntoGraph(DefaultGraphCell cell)
	{
		Object[] cells = new Object[] {cell};
		Hashtable nestedAttributeMap = getNestedAttributeMap(cell);
		insert(cells, nestedAttributeMap, null, null, null);
	}

	private Hashtable getNestedAttributeMap(DefaultGraphCell cell)
	{
		Hashtable nest = new Hashtable();
		nest.put(cell, cell.getAttributes());
		return nest;
	}
	
	private DiagramModelEvent createDiagramModelEvent(EAMGraphCell cell) throws Exception 
	{
		return new DiagramModelEvent(this, cell);
	}
	
	public void addDiagramModelListener(DiagramModelListener listener)
	{
		diagramModelListenerList.add(listener);
	}
	
    public void removeMyEventListener(DiagramModelListener listener) 
    {
    	diagramModelListenerList.remove(listener);
    }	
    
    void notifyListeners(DiagramModelEvent event, ModelEventNotifier eventNotifier) 
    {
        for (int i=0; i<diagramModelListenerList.size(); ++i) 
        {
        	eventNotifier.doNotify((DiagramModelListener)diagramModelListenerList.get(i), event);
        }                
    }

    public void removeDiagramFactor(DiagramFactorId diagramFactorId) throws Exception
    {
    	factorsToDiagramFactors.remove(cellInventory.getFactorById(diagramFactorId));
    	FactorCell diagramFactorToDelete = getFactorCellById(diagramFactorId);	
    	Object[] cells = new Object[]{diagramFactorToDelete};
		remove(cells);
		cellInventory.removeFactor(diagramFactorId);
		notifyListeners(createDiagramModelEvent(diagramFactorToDelete), new ModelEventNotifierFactorDeleted());
    }

    public DiagramFactorLink addLinkToDiagram(DiagramFactorLink diagramFactorLink) throws Exception
    {
    	CreateDiagramFactorLinkParameter extraInfo = (CreateDiagramFactorLinkParameter) diagramFactorLink.getCreationExtraInfo();
		FactorCell from = rawGetFactorById(extraInfo.getFromFactorId());
		if(from == null)
			EAM.logError("Missing from, DFL=" + diagramFactorLink.getId() + ", From=" + extraInfo.getFromFactorId());
		FactorCell to = rawGetFactorById(extraInfo.getToFactorId());
		if(to == null)
			EAM.logError("Missing to, DFL=" + diagramFactorLink.getId() + ", To=" + extraInfo.getToFactorId());
		FactorLink factorLink = getRawFactorLink(diagramFactorLink); 
		LinkCell cell = new LinkCell(factorLink, diagramFactorLink, from, to);
		
		EAMGraphCell[] newLinks = new EAMGraphCell[]{cell};
		Map nestedMap = getNestedAttributeMap(cell);
		ConnectionSet cs = new ConnectionSet(cell, from.getPort(), to.getPort());

		insert(newLinks, nestedMap, cs, null, null);
		cellInventory.addFactorLink(diagramFactorLink, cell);
		
		notifyListeners(createDiagramModelEvent(cell), new ModelEventNotifierFactorLinkAdded());
		
    	return diagramFactorLink;
    }
    
	public void deleteDiagramFactorLink(DiagramFactorLink diagramFactorLinkToDelete) throws Exception
	{
		LinkCell cell = cellInventory.getLinkCell(diagramFactorLinkToDelete);
		Object[] links = new Object[]{cell};
		
		remove(links);
		cellInventory.removeFactorLink(diagramFactorLinkToDelete);
		
		notifyListeners(createDiagramModelEvent(cell), new ModelEventNotifierFactorLinkDeleted());
	}
	
	public boolean areLinked(DiagramFactorId fromDiagramFactorId, DiagramFactorId toDiagramFactorId) throws Exception
	{
		DiagramFactor fromDiagramFactor = (DiagramFactor) project.findObject(new ORef(ObjectType.DIAGRAM_FACTOR, fromDiagramFactorId));
		DiagramFactor toDiagramFactor = (DiagramFactor) project.findObject(new ORef(ObjectType.DIAGRAM_FACTOR, toDiagramFactorId));
		
		FactorId id1 = fromDiagramFactor.getWrappedId();
		FactorId id2 = toDiagramFactor.getWrappedId();
		
		Vector links = cellInventory.getAllFactorLinks();
		for(int i = 0; i < links.size(); ++i)
		{
			DiagramFactorLink thisLink = (DiagramFactorLink)links.get(i);
			LinkCell link = findLinkCell(thisLink);
			FactorId foundId1 = link.getFrom().getWrappedId();
			FactorId foundId2 = link.getTo().getWrappedId();
			if(foundId1.equals(id1) && foundId2.equals(id2))
				return true;
			if(foundId1.equals(id2) && foundId2.equals(id1))
				return true;
		}
		
		return false;
	}
	
	public boolean isResultsChain()
	{
		return diagramContents.isResultsChain();
	}
	
	public FactorSet getDirectThreatChainNodes(Factor directThreat)
	{
		DiagramChainObject chainObject = new DiagramChainObject();
		chainObject.buildDirectThreatChain(this, directThreat);
		return chainObject.getFactors();
	}
	
	public FactorSet getNodesInChain(Factor startingFactor)
	{
		DiagramChainObject chainObject = new DiagramChainObject();
		chainObject.buildNormalChain(this, startingFactor);
		return chainObject.getFactors();
	}
		
	public FactorSet getAllUpstreamDownstreamNodes(Factor startingFactor)
	{
		DiagramChainObject chainObject = new DiagramChainObject();
		chainObject.buildUpstreamDownstreamChain(this, startingFactor);
		return chainObject.getFactors();
	}

	public FactorSet getAllUpstreamNodes(Factor startingFactor)
	{
		DiagramChainObject chainObject = new DiagramChainObject();
		chainObject.buildUpstreamChain(this, startingFactor);
		return chainObject.getFactors();
	}

	public FactorSet getDirectlyLinkedUpstreamNodes(Factor startingFactor)
	{
		DiagramChainObject chainObject = new DiagramChainObject();
		chainObject.buildDirectlyLinkedUpstreamChain(this, startingFactor);
		return chainObject.getFactors();
	}
	
	public void moveFactors(int deltaX, int deltaY, DiagramFactorId[] ids) throws Exception
	{
		moveFactorsWithoutNotification(deltaX, deltaY, ids);
		factorsWereMoved(ids);
	}

	public void moveFactorsWithoutNotification(int deltaX, int deltaY, DiagramFactorId[] ids) throws Exception
	{
		for(int i = 0; i < ids.length; ++i)
		{
			DiagramFactorId id = ids[i];
			FactorCell factorToMove = getFactorCellById(id);
			Point oldLocation = factorToMove.getLocation();
			Point newLocation = new Point(oldLocation.x + deltaX, oldLocation.y + deltaY);
			EAM.logVerbose("moved Node from:"+ oldLocation +" to:"+ newLocation);
			factorToMove.setLocation(newLocation);
			updateCell(factorToMove);
		}
	}
	
	public void factorsWereMoved(DiagramFactorId[] ids)
	{
		for(int i=0; i < ids.length; ++i)
		{
			try
			{
				FactorCell factor = getFactorCellById(ids[0]);
				notifyListeners(createDiagramModelEvent(factor), new ModelEventNotifierFactorMoved());
			}
			catch (Exception e)
			{
				EAM.logException(e);
			}
		}
	}
	
	public int getFactorCount()
	{
		return getAllDiagramFactors().size();
	}
	
	public int getFactorLinkCount()
	{
		return getAllDiagramFactorLinks().size();
	}

	public int getFactorLinksSize(DiagramFactorId diagramFactorId) throws Exception
	{
		FactorCell factorCell = getFactorCellById(diagramFactorId);
		return getFactorLinks(factorCell).size();
	}
	
	public Set getFactorLinks(FactorCell node)
	{
		return getEdges(this, new Object[] {node});
	}
	
	public LinkCell findLinkCell(DiagramFactorLink link)
	{
		return cellInventory.getLinkCell(link);
	}
	
	
	public void updateVisibilityOfFactors() throws Exception
	{
		Vector nodes = getAllDiagramFactors();
		for(int i = 0; i < nodes.size(); ++i)
		{
			FactorCell node = (FactorCell)nodes.get(i);
			updateVisibilityOfSingleFactor(node.getDiagramFactorId());
		}
		LayerManager manager = project.getLayerManager();
		getGraphLayoutCache().setVisible(getProjectScopeBox(), manager.isScopeBoxVisible());
	}	
	
	public void updateVisibilityOfSingleFactor(DiagramFactorId diagramFactorId) throws Exception
	{
		LayerManager manager = project.getLayerManager();
		FactorCell factorCell = getFactorCellById(diagramFactorId);
		boolean isVisible = manager.isVisible(getDiagramObject(), factorCell);
		getGraphLayoutCache().setVisible(factorCell, isVisible);
	}

	public void updateCell(EAMGraphCell cellToUpdate) throws Exception
	{
		edit(getNestedAttributeMap(cellToUpdate), null, null, null);
		notifyListeners(createDiagramModelEvent(cellToUpdate), new ModelEventNotifierFactorChanged());
	}
	
	public void updateDiagramFactor(DiagramFactorId diagramFactorId) throws Exception
	{
		FactorCell cellToUpdate = getFactorCellById(diagramFactorId);
		edit(getNestedAttributeMap(cellToUpdate), null, null, null);
		notifyListeners(createDiagramModelEvent(cellToUpdate), new ModelEventNotifierFactorChanged());
	}
	
	public boolean doesFactorExist(FactorId id)
	{
		return (rawGetFactorByWrappedId(id) != null);
	}
	
	public FactorCell getFactorCellById(DiagramFactorId id) throws Exception
	{
		FactorCell node = rawGetFactorById(id);
		if(node == null)
			throw new Exception("Node doesn't exist, id: " + id);
		return node;
	}

	public DiagramFactor getDiagramFactor(FactorId id)
	{
		return rawGetFactorByWrappedId(id).getDiagramFactor();	
	}
	
	public FactorCell getFactorCellByWrappedId(FactorId id)
	{
		FactorCell node = rawGetFactorByWrappedId(id);
		if(node == null)
			EAM.logDebug("getDiagramFactorByWrappedId about to return null for: " + id);
		return node;
	}

	public boolean containsDiagramFactor(DiagramFactorId diagramFactorId)
	{
		FactorCell node = rawGetFactorById(diagramFactorId);
		if(node == null)
			return false;
		
		return true;
	}
	
	public FactorLink getRawFactorLink(DiagramFactorLink diagramFactorLink)
	{
		FactorLinkId wrappedId = diagramFactorLink.getWrappedId();
		FactorLink factorLink = (FactorLink) project.findObject(ObjectType.FACTOR_LINK, wrappedId);
		
		return factorLink;
	}
	
	private FactorCell rawGetFactorById(DiagramFactorId id)
	{
		return cellInventory.getFactorById(id);
	}

	private FactorCell rawGetFactorByWrappedId(FactorId id)
	{
		return cellInventory.getFactorById(id);
	}

	public LinkCell getDiagramFactorLink(DiagramFactorLink diagramFactorLink)
	{
		return cellInventory.getLinkCell(diagramFactorLink);
	}
	
	public DiagramFactorId getDiagramFactorIdFromWrappedId(FactorId factorId)
	{
		return (DiagramFactorId) factorsToDiagramFactors.get(factorId);
	}
	
	public FactorId getWrappedId(DiagramFactorId diagramFactorId)
	{
		FactorCell wrappedId = cellInventory.getFactorById(diagramFactorId);
		return (FactorId) factorsToDiagramFactors.get(wrappedId);
	}
	
	public DiagramFactorLink getDiagramFactorLinkById(DiagramFactorLinkId id) throws Exception
	{
		DiagramFactorLink linkage = cellInventory.getFactorLinkById(id);
		if(linkage == null)
			throw new Exception("Link doesn't exist, id: " + id);
		return linkage;
	}
	
	public DiagramFactorLink getDiagramFactorLinkbyWrappedId(FactorLinkId id) throws Exception
	{
		DiagramFactorLink linkage = cellInventory.getFactorLinkById(id);
		if(linkage == null)
			throw new Exception("Link doesn't exist, id: " + id);
		return linkage;
	}

	public boolean doesDiagramFactorExist(DiagramFactorId id)
	{
		return (rawGetFactorById(id) != null);
	}

	public boolean doesDiagramFactorLinkExist(DiagramFactorLinkId linkId)
	{
		return (cellInventory.getFactorLinkById(linkId) != null);	
	}
	
	public boolean doesDiagramFactorLinkExist(DiagramFactorLink link)
	{
		return (cellInventory.getFactorLinkById(link.getDiagramLinkageId()) != null);
	}

	public Vector getAllDiagramFactors()
	{
		return cellInventory.getAllFactors();
	}
	
	public DiagramFactor[] getAllDiagramFactorsAsArray()
	{
		Vector allDiagramFactors = new Vector();
		Vector allFactorCells = getAllDiagramFactors();
		for (int i = 0; i < allFactorCells.size(); i++)
		{
			FactorCell factorCell = (FactorCell) allFactorCells.get(i);
			allDiagramFactors.add(factorCell.getDiagramFactor());
		}
		
		return (DiagramFactor[]) allDiagramFactors.toArray(new DiagramFactor[0]);
	}

	public DiagramFactorLink[] getAllDiagramLinksAsArray()
	{
		return (DiagramFactorLink[]) getAllDiagramFactorLinks().toArray(new DiagramFactorLink[0]);
	}
	
	public Vector getAllSelectedCellsWithRelatedLinkages(Object[] selectedCells) 
	{
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
				Set linkages = getFactorLinks((FactorCell)cell);
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
	
	public Vector getAllDiagramFactorLinks()
	{
		return cellInventory.getAllFactorLinks();
	}
	
	public Goal getGoalById(BaseId id)
	{
		return getGoalPool().find(id);
	}
	
	public Objective getObjectiveById(BaseId id)
	{
		return getObjectivePool().find(id);
	}
	
	public EnhancedJsonObject toJson()
	{
		Vector factors = getAllDiagramFactors();
		IdList diagramFactorIds = new IdList();
		for(int i=0; i < factors.size(); ++i)
		{
			FactorCell factorCell = (FactorCell)factors.get(i);
			diagramFactorIds.add(factorCell.getDiagramFactorId());
		}
		EnhancedJsonObject json = new EnhancedJsonObject();
		json.put(TAG_TYPE, JSON_TYPE_DIAGRAM);
		json.put(TAG_DIAGRAM_FACTOR_IDS, diagramFactorIds.toJson());
		
		return json;
	}
	
	public void fillFrom(DiagramObject diagramContentsToUse) throws Exception
	{
		diagramContents = diagramContentsToUse;
		
		clear();
		addFactorsToModel(diagramContents.toJson());
		addLinksToModel(diagramContents.toJson());
	}

	private void addFactorsToModel(EnhancedJsonObject json) throws Exception
	{
		IdList diagramFactorIds = new IdList(json.getString(TAG_DIAGRAM_FACTOR_IDS));
		// TODO: Really we should extend JSONObject to have a sane names() method
		// that returns an empty array if there are no names
		
		for(int i=0; i < diagramFactorIds.size(); ++i)
		{
			DiagramFactor diagramFactor = (DiagramFactor) project.findObject(ObjectType.DIAGRAM_FACTOR, diagramFactorIds.get(i));
			addDiagramFactor(diagramFactor);
		}
	}
	
	public void addLinksToModel(EnhancedJsonObject json) throws Exception
	{
		IdList allDiagramFactorLinkIds = new IdList(json.getString(TAG_DIAGRAM_FACTOR_LINK_IDS));
		for (int i = 0; i < allDiagramFactorLinkIds.size(); i++)
		{
			BaseId factorLinkId = allDiagramFactorLinkIds.get(i);
			DiagramFactorLink diagramFactorLink = (DiagramFactorLink) project.findObject(new ORef(ObjectType.DIAGRAM_LINK, factorLinkId));
			addLinkToDiagram(diagramFactorLink);
		}
	}
	
	public LinkCell updateCellFromDiagramFactorLink(DiagramFactorLinkId diagramFactorLinkId) throws Exception
	{
		if (! doesDiagramFactorLinkExist(diagramFactorLinkId))
			return null;
		
		DiagramFactorLink diagramFactorLink  = getDiagramFactorLinkById(diagramFactorLinkId);
		LinkCell linkCell = getDiagramFactorLink(diagramFactorLink);
		linkCell.updateFromDiagramFactorLink();
		updateCell(linkCell);
		return linkCell;
	}

	public void updateCellFromDiagramFactor(DiagramFactorId diagramFactorId) throws Exception
	{
		if (! doesDiagramFactorExist(diagramFactorId))
			return;
			
		FactorCell factorCell = getFactorCellById(diagramFactorId);
		factorCell.updateFromDiagramFactor();
		updateCell(factorCell);
	}

	public void updateProjectScopeBox()
	{
		String newText = getProject().getMetadata().getShortProjectScope();
		getProjectScopeBox().setText(EAM.text("Project Scope: " + newText));
		getProjectScopeBox().autoSurroundTargets();
	}

	public DiagramFactorPool getDiagramFactorPool()
	{
		return project.getDiagramFactorPool();
	}
	
	public DiagramFactorLinkPool getDiagramFactorLinkPool()
	{
		return project.getDiagramFactorLinkPool();
	}
	
	FactorLinkPool getFactorLinkPool()
	{
		return project.getFactorLinkPool();
	}
	
	ObjectivePool getObjectivePool()
	{
		return project.getObjectivePool();
	}
	
	GoalPool getGoalPool()
	{
		return project.getGoalPool();
	}
	
	public FactorCell[] getAllDiagramTargets()
	{
		Vector allTargets = new Vector();
		Vector allFactors = getAllDiagramFactors();
		for (int i = 0; i < allFactors.size(); i++)
		{
			FactorCell diagramFactor = (FactorCell)allFactors.get(i);
			if (diagramFactor.isTarget())
				allTargets.add(diagramFactor);
		}
		
		return (FactorCell[])allTargets.toArray(new FactorCell[0]);
	}
	
	public DiagramObject getDiagramObject()
	{
		return diagramContents;
	}
	
	public GraphLayoutCache getGraphLayoutCache()
	{
		return graphLayoutCache;
	}
	
		
	private static final String TAG_TYPE = "Type";
	public static final String TAG_DIAGRAM_FACTOR_IDS = "DiagramFactorIds";
	public static final String TAG_DIAGRAM_FACTOR_LINK_IDS = "DiagramFactorLinkIds";
	
	
	private static final String JSON_TYPE_DIAGRAM = "Diagram";
	
	Project project;
	CellInventory cellInventory;
	ProjectScopeBox projectScopeBox;
	protected List diagramModelListenerList = new ArrayList();
	
	DiagramObject diagramContents;
	
	HashMap factorsToDiagramFactors;
	GraphLayoutCache graphLayoutCache;
}

