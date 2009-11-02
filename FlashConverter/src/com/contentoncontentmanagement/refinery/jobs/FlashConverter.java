/*
 * Created by: 	David Roe
 * Email: 		davidtroe@gmail.com
 * URL:			ContentOnContentManagemnt.com
 * Date:		3/4/2007
 * 
 * FlashConverter is a java based conversion step executed by the 
 * inbound refinery.  The class is pretty simple, it basically executes
 * a shell call for the conversion engine and then if a streaming
 * server is identified, copies the file there as well
 * 
*/
package com.contentoncontentmanagement.refinery.jobs;

import docrefinery.convert.*;
import docrefinery.convert.engines.BaseCodeStep;
import docrefinery.utils.DosFileUtils;
import intradoc.common.*;
import java.io.*;
import java.util.*;
import intradoc.shared.*;


public class FlashConverter extends BaseCodeStep {
	
	private String m_AddOnName = "FlashConverter";
	private String m_AddOnVersion = "1";
	
	
	@Override
	public String getAddonName() {
		return m_AddOnName;
	}

	@Override
	public String getAddonVersion() {
		return m_AddOnVersion;
	}

	@Override
	public void initStep() {
		super.initStep();
	}

	@Override
	public void convert() throws Exception {
		try{
			String dDocName = m_convProps.getProperty("dDocName");
			String dRevLabel = m_convProps.getProperty("dRevLabel"); 	
			String InFilePath = m_convProps.getProperty("InFilePath");
	        String Directory = FileUtils.getDirectory(InFilePath);
	
	        
	        Properties properties = new Properties();        
	        properties.put("dDocName", dDocName);
	        properties.put("dRevLabel", dRevLabel);
	        m_scriptEvaluator.mergerToBinder(properties);
	        
	        ConverterLaunch converterLaunch = new ConverterLaunch(m_cxt);
	        String ResultFileFormat = SharedObjects.getEnvironmentValue("FlashFileFormat");
	        String ResultFileName = m_scriptEvaluator.evaluateScript(ResultFileFormat);
	        String OutFilePath = Directory + "/" + ResultFileName;
	        String WebFilePath = m_convProps.getProperty("WebFilePath");
	        WebFilePath = DosFileUtils.changeFileExt(WebFilePath, "flv");
	        
	        File outFile = new File(OutFilePath);
	        if(outFile.exists()){
	        	outFile.delete();
	        }
	                
	        properties.put("InFilePath", InFilePath);
	        properties.put("OutFilePath", OutFilePath);
	        m_scriptEvaluator.mergerToBinder(properties);
	        
	        String evaluatedParams = m_scriptEvaluator.evaluateScript(SharedObjects.getEnvironmentValue("FlashParameters"));
	        String FlashConverterEXE = SharedObjects.getEnvironmentValue("FlashConverterEXE");
	        converterLaunch.setTimeOuts(m_stepData.m_timeout, InFilePath);
	        
	        converterLaunch.setExeInfo(FlashConverterEXE, evaluatedParams);
	        converterLaunch.execute();
	        
	        FileUtils.copyFile(OutFilePath, WebFilePath);
	        m_format = SharedObjects.getEnvironmentValue("FLVMime");
	        m_convertedFile = WebFilePath;
	        m_stepResult.setErrorCode(1);
	        m_cxt.setCachedObject("CurrentWebviewable", m_convertedFile);
	        
	        String FlashMediaSupportEnabled = SharedObjects.getEnvironmentValue("FlashMediaSupportEnabled");
	        String FlashMediaPhysicalRoot = SharedObjects.getEnvironmentValue("FlashMediaPhysicalRoot");
	        
	        if(FlashMediaSupportEnabled.equalsIgnoreCase("true")){
	        	if(FlashMediaPhysicalRoot!=null && !FlashMediaPhysicalRoot.equals("")){
	        		
	                File FlashMediaFile = new File(FlashMediaPhysicalRoot + ResultFileName);
	                if(FlashMediaFile.exists()){
	                	FlashMediaFile.delete();
	                }
	                
	        		FileUtils.copyFile(OutFilePath, FlashMediaPhysicalRoot + ResultFileName);
	        	}
	        }
		}catch(Exception e){
			SystemUtils.traceDumpException("refinery", "An error occured during the flash converter refinery process", e);
		}
	}
}
