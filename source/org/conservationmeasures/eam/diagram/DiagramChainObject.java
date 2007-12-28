/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram;

import java.util.Vector;

import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.objecthelpers.FactorSet;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
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
		buildNormalChain(model, diagramFactor.getWrappedFactor());
		return getFactorLinksArray();
	}
	
	public FactorSet buildNormalChainAndGetFactors(DiagramModel model, Factor factor)
	{
		buildNormalChain(model, factor);
		return getFactors();
	}
	
	public FactorSet buildUpstreamChainAndGetFactors(DiagramModel model, DiagramFactor diagramFactor)
	{
		return buildUpstreamChainAndGetFactors(model.getDiagramObject(), diagramFactor);
	}

	public FactorSet buildUpstreamChainAndGetFactors(DiagramObject diagram, DiagramFactor diagramFactor)
	{
		buildUpstreamChain(diagram, diagramFactor.getWrappedFactor());
		return getFactors();
	}
	
	public FactorSet buildDownstreamChainAndGetFactors(DiagramModel model, DiagramFactor diagramFactor)
	{
		buildDownstreamChain(model, diagramFactor.getWrappedFactor());
		return getFactors();
	}
	
	public FactorSet buildUpstreamDownstreamChainAndGetFactors(DiagramModel model, DiagramFactor diagramFactor)
	{
		buildUpstreamDownstreamChain(model, diagramFactor.getWrappedFactor());
		return getFactors();
	}
	
	public FactorSet buildDirectlyLinkedUpstreamChainAndGetFactors(DiagramModel model, DiagramFactor diagramFactor)
	{
		buildDirectlyLinkedUpstreamChain(model, diagramFactor.getWrappedFactor());
		return getFactors();
	}
	
	public FactorSet buildDirectThreatChainAndGetFactors(DiagramModel model, DiagramFactor diagramFactor)
	{
		buildDirectThreatChain(model, diagramFactor.getWrappedFactor());
		return getFactors();
	}
	
	
	private void buildDirectThreatChain(DiagramModel model, Factor factor)
	{
		DiagramObject diagram = model.getDiagramObject();
		
		initializeChain(diagram, factor);
		if(startingFactor.isDirectThreat())
		{
			factorSet.attemptToAddAll(getDirectlyLinkedDownstreamFactors());
			factorSet.attemptToAddAll(getAllUpstreamFactors());
		}
	}

	private void buildNormalChain(DiagramModel model, Factor factor)
	{
		DiagramObject diagram = model.getDiagramObject();
		
		initializeChain(diagram, factor);
		if (startingFactor.isDirectThreat())
			buildDirectThreatChain(model, factor);
		else
			buildUpstreamDownstreamChain(model, factor);
	}
	
	private void buildUpstreamDownstreamChain(DiagramModel model, Factor factor)
	{
		DiagramObject diagram = model.getDiagramObject();
		
		initializeChain(diagram, factor);
		factorSet.attemptToAddAll(getAllDownstreamFactors());
		factorSet.attemptToAddAll(getAllUpstreamFactors());
	}
	
	private void buildUpstreamChain(DiagramObject diagram, Factor factor)
	{
		initializeChain(diagram, factor);
		factorSet.attemptToAddAll(getAllUpstreamFactors());
	}
	
	private void buildDownstreamChain(DiagramModel model, Factor factor)
	{
		DiagramObject diagram = model.getDiagramObject();
		
		initializeChain(diagram, factor);
		factorSet.attemptToAddAll(getAllDownstreamFactors());
	}
	
	private void buildDirectlyLinkedUpstreamChain(DiagramModel model, Factor factor)
	{
		DiagramObject diagram = model.getDiagramObject();
		
		initializeChain(diagram, factor);
		factorSet.attemptToAddAll(getDirectlyLinkedUpstreamFactors());
	}
	
	protected FactorSet getAllLinkedFactors(int direction)
	{
		FactorSet linkedFactors = new FactorSet();
		FactorSet unprocessedFactors = new FactorSet();
		linkedFactors.attemptToAdd(startingFactor);

		ORefList allDiagramLinkRefs = diagramObject.getAllDiagramLinkRefs();
		for(int i = 0; i < allDiagramLinkRefs.size(); ++i)
		{
			DiagramLink link = (DiagramLink)getProject().findObject(allDiagramLinkRefs.get(i));
			if(link.isGroupBoxLink())
				continue;
			FactorLinkId wrappedId = link.getWrappedId();
			FactorLink thisLink = (FactorLink) getProject().findObject(new ORef(ObjectType.FACTOR_LINK, wrappedId));
			processLink(unprocessedFactors, startingFactor, thisLink, direction);
		}		
		
		while(unprocessedFactors.size() > 0)
		{
			Factor thisFactor = (Factor)unprocessedFactors.toArray()[0];
			if (!linkedFactors.contains(thisFactor))
			{
				linkedFactors.attemptToAdd(thisFactor);
				for(int i = 0; i < allDiagramLinkRefs.size(); ++i)
				{
					DiagramLink link = (DiagramLink)getProject().findObject(allDiagramLinkRefs.get(i));
					if(link.isGroupBoxLink())
						continue;
					FactorLinkId wrappedId = link.getWrappedId();
					FactorLink thisLink = (FactorLink) getProject().findObject(new ORef(ObjectType.FACTOR_LINK, wrappedId));
					processLink(unprocessedFactors, thisFactor, thisLink, direction);
				}
			}
			unprocessedFactors.remove(thisFactor);
		}
		
		return linkedFactors;
	}

	protected FactorSet getDirectlyLinkedFactors(int direction)
	{
		FactorSet results = new FactorSet();
		results.attemptToAdd(startingFactor);
		
		ORefList allDiagramLinkRefs = diagramObject.getAllDiagramLinkRefs();
		for(int i = 0; i < allDiagramLinkRefs.size(); ++i)
		{
			DiagramLink link = (DiagramLink)getProject().findObject(allDiagramLinkRefs.get(i));
			FactorLinkId wrappedId = link.getWrappedId();
			FactorLink thisLink = (FactorLink) getProject().findObject(new ORef(ObjectType.FACTOR_LINK, wrappedId));
			processLink(results, startingFactor, thisLink, direction);
		}
		return results;
	}
	
	private void initializeChain(DiagramObject diagram, Factor factor)
	{
		diagramObject = diagram;
		this.startingFactor = factor;
		factorSet = new FactorSet();
		processedLinks = new Vector();
	}
	
	private Project getProject()
	{
		return startingFactor.getProject();
	}
	
	private void processLink(FactorSet unprocessedFactors, Factor thisFactor, FactorLink thisLink, int direction)
	{
		if(thisLink.getFactorRef(direction).equals(thisFactor.getRef()))
		{
			attempToAdd(thisLink);
			Factor linkedNode = (Factor) getProject().findObject(thisLink.getOppositeFactorRef(direction));
			unprocessedFactors.attemptToAdd(linkedNode);
			return;
		}
		
		if (!thisLink.isBidirectional())
			return;
		
		if(thisLink.getOppositeFactorRef(direction).equals(thisFactor.getRef()))
		{
			attempToAdd(thisLink);
			Factor linkedNode = (Factor) getProject().findObject(thisLink.getFactorRef(direction));
			unprocessedFactors.attemptToAdd(linkedNode);
		}
	}
	
	private void attempToAdd(FactorLink thisLinkage)
	{
		if (!processedLinks.contains(thisLinkage))
			processedLinks.add(thisLinkage);
	}
	
	private FactorSet getFactors()
	{
		return factorSet;
	}

	private FactorLink[] getFactorLinksArray()
	{
		return (FactorLink[])processedLinks.toArray(new FactorLink[0]);
	}
	
	private FactorSet getDirectlyLinkedDownstreamFactors()
	{
		return getDirectlyLinkedFactors(FactorLink.FROM);
	}
	
	private FactorSet getDirectlyLinkedUpstreamFactors()
	{
		return getDirectlyLinkedFactors(FactorLink.TO);
	}
	
	private FactorSet getAllUpstreamFactors()
	{
		return getAllLinkedFactors(FactorLink.TO);
	}
	
	private FactorSet getAllDownstreamFactors()
	{
		return getAllLinkedFactors(FactorLink.FROM);
	}

	private DiagramObject diagramObject;
	private FactorSet factorSet;
	private Vector processedLinks;
	private Factor startingFactor;
}
