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
package org.miradi.project;

import java.util.Vector;

import org.miradi.objecthelpers.FactorSet;
import org.miradi.objects.Factor;
import org.miradi.objects.FactorLink;


abstract public class ChainObject
{	
	abstract protected FactorSet getAllLinkedFactors(int direction);
	abstract protected FactorSet getDirectlyLinkedFactors(int direction);


	protected FactorSet getFactors()
	{
		return factorSet;
	}

	protected FactorLink[] getFactorLinksArray()
	{
		return (FactorLink[])processedLinks.toArray(new FactorLink[0]);
	}
	
	protected FactorSet getDirectlyLinkedDownstreamFactors()
	{
		return getDirectlyLinkedFactors(FactorLink.FROM);
	}
	
	protected FactorSet getDirectlyLinkedUpstreamFactors()
	{
		return getDirectlyLinkedFactors(FactorLink.TO);
	}
	
	protected FactorSet getAllUpstreamFactors()
	{
		return getAllLinkedFactors(FactorLink.TO);
	}
	
	protected FactorSet getAllDownstreamFactors()
	{
		return getAllLinkedFactors(FactorLink.FROM);
	}
	
	protected Project getProject()
	{
		return startingFactor.getProject();
	}
	
	protected void attempToAdd(FactorLink thisLinkage)
	{
		if (!processedLinks.contains(thisLinkage))
			processedLinks.add(thisLinkage);
	}
	
	protected FactorSet processLink(Factor thisFactor, FactorLink thisLink, int direction)
	{
		FactorSet newFactorIfAny = new FactorSet();
		if(thisLink.getFactorRef(direction).equals(thisFactor.getRef()))
		{
			attempToAdd(thisLink);
			Factor linkedNode = (Factor) getProject().findObject(thisLink.getOppositeFactorRef(direction));
			newFactorIfAny.attemptToAdd(linkedNode);
			return newFactorIfAny;
		}
		
		if (!thisLink.isBidirectional())
			return newFactorIfAny;
		
		if(thisLink.getOppositeFactorRef(direction).equals(thisFactor.getRef()))
		{
			attempToAdd(thisLink);
			Factor linkedNode = (Factor) getProject().findObject(thisLink.getFactorRef(direction));
			newFactorIfAny.attemptToAdd(linkedNode);
		}
		
		return newFactorIfAny;
	}
	
	protected FactorSet factorSet;
	protected Vector processedLinks;
	protected Factor startingFactor;
}
