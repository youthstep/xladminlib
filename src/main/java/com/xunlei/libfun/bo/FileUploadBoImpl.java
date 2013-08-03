package com.xunlei.libfun.bo;

import java.io.IOException;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.xunlei.common.bo.BaseBo;
import com.xunlei.common.util.FileUtil;
import com.xunlei.common.util.StringTools;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.annotations.RemoteMethod;
import com.xunlei.libfun.vo.LibConfig;

@Service("fileUploadService")
@RemoteProxy(name="fileUploadService")
public class FileUploadBoImpl extends BaseBo implements IFileUploadBo{
	/**
	 * 用于文件名的顺序数目
	 */
	private static int fileCount = 10;
	
	@Override
	@RemoteMethod
	synchronized public String uploadFile(byte[] fileData,String fileName) throws IOException {
		String path = LibConfig.getValue("RootFilesPath");
		String[] allowTypes = LibConfig.getValue("AllowFiles").split("[|]");
		String domainPath = LibConfig.getValue("FilesDomain");
		String fileType = fileName.substring(fileName.lastIndexOf(".")+1);
		if(StringTools.isNotEmpty(path)){
			for(String str : allowTypes){
				if(str.equals(fileType)){
					if(fileCount>=99)
						fileCount = 10;
					fileName = (new Date().getTime())+fileCount+"."+fileType;
					FileUtil.newFile(path + fileName, fileData);
					return domainPath + fileName;
				}
			}
			
		}
		return "";
	}
}
