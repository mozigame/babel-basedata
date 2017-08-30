package com.babel.basedata.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import javax.servlet.ServletContextEvent;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.babel.basedata.model.LookupItemPO;
import com.babel.basedata.model.LookupPO;
import com.babel.basedata.service.ILookupItemService;
import com.babel.basedata.service.ILookupService;
import com.babel.common.core.data.RetResult;
import com.babel.common.web.constants.ConfigWebUtil;
import com.babel.common.web.loader.IContextTaskLoader;

@Service("lookupJsResetTaskLoader")
public class LookupJsResetTaskLoader implements IContextTaskLoader {
	private static Logger log = Logger.getLogger(LookupJsResetTaskLoader.class);
	@Autowired
	private ILookupService lookupService;
	@Autowired
	private ILookupItemService lookupItemService;
	private static boolean isDelay=false;
	@Override
	public RetResult<String> execute(ServletContextEvent event) {
		try {
			if(!isDelay){
				Thread.sleep(1000l);//延时1秒
				isDelay=true;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String realPath=ConfigWebUtil.getConfigValue(ConfigWebUtil.config_key.SYS_WEB_REAL_PATH);
		log.info("========resetCodeTableJs1 realPath="+realPath+" isDelay="+isDelay);
		if(event!=null){
			realPath=event.getServletContext().getRealPath("/");
			log.info("========resetCodeTableJs2 realPath="+realPath);
			ConfigWebUtil.setConfigValue(ConfigWebUtil.config_key.SYS_WEB_REAL_PATH, realPath);
		}
		RetResult<String> ret1=this.resetCodeTableJs(realPath, "CN");
		if(!ret1.isSuccess()){
			return ret1;
		}
		RetResult<String> ret2=this.resetCodeTableJs(realPath, "EN");
		
		return ret2;
	}
	
	private RetResult<String> resetCodeTableJs(String path, String lang){				
		String realPath =path;
		RetResult<String> ret=new RetResult<String>();
		
		realPath = realPath.replaceAll("/./", "/");
		realPath+="/scripts/lib/comm.dict_"+lang+".js";
		log.info("========resetCodeTableJs realPath="+realPath);
		
		String value = null;
		File file = new File(realPath);
		
		
		if(file.exists()){
			LookupPO lookup = new LookupPO();
			List<LookupPO> list=this.lookupService.selectByLookup(lookup, 0, 200);

			LookupItemPO lItem=new LookupItemPO();
			lItem.setLanguage(lang);
			List<LookupItemPO> itemList=this.lookupItemService.selectByLookupItem(lItem, 0, 1000);
			log.info("----lookupList="+list.size()+" lookupItemList="+itemList.size());
			
		    
			String all = "";
//			CommMethod.printList(list);
//			all = genDictCodes(list);
			
			all+=genDictObjStr(list, itemList);
			try {
//				Path.writeFile(file, all);
				this.writeFile(file, all);
				log.info("------resetCodeTableJs reset ok");
			} catch (Exception e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
				value = e.getMessage();
				log.error("------resetCodeTableJs reset error:"+value);
				ret.initError(RetResult.msg_codes.ERR_DATA_NOT_FOUND, value, e);
			}
		}
		else{
			value = "文件不存在:"+realPath ;
			ret.initError(RetResult.msg_codes.ERR_DATA_NOT_FOUND, value, null);
//			log.error("------resetCodeTableJs "+value);
		}
				
		return ret;
		
	}
	
	private String getAttr(String attr, String attrValue){
		if(!StringUtils.isEmpty(attrValue)){
			return ", "+attr+":'"+attrValue+"'";
		}
		return "";
	}

	/**
	 * 生成数据字典内容
	 * @param list
	 * @param itemList
	 */
	private String genDictObjStr(List<LookupPO> list, List<LookupItemPO> itemList) {
		String dict_obj_str="";
		String buff="";
		String optStr="";//可选属性
		
		for(LookupPO ct:list){
			buff = "dict_"+ct.getCode()+"=[\n".toLowerCase();	
			buff = buff.toLowerCase();
			for(LookupItemPO cType:itemList){
				if(cType.getLookupId().longValue()==ct.getCid().longValue()){
					optStr="";
					optStr+=getAttr("color", cType.getColor());
					optStr+=getAttr("value", cType.getItemValue());
					optStr+=getAttr("attr1", cType.getAttr1());
					optStr+=getAttr("attr2", cType.getAttr2());
					optStr+=getAttr("attr3", cType.getAttr3());
					optStr+=getAttr("attr4", cType.getAttr4());
					buff+="	{id:'"+cType.getItemCode()+"', name:'"+cType.getItemName()+"'"+optStr+"},\n";	
				}
			}
			if(buff.endsWith(",\n")){
				buff = buff.substring(0, buff.length()-2);
			}
			buff = buff+"\n];";
			dict_obj_str += buff+"\n\n";
		}
		return dict_obj_str;
	}

	/**
	 * @param list
	 * @return
	 */
	private String genDictCodes(List<LookupPO> list) {
		String buff;
		buff = "dict_lookup"+"=[\n";	
		buff = buff.toLowerCase();
		for(LookupPO ct:list){ 
			//System.out.println(ct.getCodeType()+" "+ct.getCname());
			buff = buff+"	{id:'"+ct.getCode()+"', nameCn:'"+ct.getNameCn()+"', nameEn:'"+ct.getNameEn()+"'},\n";		
		}
		if(buff.endsWith(",\n")){
			buff = buff.substring(0, buff.length()-2);
		}
		buff = buff+"\n];\n\n";
		return buff;
	}
	
	/**
	 * 文件写入
	 * @param file
	 * @param content
	 * @throws Exception
	 */
	public void writeFile(File file, String content) throws Exception{
        FileOutputStream fos  =   new  FileOutputStream(file); 
        OutputStreamWriter osw  =   new  OutputStreamWriter(fos,  "UTF-8" ); 
        osw.write(content); 
        osw.flush(); 
        osw.close();
        fos.close();
	}

}
