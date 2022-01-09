package com.zxm.mapper;

import com.zxm.entity.Blog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zxm
 * @since 2021-12-10
 */
@Mapper
public interface BlogMapper extends BaseMapper<Blog> {

}
