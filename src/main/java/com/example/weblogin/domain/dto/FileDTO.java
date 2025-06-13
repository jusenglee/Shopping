package com.example.weblogin.domain.dto;

import com.example.weblogin.domain.dto.response.ItemOptionResponseDTO;
import com.example.weblogin.domain.file.File;
import com.example.weblogin.domain.itemOption.ItemOption;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileDTO {

    private Long id;

    private String savedName;     //저장시 파일명

    private String oriImgName;      //원본 파일명

    private String fileUrl;      //조회(저장) 경로

    private String etc;        //특수코드

    private Long targetId;   // 원본 엔티티의 ID

    private String targetType; // "ITEM", "USER" 등

    public static FileDTO toDTO(File entity) {
        return FileDTO.builder()
            .id(entity.getId())
            .savedName(entity.getSavedName())
            .oriImgName(entity.getOriImgName())
            .fileUrl(entity.getFileUrl())
            .etc(entity.getEtc())
            .targetType(entity.getTargetType().name())
            .build();
    }
}
