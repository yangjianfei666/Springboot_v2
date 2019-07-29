package com.fc.test.controller.admin;

import com.fc.test.common.base.BaseController;
import com.fc.test.common.domain.AjaxResult;
import com.fc.test.model.auto.TsysPremission;
import com.fc.test.model.custom.TableSplitResult;
import com.fc.test.model.custom.Tablepar;
import com.fc.test.model.custom.TitleVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 权限Controller
 * @author fuce 
 * @date: 2018年9月2日 下午8:08:21
 */
@Controller
@Api(value = "权限")
@RequestMapping("PermissionController")
@RequiredArgsConstructor
public class PermissionController extends BaseController {


	/**
	 * 跳转页面参数
	 */
	private static final String PREFIX = "admin/permission";
	
	@GetMapping("view")
	@RequiresPermissions("system:permission:view")
    public String view(Model model)
    {	
		String str="权限";
		setTitle(model, new TitleVo("列表", str+"管理", true,"欢迎进入"+str+"页面", true, false));
		return PREFIX + "/list";
    }
	
	/**
	 * 权限列表
	 * @param searchTxt 搜索字符
	 */
	@PostMapping("list")
	@RequiresPermissions("system:permission:list")
	@ResponseBody
	public Object list(Tablepar tablepar,String searchTxt){
		PageInfo<TsysPremission> page=sysPremissionService.list(tablepar,searchTxt) ; 
		TableSplitResult<TsysPremission> result=new TableSplitResult<TsysPremission>(page.getPageNum(), page.getTotal(), page.getList()); 
		return  result;
	}
	/**
	 * 权限列表
	 * @param searchTxt 搜索字符
	 */
	@PostMapping("list2")
	@ResponseBody
	public Object list2(Tablepar tablepar,String searchTxt){
		List<TsysPremission> page=sysPremissionService.list2(searchTxt) ; 
		return  page;
	}
	/**
     * 新增权限
     */
    @GetMapping("/add")
	public String add() {
		return PREFIX + "/add";
    }
	
	
    /**
     * 权限添加
     */
	@PostMapping("add")
	@RequiresPermissions("system:permission:add")
	@ResponseBody
	public AjaxResult add(TsysPremission role){
		int b=sysPremissionService.insertSelective(role);
		if(b>0){
			return success();
		}else{
			return error();
		}
	}
	
	/**
	 * 删除权限
	 */
	@PostMapping("remove")
	@RequiresPermissions("system:permission:remove")
	@ResponseBody
	public AjaxResult remove(String ids){
		int b=sysPremissionService.deleteByPrimaryKey(ids);
		if(b==1){
			return success();
		}else if(b==-1){
			return error("该权限有子权限，请先删除子权限");
		}else if(b==-2){
			return error("该权限绑定了角色，请解除角色绑定");
		}else {
			return error();
		}
	}
	
	/**
	 * 检查权限
	 */
	@PostMapping("checkNameUnique")
	@ResponseBody
	public int checkNameUnique(TsysPremission TsysPremission){
		int b=sysPremissionService.checkNameUnique(TsysPremission);
		if(b>0){
			return 1;
		}else{
			return 0;
		}
	}
	
	/**
	 * 检查权限URL
	 */
	@PostMapping("checkURLUnique")
	@ResponseBody
	public int checkURLUnique(TsysPremission tsysPremission){
		int b=sysPremissionService.checkURLUnique(tsysPremission);
		if(b>0){
			return 1;
		}else{
			return 0;
		}
	}
	
	/**
	 * 检查权限perms字段
	 */
	@PostMapping("checkPermsUnique")
	@ResponseBody
	public int checkPermsUnique(TsysPremission tsysPremission){
		int b=sysPremissionService.checkPermsUnique(tsysPremission);
		if(b>0){
			return 1;
		}else{
			return 0;
		}
	}
	
	/**
	 * 修改权限
	 */
	@GetMapping("/edit/{roleId}")
    public String edit(@PathVariable("roleId") String id, ModelMap mmap)
    {	
		//获取自己的权限信息
		TsysPremission mytsysPremission=sysPremissionService.selectByPrimaryKey(id);
		//获取父权限信息
		TsysPremission pattsysPremission=sysPremissionService.selectByPrimaryKey(mytsysPremission.getPid());
		mmap.put("TsysPermission", mytsysPremission);
		mmap.put("pattsysPermission", pattsysPremission);
		return PREFIX + "/edit";
    }
	
	/**
     * 修改保存权限
	 */
	@RequiresPermissions("system:permission:edit")
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(TsysPremission TsysPremission)
    {
        return toAjax(sysPremissionService.updateByPrimaryKey(TsysPremission));
    }
    
    /**
     * 获取所有的转换成bootstarp的权限数据
     */
    @GetMapping("/getTreePerm")
    @ResponseBody
    public AjaxResult getbooBootstrapTreePerm(){
    	
    	return retobject(200,sysPremissionService.getbooBootstrapTreePerm(null));
    }
    
    
    /**
     * 根据角色id获取bootstarp 所有打勾权限
     * @param roleId 角色id集合
     */
    @PostMapping("/getCheckPrem")
    @ResponseBody
	public AjaxResult getCheckPerm(String roleId){
    	
    	return retobject(200,sysPremissionService.getCheckPrem(roleId));
    }
    
    
    /**
     * 跳转到菜单树页面
     */
    @GetMapping("tree")
	public String Tree() {
		return PREFIX + "/tree";
    }
    
    /**
     * 获取菜单树
     * @param pid 父id【没用到】
     */
    @PostMapping("tree/{pid}")
    @ResponseBody
    public AjaxResult Tree(@PathVariable("pid") String pid){
    	return retobject(200,sysPremissionService.getbooBootstrapTreePerm(null));
    }
    
    
}
