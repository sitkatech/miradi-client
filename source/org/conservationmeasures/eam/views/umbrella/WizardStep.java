/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.conservationmeasures.eam.main.EAM;
import org.martus.swing.HtmlViewer;
import org.martus.swing.HyperlinkHandler;

public abstract class WizardStep extends JPanel implements HyperlinkHandler
{
	public WizardStep(WizardPanel wizardToUse)
	{
		super(new BorderLayout());
		wizard = wizardToUse;

		htmlViewer = new HtmlViewer("", this);
		JScrollPane scrollPane = new JScrollPane(htmlViewer);
		add(scrollPane);
	}
	
	public WizardPanel getWizard()
	{
		return wizard;
	}

	abstract public String getText() throws Exception;
	abstract public boolean save() throws Exception;
	
	public void buttonPressed(String buttonName)
	{
		try
		{
			if(buttonName.indexOf("Next") >= 0)
			{
				if(!save())
					return;
				
				getWizard().next();
			}
			
			if(buttonName.indexOf("Back") >= 0)
			{
				getWizard().previous();
			}
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}
	
	public void linkClicked(String linkDescription)
	{
	}

	public void valueChanged(String widget, String newValue)
	{
	}

	public void refresh() throws Exception
	{
		String htmlText = getText();
		htmlViewer.setText(htmlText);
		invalidate();
		validate();
	}



	private WizardPanel wizard;
	private HtmlViewer htmlViewer;


}
