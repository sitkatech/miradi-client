/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram;

import java.util.HashSet;
import java.util.Vector;

import org.conservationmeasures.eam.objecthelpers.FactorSet;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.DiagramLink;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.project.Project;

//TODO: If the methods took a diagram factor instead of a 
//factor we shold be able to get to both the model and 
//factor so that many of these methods could be moved to the super
public class DiagramChainObject
{
	public FactorLink[] buildNormalChainAndGetFactorLinks(DiagramModel model, DiagramFactor diagramFactor)
	{
		buildNormalChain(model, diagramFactor);
		return getFactorLinksArray();
	}
	
	public FactorSet buildNormalChainAndGetFactors(DiagramModel model, DiagramFactor diagramFactor)
	{
		buildNormalChain(model, diagramFactor);
		return getFactors();
	}
	
	public FactorSet buildUpstreamChainAndGetFactors(DiagramModel model, DiagramFactor diagramFactor)
	{
		return buildUpstreamChainAndGetFactors(model.getDiagramObject(), diagramFactor);
	}

	public FactorSet buildUpstreamChainAndGetFactors(DiagramObject diagram, DiagramFactor diagramFactor)
	{
		buildUpstreamChain(diagram, diagramFactor);
		return getFactors();
	}
	
	public FactorSet buildDownstreamChainAndGetFactors(DiagramModel model, DiagramFactor diagramFactor)
	{
		buildDownstreamChain(model, diagramFactor);
		return getFactors();
	}
	
	public FactorSet buildUpstreamDownstreamChainAndGetFactors(DiagramModel model, DiagramFactor diagramFactor)
	{
		buildUpstreamDownstreamChain(model, diagramFactor);
		return getFactors();
	}
	
	public FactorSet buildDirectlyLinkedUpstreamChainAndGetFactors(DiagramModel model, DiagramFactor diagramFactor)
	{
		buildDirectlyLinkedUpstreamChain(model, diagramFactor);
		return getFactors();
	}
	
	public FactorSet buildDirectThreatChainAndGetFactors(DiagramModel model, DiagramFactor diagramFactor)
	{
		buildDirectThreatChain(model, diagramFactor);
		return getFactors();
	}
	
	
	private void buildDirectThreatChain(DiagramModel model, DiagramFactor diagramFactor)
	{
		DiagramObject diagram = model.getDiagramObject();
		
		initializeChain(diagram, diagramFactor);
		if(getStartingFactor().isDirectThreat())
		{
			resultingFactors.addAll(getDirectlyLinkedDownstreamFactors());
			resultingFactors.addAll(getAllUpstreamFactors());
		}
	}

	private void buildNormalChain(DiagramModel model, DiagramFactor diagramFactor)
	{
		DiagramObject diagram = model.getDiagramObject();
		
		initializeChain(diagram, diagramFactor);
		if (getStartingFactor().isDirectThreat())
			buildDirectThreatChain(model, diagramFactor);
		else
			buildUpstreamDownstreamChain(model, diagramFactor);
	}
	
	private void buildUpstreamDownstreamChain(DiagramModel model, DiagramFactor diagramFactor)
	{
		DiagramObject diagram = model.getDiagramObject();
		
		initializeChain(diagram, diagramFactor);
		resultingFactors.addAll(getAllDownstreamFactors());
		resultingFactors.addAll(getAllUpstreamFactors());
	}
	
	private void buildUpstreamChain(DiagramObject diagram, DiagramFactor diagramFactor)
	{
		initializeChain(diagram, diagramFactor);
		resultingFactors.addAll(getAllUpstreamFactors());
	}
	
	private void buildDownstreamChain(DiagramModel model, DiagramFactor diagramFactor)
	{
		DiagramObject diagram = model.getDiagramObject();
		
		initializeChain(diagram, diagramFactor);
		resultingFactors.addAll(getAllDownstreamFactors());
	}
	
	private void buildDirectlyLinkedUpstreamChain(DiagramModel model, DiagramFactor diagramFactor)
	{
		DiagramObject diagram = model.getDiagramObject();
		
		initializeChain(diagram, diagramFactor);
		resultingFactors.addAll(getDirectlyLinkedUpstreamFactors());
	}
	
	protected Vector<Factor> getAllLinkedFactors(int direction)
	{
		Vector<Factor> linkedFactors = new Vector();
		Vector<Factor> unprocessedFactors = new Vector();
		linkedFactors.add(getStartingFactor());

		ORefList allDiagramLinkRefs = diagramObject.getAllDiagramLinkRefs();
		for(int i = 0; i < allDiagramLinkRefs.size(); ++i)
		{
			DiagramLink link = (DiagramLink)getProject().findObject(allDiagramLinkRefs.get(i));
			if(link.isGroupBoxLink())
				continue;
			processLink(unprocessedFactors, getStartingFactor(), link, direction);
		}		
		
		while(unprocessedFactors.size() > 0)
		{
			Factor thisFactor = (Factor)unprocessedFactors.toArray()[0];
			if (!linkedFactors.contains(thisFactor))
			{
				linkedFactors.add(thisFactor);
				for(int i = 0; i < allDiagramLinkRefs.size(); ++i)
				{
					DiagramLink link = (DiagramLink)getProject().findObject(allDiagramLinkRefs.get(i));
					if(link.isGroupBoxLink())
						continue;
					processLink(unprocessedFactors, thisFactor, link, direction);
				}
			}
			unprocessedFactors.remove(thisFactor);
		}
		
		return linkedFactors;
	}

	protected Vector<Factor> getDirectlyLinkedFactors(int direction)
	{
		Vector<Factor> results = new Vector();
		results.add(getStartingFactor());
		
		ORefList allDiagramLinkRefs = diagramObject.getAllDiagramLinkRefs();
		for(int i = 0; i < allDiagramLinkRefs.size(); ++i)
		{
			DiagramLink link = (DiagramLink)getProject().findObject(allDiagramLinkRefs.get(i));
			processLink(results, getStartingFactor(), link, direction);
		}
		return results;
	}
	
	private void initializeChain(DiagramObject diagram, DiagramFactor diagramFactor)
	{
		diagramObject = diagram;
		setStartingFactor(diagramFactor);
		resultingFactors = new HashSet<Factor>();
		processedLinks = new Vector();
	}
	
	private Project getProject()
	{
		return getStartingFactor().getProject();
	}
	
	private void processLink(Vector<Factor> unprocessedFactors, Factor thisFactor, DiagramLink diagramLink, int direction)
	{
		FactorLink thisLink = diagramLink.getUnderlyingLink();
		if(thisLink.getFactorRef(direction).equals(thisFactor.getRef()))
		{
			attempToAdd(thisLink);
			Factor linkedNode = (Factor) getProject().findObject(thisLink.getOppositeFactorRef(direction));
			unprocessedFactors.add(linkedNode);
			return;
		}
		
		if (!thisLink.isBidirectional())
			return;
		
		if(thisLink.getOppositeFactorRef(direction).equals(thisFactor.getRef()))
		{
			attempToAdd(thisLink);
			Factor linkedNode = (Factor) getProject().findObject(thisLink.getFactorRef(direction));
			unprocessedFactors.add(linkedNode);
		}
	}
	
	private void attempToAdd(FactorLink thisLinkage)
	{
		if (!processedLinks.contains(thisLinkage))
			processedLinks.add(thisLinkage);
	}
	
	private FactorSet getFactors()
	{
		FactorSet factorSet = new FactorSet();
		for(Factor factor : resultingFactors)
		{
			factorSet.attemptToAdd(factor);
		}
		return factorSet;
	}

	private FactorLink[] getFactorLinksArray()
	{
		return processedLinks.toArray(new FactorLink[0]);
	}
	
	private Vector<Factor> getDirectlyLinkedDownstreamFactors()
	{
		return getDirectlyLinkedFactors(FactorLink.FROM);
	}
	
	private Vector<Factor> getDirectlyLinkedUpstreamFactors()
	{
		return getDirectlyLinkedFactors(FactorLink.TO);
	}
	
	private Vector<Factor> getAllUpstreamFactors()
	{
		return getAllLinkedFactors(FactorLink.TO);
	}
	
	private Vector<Factor> getAllDownstreamFactors()
	{
		return getAllLinkedFactors(FactorLink.FROM);
	}
	
	private void setStartingFactor(DiagramFactor startingFactor)
	{
		this.startingFactor = startingFactor;
	}

	private Factor getStartingFactor()
	{
		return startingFactor.getWrappedFactor();
	}

	private DiagramObject diagramObject;
	private HashSet<Factor> resultingFactors;
	private Vector<FactorLink> processedLinks;
	private DiagramFactor startingFactor;
}
