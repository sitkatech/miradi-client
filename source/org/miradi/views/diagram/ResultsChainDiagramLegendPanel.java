/* 
Copyright 2005-2017, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.views.diagram;

import javax.swing.JPanel;

import org.miradi.actions.*;
import org.miradi.icons.ActivityIcon;
import org.miradi.layout.TwoColumnPanel;
import org.miradi.main.MainWindow;
import org.miradi.schemas.*;

public class ResultsChainDiagramLegendPanel extends DiagramLegendPanel
{
	public ResultsChainDiagramLegendPanel(MainWindow mainWindowToUse) throws Exception
	{
		super(mainWindowToUse);
	}
	
	@Override
	protected void createCustomLegendPanelSection(Actions actions, JPanel jpanel)
	{
		if (getProject().getMetadata().isBiophysicalFactorMode())
			addButtonLineWithCheckBox(jpanel, BiophysicalResultSchema.getObjectType(), BiophysicalResultSchema.OBJECT_NAME, actions.get(ActionInsertBiophysicalResult.class));
		addButtonLineWithCheckBox(jpanel, ThreatReductionResultSchema.getObjectType(), ThreatReductionResultSchema.OBJECT_NAME, actions.get(ActionInsertThreatReductionResult.class));
		addButtonLineWithCheckBox(jpanel, IntermediateResultSchema.getObjectType(), IntermediateResultSchema.OBJECT_NAME, actions.get(ActionInsertIntermediateResult.class));
	}
	
	@Override
	protected void addActivityLine(TwoColumnPanel jpanel)
	{
		addIconLineWithCheckBox(jpanel, TaskSchema.getObjectType(), TaskSchema.ACTIVITY_NAME, new ActivityIcon());
	}
	
	@Override
	protected int getDiagramType()
	{
		return ResultsChainDiagramSchema.getObjectType();
	}
}
