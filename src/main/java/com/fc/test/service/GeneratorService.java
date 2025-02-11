package com.fc.test.service;

import com.fc.test.mapper.auto.GeneratorMapper;
import com.fc.test.model.custom.GenVo;
import com.fc.test.model.custom.Tablepar;
import com.fc.test.model.custom.TsysTables;
import com.fc.test.util.GenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;


/**
 * 自动生成代码Service
* @Title: GeneratorService.java 
* @Package com.fc.test.service 
* @author fuce  
* @date 2019年5月9日 上午12:24:47 
* @version V1.0   
 */
@Service
@RequiredArgsConstructor
public class GeneratorService {
	private final GeneratorMapper generatorMapper;
	/**
	 * 分页查询
	 * @return
	 */
	 public PageInfo<TsysTables> list(Tablepar tablepar,String searchTxt){
		 	PageHelper.startPage(tablepar.getPageNum(), tablepar.getPageSize());
		 	List<TsysTables> list=  generatorMapper.queryList(searchTxt);
		    PageInfo<TsysTables> pageInfo = new PageInfo<TsysTables>(list);  
		    
		    return pageInfo;
	 }
	 
	 
	 
	 /**
	  * 代码
	  * @return
	  */
	public byte[] generatorCode(String[] tableNames,GenVo genVo){
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ZipOutputStream zip = new ZipOutputStream(outputStream);

		for(String tableName : tableNames){
			//查询表信息
			TsysTables table = generatorMapper.queryTable(tableName);
			//查询列信息
			List<Map<String, String>> columns =generatorMapper.queryColumns(tableName);
			//生成代码
			GenUtils.generatorCode(table, columns, zip,genVo);
		}
		IOUtils.closeQuietly(zip);
		return outputStream.toByteArray();
	 }
	
	
	
}
