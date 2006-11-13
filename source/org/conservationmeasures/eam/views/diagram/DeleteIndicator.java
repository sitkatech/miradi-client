/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.objects.ConceptualModelNode;

public class DeleteIndicator extends DeleteAnnotationDoer
{
	String[] getDialogText()
	{
		return new String[] { "Are you sure you want to delete this Indicator?",};
	}

	String getAnnotationIdListTag()
	{
		return ConceptualModelNode.TAG_INDICATOR_IDS;
	}
}
