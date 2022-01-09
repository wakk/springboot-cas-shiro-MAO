package com.zxm.service;

import com.zxm.common.dto.BlogDto;
import com.zxm.entity.Blog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zxm
 * @since 2021-12-10
 */
public interface BlogService extends IService<Blog> {
    public List<BlogDto> getBlogs();
    public void saveUrlCache(Blog blog);
}
