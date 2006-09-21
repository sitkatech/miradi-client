package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.objectpools.ObjectPool;

public class AnnotationTableModel extends ObjectManagerTableModel
{
	public AnnotationTableModel(ObjectPool resourcePool, String[] columnTagsToUse)
	{
		super(resourcePool, columnTagsToUse);
	}

	public static final String COLUMN_FACTORS = "Factor(s)";
	public static final String COLUMN_DIRECT_THREATS = "Direct Threat(s)";
	public static final String COLUMN_TARGETS = "Target(s)";
	public static final String COLUMN_INTERVENTIONS = "Intervention(s)";
	
}
