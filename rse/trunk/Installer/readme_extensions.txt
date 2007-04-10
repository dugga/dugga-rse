
COMPLETING THE INSTALLATION MANUALLY

--------
WDSC 7.x
--------

To complete the installation on WDSC 7.x or other Eclipse
3.2-based IDEs, you must define the plug-in to that IDE.
You do this by creating an "Eclipse Extension Location."

Start your IDE and go to: Help -> Software Updates -> Manage Configuration

Once there, select the "Add an Extension Location" option.

Select the appropriate location based on where you installed
the RSE Extensions. The default path would be:

C:\Program Files\SoftLanding\RSE Extensions\eclipse

Your IDE may then require a restart for the plug-ins to become available.


--------
WDSC 6.x
--------

To complete the installation on WDSC 6.x or other Eclipse
3.1-based IDEs, copy the file named

"com.softlanding.rse.extensions.link"

from the root of the installation folder to the eclipse\links
folder under your product's installation root.