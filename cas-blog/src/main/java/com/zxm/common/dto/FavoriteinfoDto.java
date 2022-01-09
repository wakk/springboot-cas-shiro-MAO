package com.zxm.common.dto;

import com.zxm.entity.Favorite;
import com.zxm.entity.Favoriteinfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class FavoriteinfoDto implements Serializable {
    private Long id;

    private String title;

    private boolean collect;

    public FavoriteinfoDto(Long id,String title){
        this.id=id;
        this.title=title;
        this.collect=true;
    }
}
