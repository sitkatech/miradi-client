package org.conservationmeasures.eam.project;

import java.util.HashMap;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRRtfExporter;

import org.conservationmeasures.eam.main.EAM;

public class MiradiReport
{
	public MiradiReport(Project projectToUse)
	{
		project = projectToUse;
	}
	
	public void getPDFReport(String reportFile, String fileOut)
	{
		try
		{
			JasperPrint print = getJasperPrint(reportFile);
			JasperExportManager.exportReportToPdfFile(print,fileOut);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}


	public void getRTFReport(String reportFile, String fileOut)
	{
		try
		{
			JasperPrint print = getJasperPrint(reportFile);
			JRRtfExporter exporter = new JRRtfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, fileOut);		    		   
			exporter.exportReport();
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}
	
	private JasperPrint getJasperPrint(String reportFile) throws JRException
	{
		HashMap parameters = new HashMap();
	//	parameters.put("MiradiSubReportDataSource", new MiradiDataSource(project));
		JasperPrint print = JasperFillManager.fillReport(reportFile, parameters, new MiradiDataSource(project));
		return print;
	}
	
	
	Project project;
}
