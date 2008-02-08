/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.wizard;

import javax.swing.JSplitPane;

import org.conservationmeasures.eam.dialogs.fieldComponents.HtmlFormViewer;
import org.conservationmeasures.eam.main.AppPreferences;
import org.conservationmeasures.eam.utils.MiradiScrollPane;


public class SplitWizardStep extends SkeletonWizardStep
{
	public SplitWizardStep(WizardPanel wizardToUse, String viewNameToUse)
	{
		super(wizardToUse, viewNameToUse);

		htmlViewerLeft = new WizardHtmlViewer(getMainWindow(),this);
		htmlViewerRight = new WizardRightSideHtmlViewer(getMainWindow(),this);
		htmlViewerRight.setBackground(AppPreferences.getSideBarBackgroundColor());

		JSplitPane splitPane = new JSplitPane();
		splitPane.setLeftComponent(new MiradiScrollPane(htmlViewerLeft));
		splitPane.setRightComponent(new MiradiScrollPane(htmlViewerRight));
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
