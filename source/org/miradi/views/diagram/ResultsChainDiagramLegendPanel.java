/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

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

import org.miradi.actions.ActionInsertIntermediateResult;
import org.miradi.actions.ActionInsertThreatReductionResult;
import org.miradi.actions.Actions;
import org.miradi.icons.ActivityIcon;
import org.miradi.layout.TwoColumnPanel;
import org.miradi.main.MainWindow;
import org.miradi.objects.IntermediateResult;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.objects.Task;
import org.miradi.objects.ThreatReductionResult;

public class ResultsChainDiagramLegendPanel extends DiagramLegendPanel
{
	public ResultsChainDiagramLegendPanel(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
	}
	
	@Override
	protected void createCustomLegendPanelSection(Actions actions, JPanel jpanel)
	{
		addButtonLineWithCheckBox(jpanel, ThreatReductionResult.getObjectType(), ThreatReductionResult.OBJECT_NAME, actions.get(ActionInsertThreatReductionResult.class));
		addButtonLineWithCheckBox(jpanel, IntermediateResult.getObjectType(), IntermediateResult.OBJECT_NAME, actions.get(ActionInsertIntermediateResult.class));
	}
	
	@Override
	protected void addActivityLine(TwoColumnPanel jpanel)
	{
		addIconLineWithCheckBox(jpanel, Task.getObjectType(), Task.ACTIVITY_NAME, new ActivityIcon());
	}
	
	@Override
	protected int getDiagramType()
	{
		return ResultsChainDiagram.getObjectType();
	}
}
