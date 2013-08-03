package com.xunlei.common.util;

import java.awt.Color;
import java.awt.Font;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.octo.captcha.CaptchaFactory;
import com.octo.captcha.component.image.backgroundgenerator.GradientBackgroundGenerator;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.NonLinearTextPaster;
import com.octo.captcha.component.image.wordtoimage.ComposedWordToImage;
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
import com.octo.captcha.engine.GenericCaptchaEngine;
import com.octo.captcha.image.gimpy.GimpyFactory;
import com.octo.captcha.service.image.ImageCaptchaService;
import com.octo.captcha.service.multitype.GenericManageableCaptchaService;

public abstract class VerifycodeUtil {
	public static final int minAcceptedWordLength = 4;
	public static final int maxAcceptedWordLength = 4;
	public static final int imgWidth = 100;
	public static final int imgHeight = 27;
	public static final int minFontSize = 20;
	public static final int maxFontSize = 25;
	public static final Font font = new Font("@YaHei Consolas Hybrid",Font.PLAIN, (minFontSize + maxFontSize) / 2);
//	public static final String captchaContent = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String captchaContent = "123456789abcdefghijklmnpqrstuvwxyz";
	private static NonLinearTextPaster testPaster = new NonLinearTextPaster(minAcceptedWordLength, maxAcceptedWordLength, new Color(60, 60, 60));
	private static GradientBackgroundGenerator backgroundGen = new GradientBackgroundGenerator(imgWidth, imgHeight, new Color(239, 246, 236), new Color(239, 246,236));
	private static RandomFontGenerator fontGen = new RandomFontGenerator(minFontSize, maxFontSize, new Font[] { font });
	private static RandomWordGenerator wordGen = new RandomWordGenerator(captchaContent);
	private static ComposedWordToImage wordToImage = new ComposedWordToImage(fontGen, backgroundGen, testPaster);
	private static GimpyFactory captchaFacotry = new GimpyFactory(wordGen,wordToImage);
	private static CaptchaFactory[] factories = new GimpyFactory[] { captchaFacotry };
	private static GenericCaptchaEngine engine = new GenericCaptchaEngine(factories);
	private static ImageCaptchaService instance = new GenericManageableCaptchaService(engine,3000,50000);

	private static Logger logger = Logger.getLogger(VerifycodeUtil.class);

	public static boolean isVerify(HttpServletRequest request, String verifycode) {
		boolean rtn = instance.validateResponseForID(request.getSession().getId(),verifycode).booleanValue();
		logger.debug("id:" + request.getSession().getId() + " verifyCode:" + verifycode + " result:" + rtn);
		return rtn;
	}

	public static ImageCaptchaService getService() {
		return instance;
	}
}
