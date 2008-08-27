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
package org.miradi.rtf;

import java.util.HashMap;
import java.util.Set;

import org.miradi.objects.AccountingCode;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Cause;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.FundingSource;
import org.miradi.objects.Goal;
import org.miradi.objects.Indicator;
import org.miradi.objects.IntermediateResult;
import org.miradi.objects.Measurement;
import org.miradi.objects.Objective;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.objects.Strategy;
import org.miradi.objects.Target;
import org.miradi.objects.Task;
import org.miradi.objects.ThreatReductionResult;

public class RtfStyleManager
{
	public RtfStyleManager()
	{
		clear();
	}
	
	private void clear()
	{
		styleMap = new HashMap();
		
		createNewStyle(NORMAL_STYLE_TAG,                    	FS_20_RTF_ID," \\sbasedon222\\snext0{\\*\\keycode \\shift\\ctrl n} ", "Normal");
		createNewStyle(Goal.OBJECT_NAME,                 		S_13_RTF_ID, " \\b\\i\\fs28 ", "Goal");
		createNewStyle(ProjectMetadata.OBJECT_NAME,		 		S_14_RTF_ID, " \\b\\i\\fs28 ", "Project Metadata");
		createNewStyle(COMMENT_STYLE_TAG, 						S_15_RTF_ID, " \\b\\i\\fs12 ", "Long Text");		
		createNewStyle(ConceptualModelDiagram.OBJECT_NAME, 		S_16_RTF_ID, " \\b\\fs16 ", "Conceptual Model Diagram");
		createNewStyle(ResultsChainDiagram.OBJECT_NAME,    		S_17_RTF_ID, " \\b\\fs16 ", "Results Chain Diagram");
		createNewStyle(Target.OBJECT_NAME,                 		S_18_RTF_ID, " \\b\\fs12 ", "Target");
		createNewStyle(Cause.OBJECT_NAME_THREAT,           		S_19_RTF_ID, " \\b\\fs14 ", "Direct Threat");
		createNewStyle(Cause.OBJECT_NAME_CONTRIBUTING_FACTOR,   S_20_RTF_ID, " \\b\\fs14 ", "Contributing Factor");
		createNewStyle(ThreatReductionResult.OBJECT_NAME,  	 	S_21_RTF_ID, " \\b\\fs14 ", "Threat Reduction Result");
		createNewStyle(IntermediateResult.OBJECT_NAME,   	    S_22_RTF_ID, " \\b\\fs14 ", "Intermediate Result");
		createNewStyle(Objective.OBJECT_NAME,  	    			S_23_RTF_ID, " \\b\\fs14 ", "Objective");
		createNewStyle(Strategy.OBJECT_NAME,  	    			S_24_RTF_ID, " \\b\\fs14 ", "Strategy");
		createNewStyle(Task.ACTIVITY_NAME,  	    			S_25_RTF_ID, " \\b\\fs14 ", "Activity");
		createNewStyle(Indicator.OBJECT_NAME,  	    			S_26_RTF_ID, " \\b\\fs14 ", "Indicator");
		createNewStyle(Task.METHOD_NAME,   	    				S_27_RTF_ID, " \\b\\i\\fs14 ", "Method");
		createNewStyle(Task.OBJECT_NAME,   	    				S_28_RTF_ID, " \\b\\i\\fs14 ", "Task");
		createNewStyle(Measurement.OBJECT_NAME,   	    		S_29_RTF_ID, " \\b\\i\\fs14 ", "Measurement");
		createNewStyle(AccountingCode.OBJECT_NAME,  	    	S_30_RTF_ID, " \\b\\fs14 ", "Accounting Code");
		createNewStyle(FundingSource.OBJECT_NAME, 	 	    	S_31_RTF_ID, " \\b\\fs14 ", "Funding Source");
	}

	private void createNewStyle(String objectName, String rtfStyleId, String rtfFormatingCommand, String styleName)
	{
		RtfStyle rtfStyle = new RtfStyle(objectName, rtfStyleId, rtfStyleId + rtfFormatingCommand, styleName);
		getStyleMap().put(objectName, rtfStyle);
	}

	public void exportRtfStyleTable(RtfWriter writer) throws Exception
	{	
		writer.newLine();
		writer.startBlock();
		writer.writeln("\\stylesheet ");
		Set<String> keys = getStyleMap().keySet();
		for(String key : keys)
		{
			writer.startBlock();
			RtfStyle rtfStyle = getStyleMap().get(key);
			writer.write(rtfStyle.getRtfFormatingCommand() + rtfStyle.getStyleName() + ";");
			writer.endBlock();
		}
		
		writer.endBlock();
	}
	
	public String createStyleTag(BaseObject baseObject)
	{
		return baseObject.getTypeName();
	}
	
	private HashMap<String, RtfStyle> getStyleMap()
	{
		return styleMap;
	} 
	
	public static String createTag(BaseObject baseObjectForRow)
	{
		return baseObjectForRow.getTypeName();
	}
	
	public static String createTag(int objectType)
	{
		if (ConceptualModelDiagram.is(objectType))
			return ConceptualModelDiagram.OBJECT_NAME;
		
		if (ProjectMetadata.is(objectType))
			return ProjectMetadata.OBJECT_NAME;
		
		if (Goal.is(objectType))
			return Goal.OBJECT_NAME;
		
		throw new RuntimeException("Could not find object name for type,  should only use this method if no object is available. type = " + objectType);
	}
	
	//FIXME this is a a temporary method
	public String getStyleFormatingCommand(String styleTag)
	{
		RtfStyle rtfStyle = getStyleMap().get(styleTag);
		if (rtfStyle != null)
			return rtfStyle.getRtfFormatingCommand();
		
		return "";
	}
			
	private HashMap<String, RtfStyle> styleMap;
	
	public static final String COMMENT_STYLE_TAG = "CommentStyle";
	public static final String NORMAL_STYLE_TAG = "NormalStyle";
	public static final String FS_20_RTF_ID = "\\fs20";
	
	public static final String S_13_RTF_ID = "\\s13";
	public static final String S_14_RTF_ID = "\\s14";
	public static final String S_15_RTF_ID = "\\s15";
	public static final String S_16_RTF_ID = "\\s16";
	public static final String S_17_RTF_ID = "\\s17";
	public static final String S_18_RTF_ID = "\\s18";
	public static final String S_19_RTF_ID = "\\s19";
	public static final String S_20_RTF_ID = "\\s20";
	public static final String S_21_RTF_ID = "\\s21";
	public static final String S_22_RTF_ID = "\\s22";
	public static final String S_23_RTF_ID = "\\s23";
	public static final String S_24_RTF_ID = "\\s24";
	public static final String S_25_RTF_ID = "\\s25";
	public static final String S_26_RTF_ID = "\\s26";
	public static final String S_27_RTF_ID = "\\s27";
	public static final String S_28_RTF_ID = "\\s28";
	public static final String S_29_RTF_ID = "\\s29";
	public static final String S_30_RTF_ID = "\\s30";
	public static final String S_31_RTF_ID = "\\s31";
}
