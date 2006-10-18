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
		impactValueOptions = findValueOptions(new IdList(json.optJson(TAG_IMPACT_VALUE_OPTION_IDS)));
		feasibilityValueOptions = findValueOptions(new IdList(json.optJson(TAG_FEASIBILITY_VALUE_OPTION_IDS)));
	}
	
	private ValueOption[] findValueOptions(IdList ids)
	{
		ValueOption[] valueOptions = new ValueOption[ids.size()];
		for(int i = 0; i < valueOptions.length; ++i)
			valueOptions[i] = (ValueOption)getProject().findObject(ObjectType.VALUE_OPTION, ids.get(i));
		
		return valueOptions;
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
		feasibilityValueOptions = new ValueOption[0];
	}
	
	public ValueOption[] getImpactValueOptions()
	{
		return impactValueOptions;
	}
	
	public ValueOption[] getFeasibilityValueOptions()
	{
		return feasibilityValueOptions;
	}
	
	public IdList getImpactValueOptionIds()
	{
		return getValueOptionIds(impactValueOptions);
	}

	public IdList getFeasibilityValueOptionIds()
	{
		return getValueOptionIds(feasibilityValueOptions);
	}

	private IdList getValueOptionIds(ValueOption[] valueOptions)
	{
		IdList result = new IdList();
		for(int i = 0; i < valueOptions.length; ++i)
			result.add(valueOptions[i].getId());
		return result;
	}
	
	public ValueOption findImpactValueOptionByNumericValue(int value)
	{
		return findValueOptionByNumericValue(impactValueOptions, value);
	}

	public ValueOption findFeasibilityValueOptionByNumericValue(int value)
	{
		return findValueOptionByNumericValue(feasibilityValueOptions, value);
	}

	private ValueOption findValueOptionByNumericValue(ValueOption[] options, int value)
	{
		for(int i = 0; i < options.length; ++i)
		{
			if(options[i].getNumericValue() == value)
				return options[i];
		}
		
		return null;
	}
	

	public void createDefaultObjectsIfNeeded() throws Exception
	{
		if(impactValueOptions.length == 0)
		{
			IdList ids = new IdList();
			ids.add(createDefaultValueOption(0, "Useless", Color.BLACK));
			ids.add(createDefaultValueOption(1, "Unlikely to solve problem(s)", Color.RED));
			ids.add(createDefaultValueOption(2, "Likely to solve some for a while", Color.ORANGE));
			ids.add(createDefaultValueOption(3, "Likely to solve all for a while", Color.YELLOW));
			ids.add(createDefaultValueOption(4, "Will solve all problems forever", Color.GREEN));
			
			impactValueOptions = new ValueOption[ids.size()];
			for(int i = 0; i < impactValueOptions.length; ++i)
				impactValueOptions[i] = (ValueOption)getProject().findObject(ObjectType.VALUE_OPTION, ids.get(i));
			
			saveFramework();
		}
		
		if(feasibilityValueOptions.length == 0)
		{
			IdList ids = new IdList();
			ids.add(createDefaultValueOption(0, "Impossible or hugely expensive", Color.BLACK));
			ids.add(createDefaultValueOption(1, "Expensive and difficult", Color.RED));
			ids.add(createDefaultValueOption(2, "Relatively inexpensive or easy, not both", Color.ORANGE));
			ids.add(createDefaultValueOption(3, "Relatively inexpensive and easy", Color.YELLOW));
			ids.add(createDefaultValueOption(4, "Inexpensive and easy", Color.GREEN));
			
			feasibilityValueOptions = new ValueOption[ids.size()];
			for(int i = 0; i < feasibilityValueOptions.length; ++i)
				feasibilityValueOptions[i] = (ValueOption)getProject().findObject(ObjectType.VALUE_OPTION, ids.get(i));
			
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
		json.put(TAG_FEASIBILITY_VALUE_OPTION_IDS, getFeasibilityValueOptionIds().toJson());
		return json;
	}
	
	public final static String TAG_IMPACT_VALUE_OPTION_IDS = "ImpactValueOptionIds";
	public final static String TAG_FEASIBILITY_VALUE_OPTION_IDS = "FeasibilityValueOptionIds";

	Project project;
	ValueOption[] impactValueOptions;
	ValueOption[] feasibilityValueOptions;
}
