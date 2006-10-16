/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import java.awt.Color;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.conservationmeasures.eam.database.ProjectServer;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objectpools.NodePool;
import org.conservationmeasures.eam.objectpools.ThreatRatingCriterionPool;
import org.conservationmeasures.eam.objectpools.ValueOptionPool;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.objects.ThreatRatingCriterion;
import org.conservationmeasures.eam.objects.ValueOption;
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
		bundles = new HashMap();
		ratingValueOptions = new ValueOption[0];
	}
	
	public Project getProject()
	{
		return project;
	}
	
	public IdList getValueOptionIds()
	{
		IdList ids = new IdList();
		for(int i = 0; i < ratingValueOptions.length; ++i)
			ids.add(ratingValueOptions[i].getId());
		return ids;
	}
	
	public void createDefaultObjectsIfNeeded() throws Exception
	{
		if(getCriteria().length == 0)
		{
			createDefaultCriterion(EAM.text("Label|Scope")); 
			createDefaultCriterion(EAM.text("Label|Severity"));
			createDefaultCriterion(EAM.text("Label|Irreversibility"));
		}
		
		if(getValueOptionIds().size() == 0)
		{
			IdList ids = new IdList();
			ids.add(createDefaultValueOption(EAM.text("Label|None"), 0, Color.WHITE));
			ids.add(createDefaultValueOption(EAM.text("Label|Very High"), 4, Color.RED));
			ids.add(createDefaultValueOption(EAM.text("Label|High"), 3, Color.ORANGE));
			ids.add(createDefaultValueOption(EAM.text("Label|Medium"), 2, Color.YELLOW));
			ids.add(createDefaultValueOption(EAM.text("Label|Low"), 1, Color.GREEN));
			
			ratingValueOptions = new ValueOption[ids.size()];
			for(int i = 0; i < ratingValueOptions.length; ++i)
				ratingValueOptions[i] = (ValueOption)getProject().findObject(ObjectType.VALUE_OPTION, ids.get(i));

			Arrays.sort(ratingValueOptions, new OptionSorter());
			saveFramework();
		}
	}

	private BaseId createDefaultValueOption(String label, int numericValue, Color color) throws Exception
	{
		int type = ObjectType.VALUE_OPTION;
		BaseId createdId = project.createObject(type);
		project.setObjectData(type, createdId, ValueOption.TAG_LABEL, label);
		project.setObjectData(type, createdId, ValueOption.TAG_NUMERIC, Integer.toString(numericValue));
		project.setObjectData(type, createdId, ValueOption.TAG_COLOR, Integer.toString(color.getRGB()));
		return createdId;
	}

	private void createDefaultCriterion(String label) throws Exception
	{
		int type = ObjectType.THREAT_RATING_CRITERION;
		BaseId createdId = project.createObject(type);
		project.setObjectData(type, createdId, ThreatRatingCriterion.TAG_LABEL, label);
	}
	
	public int getBundleCount()
	{
		return bundles.size();
	}
	
	public ValueOption[] getValueOptions()
	{
		return ratingValueOptions;
	}
	
	class OptionSorter implements Comparator
	{
		public int compare(Object raw1, Object raw2)
		{
			ValueOption option1 = (ValueOption)raw1;
			ValueOption option2 = (ValueOption)raw2;
			Integer value1 = new Integer(option1.getNumericValue());
			Integer value2 = new Integer(option2.getNumericValue());
			return -(value1.compareTo(value2));
		}
	
	}
	
	public ValueOption getValueOption(BaseId id)
	{
		ValueOptionPool pool = (ValueOptionPool)getProject().getPool(ObjectType.VALUE_OPTION);
		return (ValueOption)pool.findObject(id);
	}
	

	ThreatRatingCriterionPool getCriterionPool()
	{
		return (ThreatRatingCriterionPool)getProject().getPool(ObjectType.THREAT_RATING_CRITERION);
	}
	
	public ThreatRatingCriterion[] getCriteria()
	{
		BaseId[] ids = getCriterionPool().getIds();
		ThreatRatingCriterion[] result = new ThreatRatingCriterion[ids.length];
		for(int i = 0; i < ids.length; ++i)
		{
			result[i] = getCriterion(ids[i]);
		}
		return result;
	}
	
	public ThreatRatingCriterion getCriterion(BaseId id)
	{
		return (ThreatRatingCriterion)getCriterionPool().findObject(id);
	}
	

	public ValueOption getBundleValue(ThreatRatingBundle bundle)
	{
		TNCThreatFormula formula = new TNCThreatFormula(this);
		int numericResult = formula.computeBundleValue(bundle);
		return findValueOptionByNumericValue(numericResult);
		
	}
	
	public ValueOption getHighestValueForThreat(BaseId threatId)
	{
		ThreatRatingBundle[] bundleArray = getBundlesForThisThreat(threatId);
		if(bundleArray.length == 0)
			return findValueOptionByNumericValue(0);
		ValueOption highestValue = getBundleValue(bundleArray[0]);
		for(int i = 0; i < bundleArray.length; ++i)
		{
			ValueOption thisValue = getBundleValue(bundleArray[i]);
			if(thisValue.getNumericValue() > highestValue.getNumericValue())
				highestValue = thisValue;
		}
		return highestValue;
	}
	
	public ValueOption getProjectMajorityRating()
	{
		ConceptualModelNode[] threats = getProject().getNodePool().getDirectThreats();
		int[] highestValues = new int[threats.length];
		for(int i = 0; i < threats.length; ++i)
			highestValues[i] = getHighestValueForThreat(threats[i].getId()).getNumericValue();
		
		return getMajorityOfNumericValues(highestValues);
	}
	
	public ValueOption getOverallProjectRating()
	{
		ValueOption rollup = getProjectRollupRating();
		ValueOption majority = getProjectMajorityRating();
		if(majority.getNumericValue() > rollup.getNumericValue())
			return majority;
		return rollup;
	}
	
	public ValueOption getThreatThreatRatingValue(BaseId threatId)
	{
		ThreatRatingBundle[] bundleArray = getBundlesForThisThreat(threatId);
		return getSummaryOfBundles(bundleArray);
	}

	private ThreatRatingBundle[] getBundlesForThisThreat(BaseId threatId)
	{
		HashSet bundlesForThisThreat = new HashSet();
		
		Iterator iter = bundles.values().iterator();
		while(iter.hasNext())
		{
			ThreatRatingBundle bundle = (ThreatRatingBundle)iter.next();
			if(bundle.getThreatId().equals(threatId) && isBundleForLinkedThreatAndTarget(bundle))
				bundlesForThisThreat.add(bundle);
		}
		ThreatRatingBundle[] bundleArray = (ThreatRatingBundle[])bundlesForThisThreat.toArray(new ThreatRatingBundle[0]);
		return bundleArray;
	}
	
	public ValueOption getTargetThreatRatingValue(BaseId targetId)
	{
		HashSet bundlesForThisThreat = new HashSet();
		
		Iterator iter = bundles.values().iterator();
		while(iter.hasNext())
		{
			ThreatRatingBundle bundle = (ThreatRatingBundle)iter.next();
			if(bundle.getTargetId().equals(targetId) && isBundleForLinkedThreatAndTarget(bundle))
				bundlesForThisThreat.add(bundle);
		}
		ThreatRatingBundle[] bundleArray = (ThreatRatingBundle[])bundlesForThisThreat.toArray(new ThreatRatingBundle[0]);
		return getSummaryOfBundles(bundleArray);
	}
	
	public ValueOption getProjectRollupRating()
	{
		ConceptualModelNode[] threats = getProject().getNodePool().getDirectThreats();
		int[] numericValues = new int[threats.length];
		for(int i = 0; i < threats.length; ++i)
		{
			ValueOption threatSummary = getThreatThreatRatingValue(threats[i].getId());
			numericValues[i] = threatSummary.getNumericValue();
		}
		return getSummaryOfNumericValues(numericValues);
	}

	public boolean isBundleForLinkedThreatAndTarget(ThreatRatingBundle bundle)
	{
		NodePool nodePool = project.getNodePool();
		ModelNodeId threatId = bundle.getThreatId();
		ConceptualModelNode threat = nodePool.find(threatId);
		if(threat == null || !threat.isDirectThreat())
			return false;
		
		ModelNodeId targetId = bundle.getTargetId();
		ConceptualModelNode target = nodePool.find(targetId);
		if(target == null || !target.isTarget())
			return false;

		return project.isLinked(threatId, targetId);
	}
	
	public ValueOption getSummaryOfBundles(ThreatRatingBundle[] bundlesToSummarize)
	{
		int[] bundleValues = new int[bundlesToSummarize.length];
		for(int i = 0; i < bundlesToSummarize.length; ++i)
			bundleValues[i] = getBundleValue(bundlesToSummarize[i]).getNumericValue();

		return getSummaryOfNumericValues(bundleValues);
	}

	private ValueOption getSummaryOfNumericValues(int[] bundleValues)
	{
		TNCThreatFormula formula = new TNCThreatFormula(this);
		int numericResult = formula.getSummaryOfBundles(bundleValues);
		return findValueOptionByNumericValue(numericResult);
	}
	
	private ValueOption getMajorityOfNumericValues(int[] bundleValues)
	{
		TNCThreatFormula formula = new TNCThreatFormula(this);
		int numericResult = formula.getMajority(bundleValues);
		return findValueOptionByNumericValue(numericResult);
	}

	public ThreatRatingCriterion findCriterionByLabel(String label)
	{
		BaseId[] ids = getCriterionPool().getIds();
		for(int i = 0; i < ids.length; ++i)
		{
			ThreatRatingCriterion criterion = getCriterion(ids[i]);
			if(criterion.getLabel().equals(label))
				return criterion;
		}
		
		return null;
	}
	
	public ValueOption findValueOptionByNumericValue(int value)
	{
		for(int i = 0; i < ratingValueOptions.length; ++i)
		{
			if(ratingValueOptions[i].getNumericValue() == value)
				return ratingValueOptions[i];
		}
		
		return null;
	}
	

	
	public ThreatRatingBundle getBundle(ModelNodeId threatId, ModelNodeId targetId) throws Exception
	{
		ThreatRatingBundle existing = (ThreatRatingBundle)bundles.get(getBundleKey(threatId, targetId));
		if(existing != null)
			return existing;
		
		BaseId defaultValueId = getDefaultValueId();
		ThreatRatingBundle newBundle = new ThreatRatingBundle(threatId, targetId, defaultValueId);
		saveBundle(newBundle);
		saveFramework();
		return newBundle;
	}

	public BaseId getDefaultValueId()
	{
		return findValueOptionByNumericValue(0).getId();
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

	public static String getBundleKey(BaseId threatId, BaseId targetId)
	{
		String key = threatId.toString() + "-" + targetId.toString();
		return key;
	}
	
	public JSONObject toJson()
	{
		JSONObject json = new JSONObject();
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
		json.put(TAG_VALUE_OPTION_IDS, getValueOptionIds().toJson());
		return json;
	}
	
	public void load() throws Exception
	{
		clear();
		ProjectServer db = getDatabase();
		JSONObject json = db.readRawThreatRatingFramework();
		if(json == null)
			return;

		JSONArray bundleKeys = json.optJSONArray(TAG_BUNDLE_KEYS);
		if(bundleKeys == null)
			bundleKeys = new JSONArray();
		for(int i = 0; i < bundleKeys.length(); ++i)
		{
			JSONObject pair = bundleKeys.getJSONObject(i);
			BaseId threatId = new BaseId(pair.getInt(TAG_BUNDLE_THREAT_ID));
			BaseId targetId = new BaseId(pair.getInt(TAG_BUNDLE_TARGET_ID));
			ThreatRatingBundle bundle = db.readThreatRatingBundle(threatId, targetId);
			memorize(bundle);
		}
		
		ratingValueOptions = findValueOptions(new IdList(json.optJSONObject(TAG_VALUE_OPTION_IDS)));
	}

	private ValueOption[] findValueOptions(IdList ids)
	{
		ValueOption[] valueOptions = new ValueOption[ids.size()];
		for(int i = 0; i < valueOptions.length; ++i)
			valueOptions[i] = (ValueOption)getProject().findObject(ObjectType.VALUE_OPTION, ids.get(i));
		
		return valueOptions;
	}
	
	private ProjectServer getDatabase()
	{
		ProjectServer db = project.getDatabase();
		return db;
	}
	
	public static final String TAG_BUNDLE_KEYS = "BundleKeys";
	public static final String TAG_VALUE_OPTION_IDS = "ValueOptionIds";
	public static final String TAG_BUNDLE_THREAT_ID = "BundleThreatId";
	public static final String TAG_BUNDLE_TARGET_ID = "BundleTargetId";
	
	private Project project;
	
	private HashMap bundles;
	private ValueOption[] ratingValueOptions;
}
