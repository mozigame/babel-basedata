/**
 * 数据字典
 * dict_type=[//字典类型名, type对应codeTable中的codeType
 * 	id对应codeTable中的dataId
 * 	value对应codeTable中的showValues
 *  color对应codeTable中的showColor,showColor或color可以为空，为空时默认为黑色
 *  
 */

dict_education=[
	{id:0, value:'初中', color:''},
	{id:1, value:'高中'}
];

dict_have=[
	{id:1, value:'有'},
	{id:0, value:'无'},
	{id:2, value:'不限'}
];

dict_if=[
	{id:1, value:'是'},
	{id:0, value:'否'}
];

dict_if2=[
	{id:1, value:'是'},
	{id:0, value:'否'},
	{id:2, value:'不限'}
];

dict_if_del=[
	{id:0, value:'否'},
	{id:1, value:'是', color:'red'}
];

dict_log_level=[
	{id:1, value:'DEBUG'},
	{id:2, value:'INFO'},
	{id:3, value:'WARN', color:'yellow'},
	{id:4, value:'ERROR', color:'red'},
	{id:5, value:'FATAL', color:'red'}
];

dict_order_type=[
	{id:0, value:'正序', color:''},
	{id:1, value:'反序', color:''}
];

dict_recently_clear_type=[
	{id:0, value:'不处理'},
	{id:1, value:'去除范围以外'},
	{id:2, value:'其他'}
];

dict_recently_limit=[
	{id:0, value:'最近天数'},
	{id:1, value:'输入次数'}
];

dict_recently_show_type=[
	{id:0, value:'时间'},
	{id:1, value:'时间不重复'},
	{id:2, value:'输入次数'}
];

dict_sex=[
	{id:2, value:'女'},
	{id:1, value:'男'}
];

dict_status=[
	{id:0, value:'无效', color:'red'},
	{id:1, value:'有效'}
];

dict_if_log=[
	{id:0, value:'否'},
	{id:1, value:'是'},
	{id:2, value:'正常停止记录', color:''},
	{id:3, value:'异常停止记录'}
];