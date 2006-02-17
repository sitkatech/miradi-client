/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import java.awt.Color;
import java.util.Vector;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ThreatRatingCriterion;
import org.conservationmeasures.eam.objects.ThreatRatingValueOption;
import org.json.JSONArray;
import org.json.JSONObject;


public class ThreatRatingFramework
{
	public ThreatRatingFramework(IdAssigner idAssignerToUse)
	{
		idAssigner = idAssignerToUse;
		
		criteria = new Vector();
		criteria.add(new ThreatRatingCriterion(idAssigner.takeNextId(), "Scope")); 
		criteria.add(new ThreatRatingCriterion(idAssigner.takeNextId(), "Severity"));
		criteria.add(new ThreatRatingCriterion(idAssigner.takeNextId(), "Urgency"));
		criteria.add(new ThreatRatingCriterion(idAssigner.takeNextId(), "Custom"));
				
		options = new Vector();
		options.add(new ThreatRatingValueOption(idAssigner.takeNextId(), EAM.text("Label|Very High"), 4, Color.RED));
		options.add(new ThreatRatingValueOption(idAssigner.takeNextId(), EAM.text("Label|High"), 3, Color.ORANGE));
		options.add(new ThreatRatingValueOption(idAssigner.takeNextId(), EAM.text("Label|Medium"), 2, Color.YELLOW));
		options.add(new ThreatRatingValueOption(idAssigner.takeNextId(), EAM.text("Label|Low"), 1, Color.GREEN));
		options.add(new ThreatRatingValueOption(idAssigner.takeNextId(), EAM.text("Label|None"), 0, Color.WHITE));
		
	}
	
	public ThreatRatingFramework(JSONObject json)
	{
		JSONArray criterionArray = json.getJSONArray(TAG_CRITERIA);
		criteria = new Vector();
		for(int i = 0; i < criterionArray.length(); ++i)
			criteria.add(new ThreatRatingCriterion(criterionArray.getJSONObject(i)));
		
		JSONArray optionsArray = json.getJSONArray(TAG_OPTIONS);
		options = new Vector();
		for(int i = 0; i < optionsArray.length(); ++i)
			options.add(new ThreatRatingValueOption(optionsArray.getJSONObject(i)));
	}

	public ThreatRatingValueOption[] getValueOptions()
	{
		return (ThreatRatingValueOption[])options.toArray(new ThreatRatingValueOption[0]);
	}
	
	public ThreatRatingValueOption getValueOption(int id)
	{
		return (ThreatRatingValueOption)options.get(findValueOption(id));
	}
	
	public int createValueOption(int candidateId)
	{
		int realId = getRealId(candidateId);
		if(findValueOption(realId) >= 0)
			throw new RuntimeException("Attempted to create value option with existing id");
		ThreatRatingValueOption createdItem = new ThreatRatingValueOption(realId);
		options.add(createdItem);
		return createdItem.getId();		
	}
	
	public void deleteValueOption(int id)
	{
		int deleteAt = findValueOption(id);
		options.remove(deleteAt);
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
		for(int i = 0; i < options.size(); ++i)
		{
			ThreatRatingValueOption option = (ThreatRatingValueOption)options.get(i);
			if(option.getId() == id)
				return i;
		}
		
		return -1;
	}
	

	
	
	public ThreatRatingCriterion[] getCriteria()
	{
		return (ThreatRatingCriterion[])criteria.toArray(new ThreatRatingCriterion[0]);
	}
	
	public ThreatRatingCriterion getCriterion(int id)
	{
		return (ThreatRatingCriterion)criteria.get(findCriterion(id));
	}
	
	public int createCriterion(int candidateId)
	{
		int realId = getRealId(candidateId);
		if(findCriterion(realId) >= 0)
			throw new RuntimeException("Attempted to create criterion with existing id");
		ThreatRatingCriterion createdItem = new ThreatRatingCriterion(realId);
		criteria.add(createdItem);
		return createdItem.getId();
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
		criteria.remove(deleteAt);
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
		for(int i = 0; i < criteria.size(); ++i)
		{
			ThreatRatingCriterion criterion = (ThreatRatingCriterion)criteria.get(i);
			if(criterion.getId() == id)
				return i;
		}
		
		return -1;
	}
	
	public JSONObject toJson()
	{
		JSONObject json = new JSONObject();
		JSONArray criterionArray = new JSONArray();
		for(int i = 0; i < criteria.size(); ++i)
		{
			ThreatRatingCriterion criterion = (ThreatRatingCriterion)criteria.get(i);
			criterionArray.put(criterion.toJson());
		}
		json.put(TAG_CRITERIA, criterionArray);
		
		JSONArray optionsArray = new JSONArray();
		for(int i = 0; i < options.size(); ++i)
		{
			ThreatRatingValueOption option = (ThreatRatingValueOption)options.get(i);
			optionsArray.put(option.toJson());
		}
		json.put(TAG_OPTIONS, optionsArray);
		
		return json;
	}
	
	private final static String TAG_CRITERIA = "Criteria";
	private final static String TAG_OPTIONS = "Options";
	
	private IdAssigner idAssigner;
	private Vector criteria;
	private Vector options;
}
