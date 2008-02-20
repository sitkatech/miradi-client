/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.planning.treenodes;

import java.util.Collections;
import java.util.Vector;

import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Indicator;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;

public class PlanningTreeIndicatorNode extends AbstractPlanningTreeNode
{
	public PlanningTreeIndicatorNode(Project projectToUse, ORef indicatorRef, CodeList visibleRowsToUse) throws Exception
	{
		super(projectToUse, visibleRowsToUse);
		indicator = (Indicator)project.findObject(indicatorRef);
		rebuild();
	}

	public void rebuild() throws Exception
	{
		ORefList methodRefs = indicator.getMethods();
		createAndAddTaskNodes(methodRefs);
		addSortedMeasurementChildren();
	}

	private void addSortedMeasurementChildren() throws Exception
	{
		ORefList measurementRefs = indicator.getMeasurementRefs();
		Vector<AbstractPlanningTreeNode> measurementChildren = new Vector();
		for(int i = 0; i < measurementRefs.size(); ++i)
		{
			measurementChildren.add(createChildNode(measurementRefs.get(i), null));
		}
		
		Collections.sort(measurementChildren, new NodeSorter());
		children.addAll(measurementChildren);
	}

	public BaseObject getObject()
	{
		return indicator;
	}

	boolean shouldSortChildren()
	{
		return false;
	}

	private Indicator indicator;
}
