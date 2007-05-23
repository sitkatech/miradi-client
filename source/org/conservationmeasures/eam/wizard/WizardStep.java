/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.wizard;

import javax.swing.JSplitPane;

import org.conservationmeasures.eam.utils.FastScrollPane;
import org.conservationmeasures.eam.utils.HtmlFormViewer;


public class WizardStep extends SkeletonWizardStep
{
	public WizardStep(WizardPanel wizardToUse)
	{
		super(wizardToUse);

		htmlViewerLeft = new WizardHtmlViewer(this);
		htmlViewerRight = new WizardHtmlViewer(this);
		JSplitPane splitPane = new JSplitPane();
		splitPane.setLeftComponent(new FastScrollPane(htmlViewerLeft));
		splitPane.setRightComponent(new FastScrollPane(htmlViewerRight));
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
