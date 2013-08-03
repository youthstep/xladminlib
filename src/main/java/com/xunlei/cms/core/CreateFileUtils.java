package com.xunlei.cms.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.Map;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public class CreateFileUtils {
	/**
	 * <p>根据配置生成一个文件</p>
	 * @param config
	 * @param data
	 * @throws Exception
	 */
	public static void generatePage(CreateFileConfig config, Map<String, Object> data) throws Exception {
		//velocity is a freak
		int nameNum = config.getTemplatePath().lastIndexOf(File.separator);
		String templateFileDir = config.getTemplatePath().substring(0, nameNum+1);
		String templateFileName = config.getTemplatePath().substring(nameNum+1);
		VelocityEngine velocityEngine = new VelocityEngine();
		velocityEngine.setProperty("input.encoding", config.getTemplateEncoding());
		velocityEngine.setProperty("output.encoding", config.getDestFileEncoding());
		velocityEngine.setProperty("file.resource.loader.path", templateFileDir);
		velocityEngine.init();
		Template template = velocityEngine.getTemplate(templateFileName);
		StringWriter writer = new StringWriter();
		VelocityContext context = new VelocityContext(data);
		template.merge(context, writer);
		String content = writer.toString();
		File dir = new File(config.getDestFilePath().substring(0, config.getDestFilePath().lastIndexOf(File.separator)));
		dir.mkdirs();
		File file = new File(config.getDestFilePath());
		if(file.exists()){
			if(file.isFile()){
				file.delete();
				file = new File(config.getDestFilePath());
			}else{
				throw new Exception("dest file is a directory");
			}
		}
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), config.getDestFileEncoding()));
		bw.write(content);
		bw.close();
		velocityEngine = null;
		template = null;
		context = null;
		writer = null;
	}
}
