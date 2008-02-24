/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.reports;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

import javax.swing.JPanel;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.view.JRViewer;

import org.martus.util.DirectoryUtils;
import org.miradi.dialogs.base.MiradiPanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.project.Project;
import org.miradi.views.umbrella.PersistentHorizontalSplitPane;
import org.miradi.views.umbrella.doers.ExportProjectXmlDoer;

public class ReportSplitPane extends PersistentHorizontalSplitPane
{
	public ReportSplitPane(MainWindow mainWindowToUse) throws Exception
	{
		super(mainWindowToUse, mainWindowToUse, UNIQUE_SPLITTER_NAME);
		
		mainWindow = mainWindowToUse;
		
		setLeftComponent(new ReportsViewControlBar(this));
		setRightComponent(new MiradiPanel());
		
		// Need to set background color because Control Bar is a box which is transparent
		setBackground(AppPreferences.getControlPanelBackgroundColor());
	}
	
	private JPanel createReportPanel(URL reportURL) throws Exception
	{
		InputStream input = reportURL.openStream();
		File directory = File.createTempFile("MiradiXML", null);
		directory.delete();
		directory.mkdir();
		File xmlFile = ExportProjectXmlDoer.exportProjectToXml(getProject(), directory);

		MiradiPanel reportPanel = fillReportInsidePanel(input, xmlFile);
		input.close();
		DirectoryUtils.deleteEntireDirectoryTree(directory);

		return reportPanel;
	}
	
	public MiradiPanel fillReportInsidePanel(InputStream reportInput, File xmlFile) throws Exception
	{
		HashMap parameters = new HashMap();
		parameters.put("DATA_DIRECTORY", xmlFile.getParent());
		JRXmlDataSource xmlDataSource = new JRXmlDataSource(xmlFile, "/MiradiProject");
		JasperPrint print = JasperFillManager.fillReport(reportInput, parameters, xmlDataSource);

		MiradiPanel reportPanel = new MiradiPanel(new BorderLayout());
		reportPanel.add(new JRViewer(print));
		
		return reportPanel;
	}

	public void showReport(URL value)
	{
		Cursor cursor = getMainWindow().getCursor();
		mainWindow.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		try
		{
			setRightComponent(createReportPanel(value));
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

	private Project getProject()
	{
		return getMainWindow().getProject();
	}
	
	public MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	private MainWindow mainWindow;
	
	private static final String UNIQUE_SPLITTER_NAME = "ReportSplitPaneName";
}
