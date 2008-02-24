/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.reports;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;

import javax.swing.JFileChooser;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.layout.OneColumnPanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.Miradi;
import org.miradi.utils.JasperReportFileFilter;
import org.miradi.utils.MiradiScrollPane;

public class ReportsViewControlBar extends OneColumnPanel
{
	ReportsViewControlBar(ReportSplitPane ownerToUse)
	{
		setBackground(AppPreferences.getControlPanelBackgroundColor());
		owner = ownerToUse;
		
		reportSelectionTableModel = new ReportSelectionTableModel();
		table = new ReportSelectionTable(reportSelectionTableModel);
		table.getSelectionModel().addListSelectionListener(new TableSelectionListener());
		MiradiScrollPane scroller = new MiradiScrollPane(table);
		scroller.setMinimumSize(new Dimension(0, 0));
		
		PanelButton customReportButton = new PanelButton(EAM.text("Run External Report..."));
		customReportButton.setMinimumSize(new Dimension(0, 0));
		customReportButton.addActionListener(new CustomReportHandler());

		add(scroller);
		add(customReportButton);
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
				File reportPath = new File(home, EXTERNAL_REPORTS_DIR_NAME);
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
				if (customReportChooser.showDialog(owner.getMainWindow(), OPEN_BUTTON_TEXT) != JFileChooser.APPROVE_OPTION)
					return;
				
				File fileToImport = customReportChooser.getSelectedFile();
				URL reportURL = fileToImport.toURI().toURL();
				owner.showReport(reportURL);
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
			int selectedRow = table.getSelectedRow();
			String reportPath = reportSelectionTableModel.getReportDirForRow(selectedRow);

			URL reportURL = Miradi.class.getResource(reportPath);
			owner.showReport(reportURL);
		}

	}

	private ReportSplitPane owner;
	private ReportSelectionTableModel reportSelectionTableModel;
	private ReportSelectionTable table;
	public static final String EXTERNAL_REPORTS_DIR_NAME = "ExternalReports";
	
}
