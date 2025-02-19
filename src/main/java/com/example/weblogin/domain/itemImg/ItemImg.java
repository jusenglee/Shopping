package com.example.weblogin.domain.itemImg;

import javax.persistence.*;

import com.example.weblogin.domain.DTO.ItemImgDTO;
import com.example.weblogin.domain.DTO.ItemOptionRequest;
import com.example.weblogin.domain.item.Item;
import com.example.weblogin.domain.itemOption.ColorType;
import com.example.weblogin.domain.itemOption.ItemOption;
import com.example.weblogin.domain.itemOption.MaterialType;
import com.example.weblogin.domain.itemOption.SizeType;
import com.example.weblogin.service.Utils;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Optional;

@Getter
@ToString
@NoArgsConstructor
@Table(name = "item_img")
@Entity
public class ItemImg {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "item_img_id")
	private Long id;

	private String imgName;     //이미지 파일명

	private String oriImgName;      //원본 이미지 파일명

	private String imgUrl;      //이미지 조회 경로

	private String repimgYn;        //대표 이미지 여부

	// 어떤 Item에 속하는 옵션인지
	 @ManyToOne(fetch = FetchType.LAZY)
	 @JoinColumn(name = "item_id")
	 @JsonBackReference
	 private Item item;

	public static ItemImg toEntity(ItemImgDTO request) {
		return ItemImg.builder()
				.id(request.getId())
				.imgName(request.getImgName())
				.oriImgName(request.getOriImgName())
				.imgUrl(request.getImgUrl())
				.repimgYn(request.getRepimgYn()).build();
	}

	@Builder
	public ItemImg(Long id, String imgName, String oriImgName, String imgUrl, String repimgYn, Item item) {
		this.id = id;
		this.imgName = imgName;
		this.oriImgName = oriImgName;
		this.imgUrl = imgUrl;
		this.repimgYn = repimgYn;
		this.item = item;
	}

	public void updateItemImg(String oriImgName, String imgName, String imgUrl) {
		this.oriImgName = oriImgName;
		this.imgName = imgName;
		this.imgUrl = imgUrl;
	}

	public void setItem(Item item) {
		this.item = item;
	}
}
