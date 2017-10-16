Flash Converter
David Roe
droe@ContentOnContentManagement.com
ContentOnContentManagement.com
================================================================================
The FLV converter uses a command line conversion engine to process FLV Conversions.
If a Streaming server is available and registered in the enviornmental.cfg file, the converter will also make a copy of the outputted FLV file and copy to the streaming server's folder.

The following actions will be required for FLV conversions:
1.  A command line conversion engine will need to be installed on the conversion server
2.  The path to the executable will need to be entered in to the envionrmental.cfg
3.  Set any command line parameters in the FlashParameters entry in the envionrmental.cfg
4.  Install the FlashConverterSupport component on the content server

If a Streaming server is available
	1. Set the FlashMediaSupportEnabled entry to true
	2. Enter the path to the physical root in the FlashMediaPhysicalRoot entry

Content types will also need to be configured in the content server.  This is done using the configuration manager applet in the content server.  Some examples.
Mime			extension		conversion
------------------------------------------------------------------------------------
Video/mpeg		flv			Flash Media	
Video/quicktime	avi			Flash Media
Video/w-ms-wmv	mwv			Flash Media
Audio/xms-wma	mwa			Flash Media
