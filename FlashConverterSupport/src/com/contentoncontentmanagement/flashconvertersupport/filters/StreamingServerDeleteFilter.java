/*
 * Created by: 	David Roe
 * Email: 		davidtroe@gmail.com
 * URL:			ContentOnContentManagemnt.com
 * Date:		3/4/2007
 * 
 * StreamingServerDeleteFilter is attached to the afterLoadRecordWebChange which
 * fires during indexing.  The filter is responsible for removing copies of deleted
 * files from the streaming server.
 * 
*/
package com.contentoncontentmanagement.flashconvertersupport.filters;

import intradoc.common.ExecutionContext;
import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.ResultSet;
import intradoc.data.Workspace;
import intradoc.server.PageMerger;
import intradoc.shared.FilterImplementor;
import intradoc.shared.SharedObjects;

import java.io.File;


public class StreamingServerDeleteFilter implements FilterImplementor{

	public int doFilter(Workspace ws, DataBinder binder, ExecutionContext cxt) throws ServiceException{
		try{
		
			String isDelete = binder.getLocal("isDelete");
			if(isDelete!=null && isDelete.indexOf("1") >=0){
				ResultSet DocInfo = binder.getResultSet("DOC_INFO");
				if(DocInfo!=null && DocInfo.first()){
					String dWebExtension = DocInfo.getStringValueByName("dWebExtension");
					String FlashFileExtension = SharedObjects.getEnvironmentValue("FlashFileExtension");
					
					if(dWebExtension!=null && dWebExtension.equalsIgnoreCase(FlashFileExtension)){
						String FlashServerEnabled = SharedObjects.getEnvironmentValue("FlashMediaSupportEnabled");
						String FlashMediaPhysicalRoot = SharedObjects.getEnvironmentValue("FlashMediaPhysicalRoot");
						if(FlashServerEnabled!=null && FlashServerEnabled.equalsIgnoreCase("true")){
							if(FlashMediaPhysicalRoot!=null && !FlashMediaPhysicalRoot.equals("")){
								String namingConvention = SharedObjects.getEnvironmentValue("FlashFileFormat");
								PageMerger pagemerger = new PageMerger();
								pagemerger.setDataBinder(binder);
								pagemerger.setExecutionContext(cxt);
								String fileName = pagemerger.evaluateScript(namingConvention);								
								File StreamingFile = new File(FlashMediaPhysicalRoot + fileName);
								if(StreamingFile.exists()){
									StreamingFile.delete();
								}
							}
						}
					}
				}
			}
			return CONTINUE;
		}catch(Exception e){
			throw new ServiceException(e.getMessage());
		}
	}
	
	
}
