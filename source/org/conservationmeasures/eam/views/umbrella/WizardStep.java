/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

import java.awt.BorderLayout;
import java.io.IOException;
import java.net.URL;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.conservationmeasures.eam.main.EAM;
import org.martus.swing.HtmlViewer;
import org.martus.swing.HyperlinkHandler;
import org.martus.util.UnicodeReader;

public abstract class WizardStep extends JPanel implements HyperlinkHandler
{
	public WizardStep(WizardPanel wizardToUse)
	{
		super(new BorderLayout());
		wizard = wizardToUse;

		htmlViewer = new WizardHtmlViewer(this);
		JScrollPane scrollPane = new JScrollPane(htmlViewer);
		add(scrollPane);
	}
	
	public WizardPanel getWizard()
	{
		return wizard;
	}

	public String getText() throws Exception
	{
		String resourceFileName = getResourceFileName();
		if(resourceFileName == null)
			return "Missing text";
		
		return loadHtmlFile(getClass(), resourceFileName);
	}

	public static String loadHtmlFile(Class thisClass, String resourceFileName) throws IOException
	{
		URL htmlFile = thisClass.getResource(resourceFileName);
		UnicodeReader reader = new UnicodeReader(htmlFile.openStream());
		try
		{
			return reader.readAll();
		}
		finally
		{
			reader.close();
		}
	}

	public String getResourceFileName()
	{
		return null;
	}
	
	public boolean save() throws Exception
	{
		return true;
	}
	
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
