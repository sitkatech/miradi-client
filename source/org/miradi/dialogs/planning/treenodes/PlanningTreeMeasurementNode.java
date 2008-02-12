/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.planning.treenodes;

import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Measurement;
import org.miradi.project.Project;

public class PlanningTreeMeasurementNode extends AbstractPlanningTreeNode
{
	public PlanningTreeMeasurementNode(Project projectToUse, ORef measurementRef) throws Exception
	{
		super(projectToUse);
		measurement = (Measurement) project.findObject(measurementRef);
	}

	public BaseObject getObject()
	{
		return measurement;
	}
	
	boolean shouldSortChildren()
	{
		return false;
	}
	
	public String toString()
	{
		return measurement.toString();
	}

	private Measurement measurement;
}
