package com.fc.test.service;

import com.fc.test.common.conf.V2Config;
import com.fc.test.common.file.FileUploadUtils;
import com.fc.test.mapper.auto.TsysDatasMapper;
import com.fc.test.mapper.auto.TsysFileDataMapper;
import com.fc.test.mapper.auto.TsysFileMapper;
import com.fc.test.model.auto.TsysDatas;
import com.fc.test.model.auto.TsysFile;
import com.fc.test.model.auto.TsysFileData;
import com.fc.test.shiro.util.ShiroUtils;
import com.fc.test.util.SnowflakeIdWorker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

/**
 * 百度文件上传service
 * @ClassName:  UeditorService   
 * @author: fc
 * @date:   2019年6月30日 下午5:51:43   
 *
 */
@Service
@RequiredArgsConstructor
public class UeditorService {

	private final TsysFileDataMapper tsysFileDataMapper;

	/**
	 * 文件存储mapper
	 */
	private final TsysDatasMapper tsysDatasMapper;
	/**
	 * 文件信息mapper
	 */
	private final TsysFileMapper tsysFileMapper;

    
    /**
	 * 文件上传文件存储到文件表
	 * @return TsysDatas
	 * @throws IOException 
	 */
	public TsysDatas fileDataByinsert(MultipartFile file) throws IOException {
		//文件上传获取文件名字
        String files = FileUploadUtils.upload(file);
        //补充完整url地址 
        String filesURL="";
        if ("Y".equals(V2Config.getIsstatic())) {
        	filesURL=V2Config.getIsroot_dir()+files;
		}else {
			filesURL=V2Config.getProfile()+files;
		}
        
        
		TsysDatas record=new TsysDatas();
		//添加雪花主键id
		record.setId(SnowflakeIdWorker.getUUID());
		record.setFilePath(filesURL);
		if(tsysDatasMapper.insertSelective(record)>0)
		{
			return record;
		}
		return null;
	}


	/**
	 * 添加文件信息
	 */
	@Transactional(rollbackFor = Exception.class)
	public TsysFileData fileInfoByininsert(String dataId) {
    	TsysFile record=new TsysFile();
    	record.setFileName("百度富文本上传");
		//插入创建人id
		record.setCreateUserId(ShiroUtils.getUserId());
		//插入创建人name
		record.setCreateUserName(ShiroUtils.getLoginName());
		//插入创建时间
		record.setCreateTime(new Date());
		//添加雪花主键id
		record.setId(SnowflakeIdWorker.getUUID());
		//插入关联表
		TsysFileData tsysFileData=new TsysFileData();
		tsysFileData.setId(SnowflakeIdWorker.getUUID());
		tsysFileData.setFileId(record.getId());
		tsysFileData.setDataId(dataId);
		tsysFileDataMapper.insert(tsysFileData);
		int i= tsysFileMapper.insertSelective(record);
		if(i>0) {
			return tsysFileData;
		}
		return null;
	}
}
