package com.zxm.service;

import com.zxm.common.dto.BlogDto;
import com.zxm.common.dto.FolderDto;
import com.zxm.entity.Folder;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zxm
 * @since 2021-12-10
 */
public interface FolderService extends IService<Folder> {
    public Map<Long,FolderDto> getFolder(int id);
}
