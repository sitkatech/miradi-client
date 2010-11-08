/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogs.dashboard;

import org.miradi.dialogs.base.OneFieldObjectDataInputPanel;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class CaptureAndShareLearningTab extends LeftSideRightSideSplitterContainerTab
{
	public CaptureAndShareLearningTab(MainWindow mainWindowToUse, OneFieldObjectDataInputPanel leftPanelToUse) throws Exception
	{
		super(mainWindowToUse, leftPanelToUse);
	}
	
	public static CaptureAndShareLearningTab createTab(MainWindow mainWindowToUse) throws Exception
	{
		return new CaptureAndShareLearningTab(mainWindowToUse, createLeftPanel(mainWindowToUse));
	}

	@Override
	protected AbstractRowDescriptionProvider getMainDescriptionFileName() throws Exception
	{
		return new HtmlResourceRowDescriptionProvider("dashboard/5.html");
	}

	private static OneFieldObjectDataInputPanel createLeftPanel(MainWindow mainWindowToUse) throws Exception
	{
 		CaptureAndShareLearningLeftPanel component = new CaptureAndShareLearningLeftPanel(mainWindowToUse);
 		
 		return new OneFieldObjectDataInputPanel(mainWindowToUse.getProject(), component);
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Capture and Share Learning");
	}
}
