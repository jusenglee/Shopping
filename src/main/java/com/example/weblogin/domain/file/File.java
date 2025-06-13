package com.example.weblogin.domain.file;

import javax.persistence.*;

import com.example.weblogin.domain.dto.FileDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "item_img")
@Entity
@Builder
public class File {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String savedName;     //저장시 파일명

	private String oriImgName;      //원본 파일명

	private String fileUrl;      //조회(저장) 경로

	private String etc;        //특수코드

	private FileCategory targetType; // "ITEM", "USER" 등

	public static File toEntity(FileDTO request) {
		return File.builder()
			.savedName(request.getSavedName())
			.oriImgName(request.getOriImgName())
			.fileUrl(request.getFileUrl())
			.etc(request.getEtc())
			.targetType(FileCategory.valueOf(request.getTargetType()))
			.build();

	}
}
