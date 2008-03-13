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

import javax.swing.JComponent;
import javax.swing.JPanel;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRXmlDataSource;

import org.martus.util.DirectoryUtils;
import org.miradi.dialogs.base.MiradiPanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.project.Project;
import org.miradi.utils.FlexibleWidthHtmlViewer;
import org.miradi.views.umbrella.PersistentNonPercentageHorizontalSplitPane;
import org.miradi.views.umbrella.doers.ExportProjectXmlDoer;

public abstract class ReportSplitPane extends PersistentNonPercentageHorizontalSplitPane
{
	public ReportSplitPane(MainWindow mainWindowToUse, String uniqueSpitterName) throws Exception
	{
		super(mainWindowToUse, mainWindowToUse, uniqueSpitterName);
		
		mainWindow = mainWindowToUse;
		
		clear();
		
		// Need to set background color because Control Bar is a box which is transparent
		setBackground(AppPreferences.getControlPanelBackgroundColor());
	}
	
	public void clear()
	{
		setLeftComponent(getReportControlBar());
		setRightComponent(createInstructionsSection());
		getReportControlBar().clearSelection();
	}

	private JComponent createInstructionsSection()
	{
		return FlexibleWidthHtmlViewer.createFromResourceFile(getMainWindow(), "ReportHelpInstructions.html");		
	}
	
	private JComponent createAboveToolbarInstructionsSection()
	{
		return FlexibleWidthHtmlViewer.createFromResourceFile(getMainWindow(), "AboveToolbarInstructions.html");
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
		reportPanel.add(createAboveToolbarInstructionsSection(), BorderLayout.PAGE_START);
		reportPanel.add(new ReportPreviewPanel(print), BorderLayout.CENTER);
		
		return reportPanel;
	}

	public void showReport(URL value)
	{
		Cursor cursor = getMainWindow().getCursor();
		getMainWindow().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
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
			getMainWindow().setCursor(cursor);
		}
	}

	protected Project getProject()
	{
		return getMainWindow().getProject();
	}
	
	protected MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	abstract protected ReportsViewControlBar getReportControlBar();
	
	private MainWindow mainWindow;
}
