/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.reports;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Set;

import javax.swing.JPanel;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.view.JRViewer;

import org.conservationmeasures.eam.dialogs.fieldComponents.PanelButton;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.main.Miradi;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.umbrella.PersistentHorizontalSplitPane;
import org.conservationmeasures.eam.views.umbrella.doers.ExportProjectXmlDoer;
import org.martus.util.DirectoryUtils;

import com.jhlabs.awt.BasicGridLayout;

public class ReportSplitPane extends PersistentHorizontalSplitPane
{
	public ReportSplitPane(MainWindow mainWindowToUse) throws Exception
	{
		super(mainWindowToUse, mainWindowToUse, UNIQUE_SPLITTER_NAME);
		
		mainWindow = mainWindowToUse;
		
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
			reportButton.addActionListener(new BuiltInReportHandler());
			selectionPanel.add(reportButton);
		}
		
		return selectionPanel;
	}
	
	private void createButtonHashMap()
	{
		buttonHashMap = new HashMap<String, String>();
		buttonHashMap.put("Conceptual Model Report", "/reports/AllConceptualModelsReport.jasper");
		buttonHashMap.put("Results Chains Report", "/reports/AllResultsChainsReport.jasper");
		buttonHashMap.put("Rare Report", "/reports/RareReport.jasper");
	}
	
	public  class BuiltInReportHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			addCreatedReport(event);
		}

		private void addCreatedReport(ActionEvent event)
		{
			Cursor cursor = getMainWindow().getCursor();
			mainWindow.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			try
			{
				PanelButton source = (PanelButton) event.getSource();
				String value = buttonHashMap.get(source.getText()).toString();
				setRightComponent(generateReport(value));
				restoreSavedLocation();
			}
			catch (Exception e)
			{
				EAM.logException(e);
				EAM.errorDialog(EAM.text("Error while generating a report"));
			}
			finally
			{
				mainWindow.setCursor(cursor);
			}
		}	
	}
	
	private Project getProject()
	{
		return getMainWindow().getProject();
	}
	
	private MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	private HashMap buttonHashMap;
	private MainWindow mainWindow;
	
	private static final String UNIQUE_SPLITTER_NAME = "ReportSplitPaneName";
}
