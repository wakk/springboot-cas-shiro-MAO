package com.zxm.common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class ChildDto implements Serializable {
    @NotBlank(message = "id不能为空")
    private Long id;

    @NotBlank(message = "标签不能为空")
    private String label;

    @NotBlank(message = "节点不能为空")
    private boolean leaf;

    public ChildDto(Long id,String label){
        this.id=id;
        this.label=label;
        this.leaf=true;
    }
}
