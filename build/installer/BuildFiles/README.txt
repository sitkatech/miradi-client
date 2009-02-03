Miradi (TM)

Copyright 2005-2008, Foundations of Success, Bethesda, Maryland
     (on behalf of the Conservation Measures Partnership, "CMP") and
     Beneficent Technology, Inc. ("Benetech"), Palo Alto, California.

VERSION 2.4.0

License and Copying

Miradi is distributed under the terms of the GNU General Public License 
version 3, as published by the Free Software Foundation. Miradi is 
distributed in the hope that it will be useful, but without any warranty; 
without even the implied warranty of merchantability or fitness for a 
particular purpose.

----------------------------------------------------------------------


Updates and More Information:

Before running this program, check that you have the latest version at 

  https://miradi.org/download

You can also find more information about this software program 
at www.miradi.org or by e-mailing info@miradi.org. 
Please send suggestions and bug reports to feedback@miradi.org. 

About Miradi

This software program is being developed to assist conservation 
practitioners go through the adaptive management process outlined 
in the CMP's Open Standards for the Practice of Conservation. 
To obtain a copy of the latest version of these standards, 
go to www.conservationmeasures.org.


Third-Party Software Credits

Portions of the code are from the Martus(tm) free, social justice 
documentation and monitoring software, Copyright (C) 2001-2007, 
Beneficent Technology, Inc. ("The Benetech Initiative").

Miradi relies on the following third-party libraries and tools: 
BrowserLauncher2, jcalendar, jgraph, jing, jhlabs, 
jtreetable, jarbuilder, install4j.


---------------------
PREREQUISITES

For MS Windows:
- Windows XP or Vista
- 1 GB RAM recommended
- 80 MB available disk space

For Mac OS X:
- Java 1.5 or later
- 1 GB RAM recommended
- 30 MB available disk space

For GNU/Linux:
- Java 1.5 or later
- 1 GB RAM recommended
- 30 MB available disk space

---------------------
INSTALLING

MS Windows:
- If you had Miradi 2.3.1 or earlier, we recommend that you uninstall it 
  before installing Miradi 2.4 or later
- Download and run MiradiSetup.exe
- A copy of the latest Marine Example project will be installed automatically
- You do not need to have Java already installed on your system, because 
  the installer will offer to automatically download one if necessary.
  If you choose to automatically install Java, it will be (by default) at:
     C:\Program Files\Common Files\i4j_jres


Mac OS X:
- Make sure Apple's official Java 1.5 (aka Java 5) or 1.6 (aka Java 6) 
  is installed (Miradi will not work with Java 1.4). If you are running 
  a current version of OS X, Java should have been installed automatically.
  
- IMPORTANT: If you had manually installed Miradi earlier (from a .zip file), 
  you MUST uninstall the third-party components before installing this new 
  version. To uninstall them, open each of these folders:
    System/Library/Java/Extensions
    Users/[user]/Library/Java/Extensions
  and if any of these files exist, delete them:
      BrowserLauncher2-all-10rc4.jar
      jcalendar-1.3.2.jar
      jing.jar
      jxl.jar
      miradi-jgraph.jar
      TableLayout.jar
  If you do not have the Martus (Human Rights) application on your system,
  delete the following files also:
      icu4j_3_2_calendar.jar
      martus-swing.jar
      martus-utils.jar
      persiancalendar.jar

- Obtain Miradi.dmg from http://miradi.org and open it
- Copy the .app file to your desktop, or to your Applications folder, or 
  wherever you wish to launch it from
- MarineExample.mpz is a sample project that can be imported into Miradi. 


GNU/Linux:

- Make sure Sun's official Java 1.5 (aka Java 5) or 1.6 (aka Java 6) is 
  installed (Miradi will not work with Java 1.4, and has not been tested 
  with any non-Sun Java alternatives such as kaffe or gij)

- Obtain Miradi-Linux.zip from http://miradi.org and unzip the contents 
  into a directory, perhaps /usr/local/lib or ~/Miradi-2.0
  (We recommend not installing to ~/Miradi because that is the default 
  location for project data).

- To run Miradi, use this command line:
    java -Xmx512m -jar <path-to-miradi.jar>
    
- MarineExample.mpz is a sample project that can be imported into Miradi



---------------------
Upgrading From Earlier Versions

If you installed an earlier version of Miradi, you can just 
install this new version right over it. Your projects will be 
migrated automatically when you first open them with the new version.


Here are the most notable improvements of version 2.4 over 2.3.1:

INSTALLERS
- Windows Installer is 85% smaller, for faster downloads
- Much easier Mac installation (using a standard DMG file)

WELCOME
- Welcome page project list now shows Last Modified date for each project
- Welcome page project list can now be sorted by clicking on headers

DIAGRAM
- Ability to have links on some diagrams and not others
- Diagram Factor Properties dialog allows switching between factors
- Create Bendpoint Junction now preserves existing bend points
- Select link, Create Factor gets "inserted" into that link
- Group Boxes can now have custom font and color

THREAT RATING
- Lower panel in Simple mode has been rearranged
- Upper grid visual improvements
- Upper grid now saves column widths and sequence

PLANNING VIEW
- New row and column types available in Planning View
- WorkPlan: Better display of shared/allocated budget values
- Planning tree: Indicators now appear within IR's and TRR's
- Planning tree now remembers width of Item column

REPORTS/RTF
- RTF reports now color entire rating cells

APPWIDE
- New "Save As" functionality replaces "Save Copy To..."
- Row heights are now saved per-table within each project
- Table column widths and sequence are now saved per-project
- Factors now show separate lists for "appears on" Conceptual Model 
  pages and Results Chains

NATURE CONSERVANCY
- TNC tab now has a "data sharing" field
- ConPro (CPMZ) export now includes goals and IR/TRR objectives
- ConPro (CPMZ) import has several minor improvements



Version 2.3.1 was a minor release with these fixes over 2.3:

DIAGRAM:
- FIXED: Deleting a Strategy no longer removes any other shares of that 
  strategy from associated objectives and goals
- FIXED: Planning View export to RTF no longer gets errors for 
  Assignment rows
- Changed Spanish translations: 
  "Aceptable" -> "Regular", and "Malo" -> "Pobre"
- FIXED: Spanish translation was causing Diagram View Control Bar to 
  be too wide
  

Version 2.3 had these enhancements over version 2.2.2:

APPWIDE
- Objectives are now only initally associated with strategies 
  that are directly linked to the containing factor
- Goals now behave more like objectives, so have a Percent Complete 
  field, and can be associated with indicators, strategies, and 
  activities
- ConPro (CPMZ) import/export is now officially supported
- Activities, Methods, and Tasks now have an Id field
- Text fields now support Ctrl-Z/Y for field-level undo/redo
- Date fields now allow abbreviated date entry: entering just a year, 
  or just a year and month, will fill in default values for the rest
- News on the welcome page news is only displayed when it is updated
- Improved Automatic Row Height mode
- Tree expand/collapse now works reliably when items appear more than 
  once within the tree
- Now remember previous language setting when restarting Miradi
- Installing language packs can now be done from within 
  Miradi, avoiding the need to manually copy files around

SUMMARY:
- TNC tab now shows Lessons Learned field
- TNC tab no longer has obsolete readonly legacy Country, 
  Operating Unit, and Ecoregion fields

DIAGRAM:
- New Tagging feature:
  - Can create any number of tags (tag sets)
  - Can assign any factors to any tags
  - Each diagram page can show only factors with certain tags
- Export JPEG/PNG improvements:
  - Can control image resolution by zooming in/out
  - Image size is consistent regardless of which factors are shown 
    or hidden (handy for creating slide show presentations)
  - Works correctly in Brainstorm Mode
- New "Create Margin" feature, which automatically nudges all the 
  factors right/down as needed to create space to insert new factors
- Switching in/out of Brainstorm Mode now automatically resizes 
  group boxes

VIABILITY:
- Target properties can now be viewed/edited in the lower panel
- Targets can be switched between Simple and KEA mode
- Now shows indicators for simple-mode targets, and allows new 
  indicators to be created for simple-mode targets

THREAT RATING:
- Simple and Stress-Based modes now use the same user interface
- Stress-based grid is now sortable
- Stress-based table has better width controls
- No longer shows targets that are only on Results Chains 
  (since they can't be linked to Direct Threats)

PLANNING:
- Table to manage assignments has much better width resizing tools
- Assignments table now shows resource names in addition to their ids
- Tree now lists each Conceptual Model page separately
- New rows available for custom subviews: Contributing Factor
  and Assignment
- New Work Units column for assignments, included in Work Plan subview
- Removed Measurements-only, and added Targets-only and Threats-only
- Can now create and delete objectives
- Work Plan no longer includes Conceptual Model elements
- Tree now includes related indicators, strategies, and activities, 
  even if they are on a different diagram from the objective/goal
- Planning View Activities-only and Methods-only subviews now 
  display more sensible cost totals for shared items

REPORTS:
- Reports (custom) now offers a Viability Details section
- Various minor improvements to report output
  

Version 2.2.2 was a minor release with these enhancements:

- The Planning View tree is now built differently, presenting the rows in 
  a different order to reduce duplication
- Planning View sometimes didn't list KEA Indicators 
- Diagram layer checkboxes were not being respected during print/export
- Internal changes to prepare for a Spanish version
- Fixed a typo in the report output ("Verion")


Version 2.2.1 was a minor release that fixed a few problems with 2.2.0:

- Deleting KEA Indicators in Planning View could delete other data
  within the KEA
- Fixed a compatibility problem with Java 5 on Mac/Linux
- Threat Rating View (Stress-based) now wraps Threat Name column
- Fixed minor problem exporting non-ASCII characters to RTF


Here are the most notable improvements of Miradi 2.2 over Miradi 2.1:

- Can now import and export ConPro projects (BETA)
- Can now export almost any tab of any view as an RTF document
- Optional Automatic Row Height mode for all tables
- Objectives can now be associated with Activities, as well as Strategies
- Objective-Indicator relevancy dialog now lists associated factors
- Activity, Method, and Task names can now have hard line breaks

DIAGRAM:
- Paste now preserves Group Box Links
- Select Chain and Create Results Chain now include Group Boxes
- Checkboxes to hide/show layers is now saved per-diagram
- Now offer better selection of factor and link colors

VIABILITY:
- Tree now has Expand All and Collapse All buttons

THREAT RATING:
- Stress-based rating table now allows much better resizing
- FIXED: The "Majority Override" rule was not applied correctly in 
  certain rare cases

PLANNING VIEW:
- Custom views can now include rows for Intermediate Results 
- Progress column now shows Objective Percent Complete values
- When Measurement rows are not shown, Indicator row shows latest value
- Tree now has Expand All and Collapse All buttons
- Can now edit basic factor details from this view
- FIXED: Shared objects were sometimes omitted from the tree
- FIXED: Budget table now rounds instead of truncating

REPORTS:
- Replaced entire Reports View with a new RTF-based system
- Significant changes to Strategic Plan and Monitoring Plan 
- New "Progress Report" standard template


Here are the most notable improvements of Miradi 2.1 over Miradi 2.0:

APPLICATION-WIDE
- Much improved handling of project names during creation, rename, and import
- Improved sorting rules for Measurements and Progress Reports
- Objectives have a percent-complete table

DIAGRAM
- Can now set the color of links
- New command to create "bendpoint junctions" near factors
- Stresses are available even when in Simple Threat Rating mode
- Stresses can now be displayed in movable bubbles in the Conceptual Model
- Activities can now be displayed in movable bubbles in Results Chains
- Improved behavior of double-click within Group Boxes and Scope Box
- Group Boxes are now selected as part of a chain
- Can now Copy/Paste factors between two running copies of Miradi
- Can paste a factor's attributes into another existing factor of the 
  same type

THREAT RATING
- Fixed some errors in the Stress-Based calculations

PLANNING
- Row and column headers are now "locked" during scrolling

EXPORTING
- Tab-delimited files now handle factor names containing newlines

REPORTS
- Several minor improvements to formatting

WINDOWS INSTALLER
- Installer itself can now run in Spanish, Portuguese, and Indonesian


NOTE: Miradi 2.2.x uses different rules than 2.0 for wrapping and 
truncating text in factors and group boxes, so some boxes may need to 
be resized to re-format the text.


NOTES FOR MIRADI 1.0 USERS ONLY: 

1. On MS Windows, if your Documents directory is not on your C: drive, 
you may get a warning during installation. In this case, you will need to 
manually copy any old projects from the old project data location to the 
new one. Both directories will be mentioned in the warning message.

2. If you created your own shortcut to run Miradi, it will need to be 
updated. Instead of launching java manually, the shortcut should now 
execute one of the miradi launchers, which have .vbs extensions and 
can be found in the same directory as miradi.jar.

  
---------------------

For an up-to-date list of known issues and possible 
workarounds, see: http://miradi.org/help
