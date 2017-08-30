package com.babel.basedata.logger;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface LogService {
	public String noRet="noRet";//descs为noRet时不记录返回值,如果数据量比较大时，尽量加上",descs=LogService.noRet"
	public String noDatas="noDatas";//descs为noDatas且返回数据对象为PageVO或RetResult，不记录里面的datas,如果数据量比较大时，尽量加上",descs=LogService.noDatas"
	public String noParams="noParams";//descs为noParams不日志参数信息，用以对敏感数据不记录对应的数据
	/**
	 * 作者
	 * @return
	 */
	public String author() default "no";
	
	/**
	 * 标题
	 * @return
	 */
	public String title();

	/**
	 * descs=noRet时，不记录返回值
	 * descs=noDatas时，不记录PageVO,RetResult中的数据集合
	 * @return
	 */
	public String descs() default "";
	/**
	 * 调用接口名称，exp:xxxService.insert
	 * @return
	 */
	public String calls() default "";
}
