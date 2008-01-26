/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/
package org.conservationmeasures.eam.reports;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;

import javax.swing.JDialog;
import javax.swing.JPanel;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.view.JRViewer;

import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiLabel;

public class MiradiReport
{
	public MiradiReport(Project projectToUse)
	{
		project = projectToUse;
	}

	public void getReport(InputStream reportInput, File xmlFile) throws Exception
	{
		HashMap parameters = new HashMap();
		JRXmlDataSource xmlDataSource = new JRXmlDataSource(xmlFile, "/MiradiProject");
		JasperPrint print = JasperFillManager.fillReport(reportInput, parameters, xmlDataSource);
		getReport(print);
	}

	
	private void getReport(JasperPrint jPrint)
	{
		Dialog g = new JDialog();
		g.setUndecorated(false);
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(getExplanatoryTextComponent(),BorderLayout.BEFORE_FIRST_LINE);
		panel.add(new JRViewer(jPrint));
		g.add(panel);
		g.pack();
		g.setSize(new Dimension(900,600));
		g.setVisible(true);
	}
	
	private Component getExplanatoryTextComponent()
	{
		UiLabel label= new UiLabel();
		label.setText("<html>" +
		"<p><strong><em>This feature is still under development</em></strong></p>" +
		"<br><p>A preview of the project report is shown below. " +
		"To generate this report as a PDF, RTF, or other format file, " +
		"click on the 'disk' icon below.</p>" +
		"<br>" +
		"</html>");
		return label;
	}
	
	Project project;
	
}
