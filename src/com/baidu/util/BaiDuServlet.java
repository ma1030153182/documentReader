package com.baidu.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;

import javax.imageio.stream.FileImageInputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;

/**
 * @Author:NuoYan
 * @Date:2015-2-2 下午2:24:58 
 * TODO: 1、第一步，首先获取到需要查看的文件
 *       2、第二部，将获取的文件(doc,xls,txt,ppt,03/07版本转化为PDF),这一步需要调用OpenOffice
 *       3、第三部，将资源文件转换好的PDF文件转换为swf文件,使用FlexPaperViewer.swf进行浏览查看
 */
public class BaiDuServlet extends HttpServlet {
	private File sourceFile;// 要转化的源文件
	private File pdfFile;// pdf中间文件对象
	private File pdfFile1;
	private File swfFile;// swf目标文件对象
	private String filePath;// 用来保存文件路径
	private String fileName;// 不包括后缀名的文件名

	public File getPdfFile1() {
		return pdfFile1;
	}

	public void setPdfFile1(File pdfFile2) {
		this.pdfFile1 = pdfFile2;
	}

	public File getSourceFile() {
		return sourceFile;
	}

	public void setSourceFile(File sourceFile) {
		this.sourceFile = sourceFile;
	}

	public File getPdfFile() {
		return pdfFile;
	}

	public void setPdfFile(File pdfFile) {
		this.pdfFile = pdfFile;
	}

	public File getSwfFile() {
		return swfFile;
	}

	public void setSwfFile(File swfFile) {
		this.swfFile = swfFile;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String saveFileName = request.getParameter("savFile");		
		System.out.println(saveFileName);
	
		String webPath = request.getRealPath("/");
		filePath = webPath + "reader\\" + saveFileName;
		fileName = filePath.substring(0, filePath.lastIndexOf("."));
		String fileName1 = saveFileName.substring(0, saveFileName.lastIndexOf("."));
		// 创建三个文件对象
		sourceFile = new File(filePath);
		pdfFile = new File(fileName + ".pdf");
		pdfFile1 = new File("F:\\java\\baidudoc1\\WebContent\\reader\\"+fileName1+".pdf");
		swfFile = new File(fileName + ".swf");
		System.out.println(pdfFile);
		System.out.println(swfFile);
		// 1、将源文件转化为pdf格式文件
		src2pdf();
//		try {
//			// 2、将pdf文件转化为swf文件
//			pdf2swf();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		// 将转化好的文件绑定到session上去
		
		request.getSession().setAttribute("swfName", swfFile.getName());
		request.getSession().setAttribute("fileName1", pdfFile1.getName());
	
		// 重定向到预览页面
		response.sendRedirect(request.getContextPath() + "/pdfjs-1.4.20-dist/web/viewer.jsp");
//		request.setAttribute("fileName1",pdfFile1.getName());
//		request.getRequestDispatcher("/pdfjs-1.4.20-dist/web/viewer.jsp").forward(request, response);
	}

	/**
	 * @Author:NuoYan
	 * @Date:2015-2-2 下午2:28:22 TODO://源文件转化为PDF文件
	 */
	private void src2pdf() {
		if (sourceFile.exists()) {
			// 如果不存在，需要转份为PDF文件
			if (!pdfFile1.exists()) {
				// 启用OpenOffice提供的转化服务
				OpenOfficeConnection conn = new SocketOpenOfficeConnection(8100);
				// 连接OpenOffice服务器
				try {
					conn.connect();
					// 建立文件转换器对象
					DocumentConverter converter = new OpenOfficeDocumentConverter(
							conn);
					converter.convert(sourceFile, pdfFile1);
					// 断开链接
					conn.disconnect();
					System.out.println("转换成功");
				} catch (ConnectException e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("已经存在PDF文件，不需要在转换！！");
			}
		} else {
			System.out.println("文件路径不存在！！！");
		}

	}

	/**
	 * @Author:NuoYan
	 * @Date:2015-2-2 下午2:28:32
	 * @throws Exception
	 * TODO:PDF转化为SWF文件
	 */
	private void pdf2swf() throws Exception {
		if (!swfFile.exists()) {
			if (pdfFile.exists()) {
				String command = "C:\\Program Files\\SWFTools\\pdf2swf.exe "
						+ pdfFile.getPath() + " -o " + swfFile.getPath()
						+ " -T 9";
				System.out.println("转换命令:" + command);
				// Java调用外部命令,执行pdf转化为swf
				Runtime r = Runtime.getRuntime();
				Process p = r.exec(command);
				System.out.println(loadStream(p.getInputStream()));
				System.out.println("swf文件转份成功！！！");
				System.out.println(swfFile.getPath());
			} else {
				System.out.println("不存在PDF文件");
			}
		}

	}
	
	private static String loadStream(InputStream in) throws Exception {
		int len = 0;
		in = new BufferedInputStream(in);
		StringBuffer buffer = new StringBuffer();
		while ((len = in.read()) != -1) {
			buffer.append((char) len);
		}
		return buffer.toString();
	}

}
