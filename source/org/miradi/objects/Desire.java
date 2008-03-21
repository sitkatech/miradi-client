/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.objects;

import java.util.Arrays;

import org.miradi.ids.BaseId;
import org.miradi.ids.IdList;
import org.miradi.objectdata.StringData;
import org.miradi.objecthelpers.DirectThreatSet;
import org.miradi.objecthelpers.NonDraftStrategySet;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objecthelpers.TargetSet;
import org.miradi.project.ObjectManager;
import org.miradi.utils.EnhancedJsonObject;

abstract public class Desire extends BaseObject
{
	public Desire(ObjectManager objectManager, BaseId idToUse)
	{
		super(objectManager, idToUse);
		clear();
	}

	public Desire(ObjectManager objectManager, BaseId idToUse, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, idToUse, json);
	}

	abstract public int getType();

	public String getShortLabel()
	{
		return shortLabel.get();
	}
	
	public String getFullText()
	{
		return fullText.get();
	}

	public String toString()
	{
		if(getId().isInvalid())
			return "(None)";
		return combineShortLabelAndLabel(shortLabel.toString(), getLabel());
	}

	public String getPseudoData(String fieldTag)
	{
		if(fieldTag.equals(PSEUDO_TAG_TARGETS))
			return getRelatedLabelsAsMultiLine(new TargetSet());
		
		if(fieldTag.equals(PSEUDO_TAG_DIRECT_THREATS))
			return getRelatedLabelsAsMultiLine(new DirectThreatSet());
		
		if(fieldTag.equals(PSEUDO_TAG_STRATEGIES))
			return getRelatedLabelsAsMultiLine(new NonDraftStrategySet());
		
		if(fieldTag.equals(PSEUDO_TAG_FACTOR))
			return getOwner().getLabel();
		
		return super.getPseudoData(fieldTag);
	}
	
	public ORefSet indicatorsOnSameFactorAsRefSet()
	{
		ORefSet indicatorsOnSameFactor = new ORefSet();
		ORef[] indicators = getIndicatorsOnSameFactor().toArray();
		indicatorsOnSameFactor.addAll(Arrays.asList(indicators));
		
		return indicatorsOnSameFactor;
	}
	
	public ORefList getIndicatorsOnSameFactor()
	{
		ORefList indicatorRefs = new ORefList();
		
		ORefList referrers = findObjectsThatReferToUs();
		for(int i = 0; i < referrers.size(); ++i)
		{
			ORef thisRef = referrers.get(i);
			if(!Factor.isFactor(thisRef))
				continue;
			
			Factor factor = getObjectManager().findFactor(thisRef);
			IdList indicatorIds = factor.getDirectOrIndirectIndicators();
			for(int idIndex = 0; idIndex < indicatorIds.size(); ++idIndex)
			{
				BaseId indicatorId = indicatorIds.get(idIndex);
				if(indicatorId.isInvalid())
					continue;
				indicatorRefs.add(new ORef(Indicator.getObjectType(), indicatorId));
			}
		}
		
		return indicatorRefs;
		
	}

	void clear()
	{
		super.clear();
		shortLabel = new StringData(TAG_SHORT_LABEL);
		fullText = new StringData(TAG_FULL_TEXT);
		comments = new StringData(TAG_COMMENTS);
		multiLineTargets = new PseudoStringData(PSEUDO_TAG_TARGETS);
		multiLineDirectThreats = new PseudoStringData(PSEUDO_TAG_DIRECT_THREATS);
		multiLineStrategies = new PseudoStringData(PSEUDO_TAG_STRATEGIES);
		multiLineFactor = new PseudoStringData(PSEUDO_TAG_FACTOR);
		
		addField(TAG_SHORT_LABEL, shortLabel);
		addField(TAG_FULL_TEXT, fullText);
		addField(TAG_COMMENTS, comments);
		addField(PSEUDO_TAG_TARGETS, multiLineTargets);
		addField(PSEUDO_TAG_DIRECT_THREATS, multiLineDirectThreats);
		addField(PSEUDO_TAG_STRATEGIES, multiLineStrategies);
		addField(PSEUDO_TAG_FACTOR, multiLineFactor);
	}
	
	public final static String TAG_SHORT_LABEL = "ShortLabel";
	public final static String TAG_FULL_TEXT = "FullText";
	public final static String TAG_COMMENTS = "Comments";
	public final static String PSEUDO_TAG_TARGETS = "PseudoTagTargets";
	public final static String PSEUDO_TAG_DIRECT_THREATS = "PseudoTagDirectThreats";
	public final static String PSEUDO_TAG_STRATEGIES = "PseudoTagStrategies";
	public final static String PSEUDO_TAG_FACTOR = "PseudoTagFactor";
	
	public static final String OBJECT_NAME = "Desire";

	private StringData shortLabel;
	private StringData fullText;
	private StringData comments;
	private PseudoStringData multiLineTargets;
	private PseudoStringData multiLineDirectThreats;
	private PseudoStringData multiLineStrategies;
	private PseudoStringData multiLineFactor;
}
