/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
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

package org.miradi.views.summary;

import javax.swing.Icon;

import org.miradi.dialogs.base.AbstractObjectDataInputPanel;
import org.miradi.icons.MiradiShareIcon;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.utils.FlexibleWidthHtmlViewer;
import org.miradi.utils.Translation;

public class SummaryNonSharedMiradiSharePanel extends AbstractObjectDataInputPanel
{
	public SummaryNonSharedMiradiSharePanel(MainWindow mainWindoToUse) throws Exception
	{
		super(mainWindoToUse.getProject(), ORef.INVALID);
		
		String html = Translation.getHtmlContent("MiradiShareNonSharedProjectHelpPanel.html");
		add(new FlexibleWidthHtmlViewer(mainWindoToUse, html));
	}
	
	@Override
	public String getPanelDescription()
	{
		return SummaryMiradiSharePanel.getPlanningPanelDescription();
	}

	@Override
	public Icon getIcon()
	{
		return new MiradiShareIcon();
	}
}
