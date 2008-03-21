/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.views.reports;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;

import javax.swing.JFileChooser;

import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.main.EAM;
import org.miradi.utils.JasperReportFileFilter;

public class CustomReportsViewControlBar extends ReportsViewControlBar
{
	public CustomReportsViewControlBar(ReportSplitPane ownerToUse)
	{
		super(ownerToUse);
				
		PanelButton customReportButton = new PanelButton(EAM.text("Run External Report..."));
		customReportButton.setMinimumSize(new Dimension(0, 0));
		customReportButton.addActionListener(new CustomReportHandler());
		customReportButton.setAlignmentX(0.0f);

		
		add(customReportButton);
	}
	
	protected ReportSelectionTableModel getReportSelectionModel()
	{
		return new CustomReportsTableModel();
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
				if (customReportChooser.showDialog(getOwner().getMainWindow(), OPEN_BUTTON_TEXT) != JFileChooser.APPROVE_OPTION)
					return;
				
				File fileToImport = customReportChooser.getSelectedFile();
				URL reportURL = fileToImport.toURI().toURL();
				getOwner().showReport(reportURL);
			}
			catch (Exception e)
			{
				EAM.logException(e);
				EAM.errorDialog("Error ocurred while trying to load custom report");
			}
		}
	}
}
