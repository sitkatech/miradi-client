/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FundingSourceId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objectdata.StringData;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class FundingSource extends EAMBaseObject
{
	public FundingSource(BaseId idToUse)
	{
		super(idToUse);
		clear();
	}
	
	public FundingSource(int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(new FundingSourceId(idAsInt), json);
	}

	public int getType()
	{
		return ObjectType.FUNDING_SOURCE;
	}

	public String toString()
	{
		String result = getLabel();
		if(result.length() > 0)
			return result;
		return EAM.text("Label|(Undefined Accounting Code)");
	}
	
	public void clear()
	{
		super.clear();
		
		code = new StringData();
		comments = new StringData();
		
		addField(TAG_CODE, code);
		addField(TAG_COMMENTS, comments);
	}
	
	public static final String TAG_CODE = "Code";
	public static final String TAG_COMMENTS = "Comments";
	public static final String OBJECT_NAME = "FundingSource";
	

	StringData code;
	StringData comments;
}