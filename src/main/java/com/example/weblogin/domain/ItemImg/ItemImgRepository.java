package com.example.weblogin.domain.ItemImg;

import org.apache.xmlbeans.impl.xb.xmlconfig.NamespaceList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {


    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);


    ItemImg findByItemIdAndRepimgYn(Long itemId, String repimgYn);
}