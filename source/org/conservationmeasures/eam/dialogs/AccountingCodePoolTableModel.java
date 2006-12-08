/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.AccountingCode;
import org.conservationmeasures.eam.project.Project;

public class AccountingCodePoolTableModel extends ObjectPoolTableModel
{
	public AccountingCodePoolTableModel(Project project)
	{
		super(project, ObjectType.ACCOUNTING_CODE, COLUMN_TAGS);
	}

	private static final String[] COLUMN_TAGS = new String[] {
		AccountingCode.TAG_LABEL,
		AccountingCode.TAG_CODE,
	};
}
