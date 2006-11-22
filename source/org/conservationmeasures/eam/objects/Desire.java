/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objectdata.StringData;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

abstract public class Desire extends EAMBaseObject
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
		return shortLabel + "." + getLabel();
	}

	void clear()
	{
		super.clear();
		shortLabel = new StringData();
		fullText = new StringData();
		
		addField(TAG_SHORT_LABEL, shortLabel);
		addField(TAG_FULL_TEXT, fullText);
	}
	
	public static final String PSEUDO_TAG_DIRECT_THREAT = "PseudoTagDirectThreat";
	public final static String PSEUDO_TAG_STRATEGIES = "PseudoTagStrategies";
	public final static String PSEUDO_TAG_FACTOR = "PseudoTagFactor";
	public final static String TAG_SHORT_LABEL = "ShortLabel";
	public final static String TAG_FULL_TEXT = "FullText";

	StringData shortLabel;
	StringData fullText;

}
