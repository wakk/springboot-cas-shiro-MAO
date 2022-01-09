package com.zxm.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogDto implements Serializable {

    @NotBlank(message = "id不能为空")
    private Long id;

    @NotBlank(message = "昵称不能为空")
    private String username;

    @NotBlank(message = "创建时间不能为空")
    private String created;

    @NotBlank(message = "标题不能为空")
    private String title;

    private String description;

    private String avatar;

    @NotBlank(message = "收藏数不能为空")
    private Integer collectNum;

}
