/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.conservationmeasures.eam.utils.HtmlViewer;
import org.conservationmeasures.eam.utils.HyperlinkHandler;

public class ThreatRatingWizardPanel extends JPanel implements HyperlinkHandler
{
	public ThreatRatingWizardPanel(ThreatMatrixTableModel model)
	{
		super(new BorderLayout());
		
		String[] threatNames = model.getThreatNames();
		String[] targetNames = model.getTargetNames();
		String htmlText = new ThreatRatingWizardWelcomeText(threatNames, targetNames).getText();
		HtmlViewer contents = new HtmlViewer(htmlText, this);
		
		JScrollPane scrollPane = new JScrollPane(contents);
		add(scrollPane);
	}

	public void clicked(String linkDescription)
	{
		// TODO Auto-generated method stub
		
	}

	public void valueChanged(String widget, String newValue)
	{
		System.out.println("valueChanged for " + widget + " to " + newValue);
	}
}