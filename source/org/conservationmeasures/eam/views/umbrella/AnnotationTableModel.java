/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

import org.conservationmeasures.eam.objectpools.EAMObjectPool;

public class AnnotationTableModel extends ObjectManagerTableModel
{
	public AnnotationTableModel(EAMObjectPool resourcePool, String[] columnTagsToUse)
	{
		super(resourcePool, columnTagsToUse);
	}

	public static final String COLUMN_FACTORS = "Factors";
	public static final String COLUMN_DIRECT_THREATS = "Direct Threats";
	public static final String COLUMN_TARGETS = "Targets";
	public static final String COLUMN_INTERVENTIONS = "Strategies";
	
}
