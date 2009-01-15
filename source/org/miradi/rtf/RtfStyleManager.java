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

import java.awt.Color;
import java.util.Vector;

import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.AccountingCode;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Cause;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.FundingSource;
import org.miradi.objects.Goal;
import org.miradi.objects.Indicator;
import org.miradi.objects.IntermediateResult;
import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.objects.Measurement;
import org.miradi.objects.Objective;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.objects.Strategy;
import org.miradi.objects.Target;
import org.miradi.objects.Task;
import org.miradi.objects.ThreatReductionResult;
import org.miradi.questions.ChoiceQuestion;

public class RtfStyleManager
{
	public RtfStyleManager()
	{
		clear();
	}
	
	private void clear()
	{
		sortedStyles = new Vector();
		resetColors();
		
		createNewStyle(NORMAL_STYLE_TAG,                    	FS_20_RTF_ID," \\sbasedon222\\snext0{\\*\\keycode \\shift\\ctrl n} ", "Normal");		
		createNewStyle(HEADING_1_STYLE_TAG,						S_1_RTF_ID, HEADING_1_STYLE, "Heading 1");
		createNewStyle(HEADING_2_STYLE_TAG,						S_2_RTF_ID, HEADING_2_STYLE, "Heading 2");
		createNewStyle(HEADING_3_STYLE_TAG,						S_3_RTF_ID, HEADING_3_STYLE, "Heading 3");
		
		createNewStyle(Goal.OBJECT_NAME,                 		CS_13_RTF_ID, " \\f1\\b\\fs20 ", MIRADI_STYLE_PREFIX + "Goal");
		createNewStyle(ProjectMetadata.OBJECT_NAME,		 		CS_14_RTF_ID, " \\f1\\b\\i\\fs20 ", MIRADI_STYLE_PREFIX + "Project");
		createNewStyle(COMMENT_STYLE_TAG, 						CS_15_RTF_ID, " \\f1\\fs18 ", MIRADI_STYLE_PREFIX + "Lng Txt");
		createNewStyle(ConceptualModelDiagram.OBJECT_NAME, 		CS_16_RTF_ID, " \\f1\\b\\fs20 ", MIRADI_STYLE_PREFIX + "CM");
		createNewStyle(ResultsChainDiagram.OBJECT_NAME,    		CS_17_RTF_ID, " \\f1\\b\\fs20 ", MIRADI_STYLE_PREFIX + "RC");
		createNewStyle(Target.OBJECT_NAME,                 		CS_18_RTF_ID, " \\f1\\b\\fs20 ", MIRADI_STYLE_PREFIX + "Target");
		createNewStyle(Cause.OBJECT_NAME_THREAT,           		CS_19_RTF_ID, " \\f1\\b\\fs20 ", MIRADI_STYLE_PREFIX + "DThrt");
		createNewStyle(Cause.OBJECT_NAME_CONTRIBUTING_FACTOR,   CS_20_RTF_ID, " \\f1\\b\\fs20 ", MIRADI_STYLE_PREFIX + "CFctr");
		createNewStyle(ThreatReductionResult.OBJECT_NAME,  	 	CS_21_RTF_ID, " \\f1\\b\\fs20 ", MIRADI_STYLE_PREFIX + "TRR");
		createNewStyle(IntermediateResult.OBJECT_NAME,   	    CS_22_RTF_ID, " \\f1\\b\\fs20 ", MIRADI_STYLE_PREFIX + "IR");
		createNewStyle(Objective.OBJECT_NAME,  	    			CS_23_RTF_ID, " \\f1\\b\\fs20 ", MIRADI_STYLE_PREFIX + "Obj");
		createNewStyle(Strategy.OBJECT_NAME,  	    			CS_24_RTF_ID, " \\f1\\fs20 ", MIRADI_STYLE_PREFIX + "Strat");
		createNewStyle(Task.ACTIVITY_NAME,  	    			CS_25_RTF_ID, " \\f1\\fs18 ", MIRADI_STYLE_PREFIX + "Act");
		createNewStyle(Indicator.OBJECT_NAME,  	    			CS_26_RTF_ID, " \\f1\\fs20 ", MIRADI_STYLE_PREFIX + "Ind");
		createNewStyle(Task.METHOD_NAME,   	    				CS_27_RTF_ID, " \\f1\\fs18 ", MIRADI_STYLE_PREFIX + "Mthd");
		createNewStyle(Task.OBJECT_NAME,   	    				CS_28_RTF_ID, " \\f1\\fs18 ", MIRADI_STYLE_PREFIX + "Task");
		createNewStyle(Measurement.OBJECT_NAME,   	    		CS_29_RTF_ID, " \\f1\\fs18 ", MIRADI_STYLE_PREFIX + "Msrmnt");
		createNewStyle(AccountingCode.OBJECT_NAME,  	    	CS_30_RTF_ID, " \\f1\\fs18 ", MIRADI_STYLE_PREFIX + "Accntng Cd");
		createNewStyle(FundingSource.OBJECT_NAME, 	 	    	CS_31_RTF_ID, " \\f1\\fs18 ", MIRADI_STYLE_PREFIX + "Fndng Src");
		createNewStyle(KeyEcologicalAttribute.OBJECT_NAME,   	CS_32_RTF_ID, " \\f1\\fs20 ", MIRADI_STYLE_PREFIX + "KEA");
		createNewStyle(COLUMN_HEADER_STYLE_TAG, 				CS_33_RTF_ID, " \\f1\\b\\fs24 ", MIRADI_STYLE_PREFIX + "Col Hdr");
	}

	private void createNewStyle(String objectName, String rtfStyleId, String rtfFormatingCommand, String styleName)
	{
		RtfStyle rtfStyle = new RtfStyle(objectName, rtfStyleId, rtfStyleId + rtfFormatingCommand, styleName);
		getSortedStyles().add(rtfStyle);
	}

	public void exportRtfStyleTable(RtfWriter writer) throws Exception
	{	
		writer.newLine();
		writer.startBlock();
		writer.writelnRaw("\\stylesheet ");
		for(RtfStyle rtfStyle : getSortedStyles())
		{
			writer.startBlock();
			writer.writeRaw(rtfStyle.getRtfFormatingCommand() + rtfStyle.getStyleName() + ";");
			writer.endBlockLn();
		}
		
		writer.endBlockLn();
	}
	
	public String createStyleTag(BaseObject baseObject)
	{
		return baseObject.getTypeName();
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
		
		if (ObjectType.FAKE == objectType)
			return "";
		
		throw new RuntimeException("Could not find object name for type,  should only use this method if no object is available. type = " + objectType);
	}
	
	public String getStyleFormatingCommand(String styleTagToUse)
	{
		for (int index = 0; index < getSortedStyles().size(); ++index)
		{
			RtfStyle rtfStyle = getSortedStyles().get(index);
			if (rtfStyle.getStyleTag().equals(styleTagToUse))
				return rtfStyle.getRtfFormatingCommand();
		}
	
		
		return "";
	}

	private Vector<RtfStyle> getSortedStyles()
	{
		return sortedStyles;
	}
	
	private void resetColors()
	{
		availableColors = new Vector();
		
		availableColors.add(Color.black);
		availableColors.add(Color.blue);
		availableColors.add(Color.cyan);
		availableColors.add(Color.magenta);
		availableColors.add(Color.red);
		availableColors.add(Color.yellow);
		availableColors.add(Color.white);
		availableColors.add(ChoiceQuestion.DARK_GREEN);
		availableColors.add(ChoiceQuestion.LIGHT_GREEN);
		availableColors.add(ChoiceQuestion.DARK_YELLOW);
	}
	
	public Vector<Color> getAvailableColors()
	{
		return availableColors;
	}
	
			
	private Vector<RtfStyle> sortedStyles;
	private Vector<Color> availableColors;
	
	public static final String MIRADI_STYLE_PREFIX = "Miradi: ";
	public static final String COLUMN_HEADER_STYLE_TAG = "ColumnHeaderStyle";
	public static final String COMMENT_STYLE_TAG = "CommentStyle";
	public static final String NORMAL_STYLE_TAG = "NormalStyle";
	
	public static final String HEADING_1_STYLE_TAG = "Heading1Style";
	public static final String HEADING_2_STYLE_TAG = "Heading2Style";
	public static final String HEADING_3_STYLE_TAG = "Heading3Style";
	
	public static final String FS_20_RTF_ID = "\\fs20";
	public static final String S_1_RTF_ID = "\\s1";
	public static final String S_2_RTF_ID = "\\s2";
	public static final String S_3_RTF_ID = "\\s3";
	
	public static final String CS_13_RTF_ID = "\\cs13";
	public static final String CS_14_RTF_ID = "\\cs14";
	public static final String CS_15_RTF_ID = "\\cs15";
	public static final String CS_16_RTF_ID = "\\cs16";
	public static final String CS_17_RTF_ID = "\\cs17";
	public static final String CS_18_RTF_ID = "\\cs18";
	public static final String CS_19_RTF_ID = "\\cs19";
	public static final String CS_20_RTF_ID = "\\cs20";
	public static final String CS_21_RTF_ID = "\\cs21";
	public static final String CS_22_RTF_ID = "\\cs22";
	public static final String CS_23_RTF_ID = "\\cs23";
	public static final String CS_24_RTF_ID = "\\cs24";
	public static final String CS_25_RTF_ID = "\\cs25";
	public static final String CS_26_RTF_ID = "\\cs26";
	public static final String CS_27_RTF_ID = "\\cs27";
	public static final String CS_28_RTF_ID = "\\cs28";
	public static final String CS_29_RTF_ID = "\\cs29";
	public static final String CS_30_RTF_ID = "\\cs30";
	public static final String CS_31_RTF_ID = "\\cs31";
	public static final String CS_32_RTF_ID = "\\cs32";
	public static final String CS_33_RTF_ID = "\\cs33";
	public static final String HEADING_1_STYLE = " \\f1\\b\\fs36 ";
	public static final String HEADING_2_STYLE = " \\f1\\b\\fs28 ";
	public static final String HEADING_3_STYLE = " \\f1\\b\\fs24 ";
}
