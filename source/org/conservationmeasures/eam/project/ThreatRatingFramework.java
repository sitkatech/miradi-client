/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import java.awt.Color;
import java.util.Vector;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.IdList;
import org.conservationmeasures.eam.objects.ThreatRatingCriterion;
import org.conservationmeasures.eam.objects.ThreatRatingValueOption;


public class ThreatRatingFramework
{
	public ThreatRatingFramework(IdAssigner idAssignerToUse)
	{
		idAssigner = idAssignerToUse;
		
		criteria = new IdList();
		criterionPool = new Vector();
		
		options = new IdList();
		optionPool = new Vector();
		
		createDefaultObjects();
	}
	
	public void createDefaultObjects()
	{
		createDefaultCriterion(EAM.text("Label|Scope")); 
		createDefaultCriterion(EAM.text("Label|Severity"));
		createDefaultCriterion(EAM.text("Label|Urgency"));
		createDefaultCriterion(EAM.text("Label|Custom"));
		
		createDefaultValueOption(EAM.text("Label|Very High"), 4, Color.RED);
		createDefaultValueOption(EAM.text("Label|High"), 3, Color.ORANGE);
		createDefaultValueOption(EAM.text("Label|Medium"), 2, Color.YELLOW);
		createDefaultValueOption(EAM.text("Label|Low"), 1, Color.GREEN);
		createDefaultValueOption(EAM.text("Label|None"), 0, Color.WHITE);
	}

	private void createDefaultValueOption(String label, int numericValue, Color color)
	{
		int createdId = createValueOption(IdAssigner.INVALID_ID);
		ThreatRatingValueOption option = getValueOption(createdId);
		option.setData(ThreatRatingValueOption.TAG_LABEL, label);
		option.setData(ThreatRatingValueOption.TAG_COLOR, Integer.toString(numericValue));
		option.setData(ThreatRatingValueOption.TAG_COLOR, Integer.toString(color.getRGB()));
	}

	private void createDefaultCriterion(String label)
	{
		int createdId = createCriterion(IdAssigner.INVALID_ID);
		ThreatRatingCriterion criterion = getCriterion(createdId);
		criterion.setData(ThreatRatingCriterion.TAG_LABEL, label);
	}

	public ThreatRatingValueOption[] getValueOptions()
	{
		int count = options.size();
		ThreatRatingValueOption[] result = new ThreatRatingValueOption[count];
		for(int i = 0; i < options.size(); ++i)
		{
			result[i] = getValueOption(options.get(i));
		}
		return result;
	}
	
	public ThreatRatingValueOption getValueOption(int id)
	{
		return (ThreatRatingValueOption)optionPool.get(findValueOption(id));
	}
	
	public int createValueOption(int candidateId)
	{
		int realId = getRealId(candidateId);
		if(findValueOption(realId) >= 0)
			throw new RuntimeException("Attempted to create value option with existing id");
		ThreatRatingValueOption createdItem = new ThreatRatingValueOption(realId);
		optionPool.add(createdItem);
		options.add(realId);
		return realId;		
	}
	
	public void deleteValueOption(int id)
	{
		int deleteAt = findValueOption(id);
		optionPool.remove(deleteAt);
		options.removeId(id);
	}
	
	public void setValueOptionData(int id, String fieldTag, String dataValue)
	{
		getValueOption(id).setData(fieldTag, dataValue);
	}
	
	public String getValueOptionData(int id, String fieldTag)
	{
		return getValueOption(id).getData(fieldTag);
	}
	
	private int findValueOption(int id)
	{
		for(int i = 0; i < optionPool.size(); ++i)
		{
			ThreatRatingValueOption option = (ThreatRatingValueOption)optionPool.get(i);
			if(option.getId() == id)
				return i;
		}
		
		return -1;
	}
	

	
	
	public ThreatRatingCriterion[] getCriteria()
	{
		int count = criteria.size();
		ThreatRatingCriterion[] result = new ThreatRatingCriterion[count];
		for(int i = 0; i < criteria.size(); ++i)
		{
			result[i] = getCriterion(criteria.get(i));
		}
		return result;
	}
	
	public ThreatRatingCriterion getCriterion(int id)
	{
		return (ThreatRatingCriterion)criterionPool.get(findCriterion(id));
	}
	
	public int createCriterion(int candidateId)
	{
		int realId = getRealId(candidateId);
		if(findCriterion(realId) >= 0)
			throw new RuntimeException("Attempted to create criterion with existing id");
		ThreatRatingCriterion createdItem = new ThreatRatingCriterion(realId);
		criterionPool.add(createdItem);
		criteria.add(realId);
		return realId;
	}

	private int getRealId(int candidateId)
	{
		if(candidateId == IdAssigner.INVALID_ID)
			candidateId = idAssigner.takeNextId();
		return candidateId;
	}
	
	public void deleteCriterion(int id)
	{
		int deleteAt = findCriterion(id);
		criterionPool.remove(deleteAt);
		criteria.removeId(id);
	}
	
	public void setCriterionData(int id, String fieldTag, Object dataValue)
	{
		getCriterion(id).setData(fieldTag, dataValue);
	}
	
	public String getCriterionData(int id, String fieldTag)
	{
		return getCriterion(id).getData(fieldTag);
	}
	
	private int findCriterion(int id)
	{
		for(int i = 0; i < criterionPool.size(); ++i)
		{
			ThreatRatingCriterion criterion = (ThreatRatingCriterion)criterionPool.get(i);
			if(criterion.getId() == id)
				return i;
		}
		
		return -1;
	}
	
	private IdAssigner idAssigner;
	private IdList criteria;
	private Vector criterionPool;
	private IdList options;
	private Vector optionPool;
}
