/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.AccountingCodeId;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objectdata.StringData;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class AccountingCode extends EAMBaseObject
{
	public AccountingCode(BaseId idToUse)
	{
		super(idToUse);
		clear();
	}
	
	public AccountingCode(int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(new AccountingCodeId(idAsInt), json);
	}

	public int getType()
	{
		return ObjectType.ACCOUNTING_CODE;
	}

	public String toString()
	{
		String result = getLabel();
		if(result.length() > 0)
			return combineShortLabelAndLabel(code.get(), result);
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
	public static final String OBJECT_NAME = "AccountingCode";

	StringData code;
	StringData comments;
}