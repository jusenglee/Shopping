package com.example.weblogin.domain.DTO;

import com.example.weblogin.domain.item.Item;
import com.example.weblogin.domain.itemImg.ItemImg;
import com.example.weblogin.domain.itemOption.ItemOption;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ItemImgDTO {

    private Long id;

    private String imgName;     //이미지 파일명

    private String oriImgName;      //원본 이미지 파일명

    private String imgUrl;      //이미지 조회 경로

    private String repimgYn;        //대표 이미지 여부

    public static ItemImgDTO toDTO(ItemImg entity) {
        return ItemImgDTO.builder()
                .id(entity.getId())
                .imgName(entity.getImgName())
                .oriImgName(entity.getOriImgName())
                .imgUrl(entity.getImgUrl())
                .repimgYn(entity.getRepimgYn())
                .build();
    }

    @Builder
    public ItemImgDTO(Long id, String imgName, String oriImgName, String imgUrl, String repimgYn, Item item) {
        this.id = id;
        this.imgName = imgName;
        this.oriImgName = oriImgName;
        this.imgUrl = imgUrl;
        this.repimgYn = repimgYn;
    }

    public static List<ItemImgDTO> toDTOList(List<ItemImg> Img) {
        List<ItemImgDTO> result = new ArrayList<>();
        for(ItemImg val : Img){
            result.add(toDTO(val));
        }
        return result;
    }
}
