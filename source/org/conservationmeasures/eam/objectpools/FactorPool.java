/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectpools;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Vector;

import org.conservationmeasures.eam.diagram.factortypes.FactorType;
import org.conservationmeasures.eam.diagram.factortypes.FactorTypeStrategy;
import org.conservationmeasures.eam.diagram.factortypes.FactorTypeTarget;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Factor;

public class FactorPool extends PoolWithIdAssigner
{
	public FactorPool(IdAssigner idAssignerToUse)
	{
		super(ObjectType.FACTOR, idAssignerToUse);
	}
	
	public void put(Factor node)
	{
		put(node.getFactorId(), node);
	}
	
	public Factor find(FactorId id)
	{
		return (Factor)getRawObject(id);
	}
	
	public void remove(FactorId id)
	{
		super.remove(id);
	}

	public FactorId[] getModelNodeIds()
	{
		return (FactorId[])new HashSet(getRawIds()).toArray(new FactorId[0]);
	}
	
	public IdList getInterventionIds()
	{
		Factor[] cmNodeList = getStrategies();
		IdList interventionIds = new IdList();
		for (int i = 0; i < cmNodeList.length; i++)
			interventionIds.add(cmNodeList[i].getId());
		
		return interventionIds;
	}

	public Factor[] getStrategies()
	{
		return getNodesOfType(new FactorTypeStrategy());
	}
	
	public Factor[] getDirectThreats()
	{
		Vector cmNodes = new Vector();
		FactorId[] ids = getModelNodeIds();
		Arrays.sort(ids);
		for(int i = 0; i < ids.length; ++i)
		{
			Factor cmNode = (Factor)getRawObject(ids[i]);
			if(cmNode.isDirectThreat())
				cmNodes.add(cmNode);
		}
		return (Factor[])cmNodes.toArray(new Factor[0]);
	}

	public Factor[] getTargets()
	{
		return getNodesOfType(new FactorTypeTarget());
	}

	private Factor[] getNodesOfType(FactorType type)
	{
		Vector cmNodes = new Vector();
		FactorId[] ids = getModelNodeIds();
		Arrays.sort(ids);
		for(int i = 0; i < ids.length; ++i)
		{
			Factor cmNode = (Factor)getRawObject(ids[i]);
			if(cmNode.getNodeType().equals(type))
				cmNodes.add(cmNode);
		}
		return (Factor[])cmNodes.toArray(new Factor[0]);
	}
	
	public Factor[] getAllFactors()
	{
		Vector cmNodes = new Vector();
		FactorId[] ids = getModelNodeIds();
		for(int i = 0; i < ids.length; ++i)
		{
			cmNodes.add(getRawObject(ids[i]));
		}
		return (Factor[])cmNodes.toArray(new Factor[0]);
	}
	
}
