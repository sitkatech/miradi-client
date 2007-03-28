/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objectdata.ChoiceData;
import org.conservationmeasures.eam.objectdata.StringData;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

abstract public class Desire extends BaseObject
{
	public Desire(BaseId idToUse)
	{
		super(idToUse);
		clear();
	}

	public Desire(BaseId idToUse, EnhancedJsonObject json) throws Exception
	{
		super(idToUse, json);
	}

	abstract public int getType();

	public String getShortLabel()
	{
		return shortLabel.get();
	}

	public String toString()
	{
		if(getId().isInvalid())
			return "(None)";
		return combineShortLabelAndLabel(shortLabel.toString(), getLabel());
	}

	void clear()
	{
		super.clear();
		shortLabel = new StringData();
		fullText = new StringData();	
		desiredStatus = new ChoiceData();;
		byWhen = new StringData();
		desiredSummary = new StringData();
		desiredDetail = new StringData();
		
		addField(TAG_SHORT_LABEL, shortLabel);
		addField(TAG_FULL_TEXT, fullText);
		addField(TAG_DESIRED_STATUS, desiredStatus);
		addField(TAG_BY_WHEN, byWhen);
		addField(TAG_DESIRED_SUMMARY, desiredSummary);
		addField(TAG_DESIRED_DETAIL, desiredDetail);
		 
	}
	
	public final static String PSEUDO_TAG_TARGETS = "PseudoTagTargets";
	public final static String PSEUDO_TAG_DIRECT_THREATS = "PseudoTagDirectThreats";
	public final static String PSEUDO_TAG_STRATEGIES = "PseudoTagStrategies";
	public final static String PSEUDO_TAG_FACTOR = "PseudoTagFactor";
	public final static String TAG_SHORT_LABEL = "ShortLabel";
	public final static String TAG_FULL_TEXT = "FullText";
	public final static String TAG_DESIRED_STATUS = "DesiredStatus";
	public final static String TAG_BY_WHEN = "ByWhen";
	public final static String TAG_DESIRED_SUMMARY = "DesiredSummary";
	public final static String TAG_DESIRED_DETAIL = "DesiredDetail";
	
	public static final String OBJECT_NAME = "Desire";

	StringData shortLabel;
	StringData fullText;
	ChoiceData desiredStatus;
	StringData byWhen;
	StringData desiredSummary;
	StringData desiredDetail;
}
