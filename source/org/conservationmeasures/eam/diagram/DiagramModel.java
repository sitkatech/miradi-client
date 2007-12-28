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
import org.conservationmeasures.eam.diagram.cells.DiagramGroupBoxCell;
import org.conservationmeasures.eam.diagram.cells.DiagramIntermediateResultCell;
import org.conservationmeasures.eam.diagram.cells.DiagramStrategyCell;
import org.conservationmeasures.eam.diagram.cells.DiagramTargetCell;
import org.conservationmeasures.eam.diagram.cells.DiagramTextBoxCell;
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
import org.conservationmeasures.eam.objects.DiagramLink;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.GroupBox;
import org.conservationmeasures.eam.objects.IntermediateResult;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.objects.TextBox;
import org.conservationmeasures.eam.objects.ThreatReductionResult;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ThreatRatingFramework;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;
import org.conservationmeasures.eam.views.diagram.GroupOfDiagrams;
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
		isDamaged = false;
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
	
	DiagramChainObject getChainBuilder()
	{
		return getDiagramObject().getDiagramChainBuilder();
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
		
		if (factorType == ObjectType.TEXT_BOX)
			return new DiagramTextBoxCell((TextBox)factor, diagramFactor);
		
		if (factorType == ObjectType.GROUP_BOX)
			return new DiagramGroupBoxCell(this, (GroupBox)factor, diagramFactor);
		
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

    public DiagramLink addLinkToDiagram(DiagramLink diagramFactorLink) throws Exception
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
    
	public void deleteDiagramFactorLink(DiagramLink diagramFactorLinkToDelete) throws Exception
	{
		LinkCell cell = cellInventory.getLinkCell(diagramFactorLinkToDelete);
		Object[] links = new Object[]{cell};
		
		remove(links);
		cellInventory.removeFactorLink(diagramFactorLinkToDelete);
		
		notifyListeners(createDiagramModelEvent(cell), new ModelEventNotifierFactorLinkDeleted());
	}
	
	public boolean areDiagramFactorsLinked(DiagramFactorId fromDiagramFactorId, DiagramFactorId toDiagramFactorId) throws Exception
	{
		Vector<DiagramLink> diagramLinks = getAllDiagramFactorLinks();
		for (int i  = 0; i < diagramLinks.size(); ++i)
		{
			DiagramLink diagramLink = diagramLinks.get(i);
			if (diagramLink.getFromDiagramFactorId().equals(fromDiagramFactorId) && diagramLink.getToDiagramFactorId().equals(toDiagramFactorId))
				return true;
			
			if (diagramLink.getFromDiagramFactorId().equals(toDiagramFactorId) && diagramLink.getToDiagramFactorId().equals(fromDiagramFactorId))
				return true;
		}
		
		return false;
	}
	
	public boolean areLinked(DiagramFactorId fromDiagramFactorId, DiagramFactorId toDiagramFactorId) throws Exception
	{
		DiagramFactor fromDiagramFactor = (DiagramFactor) project.findObject(new ORef(ObjectType.DIAGRAM_FACTOR, fromDiagramFactorId));
		DiagramFactor toDiagramFactor = (DiagramFactor) project.findObject(new ORef(ObjectType.DIAGRAM_FACTOR, toDiagramFactorId));
		
		FactorId id1 = fromDiagramFactor.getWrappedId();
		FactorId id2 = toDiagramFactor.getWrappedId();
		
		return areLinked(id1, id2);
	}

	public boolean areLinked(ORef fromFactorRef, ORef toFactorRef)
	{
		FactorId fromFactorId = FactorId.createFromBaseId(fromFactorRef.getObjectId());
		FactorId toFactorId = FactorId.createFromBaseId(toFactorRef.getObjectId());
		
		return areLinked(fromFactorId, toFactorId);
	}

	public boolean areLinked(FactorId id1, FactorId id2)
	{
		return !getLink(id1, id2).isInvalid();
	}

	public ORef getLink(ORef fromFactorRef, ORef toFactorRef)
	{
		FactorId fromFactorId = FactorId.createFromBaseId(fromFactorRef.getObjectId());
		FactorId toFactorId = FactorId.createFromBaseId(toFactorRef.getObjectId());
		
		return getLink(fromFactorId, toFactorId);
	}
	
	private ORef getLink(FactorId id1, FactorId id2)
	{
		Vector links = cellInventory.getAllFactorLinks();
		for(int i = 0; i < links.size(); ++i)
		{
			DiagramLink thisLink = (DiagramLink)links.get(i);
			LinkCell link = findLinkCell(thisLink);
			FactorId foundId1 = link.getFrom().getWrappedId();
			FactorId foundId2 = link.getTo().getWrappedId();
			if(foundId1.equals(id1) && foundId2.equals(id2))
				return link.getWrappedORef();
			
			if(foundId1.equals(id2) && foundId2.equals(id1))
				return link.getWrappedORef();
		}
		
		return ORef.INVALID;
	}
	
	public boolean isResultsChain()
	{
		return diagramContents.isResultsChain();
	}
	
	public boolean isSharedInResultsChain(DiagramFactor diagramFactorToCheck)
	{
		DiagramFactor[] allResultsChainDiagramFactors = GroupOfDiagrams.findAllResultsChainDiagrams(project);
		return isSharedInDiagramFactors(allResultsChainDiagramFactors, diagramFactorToCheck);
	}
	
	public boolean isSharedInConceptualModel(DiagramFactor diagramFactorToCheck)
	{
		DiagramFactor[] allConceptualModelDiagramFactors = GroupOfDiagrams.findAllConceptualModelDiagrams(project);
		return isSharedInDiagramFactors(allConceptualModelDiagramFactors, diagramFactorToCheck);
	}
	
	public boolean isSharedInDiagramFactors(DiagramFactor[] potentialShares, DiagramFactor diagramFactorToCheck)
	{
		for (int i = 0; i < potentialShares.length; ++i)
		{
			DiagramFactor possibleAliasDiagramFactor = potentialShares[i];
			if (isAliasOf(diagramFactorToCheck, possibleAliasDiagramFactor))
				return true;
		}
		
		return false;	
	}
	
	private boolean isAliasOf(DiagramFactor diagramFactorToCheck, DiagramFactor possibleAliasDiagramFactor)
	{
		final boolean isSameDiagramFactor = diagramFactorToCheck.getId().equals(possibleAliasDiagramFactor.getId());
		if (isSameDiagramFactor)
			return false;

		final boolean isDifferentWrappedFactor = ! diagramFactorToCheck.getWrappedORef().equals(possibleAliasDiagramFactor.getWrappedORef());
		if (isDifferentWrappedFactor)
			return false;
				
		return true;
	}

	public FactorSet getDirectThreatChainNodes(Factor directThreat)
	{
		DiagramChainObject chainObject = getChainBuilder();
		return chainObject.buildDirectThreatChainAndGetFactors(this, directThreat);
	}
	
	public FactorSet getNodesInChain(Factor startingFactor)
	{
		DiagramChainObject chainObject = getChainBuilder();
		return chainObject.buildNormalChainAndGetFactors(this, startingFactor);
	}
		
	public FactorSet getAllUpstreamDownstreamNodes(Factor startingFactor)
	{
		DiagramChainObject chainObject = getChainBuilder();
		return chainObject.buildUpstreamDownstreamChainAndGetFactors(this, startingFactor);
	}

	public FactorSet getAllUpstreamNodes(DiagramFactor startingFactor)
	{
		DiagramChainObject chainObject = getChainBuilder();
		return chainObject.buildUpstreamChainAndGetFactors(this, startingFactor);
	}
	
	public FactorSet getAllDownstreamNodes(DiagramFactor startingFactor)
	{
		DiagramChainObject chainObject = getChainBuilder();
		return chainObject.buildDownstreamChainAndGetFactors(this, startingFactor);
	}

	public FactorSet getDirectlyLinkedUpstreamNodes(Factor startingFactor)
	{
		DiagramChainObject chainObject = getChainBuilder();
		return chainObject.buildDirectlyLinkedUpstreamChainAndGetFactors(this, startingFactor);
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
			Point newSnappedLocation = getProject().getSnapped(newLocation);
			EAM.logVerbose("moved Node from:"+ oldLocation +" to:"+ newSnappedLocation);
			factorToMove.setLocation(newSnappedLocation);
			updateCell(factorToMove);
		}
	}
	
	public void factorsWereMoved(DiagramFactorId[] ids)
	{
		for(int i=0; i < ids.length; ++i)
		{
			try
			{
				FactorCell factor = getFactorCellById(ids[i]);
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
		return getAllFactorCells().size();
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
	
	public LinkCell findLinkCell(DiagramLink link)
	{
		return cellInventory.getLinkCell(link);
	}
	
	public void updateVisibilityOfFactorsAndLinks() throws Exception
	{
		updateVisibilityOfFactors();
		updateVisibilityOfLinks();
	}
	
	
	private void updateVisibilityOfFactors() throws Exception
	{
		Vector nodes = getAllFactorCells();
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
		boolean isVisible = shouldFactorCellBeVisible(manager, factorCell);
		getGraphLayoutCache().setVisible(factorCell, isVisible);
	}

	private boolean shouldFactorCellBeVisible(LayerManager manager, FactorCell factorCell)
	{
		return manager.isVisible(getDiagramObject(), factorCell);
	}

	private void updateVisibilityOfLinks() throws Exception
	{
		LinkCell[] linkCells = getAllFactorLinkCells();
		for(int i = 0; i < linkCells.length; ++i)
		{
			updateVisibilityOfSingleLink(linkCells[i]);
		}
	}	
	
	public void updateVisibilityOfSingleLink(LinkCell linkCell) throws Exception
	{
		LayerManager manager = project.getLayerManager();

		boolean isLinkVisible = !linkCell.getDiagramLink().isCoveredByGroupBoxLink();
		if(!shouldFactorCellBeVisible(manager, linkCell.getFrom()))
			isLinkVisible = false;
		if(!shouldFactorCellBeVisible(manager, linkCell.getTo()))
			isLinkVisible = false;
		getGraphLayoutCache().setVisible(linkCell, isLinkVisible);
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
	
	public boolean doesFactorExist(ORef factorRef)
	{
		FactorId factorId = new FactorId(factorRef.getObjectId().asInt());
		return (rawGetFactorByWrappedId(factorId) != null);
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
	
	public FactorLink getRawFactorLink(DiagramLink diagramFactorLink)
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

	public LinkCell getDiagramFactorLink(DiagramLink diagramFactorLink)
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
	
	public DiagramLink getDiagramFactorLinkById(DiagramFactorLinkId id) throws Exception
	{
		DiagramLink linkage = cellInventory.getFactorLinkById(id);
		if(linkage == null)
			throw new Exception("Link doesn't exist, id: " + id);
		return linkage;
	}
	
	public DiagramLink getDiagramFactorLinkByWrappedRef(ORef factorLinkRef) throws Exception
	{
		return getDiagramFactorLinkbyWrappedId((FactorLinkId) factorLinkRef.getObjectId());
	}
	
	public DiagramLink getDiagramFactorLinkbyWrappedId(FactorLinkId id) throws Exception
	{
		DiagramLink linkage = cellInventory.getFactorLinkById(id);
		if(linkage == null)
			throw new Exception("Link doesn't exist, id: " + id);
		return linkage;
	}

	public boolean doesDiagramFactorLinkExist(FactorLinkId id)
	{
		DiagramLink linkage = cellInventory.getFactorLinkById(id);
		return (linkage != null);
	}

	public boolean doesDiagramFactorExist(DiagramFactorId id)
	{
		return (rawGetFactorById(id) != null);
	}

	public boolean doesDiagramFactorLinkExist(DiagramFactorLinkId linkId)
	{
		return (cellInventory.getFactorLinkById(linkId) != null);	
	}
	
	public boolean doesDiagramFactorLinkExist(DiagramLink link)
	{
		return (cellInventory.getFactorLinkById(link.getDiagramLinkageId()) != null);
	}

	public Vector getAllFactorCells()
	{
		return cellInventory.getAllFactors();
	}
	
	public DiagramFactor[] getAllDiagramFactorsAsArray()
	{
		Vector allDiagramFactors = new Vector();
		Vector allFactorCells = getAllFactorCells();
		for (int i = 0; i < allFactorCells.size(); i++)
		{
			FactorCell factorCell = (FactorCell) allFactorCells.get(i);
			allDiagramFactors.add(factorCell.getDiagramFactor());
		}
		
		return (DiagramFactor[]) allDiagramFactors.toArray(new DiagramFactor[0]);
	}

	public LinkCell[] getAllFactorLinkCells()
	{
		return (LinkCell[]) cellInventory.getAllFactorLinkCells().toArray(new LinkCell[0]);
	}
	
	public DiagramLink[] getAllDiagramLinksAsArray()
	{
		return (DiagramLink[]) getAllDiagramFactorLinks().toArray(new DiagramLink[0]);
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
		Vector factors = getAllFactorCells();
		IdList diagramFactorIds = new IdList(DiagramFactor.getObjectType());
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
		
		if (isDamaged())
			EAM.errorDialog(EAM.text("An error is preventing this diagram from displaying correctly. " +
				 "Most likely, the project has gotten corrupted. Please contact " +
				 "the Miradi team for help and advice. We recommend that you not " +
				 "make any changes to this project until this problem has been resolved."));
	}

	private void addFactorsToModel(EnhancedJsonObject json) throws Exception
	{
		IdList diagramFactorIds = new IdList(DiagramFactor.getObjectType(), json.getString(TAG_DIAGRAM_FACTOR_IDS));
		for(int i = 0; i < diagramFactorIds.size(); ++i)
		{
			try
			{
				DiagramFactor diagramFactor = (DiagramFactor) project.findObject(ObjectType.DIAGRAM_FACTOR, diagramFactorIds.get(i));
				addDiagramFactor(diagramFactor);
			}
			catch (Exception e)
			{
				EAM.logException(e);
				isDamaged = true;
			}
		}
	}
	
	public void addLinksToModel(EnhancedJsonObject json) throws Exception
	{
		IdList allDiagramFactorLinkIds = new IdList(DiagramLink.getObjectType(), json.getString(TAG_DIAGRAM_FACTOR_LINK_IDS));
		for (int i = 0; i < allDiagramFactorLinkIds.size(); i++)
		{
			BaseId factorLinkId = allDiagramFactorLinkIds.get(i);
			DiagramLink diagramFactorLink = (DiagramLink) project.findObject(new ORef(ObjectType.DIAGRAM_LINK, factorLinkId));
			addLinkToDiagram(diagramFactorLink);
		}
	}
	
	public LinkCell updateCellFromDiagramFactorLink(DiagramFactorLinkId diagramFactorLinkId) throws Exception
	{
		if (! doesDiagramFactorLinkExist(diagramFactorLinkId))
			return null;
		
		DiagramLink diagramFactorLink  = getDiagramFactorLinkById(diagramFactorLinkId);
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
		Vector allFactors = getAllFactorCells();
		for (int i = 0; i < allFactors.size(); i++)
		{
			FactorCell diagramFactor = (FactorCell)allFactors.get(i);
			if (diagramFactor.isTarget())
				allTargets.add(diagramFactor);
		}
		
		return (FactorCell[])allTargets.toArray(new FactorCell[0]);
	}
	
	public void updateGroupBoxCells()
	{
		Vector allGroupBoxes = getAllGroupBoxCells();
		for (int i = 0; i < allGroupBoxes.size(); ++i)
		{
			DiagramGroupBoxCell cell = (DiagramGroupBoxCell) allGroupBoxes.get(i);
			cell.autoSurroundChildren();
		}
	}
	
	public Vector getAllGroupBoxCells()
	{
		Vector allGroupBoxCells = new Vector();
		Vector allFactors = getAllFactorCells();
		for (int i = 0; i < allFactors.size(); i++)
		{
			FactorCell factorCell = (FactorCell)allFactors.get(i);
			if (factorCell.isGroupBox())
				allGroupBoxCells.add(factorCell);
		}
		
		return allGroupBoxCells;	
	}
	
	public DiagramObject getDiagramObject()
	{
		return diagramContents;
	}
	
	public GraphLayoutCache getGraphLayoutCache()
	{
		return graphLayoutCache;
	}
	
	private boolean isDamaged()
	{
		return isDamaged;
	}
		
	private static final String TAG_TYPE = "Type";
	public static final String TAG_DIAGRAM_FACTOR_IDS = "DiagramFactorIds";
	public static final String TAG_DIAGRAM_FACTOR_LINK_IDS = "DiagramFactorLinkIds";
	
	
	private static final String JSON_TYPE_DIAGRAM = "Diagram";
	
	private Project project;
	private CellInventory cellInventory;
	private ProjectScopeBox projectScopeBox;
	protected List diagramModelListenerList = new ArrayList();
	
	private DiagramObject diagramContents;
	
	private HashMap factorsToDiagramFactors;
	private GraphLayoutCache graphLayoutCache;
	private boolean isDamaged;
}

