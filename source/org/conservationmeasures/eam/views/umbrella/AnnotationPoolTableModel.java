/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objectpools.EAMObjectPool;

public abstract class AnnotationPoolTableModel extends LegacyObjectPoolTableModel
{
	public AnnotationPoolTableModel(EAMObjectPool resourcePool, String[] columnTagsToUse)
	{
		super(resourcePool, columnTagsToUse);
	}

	public static final String COLUMN_FACTORS = "Factors";
	public static final String COLUMN_DIRECT_THREATS = "Direct Threats";
	public static final String COLUMN_TARGETS = "Targets";
	public static final String COLUMN_INTERVENTIONS = "Strategies";
	
	public abstract String getTableCellDisplayString(int rowIndex, int columnIndex, BaseId indicatorId, String columnTag);
	
}
