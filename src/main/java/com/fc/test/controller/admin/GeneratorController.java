package com.fc.test.controller.admin;

import com.fc.test.common.base.BaseController;
import com.fc.test.model.custom.*;
import com.fc.test.service.GeneratorService;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 自动生成Controller
* @author fuce  
* @date 2019年5月9日 上午12:40:47 
* @version V1.0   
 */
@Controller
@RequestMapping("generatorController")
@RequiredArgsConstructor
public class GeneratorController extends BaseController{

	private static final String PREFIX = "admin/generator";
	private final GeneratorService generatorService;
	
	@GetMapping("view")
	@RequiresPermissions("system:generator:view")
    public String view(Model model)
    {	
		String str="自动生成";
		setTitle(model, new TitleVo("列表", str+"管理", true,"欢迎进入"+str+"页面", true, false));
		return PREFIX + "/list";
    }
	
	
	@PostMapping("list")
	@RequiresPermissions("system:generator:list")
	@ResponseBody
	public Object list(Tablepar tablepar,String searchTxt){
		PageInfo<TsysTables> page=generatorService.list(tablepar,searchTxt) ; 
		TableSplitResult<TsysTables> result=new TableSplitResult<TsysTables>(page.getPageNum(), page.getTotal(), page.getList()); 
		return  result;
	}
	
	
	/**
	 * 生成代码
	 */
	@GetMapping("/code")
	public void code(String tables,GenVo genVo, HttpServletResponse response) throws IOException{
		System.out.println(genVo);
		byte[] data = generatorService.generatorCode(tables.split(","),genVo);
		response.reset();  
        response.setHeader("Content-Disposition", "attachment; filename=\"SpringBootV2.zip\"");  
        response.addHeader("Content-Length", "" + data.length);  
        response.setContentType("application/octet-stream; charset=UTF-8");  
        
        IOUtils.write(data, response.getOutputStream());  
	}
}
