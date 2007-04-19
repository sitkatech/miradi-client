/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram;

import java.util.Vector;

import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.objecthelpers.FactorSet;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.DiagramFactorLink;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.project.Project;

// TODO: Hopefully this can be eliminated when everyone 
// is using ProjectChainObject instead
public class DiagramChainObject
{
	public FactorSet getFactors()
	{
		return factorSet;
	}
	
	public Factor[] getFactorsArray()
	{	
		Factor[] cmNodes = (Factor[])factorSet.toArray(new Factor[0]);
		return cmNodes;
	}
	
	public FactorLink[] getFactorLinksArray()
	{
		FactorLink[] cmLinks = (FactorLink[])processedLinks.toArray(new FactorLink[0]);
		return cmLinks;
	} 
		
	public void buildDirectThreatChain(DiagramModel model, Factor factor)
	{
		initializeChain(model, factor);
		if(startingFactor.isDirectThreat())
		{
			factorSet.attemptToAddAll(getDirectlyLinkedDownstreamFactors());
			factorSet.attemptToAddAll(getAllUpstreamFactors());
		}
	}

	public void buildNormalChain(DiagramModel model, Factor factor)
	{
		initializeChain(model, factor);
		if (startingFactor.isDirectThreat())
			buildDirectThreatChain(model, factor);
		else
			buildUpstreamDownstreamChain(model, factor);
	}
	
	public void buildUpstreamDownstreamChain(DiagramModel model, Factor factor)
	{
		initializeChain(model, factor);
		factorSet.attemptToAddAll(getAllDownstreamFactors());
		factorSet.attemptToAddAll(getAllUpstreamFactors());
	}
	
	public void buildUpstreamChain(DiagramModel model, Factor factor)
	{
		initializeChain(model, factor);
		factorSet.attemptToAddAll(getAllUpstreamFactors());
	}
	
	public void buildDownstreamChain(DiagramModel model, Factor factor)
	{
		initializeChain(model, factor);
		factorSet.attemptToAddAll(getAllDownstreamFactors());
	}
	
	public void buidDirectlyLinkedDownstreamChain(DiagramModel model, Factor factor)
	{
		initializeChain(model, factor);
		factorSet.attemptToAddAll(getDirectlyLinkedDownstreamFactors());
	}
	
	public void buildDirectlyLinkedUpstreamChain(DiagramModel model, Factor factor)
	{
		initializeChain(model, factor);
		factorSet.attemptToAddAll(getDirectlyLinkedUpstreamFactors());
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
	
	private FactorSet getAllLinkedFactors(int direction)
	{
		FactorSet linkedFactors = new FactorSet();
		FactorSet unprocessedFactors = new FactorSet();
		linkedFactors.attemptToAdd(startingFactor);
		DiagramFactorLink[] allDiagramLinks = diagramModel.getAllDiagramLinksAsArray();
		
		for(int i = 0; i < allDiagramLinks.length; ++i)
		{
			FactorLinkId wrappedId = allDiagramLinks[i].getWrappedId();
			FactorLink thisLink = (FactorLink) getProject().findObject(new ORef(ObjectType.FACTOR_LINK, wrappedId));
			if(thisLink.getNodeId(direction).equals(startingFactor.getId()))
			{
				attempToAdd(thisLink);
				Factor linkedFactor = getProject().findNode(thisLink.getOppositeNodeId(direction));
				unprocessedFactors.attemptToAdd(linkedFactor);
			}
		}		
		
		while(unprocessedFactors.size() > 0)
		{
			Factor thisFactor = (Factor)unprocessedFactors.toArray()[0];
			linkedFactors.attemptToAdd(thisFactor);
			for(int i = 0; i < allDiagramLinks.length; ++i)
			{
				FactorLinkId wrappedId = allDiagramLinks[i].getWrappedId();
				FactorLink thisLink = (FactorLink) getProject().findObject(new ORef(ObjectType.FACTOR_LINK, wrappedId));
				if(thisLink.getNodeId(direction).equals(thisFactor.getId()))
				{
					attempToAdd(thisLink);
					Factor linkedNode = getProject().findNode(thisLink.getOppositeNodeId(direction));
					unprocessedFactors.attemptToAdd(linkedNode);
				}
			}
			
			unprocessedFactors.remove(thisFactor);
		}
		
		return linkedFactors;
	}

	private Project getProject()
	{
		return diagramModel.getProject();
	}

	private FactorSet getDirectlyLinkedFactors(int direction)
	{
		FactorSet results = new FactorSet();
		results.attemptToAdd(startingFactor);
		
		DiagramFactorLink[] allDiagramLinks = diagramModel.getAllDiagramLinksAsArray();
		for(int i = 0; i < allDiagramLinks.length; ++i)
		{
			FactorLinkId wrappedId = allDiagramLinks[i].getWrappedId();
			FactorLink thisLink = (FactorLink) getProject().findObject(new ORef(ObjectType.FACTOR_LINK, wrappedId));
			if(thisLink.getNodeId(direction).equals(startingFactor.getId()))
			{
				attempToAdd(thisLink);
				FactorId downstreamFactorId = thisLink.getOppositeNodeId(direction);
				Factor downstreamFactor = getProject().findNode(downstreamFactorId);
				results.attemptToAdd(downstreamFactor);
			}
		}
		return results;
	}
	
	private void initializeChain(DiagramModel model, Factor factor)
	{
		this.diagramModel = model;
		this.startingFactor = factor;
		factorSet = new FactorSet();
		processedLinks = new Vector();
	}
	
	private void attempToAdd(FactorLink thisLinkage)
	{
		if (!processedLinks.contains(thisLinkage))
			processedLinks.add(thisLinkage);
	}

	private FactorSet factorSet;
	private Factor startingFactor;
	private DiagramModel diagramModel;
	private Vector processedLinks;
}
