/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.project.threatrating;

import java.awt.Color;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;
import org.miradi.database.ProjectServer;
import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.ids.IdList;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objectpools.RatingCriterionPool;
import org.miradi.objectpools.ValueOptionPool;
import org.miradi.objects.Cause;
import org.miradi.objects.Factor;
import org.miradi.objects.FactorLink;
import org.miradi.objects.RatingCriterion;
import org.miradi.objects.ValueOption;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.utils.EnhancedJsonArray;
import org.miradi.utils.EnhancedJsonObject;
import org.miradi.utils.Utility;


public class SimpleThreatRatingFramework extends ThreatRatingFramework
{
	public SimpleThreatRatingFramework(Project projectToUse)
	{
		super(projectToUse);
		
		clear();
	}

	public void clear()
	{
		bundles = new HashMap();
		criteria = new RatingCriterion[0];
		ratingValueOptions = new ValueOption[0];
	}
	
	public SimpleThreatFormula getSimpleThreatFormula()
	{
		return new SimpleThreatFormula(this);
	}
		
	public IdList getValueOptionIds()
	{
		IdList ids = new IdList(ValueOption.getObjectType());
		for(int i = 0; i < ratingValueOptions.length; ++i)
			ids.add(ratingValueOptions[i].getId());
		return ids;
	}
	
	public IdList getCriterionIds()
	{
		IdList ids = new IdList(RatingCriterion.getObjectType());
		for(int i = 0; i < criteria.length; ++i)
			ids.add(criteria[i].getId());
		return ids;
	}
	
	public void createDefaultObjectsIfNeeded() throws Exception
	{
		if(criteria.length == 0)
		{
			IdList ids = new IdList(RatingCriterion.getObjectType());
			ids.add(createDefaultCriterion("Scope")); 
			ids.add(createDefaultCriterion("Severity"));
			ids.add(createDefaultCriterion("Irreversibility"));
			
			criteria = new RatingCriterion[ids.size()];
			for(int i = 0; i < criteria.length; ++i)
				criteria[i] = (RatingCriterion)getProject().findObject(ObjectType.RATING_CRITERION, ids.get(i));
			
			saveFramework();
		}
		
		if(ratingValueOptions.length == 0)
		{
			IdList ids = new IdList(ValueOption.getObjectType());
			ids.add(createDefaultValueOption("None", 0, Color.WHITE));
			ids.add(createDefaultValueOption("Very High", 4, Color.RED));
			ids.add(createDefaultValueOption("High", 3, Color.ORANGE));
			ids.add(createDefaultValueOption("Medium", 2, Color.YELLOW));
			ids.add(createDefaultValueOption("Low", 1, Color.GREEN));
			
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
		BaseId createdId = project.createObjectAndReturnId(type);
		project.setObjectData(type, createdId, ValueOption.TAG_LABEL, label);
		project.setObjectData(type, createdId, ValueOption.TAG_NUMERIC, Integer.toString(numericValue));
		project.setObjectData(type, createdId, ValueOption.TAG_COLOR, Integer.toString(color.getRGB()));
		return createdId;
	}

	private BaseId createDefaultCriterion(String label) throws Exception
	{
		int type = ObjectType.RATING_CRITERION;
		BaseId createdId = project.createObjectAndReturnId(type);
		project.setObjectData(type, createdId, RatingCriterion.TAG_LABEL, label);
		return createdId;
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
	

	RatingCriterionPool getCriterionPool()
	{
		return (RatingCriterionPool)getProject().getPool(ObjectType.RATING_CRITERION);
	}
	
	public RatingCriterion[] getCriteria()
	{
		return criteria;
	}
	
	public RatingCriterion getCriterion(BaseId id)
	{
		return (RatingCriterion)getCriterionPool().findObject(id);
	}
	

	public int getScopeNumericValue(ThreatRatingBundle bundle)
	{
		return getNumericValue(bundle, getScopeCriterion());
	}

	public int getSeverityNumericValue(ThreatRatingBundle bundle)
	{
		return getNumericValue(bundle, getSeverityCriterion());
	}

	public int getIrreversibilityNumericValue(ThreatRatingBundle bundle)
	{
		return getNumericValue(bundle, getIrreversibilityCriterion());
	}

	private int getNumericValue(ThreatRatingBundle bundle, RatingCriterion criterion)
	{
		BaseId valueId = bundle.getValueId(criterion.getId());
		ValueOption valueOption = (ValueOption)getProject().findObject(ValueOption.getObjectType(), valueId);
		return valueOption.getNumericValue();
	}

	public ValueOption getBundleValue(ThreatRatingBundle bundle)
	{
		SimpleThreatFormula formula = getSimpleThreatFormula();
		int numericResult = formula.computeBundleValue(bundle);
		return findValueOptionByNumericValue(numericResult);
		
	}
	
	public int getHighestValueForTarget(BaseId targetId)
	{
		ThreatRatingBundle[] bundleArray = getBundlesForThisTarget(targetId);
		int[] bundleValues = extractBundleValues(bundleArray);
		return getSimpleThreatFormula().getHighestRating357Not2Prime(bundleValues);
	}
	
	private int[] extractBundleValues(ThreatRatingBundle[] bundleArray)
	{
		int[] values = new int[bundleArray.length];
		for(int i = 0; i < values.length; ++i)
			values[i] = getBundleValue(bundleArray[i]).getNumericValue();
		return values;
	}

	public ValueOption getProjectMajorityRating()
	{
		Factor[] targets = getProject().getTargetPool().getTargets();
		Vector<Integer> highestValues = new Vector();
		for(int i = 0; i < targets.length; ++i)
		{
			int targetRating = getHighestValueForTarget(targets[i].getId());
			if (targetRating > 0)
				highestValues.add(targetRating);
		}
		
		return getMajorityOfNumericValues(Utility.convertToIntArray(highestValues));
	}
	
	public ValueOption getOverallProjectRating()
	{
		ValueOption rollup = getProjectRollupRating();
		ValueOption majority = getProjectMajorityRating();
		if(majority.getNumericValue() > rollup.getNumericValue())
			return majority;
		return rollup;
	}
	
	public ChoiceItem getThreatThreatRatingValue(ORef threatRef) throws Exception
	{
		ValueOption valueOption = getThreatThreatRatingValue(threatRef.getObjectId());
		return new ChoiceItem(Integer.toString(valueOption.getNumericValue()), valueOption.getLabel(), valueOption.getColor());
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
		ThreatRatingBundle[] bundleArray = getBundlesForThisTarget(targetId);
		return getSummaryOfBundles(bundleArray);
	}

	private ThreatRatingBundle[] getBundlesForThisTarget(BaseId targetId)
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
		return bundleArray;
	}
	
	public ValueOption getProjectRollupRating()
	{
		Factor[] threats = getProject().getCausePool().getDirectThreats();
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
		FactorId threatId = bundle.getThreatId();
		FactorId targetId = bundle.getTargetId();
		Factor threat = Cause.find(project, new ORef(Cause.getObjectType(), threatId));
		if(threat == null)
			return false;
		
		ORefList links = threat.findObjectsThatReferToUs(FactorLink.getObjectType());
		for(int i = 0; i < links.size(); ++i)
		{
			FactorLink link = (FactorLink)project.findObject(links.get(i));
			FactorId fromId = new FactorId(link.getFromFactorRef().getObjectId().asInt());
			FactorId toId = new FactorId(link.getToFactorRef().getObjectId().asInt());
			if(fromId.equals(threatId) && toId.equals(targetId))
				return true;

			if(link.isBidirectional() && fromId.equals(targetId) && toId.equals(threatId))
				return true;
		}
		
		return false;
	}
	
	public ValueOption getSummaryOfBundles(ThreatRatingBundle[] bundlesToSummarize)
	{
		int[] bundleValues = new int[bundlesToSummarize.length];
		for(int i = 0; i < bundlesToSummarize.length; ++i)
			bundleValues[i] = getBundleValue(bundlesToSummarize[i]).getNumericValue();

		return getSummaryOfNumericValues(bundleValues);
	}

	protected ValueOption getSummaryOfNumericValues(int[] bundleValues)
	{
		SimpleThreatFormula formula = getSimpleThreatFormula();
		int numericResult = formula.getSummaryOfBundlesWithTwoPrimeRule(bundleValues);
		return findValueOptionByNumericValue(numericResult);
	}

	private ValueOption getMajorityOfNumericValues(int[] bundleValues)
	{
		SimpleThreatFormula formula = getSimpleThreatFormula();
		int numericResult = formula.getMajority(bundleValues);
		return findValueOptionByNumericValue(numericResult);
	}

	public RatingCriterion findCriterionByLabel(String label)
	{
		for(int i = 0; i < criteria.length; ++i)
		{
			if(criteria[i].getLabel().equals(label))
				return criteria[i];
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
	
	//FIXME creating factorId from id.asInt  (cant cast)
	public ThreatRatingBundle getBundle(ORef threatRef, ORef targetRef) throws Exception
	{
		return getBundle(new FactorId(threatRef.getObjectId().asInt()), new FactorId(targetRef.getObjectId().asInt()));
	}
	
	public ThreatRatingBundle getBundle(FactorId threatId, FactorId targetId) throws Exception
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
	
	public RatingCriterion getScopeCriterion()
	{
		return findCriterionByLabel(CRITERION_SCOPE);
	}
	
	public RatingCriterion getSeverityCriterion()
	{
		return findCriterionByLabel(CRITERION_SEVERITY);
	}
	
	public RatingCriterion getIrreversibilityCriterion()
	{
		return findCriterionByLabel(CRITERION_IRREVERSIBILITY);
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
		json.put(TAG_CRITERION_IDS, getCriterionIds().toJson());
		return json;
	}
	
	public void load() throws Exception
	{
		clear();
		ProjectServer db = getDatabase();
		EnhancedJsonObject json = db.readRawThreatRatingFramework();
		if(json == null)
			return;

		EnhancedJsonArray bundleKeys = json.optJsonArray(TAG_BUNDLE_KEYS);
		if(bundleKeys == null)
			bundleKeys = new EnhancedJsonArray();
		for(int i = 0; i < bundleKeys.length(); ++i)
		{
			JSONObject pair = bundleKeys.getJson(i);
			BaseId threatId = new BaseId(pair.getInt(TAG_BUNDLE_THREAT_ID));
			BaseId targetId = new BaseId(pair.getInt(TAG_BUNDLE_TARGET_ID));
			ThreatRatingBundle bundle = db.readThreatRatingBundle(threatId, targetId);
			memorize(bundle);
		}
		
		ratingValueOptions = findValueOptions(new IdList(ValueOption.getObjectType(), json.optJson(TAG_VALUE_OPTION_IDS)));
		Arrays.sort(ratingValueOptions, new OptionSorter());
		criteria = findCriteria(new IdList(RatingCriterion.getObjectType(), json.optJson(TAG_CRITERION_IDS)));
		sortCriteria();
	}

	private ValueOption[] findValueOptions(IdList ids)
	{
		ValueOption[] valueOptions = new ValueOption[ids.size()];
		for(int i = 0; i < valueOptions.length; ++i)
		{
			int type = ObjectType.VALUE_OPTION;
			valueOptions[i] = (ValueOption)getProject().findObject(type, ids.get(i));
		}
		
		return valueOptions;
	}
	
	private void sortCriteria()
	{
		RatingCriterion[] sorted = new RatingCriterion[criteria.length];
		sorted[0] = findCriterionByLabel(CRITERION_SCOPE);
		sorted[1] = findCriterionByLabel(CRITERION_SEVERITY);
		sorted[2] = findCriterionByLabel(CRITERION_IRREVERSIBILITY);
		criteria = sorted;
	}
	
	private RatingCriterion[] findCriteria(IdList ids)
	{
		if(ids.contains(BaseId.INVALID))
			ids.removeId(BaseId.INVALID);
		RatingCriterion[] ratingCriteria = new RatingCriterion[ids.size()];
		for(int i = 0; i < ratingCriteria.length; ++i)
		{
			int type = ObjectType.RATING_CRITERION;
			ratingCriteria[i] = (RatingCriterion)getProject().findObject(type, ids.get(i));
		}
		
		return ratingCriteria;
	}
	
	private ProjectServer getDatabase()
	{
		ProjectServer db = project.getDatabase();
		return db;
	}
	
	public static final String TAG_BUNDLE_KEYS = "BundleKeys";
	public static final String TAG_VALUE_OPTION_IDS = "ValueOptionIds";
	public static final String TAG_CRITERION_IDS = "CriterionIds";
	public static final String TAG_BUNDLE_THREAT_ID = "BundleThreatId";
	public static final String TAG_BUNDLE_TARGET_ID = "BundleTargetId";
	
	private static final String CRITERION_IRREVERSIBILITY = "Irreversibility";
	private static final String CRITERION_SEVERITY = "Severity";
	private static final String CRITERION_SCOPE = "Scope";

	private HashMap bundles;
	private ValueOption[] ratingValueOptions;
	private RatingCriterion[] criteria;
}
