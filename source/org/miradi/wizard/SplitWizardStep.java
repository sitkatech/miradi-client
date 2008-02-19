/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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
