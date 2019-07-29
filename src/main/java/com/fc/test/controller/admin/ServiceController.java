package com.fc.test.controller.admin;

import com.fc.test.common.base.BaseController;
import com.fc.test.model.custom.Service;
import com.fc.test.model.custom.TitleVo;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 服务器信息Controller
 * @ClassName: ServiceController
 * @author fuce
 * @date 2019-06-23 00:55
 * @version V1.0
 */
@Controller
@Api(value = "服务器信息")
@RequestMapping("ServiceController")
public class ServiceController extends BaseController{

	/**
	 * 跳转页面参数
	 */
	private static final String PREFIX = "admin/service";
	
	@GetMapping("view")
	@RequiresPermissions("system:service:view")
    public String view(Model model)
    {	
		
		String str="服务器";
		setTitle(model, new TitleVo("列表", str+"管理", true,"欢迎进入"+str+"页面", true, false));
		
		model.addAttribute("service", new Service());

		return PREFIX + "/list";
    }
}
