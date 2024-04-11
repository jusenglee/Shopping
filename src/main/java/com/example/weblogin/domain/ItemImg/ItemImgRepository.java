package com.example.weblogin.domain.ItemImg;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {

	ItemImg findByImgName(String ImgName);

	List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);

	ItemImg findByItemIdAndRepimgYn(Long itemId, String repimgYn);
}
