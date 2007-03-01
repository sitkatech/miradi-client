/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.conservationmeasures.eam.diagram.cells.DiagramFactor;
import org.conservationmeasures.eam.diagram.cells.DiagramFactorCluster;
import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
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
import org.conservationmeasures.eam.objectpools.FactorLinkPool;
import org.conservationmeasures.eam.objectpools.FactorPool;
import org.conservationmeasures.eam.objectpools.GoalPool;
import org.conservationmeasures.eam.objectpools.ObjectivePool;
import org.conservationmeasures.eam.objects.DiagramFactorLink;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.FactorCluster;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ThreatRatingFramework;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;
import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.json.JSONArray;

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
		insertCellIntoGraph(projectScopeBox);
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
	
	public DiagramFactor createDiagramFactor(FactorId idToWrap) throws Exception
	{
		return createDiagramFactor(idToWrap, new DiagramFactorId(BaseId.INVALID.asInt()));
	}

	public DiagramFactor createDiagramFactor(FactorId idToWrap, DiagramFactorId requestedId) throws Exception
	{
		Factor factor = getFactorPool().find(idToWrap);
		DiagramFactorId factorId = requestedId;
		if(factorId.isInvalid())
			factorId = takeNextDiagramFactorId();
		EAM.logDebug("DiagramModel.createDiagramFactor: " + factorId);
		DiagramFactor diagramFactor = DiagramFactor.wrapConceptualModelObject(factorId, factor);
		addFactorToModel(diagramFactor);
		return diagramFactor;
	}
	
	private DiagramFactorId takeNextDiagramFactorId()
	{
		return new DiagramFactorId(getProject().getAnnotationIdAssigner().takeNextId().asInt());
	}

	private void addFactorToModel(DiagramFactor factor) throws Exception
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

	public Hashtable getNestedAttributeMap(DefaultGraphCell cell)
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
	
    public void deleteDiagramFactor(DiagramFactor diagramFactorToDelete) throws Exception
	{
		Object[] cells = new Object[]{diagramFactorToDelete};
		remove(cells);
		cellInventory.removeFactor(diagramFactorToDelete);

		notifyListeners(createDiagramModelEvent(diagramFactorToDelete), new ModelEventNotifierFactorDeleted());
	}
	
	public DiagramFactorLink createDiagramFactorLink(FactorLink factorLinkToWrap) throws Exception
	{
		FactorLinkId factorLinkId = (FactorLinkId)factorLinkToWrap.getId();
		DiagramFactor from = getDiagramFactorByWrappedId(factorLinkToWrap.getFromFactorId());
		DiagramFactor to = getDiagramFactorByWrappedId(factorLinkToWrap.getToFactorId());
		
		DiagramFactorLinkId newDiagramLinkId = new DiagramFactorLinkId(factorLinkId.asInt());
		CreateDiagramFactorLinkParameter extraInfo = new CreateDiagramFactorLinkParameter(
				factorLinkId, from.getDiagramFactorId(), to.getDiagramFactorId());
		DiagramFactorLink newLink = new DiagramFactorLink(newDiagramLinkId, extraInfo);
		LinkCell cell = new LinkCell(factorLinkToWrap, newLink, from, to);
		
		EAMGraphCell[] newLinks = new EAMGraphCell[]{cell};
		Map nestedMap = getNestedAttributeMap(cell);
		ConnectionSet cs = new ConnectionSet(cell, from.getPort(), to.getPort());
		insert(newLinks, nestedMap, cs, null, null);

		cellInventory.addFactorLink(newLink, cell);
		notifyListeners(createDiagramModelEvent(cell), new ModelEventNotifierFactorLinkAdded());
		
		return newLink;
	}
	
	public void deleteDiagramFactorLink(DiagramFactorLink diagramFactorLinkToDelete) throws Exception
	{
		LinkCell cell = cellInventory.getLinkCell(diagramFactorLinkToDelete);
		Object[] links = new Object[]{cell};
		remove(links);
		cellInventory.removeFactorLink(diagramFactorLinkToDelete);
		notifyListeners(createDiagramModelEvent(cell), new ModelEventNotifierFactorLinkDeleted());
	}
	
	public boolean areLinked(DiagramFactor fromFactor, DiagramFactor toFactor) throws Exception
	{
		FactorId id1 = fromFactor.getWrappedId();
		FactorId id2 = toFactor.getWrappedId();
		
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
	
	public FactorSet getDirectThreatChainNodes(Factor directThreat)
	{
		ChainObject chainObject = new ChainObject();
		chainObject.buildDirectThreatChain(this, directThreat);
		return chainObject.getFactors();
	}
	
	public FactorSet getNodesInChain(Factor startingFactor)
	{
		ChainObject chainObject = new ChainObject();
		chainObject.buildNormalChain(this, startingFactor);
		return chainObject.getFactors();
	}
		
	public FactorSet getAllUpstreamDownstreamNodes(Factor startingFactor)
	{
		ChainObject chainObject = new ChainObject();
		chainObject.buildUpstreamDownstreamChain(this, startingFactor);
		return chainObject.getFactors();
	}

	public FactorSet getAllUpstreamNodes(Factor startingFactor)
	{
		ChainObject chainObject = new ChainObject();
		chainObject.buildUpstreamChain(this, startingFactor);
		return chainObject.getFactors();
	}

	public FactorSet getDirectlyLinkedUpstreamNodes(Factor startingFactor)
	{
		ChainObject chainObject = new ChainObject();
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
			DiagramFactor factorToMove = getDiagramFactorById(id);
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
				DiagramFactor factor = getDiagramFactorById(ids[0]);
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

	public Set getFactorLinks(DiagramFactor node)
	{
		return getEdges(this, new Object[] {node});
	}
	
	public LinkCell findLinkCell(DiagramFactorLink link)
	{
		return cellInventory.getLinkCell(link);
	}
	
	public void updateCell(EAMGraphCell cellToUpdate) throws Exception
	{
		edit(getNestedAttributeMap(cellToUpdate), null, null, null);
		notifyListeners(createDiagramModelEvent(cellToUpdate), new ModelEventNotifierFactorChanged());
	}
	
	public boolean doesDiagramFactorExist(DiagramFactorId id)
	{
		return (rawGetFactorById(id) != null);
	}
	
	public boolean doesFactorExist(FactorId id)
	{
		return (rawGetFactorByWrappedId(id) != null);
	}
	
	
	
	public DiagramFactor getDiagramFactorById(DiagramFactorId id) throws Exception
	{
		DiagramFactor node = rawGetFactorById(id);
		if(node == null)
			throw new Exception("Node doesn't exist, id: " + id);
		return node;
	}

	public DiagramFactor getDiagramFactorByWrappedId(FactorId id)
	{
		DiagramFactor node = rawGetFactorByWrappedId(id);
		if(node == null)
			EAM.logDebug("getDiagramFactorByWrappedId about to return null for: " + id);
		return node;
	}

	private DiagramFactor rawGetFactorById(DiagramFactorId id)
	{
		return cellInventory.getFactorById(id);
	}

	private DiagramFactor rawGetFactorByWrappedId(FactorId id)
	{
		return cellInventory.getFactorById(id);
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
	
	public boolean doesDiagramFactorExist(DiagramFactor factor)
	{
		return (cellInventory.getFactorById(factor.getDiagramFactorId()) != null);
	}

	public boolean doesDiagramFactorLinkExist(DiagramFactorLink link)
	{
		return (cellInventory.getFactorLinkById(link.getDiagramLinkageId()) != null);
	}

	public Vector getAllDiagramFactors()
	{
		return cellInventory.getAllFactors();
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
		EnhancedJsonObject jsonFactors = new EnhancedJsonObject();
		Vector factors = getAllDiagramFactors();
		for(int i=0; i < factors.size(); ++i)
		{
			DiagramFactor factor = (DiagramFactor)factors.get(i);
			jsonFactors.put(Integer.toString(factor.getDiagramFactorId().asInt()), factor.toJson());
		}
		EnhancedJsonObject json = new EnhancedJsonObject();
		json.put(TAG_TYPE, JSON_TYPE_DIAGRAM);
		json.put(TAG_FACTORS, jsonFactors);
		return json;
	}
	
	public void fillFrom(EnhancedJsonObject json) throws Exception
	{
		addFactorsToModel(json);
		addLinksToModel();
	}

	private void addFactorsToModel(EnhancedJsonObject json) throws Exception
	{
		EnhancedJsonObject jsonFactors = json.getJson(TAG_FACTORS);
		JSONArray keys = jsonFactors.names();
		
		// TODO: Really we should extend JSONObject to have a sane names() method
		// that returns an empty array if there are no names
		if(keys == null)
			return;
		for(int i=0; i < keys.length(); ++i)
		{
			String key = keys.getString(i);
			EnhancedJsonObject factorJson = jsonFactors.getJson(key);
			DiagramFactor factor = DiagramFactor.createFromJson(getProject(), factorJson);
			addFactorToModel(factor);
		}
		
		FactorId[] factorIds = getFactorPool().getModelNodeIds();
		for(int i = 0;i < factorIds.length; ++i)
		{
			FactorId factorId = factorIds[i];
			if(!doesFactorExist(factorId))
				addFactorToDiagram(factorId);
			try
			{
				DiagramFactor diagramFactor = getDiagramFactorByWrappedId(factorId);
				if(diagramFactor.isFactorCluster())
					addFactorsToCluster((DiagramFactorCluster)diagramFactor);
			}
			catch(Exception e)
			{
				EAM.logException(e);
				EAM.errorDialog("Errors detected in the project. " +
						"Continuing to load, but you may experience problems. " +
						"Please contact technical support.");
			}
		}
	}
	
	void addFactorToDiagram(FactorId factorId)
	{
		try
		{
			createDiagramFactor(factorId);
			String[] bodyLines = {"A factor was missing from the diagram. It has been added."};
			EAM.okDialog("Repairing Project", bodyLines);
		}
		catch(Exception e)
		{
			EAM.errorDialog("This project has some internal errors that could not be automatically repaired. " +
					"Please contact technical support.");
		}
	}
	
	void addFactorsToCluster(DiagramFactorCluster diagramCluster) throws Exception
	{
		FactorCluster cluster = (FactorCluster)diagramCluster.getUnderlyingObject();
		IdList members = cluster.getMemberIds();
		for(int i = 0; i < members.size(); ++i)
		{
			DiagramFactor memberFactor = getDiagramFactorByWrappedId((FactorId)members.get(i));
			project.addDiagramFactorToCluster(diagramCluster, memberFactor);
		}
	}
	
	public void addLinksToModel() throws Exception
	{
		FactorLinkId[] linkIds = getFactorLinkPool().getFactorLinkIds();
		for(int i = 0; i < linkIds.length; ++i)
		{
			FactorLink link = getFactorLinkPool().find(linkIds[i]);
			if(doesFactorExist(link.getFromFactorId()) && doesFactorExist(link.getToFactorId()))
			{
				createDiagramFactorLink(link);
			}
		}
	}

	public void updateProjectScopeBox()
	{
		String newText = getProject().getMetadata().getShortProjectScope();
		getProjectScopeBox().setText(EAM.text("Project Scope: " + newText));
		getProjectScopeBox().autoSurroundTargets();
	}

	
	public FactorPool getFactorPool()
	{
		return project.getFactorPool();
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
	
	public DiagramFactor[] getAllDiagramTargets()
	{
		Vector allTargets = new Vector();
		Vector allFactors = getAllDiagramFactors();
		for (int i = 0; i < allFactors.size(); i++)
		{
			DiagramFactor diagramFactor = (DiagramFactor)allFactors.get(i);
			if (diagramFactor.isTarget())
				allTargets.add(diagramFactor);
		}
		
		return (DiagramFactor[])allTargets.toArray(new DiagramFactor[0]);
	}
	
	
	private static final String TAG_TYPE = "Type";
	private static final String TAG_FACTORS = "Nodes";
	
	private static final String JSON_TYPE_DIAGRAM = "Diagram";
	
	Project project;
	CellInventory cellInventory;
	ProjectScopeBox projectScopeBox;
	protected List diagramModelListenerList = new ArrayList();
}

