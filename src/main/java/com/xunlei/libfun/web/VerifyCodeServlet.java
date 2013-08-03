package com.xunlei.libfun.web;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.xunlei.common.util.VerifycodeUtil;

/**
 * Servlet implementation class VerifyCodeServlet
 */
@WebServlet("/libfun/vcode")
public class VerifyCodeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(VerifyCodeServlet.class);
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VerifyCodeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Cache-Control", "no-store"); 
		response.setHeader("Pragma", "no-cache"); 
		response.setDateHeader("Expires", 0); 
		response.setContentType("image/jpeg"); 

		logger.debug("get verify img id:" + request.getSession().getId());
		BufferedImage bufferedImage = VerifycodeUtil.getService().getImageChallengeForID(request.getSession().getId()); 
		ServletOutputStream servletOutputStream = response.getOutputStream(); 
		ImageIO.write(bufferedImage, "jpg", servletOutputStream); 
		  
		try { 
			servletOutputStream.flush(); 
		} finally { 
			servletOutputStream.close(); 
		} 
	}
}
