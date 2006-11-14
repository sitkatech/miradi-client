package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ConceptualModelNode;

public class CreateObjective extends CreateAnnotationDoer
{
	int getAnnotationType()
	{
		return ObjectType.OBJECTIVE;
	}
	String getAnnotationIdListTag()
	{
		return ConceptualModelNode.TAG_OBJECTIVE_IDS;
	}
}