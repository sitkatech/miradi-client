/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import org.conservationmeasures.eam.database.ProjectServer;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.IdList;
import org.conservationmeasures.eam.objects.ObjectType;
import org.conservationmeasures.eam.objects.ThreatRatingBundle;
import org.conservationmeasures.eam.objects.ThreatRatingCriterion;
import org.conservationmeasures.eam.objects.ThreatRatingValueOption;
import org.json.JSONArray;
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
		createDefaultCriterion(EAM.text("Label|Irreversibility"));
		
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
	
	public int getBundleCount()
	{
		return bundles.size();
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
	
	public ThreatRatingValueOption getThreatThreatRatingValue(int threatId)
	{
		HashSet bundlesForThisThreat = new HashSet();
		
		Iterator iter = bundles.values().iterator();
		while(iter.hasNext())
		{
			ThreatRatingBundle bundle = (ThreatRatingBundle)iter.next();
			if(bundle.getThreatId() == threatId && isBundleForLinkedThreatAndTarget(bundle))
				bundlesForThisThreat.add(bundle);
		}
		ThreatRatingBundle[] bundleArray = (ThreatRatingBundle[])bundlesForThisThreat.toArray(new ThreatRatingBundle[0]);
		return getSummaryOfBundles(bundleArray);
	}
	
	public ThreatRatingValueOption getTargetThreatRatingValue(int targetId)
	{
		HashSet bundlesForThisThreat = new HashSet();
		
		Iterator iter = bundles.values().iterator();
		while(iter.hasNext())
		{
			ThreatRatingBundle bundle = (ThreatRatingBundle)iter.next();
			if(bundle.getTargetId() == targetId && isBundleForLinkedThreatAndTarget(bundle))
				bundlesForThisThreat.add(bundle);
		}
		ThreatRatingBundle[] bundleArray = (ThreatRatingBundle[])bundlesForThisThreat.toArray(new ThreatRatingBundle[0]);
		return getSummaryOfBundles(bundleArray);
	}
	
	public ThreatRatingValueOption getProjectThreatRatingValue(int[] threatIds)
	{
		int[] numericValues = new int[threatIds.length];
		for(int i = 0; i < threatIds.length; ++i)
		{
			ThreatRatingValueOption threatSummary = getThreatThreatRatingValue(threatIds[i]);
			numericValues[i] = threatSummary.getNumericValue();
		}
		return getSummaryOfNumericValues(numericValues);
	}
	
	boolean isBundleForLinkedThreatAndTarget(ThreatRatingBundle bundle)
	{
		NodePool nodePool = project.getNodePool();
		int threatId = bundle.getThreatId();
		if(!nodePool.find(threatId).isDirectThreat())
			return false;
		
		int targetId = bundle.getTargetId();
		if(!nodePool.find(targetId).isTarget())
			return false;

		return project.isLinked(threatId, targetId);
	}
	
	public ThreatRatingValueOption getSummaryOfBundles(ThreatRatingBundle[] bundlesToSummarize)
	{
		int[] bundleValues = new int[bundlesToSummarize.length];
		for(int i = 0; i < bundlesToSummarize.length; ++i)
			bundleValues[i] = getBundleValue(bundlesToSummarize[i]).getNumericValue();

		return getSummaryOfNumericValues(bundleValues);
	}

	private ThreatRatingValueOption getSummaryOfNumericValues(int[] bundleValues)
	{
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
	
	public ThreatRatingBundle getBundle(int threatId, int targetId) throws Exception
	{
		ThreatRatingBundle existing = (ThreatRatingBundle)bundles.get(getBundleKey(threatId, targetId));
		if(existing != null)
			return existing;
		
		int defaultValueId = getDefaultValueId();
		ThreatRatingBundle newBundle = new ThreatRatingBundle(threatId, targetId, defaultValueId);
		saveBundle(newBundle);
		saveFramework();
		return newBundle;
	}

	public int getDefaultValueId()
	{
		return ((ThreatRatingValueOption)optionPool.get(0)).getId();
	}
	
	public void saveFramework() throws IOException
	{
		getDatabase().writeThreatRatingFramework(this);
	}

	public void saveBundle(ThreatRatingBundle newBundle) throws Exception
	{
		getDatabase().writeThreatRatingBundle(newBundle);
		memorize(newBundle);
	}

	private void memorize(ThreatRatingBundle newBundle)
	{
		String key = getBundleKey(newBundle.getThreatId(), newBundle.getTargetId());
		bundles.put(key, newBundle);
	}

	public static String getBundleKey(int threatId, int targetId)
	{
		String key = Integer.toString(threatId) + "-" + Integer.toString(targetId);
		return key;
	}
	
	public JSONObject toJson()
	{
		JSONObject json = new JSONObject();
		json.put(TAG_CRITERION_IDS, criteria.toJson());
		json.put(TAG_VALUE_OPTION_IDS, options.toJson());
		JSONArray bundleKeys = new JSONArray();
		Iterator iter = bundles.keySet().iterator();
		while(iter.hasNext())
		{
			ThreatRatingBundle bundle = (ThreatRatingBundle)bundles.get(iter.next());
			JSONObject pair = new JSONObject();
			pair.put(TAG_BUNDLE_THREAT_ID, bundle.getThreatId());
			pair.put(TAG_BUNDLE_TARGET_ID, bundle.getTargetId());
			bundleKeys.put(pair);
		}
		json.put(TAG_BUNDLE_KEYS, bundleKeys);
		return json;
	}
	
	public void load() throws Exception
	{
		clear();
		ProjectServer db = getDatabase();
		JSONObject json = db.readRawThreatRatingFramework();
		if(json == null)
			return;

		criteria = new IdList(json.getJSONObject(TAG_CRITERION_IDS));
		for(int i = 0; i < criteria.size(); ++i)
			criterionPool.add(db.readObject(ObjectType.THREAT_RATING_CRITERION, criteria.get(i)));

		options = new IdList(json.getJSONObject(TAG_VALUE_OPTION_IDS));
		for(int i = 0; i < options.size(); ++i)
			optionPool.add(db.readObject(ObjectType.THREAT_RATING_VALUE_OPTION, options.get(i)));
		
		JSONArray bundleKeys = json.getJSONArray(TAG_BUNDLE_KEYS);
		for(int i = 0; i < bundleKeys.length(); ++i)
		{
			JSONObject pair = bundleKeys.getJSONObject(i);
			int threatId = pair.getInt(TAG_BUNDLE_THREAT_ID);
			int targetId = pair.getInt(TAG_BUNDLE_TARGET_ID);
			ThreatRatingBundle bundle = db.readThreatRatingBundle(threatId, targetId);
			memorize(bundle);
		}
	}

	private ProjectServer getDatabase()
	{
		ProjectServer db = project.getDatabase();
		return db;
	}
	
	public static final String TAG_CRITERION_IDS = "CriterionIds";
	public static final String TAG_VALUE_OPTION_IDS = "ValueOptionIds";
	public static final String TAG_BUNDLE_KEYS = "BundleKeys";
	public static final String TAG_BUNDLE_THREAT_ID = "BundleThreatId";
	public static final String TAG_BUNDLE_TARGET_ID = "BundleTargetId";
	
	private Project project;
	private IdList criteria;
	private Vector criterionPool;
	private IdList options;
	private Vector optionPool;
	
	private HashMap bundles;
}
