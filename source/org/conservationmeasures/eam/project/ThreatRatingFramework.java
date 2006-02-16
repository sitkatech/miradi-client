/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import java.awt.Color;

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
		
		criteria = new ThreatRatingCriterion[] {
			new ThreatRatingCriterion(idAssigner.takeNextId(), "Scope"), 
			new ThreatRatingCriterion(idAssigner.takeNextId(), "Severity"),
			new ThreatRatingCriterion(idAssigner.takeNextId(), "Urgency"),
			new ThreatRatingCriterion(idAssigner.takeNextId(), "Custom"),
		};
				
		options =  new ThreatRatingValueOption[] {
			new ThreatRatingValueOption(idAssigner.takeNextId(), EAM.text("Label|Very High"), Color.RED),
			new ThreatRatingValueOption(idAssigner.takeNextId(), EAM.text("Label|High"), Color.ORANGE),
			new ThreatRatingValueOption(idAssigner.takeNextId(), EAM.text("Label|Medium"), Color.YELLOW),
			new ThreatRatingValueOption(idAssigner.takeNextId(), EAM.text("Label|Low"), Color.GREEN),
			new ThreatRatingValueOption(idAssigner.takeNextId(), EAM.text("Label|None"), Color.WHITE),
		};
		
	}
	
	public ThreatRatingFramework(JSONObject json)
	{
		JSONArray criterionArray = json.getJSONArray(TAG_CRITERIA);
		criteria = new ThreatRatingCriterion[criterionArray.length()];
		for(int i = 0; i < criteria.length; ++i)
			criteria[i] = new ThreatRatingCriterion(criterionArray.getJSONObject(i));
		
		JSONArray optionsArray = json.getJSONArray(TAG_OPTIONS);
		options = new ThreatRatingValueOption[optionsArray.length()];
		for(int i = 0; i < options.length; ++i)
			options[i] = new ThreatRatingValueOption(optionsArray.getJSONObject(i));
	}

	public ThreatRatingCriterion[] getCriteria()
	{
		return criteria;
	}
	
	public ThreatRatingValueOption[] getValueOptions()
	{
		return options;
	}
	
	public JSONObject toJson()
	{
		JSONObject json = new JSONObject();
		JSONArray criterionArray = new JSONArray();
		for(int i = 0; i < criteria.length; ++i)
			criterionArray.put(criteria[i].toJson());
		json.put(TAG_CRITERIA, criterionArray);
		
		JSONArray optionsArray = new JSONArray();
		for(int i = 0; i < options.length; ++i)
			optionsArray.put(options[i].toJson());
		json.put(TAG_OPTIONS, optionsArray);
		
		return json;
	}
	
	private final static String TAG_CRITERIA = "Criteria";
	private final static String TAG_OPTIONS = "Options";
	
	private IdAssigner idAssigner;
	private ThreatRatingCriterion[] criteria;
	private ThreatRatingValueOption[] options;
}
