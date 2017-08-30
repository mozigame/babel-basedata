<%@ page contentType="text/html;charset=UTF-8"
    pageEncoding="UTF-8" import="java.net.*,java.io.*,java.util.*,java.text.SimpleDateFormat
    ,com.babel.basedata.util.Sysconfigs,com.babel.common.web.context.AppContext"%>

<%!
int MAX_TOTAL_ROW_READ=10000;
List<String> readLineFile(File file, int maxCount, Map<String, String> filterMap, String charset) throws Exception {
	long time=System.currentTimeMillis();
	
	List<String> lineList = new ArrayList<String>();
	if(charset==null ||"".equals(charset)){
		charset = "UTF-8";
	}
	
	int count = 0;
	int totalCount=0;
	
	RandomAccessFile rf = null;
	try {
		rf = new RandomAccessFile(file, "r");
		long len = rf.length();
		long start = rf.getFilePointer();
		long nextend = start + len - 1;
		String line;
		rf.seek(nextend);
		int c = -1;
		String filter=filterMap.get("filter");
		String stop_key=filterMap.get("stop_key");
		String hide_key=filterMap.get("hide_key");
		if(stop_key!=null && (stop_key.equals("") ||stop_key.equals("null"))){
			stop_key=null;
		}
		if(hide_key!=null && (hide_key.equals("") ||hide_key.equals("null"))){
			hide_key=null;
		}
		String[] hides=null;
		if(hide_key!=null){
			hides=hide_key.split(",");
		}
		
		
		boolean isHide=false;
		while (nextend > start) {
			c = rf.read();
			if (c == '\n' || c == '\r') {
				totalCount++;
				if(totalCount>MAX_TOTAL_ROW_READ){
					break;
				}
				if (count > maxCount) {
					break;
				}
				line = rf.readLine();
				if (line != null) {
					line = new String(line.getBytes("ISO-8859-1"), charset);
					//stop
					if(stop_key!=null && line.indexOf(stop_key)>=0){
						break;
					}
					//hide
					if(hides!=null){
						isHide=false;
						for(String hide:hides){
							if(hide.equals("hide_empty") && "".equals(line)){
								isHide=true;
								break;
							}
							else if(line.indexOf(hide)>=0){
								isHide=true;
								break;
							}
						}
						if(isHide){
							continue;
						}
					}
					//filter
					if(filter==null|| line.indexOf(filter)>=0){
						count++;
						lineList.add(line);
					}
				}
				nextend--;
			}
			nextend--;
			rf.seek(nextend);
			if (nextend == 0) {// output first line on seek file head
				line = rf.readLine();
				line = new String(line.getBytes("ISO-8859-1"), charset);
				lineList.add(line);
			}
		}
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		try {
			if (rf != null)
				rf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	System.out.println("totalCount="+totalCount+" foundCount="+count+" time="+(System.currentTimeMillis()-time));
	return lineList;
}

private static List<String> whiteipList=new ArrayList<String>();
private List<String> getWhiteList(){
	List<String> ipList=new ArrayList<>();
//	whiteipList.clear();
	if(whiteipList.isEmpty()){
		whiteipList.add("192.168.");
        whiteipList.add("localhost");
        whiteipList.add("0:0:0:0:0:0:0:1");
        whiteipList.add("127.0.0.1");
	}
	ipList.addAll(whiteipList);
	String whiteip=(String)Sysconfigs.getEnvMap().get("app.serverInfo.whiteip");
    if(whiteip!=null){
    	String[] ips=whiteip.split(",");
    	for(String ip:ips){
    		ipList.add(ip);
    	}
    }
    return ipList;
}
%>

<%
String appRunType=(String)Sysconfigs.getEnvMap().get("app.runType");
if("product".equals(appRunType)){
	String remoteIp= AppContext.getRemoteIp(request);
	List<String> whiteipList=getWhiteList();
    boolean isExist=false;
    for(String ip:whiteipList){
    	if(remoteIp.startsWith(ip)){
    		isExist=true;
    		break;
    	}
    }
    
    if(!isExist){
    	out.println("ip:"+remoteIp+" out of limit!");
    	out.close();
    	return;
    }
}
String path=System.getProperty("user.dir");
SimpleDateFormat sdf_date=new SimpleDateFormat("yyyy_MM_dd");
String filePath=path+"/tomcat8/logs/catalina.out";
int max_line_count=100;
String counts=request.getParameter("count");//stop on find count
String filter=request.getParameter("filter");//find by filter value
String stopKey=request.getParameter("stopKey");//find stop on find stopKey
String hideKey=request.getParameter("hideKey");//hide find hideKey

long time=System.currentTimeMillis();

try{
	int cout=Integer.parseInt(counts);
	max_line_count=cout;
}catch(Exception e){
	
}

try{
	File file = new File(filePath);
	if(!file.exists()){
		String info=filePath+" not found!";
		out.print(info);
		System.out.println(info);
	}
	else{
		Map<String, String> filterMap=new HashMap();
		filterMap.put("filter", filter);
		filterMap.put("stop_key", stopKey);
		filterMap.put("hide_key", hideKey);
		List<String> lineList=readLineFile(file, max_line_count, filterMap, null);
		for(String line:lineList){
			out.print(line+"<br/>\n");
		}
	}
}
catch(Exception e){
	out.println(e.getMessage());
}


%>