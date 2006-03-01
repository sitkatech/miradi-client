/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import java.awt.Color;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Vector;

import org.conservationmeasures.eam.database.ProjectServer;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.IdList;
import org.conservationmeasures.eam.objects.ObjectType;
import org.conservationmeasures.eam.objects.ThreatRatingBundle;
import org.conservationmeasures.eam.objects.ThreatRatingCriterion;
import org.conservationmeasures.eam.objects.ThreatRatingValueOption;
import org.json.JSONObject;


public class ThreatRatingFramework
{
	public ThreatRatingFramework(Project owningProject)
	{
		project = owningProject;
		
		clear();
	}

	public void clear()
	{
		criteria = new IdList();
		criterionPool = new Vector();
		
		options = new IdList();
		optionPool = new Vector();
		
		bundles = new HashMap();
	}
	
	public void createDefaultObjects() throws IOException
	{
		createDefaultCriterion(EAM.text("Label|Scope")); 
		createDefaultCriterion(EAM.text("Label|Severity"));
		createDefaultCriterion(EAM.text("Label|Custom1"));
		createDefaultCriterion(EAM.text("Label|Custom2"));
		
		createDefaultValueOption(EAM.text("Label|None"), 0, Color.WHITE);
		createDefaultValueOption(EAM.text("Label|Very High"), 4, Color.RED);
		createDefaultValueOption(EAM.text("Label|High"), 3, Color.ORANGE);
		createDefaultValueOption(EAM.text("Label|Medium"), 2, Color.YELLOW);
		createDefaultValueOption(EAM.text("Label|Low"), 1, Color.GREEN);
	}

	private void createDefaultValueOption(String label, int numericValue, Color color) throws IOException
	{
		int type = ObjectType.THREAT_RATING_VALUE_OPTION;
		int createdId = project.createObject(type);
		project.setObjectData(type, createdId, ThreatRatingValueOption.TAG_LABEL, label);
		project.setObjectData(type, createdId, ThreatRatingValueOption.TAG_NUMERIC, Integer.toString(numericValue));
		project.setObjectData(type, createdId, ThreatRatingValueOption.TAG_COLOR, Integer.toString(color.getRGB()));
	}

	private void createDefaultCriterion(String label) throws IOException
	{
		int type = ObjectType.THREAT_RATING_CRITERION;
		int createdId = project.createObject(type);
		project.setObjectData(type, createdId, ThreatRatingCriterion.TAG_LABEL, label);
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
	
	public ThreatRatingValueOption getBundleValue(ThreatRatingBundle bundle)
	{
		TNCThreatFormula formula = new TNCThreatFormula(this);
		int numericResult = formula.computeBundleValue(bundle);
		return findValueOptionByNumericValue(numericResult);
		
	}
	
	public ThreatRatingValueOption getSummaryOfBundles(ThreatRatingBundle[] bundlesToSummarize)
	{
		int[] bundleValues = new int[bundlesToSummarize.length];
		for(int i = 0; i < bundlesToSummarize.length; ++i)
			bundleValues[i] = getBundleValue(bundlesToSummarize[i]).getNumericValue();

		TNCThreatFormula formula = new TNCThreatFormula(this);
		int numericResult = formula.getSummaryOfBundles(bundleValues);
		return findValueOptionByNumericValue(numericResult);
	}

	public ThreatRatingCriterion findCriterionByLabel(String label)
	{
		for(int i = 0; i < criteria.size(); ++i)
		{
			int id = criteria.get(i);
			ThreatRatingCriterion criterion = getCriterion(id);
			if(criterion.getLabel().equals(label))
				return criterion;
		}
		
		return null;
	}
	
	public ThreatRatingValueOption findValueOptionByNumericValue(int value)
	{
		for(int i = 0; i < options.size(); ++i)
		{
			int id = options.get(i);
			ThreatRatingValueOption option = getValueOption(id);
			if(option.getNumericValue() == value)
				return option;
		}
		
		return null;
	}
	

	private int getRealId(int candidateId)
	{
		IdAssigner idAssigner = project.getAnnotationIdAssigner();
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
	
	public ThreatRatingBundle getBundle(int threatId, int targetId)
	{
		ThreatRatingBundle existing = (ThreatRatingBundle)bundles.get(getBundleKey(threatId, targetId));
		if(existing != null)
			return existing;
		
		int defaultValueId = ((ThreatRatingValueOption)optionPool.get(0)).getId();
		ThreatRatingBundle newBundle = new ThreatRatingBundle(threatId, targetId, defaultValueId);
		saveBundle(newBundle);
		return newBundle;
	}

	public void saveBundle(ThreatRatingBundle newBundle)
	{
		String key = getBundleKey(newBundle.getThreatId(), newBundle.getTargetId());
		bundles.put(key, newBundle);
	}

	private String getBundleKey(int threatId, int targetId)
	{
		String key = Integer.toString(threatId) + "-" + Integer.toString(targetId);
		return key;
	}
	
	public JSONObject toJson()
	{
		JSONObject json = new JSONObject();
		json.put(TAG_CRITERION_IDS, criteria.toJson());
		json.put(TAG_VALUE_OPTION_IDS, options.toJson());
		return json;
	}
	
	public void load() throws IOException, ParseException
	{
		clear();
		ProjectServer db = project.getDatabase();
		JSONObject json = db.readRawThreatRatingFramework();
		if(json == null)
			return;

		criteria = new IdList(json.getJSONObject(TAG_CRITERION_IDS));
		for(int i = 0; i < criteria.size(); ++i)
			criterionPool.add(db.readObject(ObjectType.THREAT_RATING_CRITERION, criteria.get(i)));

		options = new IdList(json.getJSONObject(TAG_VALUE_OPTION_IDS));
		for(int i = 0; i < options.size(); ++i)
			optionPool.add(db.readObject(ObjectType.THREAT_RATING_VALUE_OPTION, options.get(i)));
	}
	
	public static final String TAG_CRITERION_IDS = "CriterionIds";
	public static final String TAG_VALUE_OPTION_IDS = "ValueOptionIds";
	
	private Project project;
	private IdList criteria;
	private Vector criterionPool;
	private IdList options;
	private Vector optionPool;
	
	private HashMap bundles;
}
