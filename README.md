# OpenIEC61850 - Overview
This is UNOFFICIAL, fork from [openmuc](http://www.openmuc.org/index.php?id=35).

OpenIEC61850 is an open source implementation of the IEC 61850 standards series licensed under the LGPL. The library is written in Java and consists of an MMS client and server. Up to now the following ACSI services are supported:
 
- Association model services
- All GetDirectory and GetDataDefinition services
- GetDataValues and SetDataValues
- DATA-SET model services
- Report-Control-Block services (client only)
- Control

At Fraunhofer ISE we are using OpenIEC61850 for communication with DERs such as CHP units and PV plants.

Read the user guide on how to get started with the library. 

Development of OpenIEC61850 was started by [Fraunhofer ISE, energy & meteo Systems](http://www.ise.fraunhofer.de/en) GmbH, and [OFFIS](http://www.offis.de/en/start.html) as part of the [eTelligence](http://www.etelligence.de/etelligence.php) research project funded by Germany's [Federal Ministery of Economics and Technology](http://www.bmwi.de/en/). Now that the research project has ended OpenIEC61850 is actively maintained by Fraunhofer ISE.

In cases where Java is not an option (e.g. if you want to implement a server on very resource constrained systems) you can also consider [libiec61850](http://libiec61850.com/).