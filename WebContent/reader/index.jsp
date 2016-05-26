<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>百度文库在线预览</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script src="../<%=request.getContextPath()%>/pdfjs-1.4.20-dist/web/viewer.js"></script>
  </head>
  <body>
   <a href="<%=request.getContextPath()%>/BaiDuServlet?savFile=12345.pptx">在线预览PPT</a><br><br>
  <a href="<%=request.getContextPath()%>/BaiDuServlet?savFile=1.doc">在线预览word</a><br><br>
  <a href="<%=request.getContextPath()%>/BaiDuServlet?savFile=1234.xls">在线预览excel</a><br><br>
   <button onclick="tran(12345)">测试</button>
  </body>
</html>
