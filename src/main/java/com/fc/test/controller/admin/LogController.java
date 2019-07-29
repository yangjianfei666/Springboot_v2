package com.fc.test.controller.admin;


import com.fc.test.common.base.BaseController;
import com.fc.test.common.domain.AjaxResult;
import com.fc.test.model.auto.TsysOperLog;
import com.fc.test.model.custom.TableSplitResult;
import com.fc.test.model.custom.Tablepar;
import com.fc.test.model.custom.TitleVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 日志记录controller
 * @author fuce 
 * @date: 2018年9月30日 下午9:28:31
 */
@Controller
@Api(value = "日志记录")
@RequestMapping("LogController")
public class LogController extends BaseController{

	/**
	 * 跳转页面参数
	 */
	private static final String PREFIX = "admin/log";
	
	@GetMapping("view")
	@RequiresPermissions("system:log:view")
    public String view(Model model)
    {	
		String str="操作日志";
		setTitle(model, new TitleVo("列表", str+"管理", true,"欢迎进入"+str+"页面", true, false));

		return PREFIX + "/list";
    }
	
	/**
	 * 文件列表
	 * @param searchTxt 搜索字符
	 */
	@PostMapping("list")
	@RequiresPermissions("system:log:list")
	@ResponseBody
	public Object list(Tablepar tablepar,String searchTxt){
		PageInfo<TsysOperLog> page=sysOperLogService.list(tablepar,searchTxt) ; 
		TableSplitResult<TsysOperLog> result=new TableSplitResult<TsysOperLog>(page.getPageNum(), page.getTotal(), page.getList()); 
		return  result;
	}

	
	/**
	 * 删除日志
	 */
	@PostMapping("remove")
	@RequiresPermissions("system:log:remove")
	@ResponseBody
	public AjaxResult remove(String ids){
		int b=sysOperLogService.deleteByPrimaryKey(ids);
		if(b>0){
			return success();
		}else{
			return error();
		}
	}
	
	

    
    
}
