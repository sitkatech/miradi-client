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
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.DiagramLink;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.project.ChainObject;

//TODO: If the methods took a diagram factor instead of a 
//factor we shold be able to get to both the model and 
//factor so that many of these methods could be moved to the super
public class DiagramChainObject extends ChainObject
{
	public FactorLink[] buildNormalChainAndGetFactorLinks(DiagramModel model, Factor factor)
	{
		buildNormalChain(model, factor);
		return getFactorLinksArray();
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
	
	protected FactorSet getAllLinkedFactors(int direction)
	{
		FactorSet linkedFactors = new FactorSet();
		FactorSet unprocessedFactors = new FactorSet();
		linkedFactors.attemptToAdd(startingFactor);
		DiagramLink[] allDiagramLinks = diagramModel.getAllDiagramLinksAsArray();
		
		for(int i = 0; i < allDiagramLinks.length; ++i)
		{
			FactorLinkId wrappedId = allDiagramLinks[i].getWrappedId();
			FactorLink thisLink = (FactorLink) getProject().findObject(new ORef(ObjectType.FACTOR_LINK, wrappedId));
			processLink(unprocessedFactors, startingFactor, thisLink, direction);
		}		
		
		while(unprocessedFactors.size() > 0)
		{
			Factor thisFactor = (Factor)unprocessedFactors.toArray()[0];
			if (!linkedFactors.contains(thisFactor))
			{
				linkedFactors.attemptToAdd(thisFactor);
				for(int i = 0; i < allDiagramLinks.length; ++i)
				{
					FactorLinkId wrappedId = allDiagramLinks[i].getWrappedId();
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
		
		DiagramLink[] allDiagramLinks = diagramModel.getAllDiagramLinksAsArray();
		for(int i = 0; i < allDiagramLinks.length; ++i)
		{
			FactorLinkId wrappedId = allDiagramLinks[i].getWrappedId();
			FactorLink thisLink = (FactorLink) getProject().findObject(new ORef(ObjectType.FACTOR_LINK, wrappedId));
			processLink(results, startingFactor, thisLink, direction);
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
	
	private DiagramModel diagramModel;
}
