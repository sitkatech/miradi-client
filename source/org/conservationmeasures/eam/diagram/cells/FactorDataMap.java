/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram.cells;

import java.text.ParseException;

import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.utils.DataMap;
import org.json.JSONObject;

//TODO do we really want this class
public class FactorDataMap extends DataMap 
{
	public FactorDataMap()
	{
	}
	
	public FactorDataMap(JSONObject copyFrom) throws ParseException
	{
		super(copyFrom);
	}	

	public String getFactorType()
	{
		return getString(Factor.TAG_NODE_TYPE);
	}
	
	public String getLabel()
	{
		return getString(Factor.TAG_LABEL);
	}
	
}
