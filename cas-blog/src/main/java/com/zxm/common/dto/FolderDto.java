package com.zxm.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;


//专栏列表数据传输对象
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FolderDto  implements Serializable{
    @NotBlank(message = "id不能为空")
    private Long id;

    @NotBlank(message = "标签不能为空")
    private String label;

    private List<ChildDto> children;
}