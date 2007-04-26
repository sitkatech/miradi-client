package org.conservationmeasures.eam.reports;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.io.InputStream;
import java.util.HashMap;

import javax.swing.JDialog;
import javax.swing.JPanel;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JRViewer;

import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiLabel;

public class MiradiReport
{
	public MiradiReport(Project projectToUse)
	{
		project = projectToUse;
	}

	public void getReport(InputStream reportInput) throws Exception
	{
		HashMap parameters = new HashMap();
		JasperPrint print = JasperFillManager.fillReport(reportInput, parameters, new MiradiDataSource(project));
		getReport(print);
	}

	
	private void getReport(JasperPrint jPrint)
	{
		Dialog g = new JDialog();
		g.setUndecorated(false);
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(getBudgetComponent(),BorderLayout.BEFORE_FIRST_LINE);
		panel.add(new JRViewer(jPrint));
		g.add(panel);
		g.pack();
		g.setSize(new Dimension(900,600));
		g.setVisible(true);
	}
	
	private Component getBudgetComponent()
	{
		UiLabel label= new UiLabel();
		label.setText("<html>" +
		"Tools to enter actual expenditures <P></P> and to match these up to budget line items " +
		"and report on them by programmatic objectives and activities, " +
		"by accounting codes, or by funding sources, " +
		"using a Quicken or Quickbooks style interface.</html>");
		return label;
	}
	
	Project project;
	
}
