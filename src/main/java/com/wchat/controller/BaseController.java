package com.wchat.controller;

import com.wchat.bean.ResultBean;

public class BaseController {

	protected static final String failCode = "500";
	protected static final String succCode = "200";
	protected static final String invalid = "100";

	protected ResultBean getResult(String code,String msg,Object data){
		return new ResultBean(code,msg,data);
	}

	protected ResultBean getFailResult(String msg,Object data){
		return new ResultBean(failCode,msg,data);
	}

	protected ResultBean getFailResult(String msg){
		return new ResultBean(failCode,msg,null);
	}


	protected ResultBean getSuccResult(String msg,Object data){
		return new ResultBean(succCode,msg,data);
	}

	protected ResultBean getSuccResult(Object data){
		return new ResultBean(succCode,"",data);
	}

	protected ResultBean getSuccResult(String msg){
		return new ResultBean(succCode,msg,null);
	}
	protected ResultBean getSuccResult4Data(Object data){
		return this.getSuccResult(data);
	}
	protected ResultBean getResult(){
		return new ResultBean();
	}
	protected ResultBean getResult(String code,String msg){
		return getResult(code,msg,null);
	}
}
