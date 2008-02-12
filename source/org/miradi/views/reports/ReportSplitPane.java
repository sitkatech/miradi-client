/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.reports;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.view.JRViewer;

import org.martus.util.DirectoryUtils;
import org.miradi.dialogs.base.MiradiPanel;
import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.layout.OneColumnPanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.main.Miradi;
import org.miradi.project.Project;
import org.miradi.utils.JasperReportFileFilter;
import org.miradi.utils.MiradiScrollPane;
import org.miradi.views.umbrella.PersistentHorizontalSplitPane;
import org.miradi.views.umbrella.doers.ExportProjectXmlDoer;

public class ReportSplitPane extends PersistentHorizontalSplitPane
{
	public ReportSplitPane(MainWindow mainWindowToUse) throws Exception
	{
		super(mainWindowToUse, mainWindowToUse, UNIQUE_SPLITTER_NAME);
		
		mainWindow = mainWindowToUse;
		
		setLeftComponent(createReportSelectionPanel());
		setRightComponent(new MiradiPanel());
	}
	
	private JPanel createReportPanel(String reportPath) throws Exception
	{
		URL resourcePath = Miradi.class.getResource(reportPath);
		return createReportPanel(resourcePath);
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

	private JPanel createReportSelectionPanel()
	{
		OneColumnPanel selectionPanel = new OneColumnPanel();
		selectionPanel.setBackground(AppPreferences.getControlPanelBackgroundColor());
		
		reportSelectionTableModel = new ReportSelectionTableModel();
		table = new ReportSelectionTable(reportSelectionTableModel);
		table.getSelectionModel().addListSelectionListener(new TableSelectionListener());
		MiradiScrollPane scroller = new MiradiScrollPane(table);
		scroller.setMinimumSize(new Dimension(0, 0));
		
		PanelButton customReportButton = new PanelButton(EAM.text("Run Custom Report..."));
		customReportButton.setMinimumSize(new Dimension(0, 0));
		customReportButton.addActionListener(new CustomReportHandler());

		selectionPanel.add(scroller);
		selectionPanel.add(customReportButton);
		
		return selectionPanel;
	}
	
	public class CustomReportHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			askUserForCustomReport();
		}	
		
		private void askUserForCustomReport()
		{
			try
			{
				File home = EAM.getHomeDirectory();
				File reportPath = new File(home, CUSTOM_REPORTS_DIR_NAME);
				if (!reportPath.exists())
					reportPath.mkdir();
				
				String[] reportFilenames = reportPath.list();
				if(reportFilenames == null || reportFilenames.length == 0)
				{
					EAM.notifyDialog(EAM.text("<html>" +
							"<strong>Custom Reports</Strong><br><br>" +
							"Miradi is able to run custom reports created using the free iReports tool. <br>" +
							"This requires technical knowledge, so if you are interested in creating <br>" +
							"your own reports, please contact the Miradi team (email support@miradi.org)"));
				}
				
				JFileChooser customReportChooser = new JFileChooser(reportPath);
				customReportChooser.setDialogTitle(EAM.text("Choose Custom Report"));
				customReportChooser.addChoosableFileFilter(new JasperReportFileFilter());
				customReportChooser.setDialogType(JFileChooser.CUSTOM_DIALOG);
				final String OPEN_BUTTON_TEXT = EAM.text("Open");
				customReportChooser.setApproveButtonToolTipText(OPEN_BUTTON_TEXT);
				if (customReportChooser.showDialog(getMainWindow(), OPEN_BUTTON_TEXT) != JFileChooser.APPROVE_OPTION)
					return;
				
				File fileToImport = customReportChooser.getSelectedFile();
				setRightComponent(createReportPanel(fileToImport.toURI().toURL()));
				restoreSavedLocation();
			}
			catch (Exception e)
			{
				EAM.logException(e);
				EAM.errorDialog("Error ocurred while trying to load custom report");
			}
		}
	}
	
	public class TableSelectionListener implements ListSelectionListener
	{
		public void valueChanged(ListSelectionEvent event)
		{
			Cursor cursor = getMainWindow().getCursor();
			mainWindow.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			try
			{
				int selectedRow = table.getSelectedRow();
				String value = reportSelectionTableModel.getReportDirForRow(selectedRow);
				resetReportPanel(value);
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

	private void resetReportPanel(String value) throws Exception
	{
		setRightComponent(createReportPanel(value));
		restoreSavedLocation();
	}	
	
	private Project getProject()
	{
		return getMainWindow().getProject();
	}
	
	private MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	private MainWindow mainWindow;
	
	private ReportSelectionTableModel reportSelectionTableModel;
	private ReportSelectionTable table;
	
	private static final String UNIQUE_SPLITTER_NAME = "ReportSplitPaneName";
	public static final String CUSTOM_REPORTS_DIR_NAME = "CustomReports";
}
