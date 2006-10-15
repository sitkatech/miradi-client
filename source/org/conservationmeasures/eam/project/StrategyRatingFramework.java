/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import java.awt.Color;
import java.io.IOException;

import org.conservationmeasures.eam.database.ProjectServer;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ValueOption;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class StrategyRatingFramework
{
	public StrategyRatingFramework(Project owningProject)
	{
		project = owningProject;
		
		clear();
	}
	
	public StrategyRatingFramework(Project owningProject, EnhancedJsonObject json)
	{
		this(owningProject);
		IdList impactOptionIds = new IdList(json.optJSONObject(TAG_IMPACT_VALUE_OPTION_IDS));
		impactValueOptions = new ValueOption[impactOptionIds.size()];
		for(int i = 0; i < impactValueOptions.length; ++i)
			impactValueOptions[i] = (ValueOption)getProject().findObject(ObjectType.VALUE_OPTION, impactOptionIds.get(i));
			
	}
	
	public Project getProject()
	{
		return project;
	}
	
	public ProjectServer getDatabase()
	{
		return getProject().getDatabase();
	}
	
	public void clear()
	{
		impactValueOptions = new ValueOption[0];
	}
	
	public ValueOption[] getImpactValueOptions()
	{
		return impactValueOptions;
	}
	
	public IdList getImpactValueOptionIds()
	{
		IdList result = new IdList();
		for(int i = 0; i < impactValueOptions.length; ++i)
			result.add(impactValueOptions[i].getId());
		return result;
	}
	
	public ValueOption findImpactValueOptionByNumericValue(int value)
	{
		for(int i = 0; i < impactValueOptions.length; ++i)
		{
			if(impactValueOptions[i].getNumericValue() == value)
				return impactValueOptions[i];
		}
		
		return null;
	}


	public void createDefaultObjectsIfNeeded() throws Exception
	{
		IdList ids = new IdList();
		if(impactValueOptions.length == 0)
		{
			ids.add(createDefaultValueOption(0, "None", Color.BLACK));
			ids.add(createDefaultValueOption(1, "Unlikely to solve problem(s)", Color.RED));
			ids.add(createDefaultValueOption(2, "Likely to solve some for a while", Color.ORANGE));
			ids.add(createDefaultValueOption(3, "Likely to solve all for a while", Color.YELLOW));
			ids.add(createDefaultValueOption(4, "Will solve all problems forever", Color.GREEN));
			
			impactValueOptions = new ValueOption[ids.size()];
			for(int i = 0; i < impactValueOptions.length; ++i)
				impactValueOptions[i] = (ValueOption)getProject().findObject(ObjectType.VALUE_OPTION, ids.get(i));
			
			saveFramework();
		}
	}
	
	private void saveFramework() throws IOException
	{
		getDatabase().writeStrategyRatingFramework(this);
	}
	
	private BaseId createDefaultValueOption(int numericValue, String label, Color color) throws Exception
	{
		BaseId createdId = getProject().createObject(ObjectType.VALUE_OPTION);
		getProject().setObjectData(ObjectType.VALUE_OPTION, createdId, ValueOption.TAG_NUMERIC, Integer.toString(numericValue));
		getProject().setObjectData(ObjectType.VALUE_OPTION, createdId, ValueOption.TAG_LABEL, label);
		getProject().setObjectData(ObjectType.VALUE_OPTION, createdId, ValueOption.TAG_COLOR, Integer.toString(color.getRGB()));
		return createdId;
	}
	
	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject json = new EnhancedJsonObject();
		json.put(TAG_IMPACT_VALUE_OPTION_IDS, getImpactValueOptionIds().toJson());
		return json;
	}
	
	public final static String TAG_IMPACT_VALUE_OPTION_IDS = "ImpactValueOptionIds";

	Project project;
	ValueOption[] impactValueOptions;
}
