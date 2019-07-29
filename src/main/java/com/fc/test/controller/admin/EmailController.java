package com.fc.test.controller.admin;

import com.fc.test.common.base.BaseController;
import com.fc.test.common.domain.AjaxResult;
import com.fc.test.model.auto.TSysEmail;
import com.fc.test.model.custom.TableSplitResult;
import com.fc.test.model.custom.Tablepar;
import com.fc.test.model.custom.TitleVo;
import com.fc.test.service.TsysEmailServiceImpl;
import com.fc.test.util.SimpleEmailUtil;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

/**
 * 邮件发送Controller
 * @ClassName: EmailController
 * @author fuce
 * @date 2019-06-10 00:39
 * @version V1.0
 */
@Controller
@Api(value = "邮件发送Controller")
@RequestMapping("EmailController")
@RequiredArgsConstructor
public class EmailController extends BaseController{
		
	private String prefix = "admin/email";

	private final TsysEmailServiceImpl tSysEmailService;
	
	
	@GetMapping("view")
	@RequiresPermissions("system:email:view")
    public String view(Model model)
    {	
		String str="邮件";
		setTitle(model, new TitleVo("发送", str+"管理", true,"欢迎进入"+str+"页面", true, false));
        return prefix + "/list";
    }
	


	
	@PostMapping("list")
	@RequiresPermissions("system:email:list")
	@ResponseBody
	public Object list(Tablepar tablepar,String searchTxt){
		PageInfo<TSysEmail> page=tSysEmailService.list(tablepar,searchTxt) ; 
		TableSplitResult<TSysEmail> result=new TableSplitResult<TSysEmail>(page.getPageNum(), page.getTotal(), page.getList()); 
		return  result;
	}
	
	/**
     * 新增
     */
    @GetMapping("/add")
    public String add(ModelMap modelMap)
    {
        return prefix + "/add";
    }
	
	
	@PostMapping("add")
	@RequiresPermissions("system:email:add")
	@ResponseBody
	public AjaxResult add(TSysEmail tSysEmail,Model model) throws Exception{
		int b=tSysEmailService.insertSelective(tSysEmail);
		SimpleEmailUtil.sendEmail(tSysEmail);
		if(b>0){
			return success();
		}else{
			return error();
		}
	}
	
	/**
	 * 删除用户
	 * @param ids
	 * @return
	 */
	@PostMapping("remove")
	@RequiresPermissions("system:email:remove")
	@ResponseBody
	public AjaxResult remove(String ids){
		int b=tSysEmailService.deleteByPrimaryKey(ids);
		if(b>0){
			return success();
		}else{
			return error();
		}
	}
	
	/**
	 * 检查用户
	 * @return
	 */
	@PostMapping("checkNameUnique")
	@ResponseBody
	public int checkNameUnique(TSysEmail tSysEmail){
		int b=tSysEmailService.checkNameUnique(tSysEmail);
		if(b>0){
			return 1;
		}else{
			return 0;
		}
	}
	
	
	/**
	 * 修改跳转
	 * @param id
	 * @param mmap
	 * @return
	 */
	@GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap)
    {
        mmap.put("TSysEmail", tSysEmailService.selectByPrimaryKey(id));

        return prefix + "/edit";
    }
	
	/**
     * 修改保存
     */
    @RequiresPermissions("system:email:edit")
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(TSysEmail record)
    {
        return toAjax(tSysEmailService.updateByPrimaryKeySelective(record));
    }


}
