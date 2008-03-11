/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.reports;

import java.lang.reflect.Method;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.util.JRClassLoader;
import net.sf.jasperreports.view.JRSaveContributor;
import net.sf.jasperreports.view.JRViewer;

public class ReportPreviewPanel extends JRViewer
{
	public ReportPreviewPanel(JasperPrint print)
	{
		super(print);
	}

	@Override
	protected void initSaveContributors()
	{
		// NOTE: Most of this code was duplicated from the JRViewer class
		// because it doesn't expose the ability to override the parts we need to
		final String[] DEFAULT_CONTRIBUTORS =
		{
			"net.sf.jasperreports.view.save.JRPdfSaveContributor",
			"net.sf.jasperreports.view.save.JRRtfSaveContributor",
			"net.sf.jasperreports.view.save.JROdtSaveContributor",
			"net.sf.jasperreports.view.save.JRHtmlSaveContributor",
		};

		for(int i = 0; i < DEFAULT_CONTRIBUTORS.length; i++)
		{
			try
			{
				Class saveContribClass = JRClassLoader.loadClassForName(DEFAULT_CONTRIBUTORS[i]);
				Method method = saveContribClass.getMethod("getInstance", (Class[])null);
				JRSaveContributor saveContrib = (JRSaveContributor)method.invoke(null, (Object[])null);
				saveContributors.add(saveContrib);
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
		}
	}

	
}
