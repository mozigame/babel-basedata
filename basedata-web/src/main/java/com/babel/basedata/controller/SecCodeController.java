package com.babel.basedata.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import com.babel.common.core.tools.qr.MatrixToImageWriter;
import com.babel.common.web.context.AppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;



/** 
 * 类名称：登录验证码
 * 类描述： 
 * 联系方式：
 * @version
 */
@Controller
@RequestMapping("/code")
public class SecCodeController {
	private static final Logger logger = LoggerFactory.getLogger(SecCodeController.class);

	@RequestMapping
	public void generate(HttpServletResponse response){
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		String code = drawImg(output);
		
		Subject currentUser = SecurityUtils.getSubject();  
		Session session = currentUser.getSession();
		session.setAttribute(AppContext.SESSION_SECURITY_CODE, code);
		
		try {
			ServletOutputStream out = response.getOutputStream();
			output.writeTo(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String drawImg(ByteArrayOutputStream output){
		String code = "";
		for(int i=0; i<4; i++){
			code += randomChar();
		}
		int width = 70;
		int height = 25;
		BufferedImage bi = new BufferedImage(width,height,BufferedImage.TYPE_3BYTE_BGR);
		Font font = new Font("Times New Roman",Font.PLAIN,20);
		Graphics2D g = bi.createGraphics();
		g.setFont(font);
		Color color = new Color(66,2,82);
		g.setColor(color);
		g.setBackground(new Color(226,226,240));
		g.clearRect(0, 0, width, height);
		FontRenderContext context = g.getFontRenderContext();
		Rectangle2D bounds = font.getStringBounds(code, context);
		double x = (width - bounds.getWidth()) / 2;
		double y = (height - bounds.getHeight()) / 2;
		double ascent = bounds.getY();
		double baseY = y - ascent;
		g.drawString(code, (int)x, (int)baseY);
		g.dispose();
		try {
			ImageIO.write(bi, "jpg", output);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return code;
	}
	
	private char randomChar(){
		Random r = new Random();
		String s = "ABCDEFGHJKLMNPRSTUVWXYZ0123456789";
		return s.charAt(r.nextInt(s.length()));
	}
	
	/**
	 * 生成二维码
	 * @param response
	 * @param width
	 * @param height
	 */
	@RequestMapping("qrcode")
	public void qrcode(HttpServletRequest request, HttpServletResponse response, Integer width, Integer height) throws IOException{
		
		
		String url = request.getParameter("url");
		if(StringUtils.isEmpty(url)){
			response.getWriter().println("url is empty");
			return;
		}
		String sysType=request.getParameter("sysType");
		if(StringUtils.isEmpty(sysType)){
			sysType="lawHelp";
		}
//		String logoUrl = "/picview/img/logo.png";
		String logoUrl="/picview/img/"+sysType+".jpg";
		logoUrl=request.getRealPath(logoUrl);
		File file = new File(logoUrl);
		if(!file.exists()){
			logger.warn("----qrcode--logoUrl="+logoUrl+" not exist");
			logoUrl=null;
//			response.getWriter().println("logo:"+logoUrl+" not exist");
//			return;
		}

		try {
			int iWidth = (width == null ? 400 : width);
			int iHeight = (height == null ? 400 : height);
			logger.info("----qrcode--sysType="+sysType+" logoUrl="+logoUrl+" url="+url);
			MatrixToImageWriter.createRqCode(url,logoUrl, iWidth, iHeight, response.getOutputStream());
		} catch (Exception e) {
			logger.error(String.format("生成二维码失败： url： %s", url), e);
		}
	}
}
