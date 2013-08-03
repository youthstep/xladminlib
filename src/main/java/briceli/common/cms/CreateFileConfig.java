package briceli.common.cms;

public final class CreateFileConfig {
	/**
	 * 模板文件编码，默认utf8 
	 */
	private String templateEncoding;
	
	/**
	 * 生成目标文件编码，默认utf8
	 */
	private String destFileEncoding;
	
	/**
	 * 模板文件完整路径
	 */
	private String templatePath;
	
	/**
	 * 生成目标文件完整路径
	 */
	private String destFilePath;

	public CreateFileConfig(String templateEncoding, String destFileEncoding) {
		super();
		this.templateEncoding = templateEncoding;
		this.destFileEncoding = destFileEncoding;
	}

	public CreateFileConfig(String templateEncoding, String destFileEncoding,
			String templatePath, String destFilePath) {
		super();
		this.templateEncoding = templateEncoding;
		this.destFileEncoding = destFileEncoding;
		this.templatePath = templatePath;
		this.destFilePath = destFilePath;
	}

	public String getTemplateEncoding() {
		return templateEncoding;
	}

	public void setTemplateEncoding(String templateEncoding) {
		this.templateEncoding = templateEncoding;
	}

	public String getDestFileEncoding() {
		return destFileEncoding;
	}

	public void setDestFileEncoding(String destFileEncoding) {
		this.destFileEncoding = destFileEncoding;
	}

	public String getTemplatePath() {
		return templatePath;
	}

	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}

	public String getDestFilePath() {
		return destFilePath;
	}

	public void setDestFilePath(String destFilePath) {
		this.destFilePath = destFilePath;
	}
	
}
