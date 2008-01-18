/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram;

import java.util.HashSet;

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
	
	protected HashSet<Factor> getAllLinkedFactors(int direction)
	{
		HashSet<Factor> linkedFactors = new HashSet();
		HashSet<Factor> unprocessedFactors = new HashSet();
		linkedFactors.add(getStartingFactor());

		ORefList allDiagramLinkRefs = diagramObject.getAllDiagramLinkRefs();
		for(int i = 0; i < allDiagramLinkRefs.size(); ++i)
		{
			DiagramLink link = (DiagramLink)getProject().findObject(allDiagramLinkRefs.get(i));
			if(link.isGroupBoxLink())
				continue;
			unprocessedFactors.addAll(processLink(getStartingFactor(), link, direction));
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
					unprocessedFactors.addAll(processLink(thisFactor, link, direction));
				}
			}
			unprocessedFactors.remove(thisFactor);
		}
		
		return linkedFactors;
	}

	protected HashSet<Factor> getDirectlyLinkedFactors(int direction)
	{
		HashSet<Factor> results = new HashSet();
		results.add(getStartingFactor());
		
		ORefList allDiagramLinkRefs = diagramObject.getAllDiagramLinkRefs();
		for(int i = 0; i < allDiagramLinkRefs.size(); ++i)
		{
			DiagramLink link = (DiagramLink)getProject().findObject(allDiagramLinkRefs.get(i));
			results.addAll(processLink(getStartingFactor(), link, direction));
		}
		return results;
	}
	
	private void initializeChain(DiagramObject diagram, DiagramFactor diagramFactor)
	{
		diagramObject = diagram;
		setStartingFactor(diagramFactor);
		resultingFactors = new HashSet<Factor>();
		processedLinks = new HashSet();
	}
	
	private Project getProject()
	{
		return getStartingFactor().getProject();
	}
	
	private HashSet<Factor> processLink(Factor thisFactor, DiagramLink diagramLink, int direction)
	{
		HashSet<Factor> newFactorIfAny = new HashSet<Factor>();
		
		if(diagramLink.getDiagramFactor(direction).getWrappedORef().equals(thisFactor.getRef()))
		{
			processedLinks.add(diagramLink);
			Factor linkedNode = diagramLink.getOppositeDiagramFactor(direction).getWrappedFactor();
			newFactorIfAny.add(linkedNode);
			return newFactorIfAny;
		}
		
		if (!diagramLink.isBidirectional())
			return newFactorIfAny;
		
		if(diagramLink.getDiagramFactor(direction).getWrappedORef().equals(thisFactor.getRef()))
		{
			processedLinks.add(diagramLink);
			Factor linkedNode = diagramLink.getDiagramFactor(direction).getWrappedFactor();
			newFactorIfAny.add(linkedNode);
		}
		
		return newFactorIfAny;
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
		HashSet<FactorLink> links = new HashSet();
		for(DiagramLink link : processedLinks)
		{
			links.add(link.getUnderlyingLink());
		}
		return links.toArray(new FactorLink[0]);
	}
	
	private HashSet<Factor> getDirectlyLinkedDownstreamFactors()
	{
		return getDirectlyLinkedFactors(FactorLink.FROM);
	}
	
	private HashSet<Factor> getDirectlyLinkedUpstreamFactors()
	{
		return getDirectlyLinkedFactors(FactorLink.TO);
	}
	
	private HashSet<Factor> getAllUpstreamFactors()
	{
		return getAllLinkedFactors(FactorLink.TO);
	}
	
	private HashSet<Factor> getAllDownstreamFactors()
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
	private HashSet<DiagramLink> processedLinks;
	private DiagramFactor startingFactor;
}
