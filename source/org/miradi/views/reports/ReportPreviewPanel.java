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

import net.sf.jasperreports.engine.JasperPrint;
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
		addSaveContributor(new MiradiPdfSaveContributor());
		addSaveContributor(new MiradiRtfSaveContributor());
		addSaveContributor(new MiradiOdtSaveContributor());
		addSaveContributor(new MiradiHtmlSaveContributor());
	}

	
}
