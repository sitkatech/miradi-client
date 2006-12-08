/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.FundingSource;
import org.conservationmeasures.eam.project.Project;

public class FundingSourcePoolTableModel extends ObjectPoolTableModel
{
	public FundingSourcePoolTableModel(Project project)
	{
		super(project, ObjectType.FUNDING_SOURCE, COLUMN_TAGS);
	}

	private static final String[] COLUMN_TAGS = new String[] {
		FundingSource.TAG_CODE,
		FundingSource.TAG_LABEL,
	};
}
