/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.interview;

import java.io.IOException;
import java.util.Vector;

import org.conservationmeasures.eam.main.EAM;
import org.martus.util.UnicodeStringReader;

public class InterviewModel
{
	public InterviewModel() throws IOException
	{
		steps = new Vector();
	}
	
	public void loadSteps() throws IOException
	{
		addStep(StepLoader.load(new UnicodeStringReader(templateWelcome)));
		addStep(StepLoader.load(new UnicodeStringReader(template_1_0_1_0_A_2_a)));
		addStep(StepLoader.load(new UnicodeStringReader(template_1_0_1_0_A_2_b)));
		addStep(StepLoader.load(new UnicodeStringReader(template_2_1_2_1_A_3_a)));
		addStep(StepLoader.load(new UnicodeStringReader(template_2_1_2_1_A_3_b)));
	}
	
	public void addStep(InterviewStepModel stepModel)
	{
		steps.add(stepModel);
		if(currentStep == null)
			currentStep = stepModel;
	}
	
	public int getStepCount()
	{
		return steps.size();
	}
	
	public InterviewStepModel getStep(int n)
	{
		return (InterviewStepModel)steps.get(n);
	}
	
	public InterviewStepModel getStep(String stepName)
	{
		for(int i=0; i < steps.size(); ++i)
		{
			InterviewStepModel step = (InterviewStepModel)steps.get(i);
			if(step.getStepName().equals(stepName))
				return step;
		}
		return null;
	}
	
	public String getCurrentStepName()
	{
		return currentStep.getStepName();
	}
	
	public void setCurrentStepName(String newStepName)
	{
		InterviewStepModel destination = getStep(newStepName);
		if(destination == null)
		{
			EAM.logError("Attempted to set step to: " + newStepName);
			return;
		}
		
		currentStep = destination;
	}
	
	public InterviewStepModel getCurrentStep()
	{
		for(int i=0; i < getStepCount(); ++i)
		{
			InterviewStepModel step = getStep(i);
			if(step.getStepName().equals(getCurrentStepName()))
				return step;
		}
		return null;
	}
	
	private InterviewStepModel currentStep;
	private Vector steps;

	// TODO: Extract out to text file(s)
	static public final String templateWelcome = 
		"welcome\n" +
		"1.0.1_0_A.2.a\n" +
		"\n" + 
		":html:\n" +
			"<h2>Interview</h2>" +
			"<table><tr>" +
			"<td valign='top'>" +
			"<font face='Arial'>" +
			"<p>This interview will walk you through each step of the CMP Open Standards.</p>" +
			"</font>" +
			"</td>" +
			"<td valign='top'>" +
			"<h3>Navigation Hints for the Interview</h3>" +
			"<font face='Arial'>" +
			"<p><font size='-1'>The lefthand column typically contains the instructions " +
			"for the current step.  The righthand column (this one) usually contains " +
			"definitions, explanations, or user hints.</font></p>" +
			"<p><font size='-1'>Use the 'Back' and 'Next' buttons to move to the previous or " +
			"next step.   Note that clicking next automatically saves any work " +
			"that you have done on that page.</font></p>" +
			"<p><font size='-1'>Use the 'Step-by-Step' Menu to jump to anther step " +
			"in the overall process.</font></p>" +
			"<p><font size='-1'>The following buttons can be found on most pages and provide:</font></p>" +
			"<ul><font size='-1'>" +
			"<li><strong>Get More Info</strong> - Links to more detailed guidance about the current step.</li>" +
			"<li><strong>Examples</strong> - Links to relevant excerpts from other projects.</li>" +
			"<li><strong>Workshop Hints</strong> - Ideas for how to present this material in a workshop setting, " +
			"primarily using non-computerized methods.</li>" +
			"</font></ul>" +
			"<p><font size='-1'>Once you become experienced with a given step, " +
			"you can use the splitter to hide the interview screen and " +
			"just work directly with the relevant views of your project.</font></p>" +
			"</font>" +
			"</td>" +
			"</tr></table>";

	static public final String template_1_0_1_0_A_2_a = 	
		"1.0.1_0_A.2.a\n" +
		"1.0.1_0_A.2.b\n" +
		"welcome\n" +
		":html:\n" +
			"<h2>Step 1.  Conceptualize</h2>\n" + 
			"<h3>Principle 1A.  Be clear and specific about the issue or problem</h3>" +
			"<hr></hr>\n" +
			"<font face='Arial'>&nbsp;&nbsp;" +
			"<h4>Task 2. Define the scope of the area or theme</strong></h4>" +
			"<font face='Arial'>" +
			"<p>Most conservation projects will focus on a defined geographic " +
			"<a href='none'><em>project area</em></a> " + 
			"that contains the biodiversity that is of interest.  " + 
			"In a few cases, a conservation project may not focus on biodiversity in a specific area, " + 
			"but instead will have a " +
			"<a href='none'><em>theme</em></a> that focuses on a population of wide-ranging animals, " + 
			"such as migratory birds.</p>" +
			"\n" +
			"<p>Describe in a few sentences the project area or theme for your project:</p>\n" + 
			"<textarea rows='5' cols='80'></textarea>" +
			"</font>";
	
	private static final String template_1_0_1_0_A_2_b =
		"1.0.1_0_A.2.b\n" +
		"2.1.2.1.A.3.a\n" +
		"1.0.1_0_A.2.a\n" +
		":html:\n" +
			"<h2>Step 1.  Conceptualize</h2>\n" + 
			"<h3>Principle 1A.  Be clear and specific about the issue or problem</h3>" +
			"<hr></hr>\n" +
			"<font face='Arial'>&nbsp;&nbsp;" +
			"<h4>Task 2. Define the scope of the area or theme</strong></h4>" +
			"<font face='Arial'>" +
			"\n" + 
			"Outline your <a href='none'><em>project area</em></a> on your project map.";
		
	private static final String template_2_1_2_1_A_3_a =
		"2.1.2.1.A.3.a\n" +
		"2.1.2.1.A.3.b\n" +
		"1.0.1_0_A.2.b\n" +
		":html:\n" +
			"<h2>Step 2.1.  Plan Your Actions</h2>\n" +
			"<h3>Principle 2.1 A.  Develop clear goal and objectives</h3>" +
			"<hr></hr>" +
			"<h4><strong>Task 3. Develop Objectives</strong></h4>" +
			"<font face='Arial'>" +
			"<p>An " +
			"<a href='none'><em>objective</em></a> " +
			"is a specific statement detailing the desired accomplishments, " + 
			"milestones or outcomes of a project.  To develop a good objective, " + 
			"select one of your high ranked threats:</p>\n" +
			"<br></br>" +
			"<select>" +
			"<option>Diver Anchor Damage</option>" +
			"<option>Illegal Shark Fishing by Mainland Boats</option>" +
			"<option>Unsustainable Legal Fishing by Locals</option>" +
			"<option>Increased Water Temperatures</option>" +
			"<option>Sewage</option>" +
			"<option>Introduced Predators (Rats)</option>" +
			"<option>Potential Oil Spills</option>" +
			"</select>" +
			"</font>";
	
	private static final String template_2_1_2_1_A_3_b =
		"2.1.2.1.A.3.b\n" +
		"\n" +
		"2.1.2.1.A.3.a\n" +
		":html:\n" +
			"<h2>Step 2.1.  Plan Your Actions</h2>\n" +
			"<h3>Principle 2.1 A.  Develop clear goal and objectives</h3>" +
			"<hr></hr>" +
			"<h4><strong>Task 3. Develop Objectives</strong></h4>" +
			"<font face='Arial'>" +
			"<p>The threat you have selected to address is: 'Cutting Trees'</p>" +
			"<p>Write down a few words that generally summarize how you hope to change this factor:</p>" +
			"<textarea rows='5' cols='80'></textarea>" +
			"</font>";
}
