/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogs.reportTemplate;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;

import javax.swing.JFileChooser;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.martus.util.UnicodeStringReader;
import org.martus.util.UnicodeStringWriter;
import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.layout.TwoColumnPanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.project.Project;
import org.miradi.xml.wcs.XmpzXmlExporter;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class XsltReportPanel extends TwoColumnPanel
{
	public XsltReportPanel(MainWindow mainWindowToUse)
	{
		mainWindow = mainWindowToUse;
		setBackground(AppPreferences.getDataPanelBackgroundColor());
		addRunButton();
	}
	
	private void addRunButton()
	{
		PanelButton runButton = new PanelButton(EAM.text("Run"));
		runButton.addActionListener(new ActionHandler());
		add(runButton);
	}
	
	private void transform(final File selectedXslFile) 
	{
		try
		{
			final InputSource projectXmlInputSource = getExportedProjectXmlAsString(); 

			DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
			dfactory.setNamespaceAware(true);
			Document doc = dfactory.newDocumentBuilder().parse(projectXmlInputSource);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			final StreamSource xslStreamSource = new StreamSource(selectedXslFile);
			Transformer transformer = transformerFactory.newTransformer(xslStreamSource);
			
			final File outputFile = getUserChosenFile(EAM.text("Select Output File"), EAM.text("Save"));
			if (outputFile != null)
			{				
				transformer.transform(new DOMSource(doc), new StreamResult(new FileOutputStream(outputFile)));
				EAM.notifyDialog(EAM.text("Transformation Completed!"));
			}
		}
		catch (Exception e)
		{
			EAM.unexpectedErrorDialog(e);
			EAM.logException(e);
		}
	}

	private InputSource getExportedProjectXmlAsString() throws Exception
	{
		UnicodeStringWriter projectWriter = UnicodeStringWriter.create();
		try
		{
			new XmpzXmlExporter(getProject()).exportProject(projectWriter);
			projectWriter.flush();
			final String projectXmlAsString = projectWriter.toString();
			UnicodeStringReader reader = new UnicodeStringReader(projectXmlAsString);
			
			return new InputSource(reader);
		}
		finally
		{
			projectWriter.close();
		}
	}

	private File getUserChosenFile(final String diaglogTitle, final String buttonText)
	{
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle(diaglogTitle);
		fileChooser.setDialogType(JFileChooser.CUSTOM_DIALOG);
		if (fileChooser.showDialog(getMainWindow(), buttonText) != JFileChooser.APPROVE_OPTION)
			return null;

		return fileChooser.getSelectedFile();
	}

	private MainWindow getMainWindow()
	{
		return mainWindow;
	}

	private Project getProject()
	{
		return getMainWindow().getProject();
	}

	private class ActionHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			final File selectedFile = getUserChosenFile(EAM.text("Select XSL File"), EAM.text("Load XSL"));
			if (selectedFile != null)
				transform(selectedFile);
		}
	}
			
	private MainWindow mainWindow;
}
