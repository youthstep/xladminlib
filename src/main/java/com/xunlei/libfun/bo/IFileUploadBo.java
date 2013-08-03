package com.xunlei.libfun.bo;

import java.io.IOException;

public interface IFileUploadBo {
	/**
	 * 返回文件路径
	 * @param fileData
	 * @return
	 */
	String uploadFile(byte[] fileData,String fileName)  throws IOException;
}
