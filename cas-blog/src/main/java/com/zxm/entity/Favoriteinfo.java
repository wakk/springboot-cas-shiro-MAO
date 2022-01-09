package com.zxm.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author zxm
 * @since 2021-12-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("blog_favoriteinfo")
public class Favoriteinfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long favoriteId;

    private Long blogId;

    private String blogName;

    private Long userId;
}
