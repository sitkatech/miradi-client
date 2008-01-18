/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.AccountingCodeId;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objectdata.StringData;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class AccountingCode extends BaseObject
{
	public AccountingCode(ObjectManager objectManager, BaseId idToUse)
	{
		super(objectManager, idToUse);
		clear();
	}
	
	public AccountingCode(BaseId idToUse)
	{
		super(idToUse);
		clear();
	}
	
	public AccountingCode(ObjectManager objectManager, int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, new AccountingCodeId(idAsInt), json);
	}

	
	public AccountingCode(int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(new AccountingCodeId(idAsInt), json);
	}

	public int getType()
	{
		return getObjectType();
	}
	
	public String getTypeName()
	{
		return OBJECT_NAME;
	}

	public static int getObjectType()
	{
		return ObjectType.ACCOUNTING_CODE;
	}
	
	
	public static boolean canOwnThisType(int type)
	{
		return false;
	}
	
	
	public static boolean canReferToThisType(int type)
	{
		return false;
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