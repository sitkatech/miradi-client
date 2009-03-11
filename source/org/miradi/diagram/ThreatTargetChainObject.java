/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.diagram;

import java.util.HashSet;

import org.miradi.objecthelpers.FactorSet;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Factor;
import org.miradi.objects.FactorLink;
import org.miradi.project.Project;

//FIXME This class it in progress.  and might completely change. its goal is to find upstream threats for a target and down stream targets for a threat.
// it it is only being used by its test.  it is not working and should not be used
public class ThreatTargetChainObject
{
	public ThreatTargetChainObject(Project projectToUse)
	{
		project = projectToUse;
	}
	
	private void initializeChain(DiagramObject diagram, DiagramFactor diagramFactor)
	{
		diagramObject = diagram;
		setStartingFactor(diagramFactor);
		resultingThreats = new HashSet();
		resultingTargets = new HashSet();
		processedLinks = new HashSet();
	}
	
	public HashSet<DiagramFactor> getUpstreamThreatsFromTarget(DiagramObject diagramObjectToUse, DiagramFactor diagramFactor)
	{
		initializeChain(diagramObjectToUse, diagramFactor);
		buildUpstreamChain();
		return getDiagramFactors(resultingThreats);
	}
	
	public HashSet<DiagramFactor> getDownstreamTargetsFromThreat(DiagramObject diagramObjectToUse, DiagramFactor diagramFactor)
	{
		initializeChain(diagramObjectToUse, diagramFactor);
		buildDownstreamChain();
		return getDiagramFactors(resultingTargets);
	}
	
	private void buildChain(int direction)
	{
		HashSet<Factor> linkedFactors = new HashSet();
		HashSet<Factor> unprocessedFactors = new HashSet();
		linkedFactors.add(getStartingFactor());
		
		ORefList allDiagramLinkRefs = diagramObject.getAllDiagramLinkRefs();
		unprocessedFactors.addAll(getFactorsToProcess(direction, allDiagramLinkRefs, getStartingFactor()));
		
		while(unprocessedFactors.size() > 0)
		{
			Factor factorToProcess = (Factor)unprocessedFactors.toArray()[0];
			if (!linkedFactors.contains(factorToProcess))
			{
				linkedFactors.add(factorToProcess);
				unprocessedFactors.addAll(getFactorsToProcess(direction, allDiagramLinkRefs, factorToProcess));
			}
			unprocessedFactors.remove(factorToProcess);
		}
	}

	private HashSet<Factor> getFactorsToProcess(int direction, ORefList allDiagramLinkRefs, Factor factorToProcess)
	{
		HashSet<Factor> unprocessedFactors = new HashSet();
		for(int i = 0; i < allDiagramLinkRefs.size(); ++i)
		{
			DiagramLink link = (DiagramLink)getProject().findObject(allDiagramLinkRefs.get(i));
			Factor thisFactorToProcess = processLink(factorToProcess, link, direction);
			if (thisFactorToProcess == null)
			{
				continue;
			}
			
			if (thisFactorToProcess.isDirectThreat())
			{
				resultingThreats.add(thisFactorToProcess);
				
				if(FactorLink.isFrom(direction))
					unprocessedFactors.add(thisFactorToProcess);
			}
			else if (thisFactorToProcess.isTarget())
			{
				resultingTargets.add(thisFactorToProcess);
				unprocessedFactors.add(thisFactorToProcess);
			}
			else
			{
				unprocessedFactors.add(thisFactorToProcess);
			}
		}
		
		return unprocessedFactors;
	}

	private Factor processLink(Factor thisFactor, DiagramLink diagramLink, int direction)
	{
		if(diagramLink.getDiagramFactor(direction).getWrappedORef().equals(thisFactor.getRef()))
		{
			processedLinks.add(diagramLink);	
			Factor wrappedFactor = diagramLink.getOppositeDiagramFactor(direction).getWrappedFactor();
			return wrappedFactor;
		}
		
		if (diagramLink.isBidirectional() && diagramLink.getOppositeDiagramFactor(direction).getWrappedORef().equals(thisFactor.getRef()))
		{
			processedLinks.add(diagramLink);
			return diagramLink.getDiagramFactor(direction).getWrappedFactor();
		}
		
		return null;
	}
	
	private FactorSet getFactors(HashSet<Factor> factors)
	{
		FactorSet factorSet = new FactorSet();
		for(Factor factor : factors)
		{
			factorSet.attemptToAdd(factor);
		}
		return factorSet;
	}
	
	private HashSet<DiagramFactor> getDiagramFactors(HashSet<Factor> factors)
	{
		HashSet<DiagramFactor> diagramFactors = new HashSet();
 		FactorSet processedFactors = getFactors(factors);
 		for(Factor factor : processedFactors)
		{
			DiagramFactor diagramFactor = diagramObject.getDiagramFactor(factor.getRef());
			diagramFactors.add(diagramFactor);
		}
 		
 		return diagramFactors;
 		
	}

	private void  buildUpstreamChain()
	{
		buildChain(FactorLink.TO);
	}
		
	private void buildDownstreamChain()
	{
		buildChain(FactorLink.FROM);
	}
	
	private void setStartingFactor(DiagramFactor startingFactorToUse)
	{
		startingFactor = startingFactorToUse;
	}

	private Factor getStartingFactor()
	{
		return startingFactor.getWrappedFactor();
	}
	
	private Project getProject()
	{
		return project;
	}
	
	private DiagramObject diagramObject;
	private HashSet<Factor> resultingThreats;
	private HashSet<Factor> resultingTargets;
	private HashSet<DiagramLink> processedLinks;
	private DiagramFactor startingFactor;
	private Project project;
}
