/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.reports;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.view.JRViewer;

import org.conservationmeasures.eam.dialogs.fieldComponents.PanelButton;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.Miradi;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.umbrella.doers.ExportProjectXmlDoer;
import org.martus.util.DirectoryUtils;

import com.jhlabs.awt.BasicGridLayout;

public class ReportSplitPane extends JSplitPane
{
	public ReportSplitPane(Project projectToUse) throws Exception
	{
		project = projectToUse;
		
		createButtonHashMap();
		setLeftComponent(createReportSelectionPanel());
		setRightComponent(new JPanel());
	}
	
	private JPanel generateReport(String reportPath) throws Exception
	{
		URL resourcePath = Miradi.class.getResource(reportPath);
		InputStream input = resourcePath.openStream();

		File directory = File.createTempFile("MiradiXML", null);
		directory.delete();
		directory.mkdir();
		File xmlFile = ExportProjectXmlDoer.exportProjectToXml(getProject(), directory);

		JPanel reportPanel = getReport(input, xmlFile);
		input.close();
		DirectoryUtils.deleteEntireDirectoryTree(directory);

		return reportPanel;
	}
	
	public JPanel getReport(InputStream reportInput, File xmlFile) throws Exception
	{
		HashMap parameters = new HashMap();
		parameters.put("DATA_DIRECTORY", xmlFile.getParent());
		JRXmlDataSource xmlDataSource = new JRXmlDataSource(xmlFile, "/MiradiProject");
		JasperPrint print = JasperFillManager.fillReport(reportInput, parameters, xmlDataSource);

		JPanel reportPanel = new JPanel(new BorderLayout());
		reportPanel.add(new JRViewer(print));
		
		return reportPanel;
	}
	
	private JPanel createReportSelectionPanel()
	{
		JPanel selectionPanel = new JPanel(new BasicGridLayout(0, 1));
		
		Set keys = buttonHashMap.keySet();
		for(Object key : keys)
		{
			PanelButton reportButton = new PanelButton(key.toString());
			reportButton.addActionListener(new ReportGenerator());
			selectionPanel.add(reportButton);
		}
		
		return selectionPanel;
	}
	
	private void createButtonHashMap()
	{
		buttonHashMap = new HashMap<String, String>();
		buttonHashMap.put("Rare Report", "/reports/RareReport.jasper");
		buttonHashMap.put("Diagram Report", "/reports/AllConceptualModelsReport.jasper");
	}
	
	public  class ReportGenerator implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			try
			{
				PanelButton source = (PanelButton) event.getSource();
				String value = buttonHashMap.get(source.getText()).toString();
				setRightComponent(generateReport(value));
			}
			catch (Exception e)
			{
				EAM.logException(e);
				EAM.errorDialog(EAM.text("Error while generating a report"));
			}
		}	
	}
	
	private Project getProject()
	{
		return project;
	}
	
	private HashMap buttonHashMap;
	private Project project;
}
