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

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.Miradi;
import org.miradi.utils.JasperReportFileFilter;
import org.miradi.utils.MiradiScrollPane;

public class ReportsViewControlBar extends Box
{
	ReportsViewControlBar(ReportSplitPane ownerToUse)
	{
		super(BoxLayout.PAGE_AXIS);
		setBackground(AppPreferences.getControlPanelBackgroundColor());
		owner = ownerToUse;
		
		MiradiScrollPane standardReportPanel = createReportSelectionPanel(new StandardReportsTableModel());
		
		PanelButton customReportButton = new PanelButton(EAM.text("Run External Report..."));
		customReportButton.setMinimumSize(new Dimension(0, 0));
		customReportButton.addActionListener(new CustomReportHandler());
		customReportButton.setAlignmentX(0.0f);

		add(standardReportPanel);
		add(Box.createVerticalGlue());
		add(customReportButton);
	}
	private MiradiScrollPane createReportSelectionPanel(ReportSelectionTableModel standardReportTableModel)
	{
		ReportSelectionTable standardReportTable = new ReportSelectionTable(standardReportTableModel);
		standardReportTable.getSelectionModel().addListSelectionListener(new TableSelectionListener(standardReportTable));

		MiradiScrollPane scroller = new MiradiScrollPane(standardReportTable);
		scroller.setBackground(AppPreferences.getControlPanelBackgroundColor());
		scroller.getViewport().setBackground(AppPreferences.getControlPanelBackgroundColor());
		scroller.setAlignmentX(0.0f);
		return scroller;
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
		public TableSelectionListener(ReportSelectionTable tableToListenTo)
		{
			table = tableToListenTo;
		}
		
		public void valueChanged(ListSelectionEvent event)
		{
			int selectedRow = table.getSelectedRow();
			if(selectedRow < 0)
				return;
			
			String reportPath = table.getReportDirForRow(selectedRow);
			URL reportURL = Miradi.class.getResource(reportPath);
			owner.showReport(reportURL);
		}

		private ReportSelectionTable table;
	}

	private ReportSplitPane owner;
	public static final String EXTERNAL_REPORTS_DIR_NAME = "ExternalReports";
	
}
