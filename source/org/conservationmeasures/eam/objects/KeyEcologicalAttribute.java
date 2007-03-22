/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.ids.KeyEcologicalAttributeId;
import org.conservationmeasures.eam.objectdata.IdListData;
import org.conservationmeasures.eam.objectdata.StringData;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class KeyEcologicalAttribute extends EAMBaseObject
{
	public KeyEcologicalAttribute(KeyEcologicalAttributeId idToUse)
	{
		super(idToUse);
		clear();
	}

	public KeyEcologicalAttribute(int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(new BaseId(idAsInt), json);
	}
	
	void clear()
	{
		super.clear();
		indicatorIds = new IdListData();
		description = new StringData();
		keyEcologicalAttributeType = new StringData();
		
		addField(TAG_INDICATOR_IDS, indicatorIds);
		addField(TAG_DESCRIPTION, description);
		addField(TAG_KEY_ECOLOGICAL_ATTRIBUTE_TYPE, keyEcologicalAttributeType);
	}
	
	public int getType()
	{
		return ObjectType.KEY_ECOLOGICAL_ATTRIBUTE;
	}

	public IdList getIndicatorIds()
	{
		return indicatorIds.getIdList();
	}
	
	public static final String TAG_INDICATOR_IDS = "IndicatorIds";
	public static final String TAG_DESCRIPTION = "Description";
	public static final String TAG_KEY_ECOLOGICAL_ATTRIBUTE_TYPE = "KeyEcologicalAttributeType";
	public static final String PSUEDO_TAG_VIABILITY_STATUS = "PseudoTagViabilityStatus";
	
	public static final String OBJECT_NAME = "Key Ecological Attribute";
	
	IdListData indicatorIds;
	StringData description;
	StringData keyEcologicalAttributeType;
}
