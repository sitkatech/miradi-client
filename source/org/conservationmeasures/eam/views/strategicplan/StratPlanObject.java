/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.views.TreeTableNode;

abstract public class StratPlanObject extends TreeTableNode
{
	abstract public BaseId getId();
	abstract public boolean canInsertActivityHere();
	abstract public void rebuild();
}