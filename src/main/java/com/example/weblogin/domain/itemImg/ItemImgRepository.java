package com.example.weblogin.domain.itemImg;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.weblogin.domain.item.Item;

@Repository
public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {

	ItemImg findByImgName(String ImgName);

	List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);

	ItemImg findByItemIdAndRepimgYn(Long itemId, String repimgYn);

	 // 메서드 수준에서 EntityGraph로 inventories를 Fetch
	 @EntityGraph(attributePaths = {"inventories"})
	 @Query("SELECT i FROM Item i WHERE i.id = :id")
	 Optional<Item> findWithInventoriesById(@Param("id") Long id);
}
