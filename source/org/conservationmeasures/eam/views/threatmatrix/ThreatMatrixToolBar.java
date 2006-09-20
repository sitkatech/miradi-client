/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.views.ActionViewThreatMatrix;
import org.conservationmeasures.eam.main.EAMToolBar;

public class ThreatMatrixToolBar extends EAMToolBar
{
	public ThreatMatrixToolBar(Actions actions)
	{
		super(actions, ActionViewThreatMatrix.class);
	}
}
