package com.zxm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zxm.common.dto.BlogDto;
import com.zxm.common.dto.FolderDto;
import com.zxm.entity.Blog;
import com.zxm.entity.Folder;
import com.zxm.entity.User;
import com.zxm.mapper.FolderMapper;
import com.zxm.service.FavoriteinfoService;
import com.zxm.service.FolderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zxm
 * @since 2021-12-10
 */
@Service
public class FolderServiceImpl extends ServiceImpl<FolderMapper, Folder> implements FolderService {
    @Autowired
    FolderMapper folderMapper;

    @Override
    public Map<Long,FolderDto> getFolder(int id) {
        List<Folder> folderList = folderMapper.selectList(new QueryWrapper<Folder>().eq("user_id", id));
        Map<Long, FolderDto> map=new HashMap<>();
        for (Folder folder : folderList) map.put(folder.getId(),new FolderDto(folder.getId(),folder.getName(),new ArrayList<>()));
        if(folderList.size()==0)return null;
        return map;
    }
}
