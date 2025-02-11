package com.fc.test.controller.admin;

import com.fc.test.common.base.BaseController;
import com.fc.test.model.custom.TitleVo;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author yangjianfei
 */

@Controller
@Api(value = "系统工具类")
@RequestMapping("ToolController")
public class ToolController  extends BaseController{

	/**
	 * 跳转页面参数
	 */
	private static final String PREFIX = "admin/tool";
	
	@GetMapping("view")
    public String view(Model model)
    {	
		
		String str="工具";
		setTitle(model, new TitleVo("列表", str+"管理", true,"欢迎进入"+str+"页面", true, false));
		return PREFIX + "/list";
    }
			
}
