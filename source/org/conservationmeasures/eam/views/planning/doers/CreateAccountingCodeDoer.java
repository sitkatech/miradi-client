/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.planning.doers;

import org.conservationmeasures.eam.objects.AccountingCode;

public class CreateAccountingCodeDoer extends CreatePoolObjectDoer
{
	@Override
	protected int getTypeToCreate()
	{
		return AccountingCode.getObjectType();
	}
}
