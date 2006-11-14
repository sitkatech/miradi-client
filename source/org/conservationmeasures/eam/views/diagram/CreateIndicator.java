/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ConceptualModelNode;

public class CreateIndicator  extends CreateAnnotationDoer
{
	int getAnnotationType()
	{
		return ObjectType.INDICATOR;
	}
	String getAnnotationIdListTag()
	{
		return ConceptualModelNode.TAG_INDICATOR_IDS;
	}
}