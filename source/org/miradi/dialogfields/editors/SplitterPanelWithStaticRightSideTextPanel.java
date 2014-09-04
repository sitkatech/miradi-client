/* 
Copyright 2005-2014, Foundations of Success, Bethesda, Maryland
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

package org.miradi.dialogfields.editors;

import java.awt.Color;

import org.miradi.dialogs.base.DisposablePanel;
import org.miradi.dialogs.dashboard.AbstractLongDescriptionProvider;
import org.miradi.dialogs.dashboard.SplitterPanelWithRightSideTextPanel;
import org.miradi.dialogs.dashboard.StaticLongDescriptionProvider;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.views.umbrella.PersistentNonPercentageHorizontalSplitPane;

public class SplitterPanelWithStaticRightSideTextPanel extends SplitterPanelWithRightSideTextPanel
{
	public SplitterPanelWithStaticRightSideTextPanel(MainWindow mainWindowToUse, DisposablePanel leftPanelEditorComponentToUse) throws Exception
	{
		super(mainWindowToUse, leftPanelEditorComponentToUse);		
	}		
		
	@Override
	protected AbstractLongDescriptionProvider getDefaultDescriptionProvider() throws Exception
	{
		return new StaticLongDescriptionProvider();
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Edit..");
	}

	@Override
	protected Color getRightPanelBackgroundColor()
	{
		return AppPreferences.getWizardBackgroundColor();
	}

	@Override
	protected String getSplitterIdentifier()
	{
		return "Splitter:" + getPanelDescription();
	}
	
	@Override
	protected PersistentNonPercentageHorizontalSplitPane createRawSplitPaneComponent(MainWindow mainWindowToUse)
	{
		return new PersistentNonPercentageHorizontalSplitPane(this, mainWindowToUse, getSplitterIdentifier());
	}
}
