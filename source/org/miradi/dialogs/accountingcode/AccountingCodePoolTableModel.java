/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.accountingcode;

import org.miradi.dialogs.base.ObjectPoolTableModel;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.AccountingCode;
import org.miradi.project.Project;

public class AccountingCodePoolTableModel extends ObjectPoolTableModel
{
	public AccountingCodePoolTableModel(Project project)
	{
		super(project, ObjectType.ACCOUNTING_CODE, COLUMN_TAGS);
	}

	private static final String[] COLUMN_TAGS = new String[] {
		AccountingCode.TAG_CODE,
		AccountingCode.TAG_LABEL,
	};
}
