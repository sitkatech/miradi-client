/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.views.TreeTableNode;

abstract public class WorkPlanTreeTableNode extends TreeTableNode
{
	abstract public BaseId getId();
	abstract public boolean canInsertActivityHere();
	abstract public boolean canInsertTaskHere();
	abstract public void rebuild();
}