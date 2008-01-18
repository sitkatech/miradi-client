/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.treenodes;

import java.util.Collections;
import java.util.Vector;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.project.Project;

public class PlanningTreeIndicatorNode extends AbstractPlanningTreeNode
{
	public PlanningTreeIndicatorNode(Project projectToUse, ORef indicatorRef) throws Exception
	{
		super(projectToUse);
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
