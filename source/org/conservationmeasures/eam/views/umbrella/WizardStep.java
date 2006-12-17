/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

import java.util.HashMap;

import javax.swing.JScrollPane;

import org.martus.swing.HtmlViewer;


public class WizardStep extends SkeletonWizardStep
{
	public WizardStep(WizardPanel wizardToUse)
	{
		super(wizardToUse);

		htmlViewer = new WizardHtmlViewer(this);
		JScrollPane scrollPane = new JScrollPane(htmlViewer);
		add(scrollPane);
	}

	public void refresh() throws Exception
	{
		String htmlText = getText();
		htmlViewer.setText(htmlText);
		invalidate();
		validate();
	}
	
	public void valueChanged(String name, String value)
	{
		nameToValueMap.put(name, value);
	}
	
	public String getValue(String name)
	{
		if (nameToValueMap.containsKey(name))
			return (String)nameToValueMap.get(name);
		return "";
	}

	HashMap nameToValueMap = new HashMap();
	private HtmlViewer htmlViewer;
}
