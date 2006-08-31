package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.objectpools.ObjectPool;
import org.conservationmeasures.eam.objects.ConceptualModelNode;

public class AnnotationTableModel extends ObjectManagerTableModel
{
	public AnnotationTableModel(ObjectPool resourcePool, String[] columnTagsToUse)
	{
		super(resourcePool, columnTagsToUse);
	}

	protected String getNodeLabelsAsString(ConceptualModelNode[] modelNodes)
	{
		StringBuffer result = new StringBuffer();
		for(int i = 0; i < modelNodes.length; ++i)
		{
			if(i > 0)
				result.append(", ");
			result.append(modelNodes[i].getLabel());
		}
		
		return result.toString();
	}
	
	public static final String COLUMN_FACTORS = "Factor(s)";
	public static final String COLUMN_DIRECT_THREATS = "Direct Threat(s)";
	public static final String COLUMN_TARGETS = "Target(s)";
	public static final String COLUMN_INTERVENTIONS = "Intervention(s)";
	
}
