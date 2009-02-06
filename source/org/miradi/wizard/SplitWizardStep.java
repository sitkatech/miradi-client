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
package org.miradi.wizard;

import javax.swing.JSplitPane;

import org.miradi.dialogs.fieldComponents.HtmlFormViewer;
import org.miradi.main.AppPreferences;
import org.miradi.utils.MiradiWizardScrollPane;


public class SplitWizardStep extends SkeletonWizardStep
{
	public SplitWizardStep(WizardPanel wizardToUse, String viewNameToUse)
	{
		super(wizardToUse, viewNameToUse);

		htmlViewerLeft = new WizardHtmlViewer(getMainWindow(),this);
		htmlViewerRight = new WizardRightSideHtmlViewer(getMainWindow(),this);
		htmlViewerRight.setBackground(AppPreferences.getSideBarBackgroundColor());

		JSplitPane splitPane = new JSplitPane();
		splitPane.setLeftComponent(new MiradiWizardScrollPane(htmlViewerLeft));
		splitPane.setRightComponent(new MiradiWizardScrollPane(htmlViewerRight));
		splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setDividerLocation(600);
		add(splitPane);
	}

	public void refresh() throws Exception
	{
		htmlViewerLeft.setText(getTextLeft());
		htmlViewerRight.setText(getTextRight());
		invalidate();
		validate();
	}
	
	
	private HtmlFormViewer htmlViewerLeft;
	private HtmlFormViewer htmlViewerRight;
}
