/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objectpools;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Vector;

import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.ids.IdAssigner;
import org.miradi.objecthelpers.CreateObjectParameter;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Factor;
import org.miradi.objects.Strategy;
import org.miradi.project.ObjectManager;

public class StrategyPool extends EAMNormalObjectPool
{

	public StrategyPool(IdAssigner idAssignerToUse)
	{
		super(idAssignerToUse, ObjectType.STRATEGY);
	}
	
	public void put(Strategy strategy)
	{
		put(strategy.getId(), strategy);
	}
	
	public Strategy find(BaseId id)
	{
		return (Strategy)getRawObject(id);
	}
	
	BaseObject createRawObject(ObjectManager objectManager, BaseId actualId, CreateObjectParameter extraInfo) throws Exception
	{
		return new Strategy(objectManager ,new FactorId(actualId.asInt()));
	}
	
	public Factor[] getDraftStrategies()
	{
		Vector draftStrategies = new Vector();
		Factor[] allStrategies = getDraftAndNonDraftStrategies();
		for(int i = 0; i < allStrategies.length; ++i)
		{
			Strategy strategy = (Strategy)allStrategies[i];
			if(strategy.isStatusDraft())
				draftStrategies.add(strategy);
		}
		return (Factor[])draftStrategies.toArray(new Factor[0]);
	}
	
	public Factor[] getDraftAndNonDraftStrategies()
	{
		return getNodesOfType(ObjectType.STRATEGY);
	}
	
	private Factor[] getNodesOfType(int type)
	{
		Vector cmNodes = new Vector();
		FactorId[] ids = getModelNodeIds();
		Arrays.sort(ids);
		for(int i = 0; i < ids.length; ++i)
		{
			Factor cmNode = (Factor)getRawObject(ids[i]);
			if(cmNode.getType() == type)
				cmNodes.add(cmNode);
		}
		return (Factor[])cmNodes.toArray(new Factor[0]);
	}

	public FactorId[] getModelNodeIds()
	{
		return (FactorId[])new HashSet(getRawIds()).toArray(new FactorId[0]);
	}
	
	public Factor[] getNonDraftStrategies()
	{
		Vector nonDraftStrategies = new Vector();
		Factor[] allStrategies = getDraftAndNonDraftStrategies();
		for(int i = 0; i < allStrategies.length; ++i)
		{
			Strategy strategy = (Strategy)allStrategies[i];
			if(!strategy.isStatusDraft())
				nonDraftStrategies.add(strategy);
		}
		return (Factor[])nonDraftStrategies.toArray(new Factor[0]);
	}
	
	public Strategy[] getAllStrategies()
	{
		return (Strategy[]) getValues().toArray(new Strategy[0]);
	}

}
