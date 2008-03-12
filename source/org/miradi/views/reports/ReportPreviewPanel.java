/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.reports;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JRViewer;
import net.sf.jasperreports.view.save.JRHtmlSaveContributor;
import net.sf.jasperreports.view.save.JROdtSaveContributor;
import net.sf.jasperreports.view.save.JRRtfSaveContributor;

public class ReportPreviewPanel extends JRViewer
{
	public ReportPreviewPanel(JasperPrint print)
	{
		super(print);
	}

	@Override
	protected void initSaveContributors()
	{
		addSaveContributor(new MiradiPdfSaveContributor());
		addSaveContributor(JRRtfSaveContributor.getInstance());
		addSaveContributor(JROdtSaveContributor.getInstance());
		addSaveContributor(JRHtmlSaveContributor.getInstance());
	}

	
}
