package org.conservationmeasures.eam.reports;

import java.awt.Dialog;
import java.awt.Dimension;
import java.util.HashMap;

import javax.swing.JDialog;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JRViewer;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;

public class MiradiReport
{
	public MiradiReport(Project projectToUse)
	{
		project = projectToUse;
	}

	public void getReport(String reportFile)
	{
		try
		{
			HashMap parameters = new HashMap();
			JasperPrint print = JasperFillManager.fillReport(reportFile, parameters, new MiradiDataSource(project));
			getReport(print);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}

	private void getReport(JasperPrint jPrint)
	{
        Dialog g = new JDialog();
        g.setUndecorated(false);
        g.add(new JRViewer(jPrint));
        g.pack();
        g.setSize(new Dimension(900,600));
        g.setVisible(true);
	}
	
	Project project;
}
