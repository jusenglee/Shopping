package com.example.weblogin.controller;

import com.example.weblogin.domain.dto.FileDTO;
import com.example.weblogin.service.FileService;
import com.example.weblogin.service.ItemService;
import com.example.weblogin.service.MemberService;
import com.example.weblogin.service.SellerPageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SellerPageController.class)
class SellerPageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SellerPageService sellerPageService;
    @MockBean
    private ItemService itemService;
    @MockBean
    private MemberService memberService;
    @MockBean
    private FileService fileService;

    @Test
    void getItemImages_ReturnsFileList() throws Exception {
        List<FileDTO> files = List.of(
                FileDTO.builder().id(1L).oriImgName("img1.png").build(),
                FileDTO.builder().id(2L).oriImgName("img2.png").build()
        );
        when(sellerPageService.getItemImages(1L)).thenReturn(files);

        mockMvc.perform(get("/seller/item/1/images"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].oriImgName").value("img1.png"));
    }
}
