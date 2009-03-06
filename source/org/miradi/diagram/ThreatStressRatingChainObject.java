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
public class ThreatStressRatingChainObject
{
	public ThreatStressRatingChainObject(Project projectToUse)
	{
		project = projectToUse;
	}
	
	private void initializeChain(DiagramObject diagram, DiagramFactor diagramFactor)
	{
		diagramObject = diagram;
		setStartingFactor(diagramFactor);
		resultingFactors = new HashSet<Factor>();
		processedLinks = new HashSet();
	}
	
	public HashSet<DiagramFactor> upstreamThreatsFromTarget(DiagramObject diagramObjectToUse, DiagramFactor diagramFactor)
	{
		initializeChain(diagramObjectToUse, diagramFactor);
		resultingFactors.addAll(getAllUpstreamFactors());

		return getDiagramFactors();
	}
	
	private HashSet<Factor> getAllLinkedFactors(int direction)
	{
		HashSet<Factor> linkedFactors = new HashSet();
		HashSet<Factor> unprocessedFactors = new HashSet();
		linkedFactors.add(getStartingFactor());
		HashSet<Factor> foundFactors = new HashSet();

		ORefList allDiagramLinkRefs = diagramObject.getAllDiagramLinkRefs();
		unprocessedFactors.addAll(getFactorsToProcess(direction, foundFactors, allDiagramLinkRefs, getStartingFactor()));
		
		while(unprocessedFactors.size() > 0)
		{
			Factor factorToProcess = (Factor)unprocessedFactors.toArray()[0];
			if (!linkedFactors.contains(factorToProcess))
			{
				linkedFactors.add(factorToProcess);
				unprocessedFactors.addAll(getFactorsToProcess(direction, foundFactors, allDiagramLinkRefs, factorToProcess));
			}
			unprocessedFactors.remove(factorToProcess);
		}
		
		return foundFactors;
	}

	private HashSet<Factor> getFactorsToProcess(int direction, HashSet<Factor> foundFactors, ORefList allDiagramLinkRefs, Factor factorToProcess)
	{
		HashSet<Factor> unprocessedFactors = new HashSet();
		for(int i = 0; i < allDiagramLinkRefs.size(); ++i)
		{
			DiagramLink link = (DiagramLink)getProject().findObject(allDiagramLinkRefs.get(i));
			Factor thisFactorToProcess = processLink(factorToProcess, link, direction);
			if (thisFactorToProcess == null)
				continue;
			
			if (thisFactorToProcess.isCause())
				foundFactors.add(thisFactorToProcess);
			else
				unprocessedFactors.add(thisFactorToProcess);
		}
		
		return unprocessedFactors;
	}

	private Factor processLink(Factor thisFactor, DiagramLink diagramLink, int direction)
	{
		if(diagramLink.getDiagramFactor(direction).getWrappedORef().equals(thisFactor.getRef()))
		{
			processedLinks.add(diagramLink);	
			return diagramLink.getOppositeDiagramFactor(direction).getWrappedFactor();
		}
		
		if (diagramLink.isBidirectional() && diagramLink.getOppositeDiagramFactor(direction).getWrappedORef().equals(thisFactor.getRef()))
		{
			processedLinks.add(diagramLink);
			return diagramLink.getDiagramFactor(direction).getWrappedFactor();
		}
		
		return null;
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
	
	private HashSet<DiagramFactor> getDiagramFactors()
	{
		HashSet<DiagramFactor> diagramFactors = new HashSet();
 		FactorSet processedFactors = getFactors();
 		for(Factor factor : processedFactors)
		{
			DiagramFactor diagramFactor = diagramObject.getDiagramFactor(factor.getRef());
			diagramFactors.add(diagramFactor);
		}
 		
 		return diagramFactors;
 		
	}

	private HashSet<Factor> getAllUpstreamFactors()
	{
		return getAllLinkedFactors(FactorLink.TO);
	}
	
//TODO this method will be used but currently its commented to avoid unused method warnings	
//	private HashSet<Factor> getAllDownstreamFactors()
//	{
//		return getAllLinkedFactors(FactorLink.FROM);
//	}
	
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
	private HashSet<Factor> resultingFactors;
	private HashSet<DiagramLink> processedLinks;
	private DiagramFactor startingFactor;
	private Project project;
}
