package com.shop.dto;

import com.shop.constant.Category;
import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class ItemFormDto {

    private Long id;

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String itemNm;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Integer price;

    @NotBlank(message = "상품 상세는 필수 입력 값입니다.")
    private String itemDetail;

    @NotNull(message = "재고는 필수 입력 값입니다.")
    private Integer stockNumber;

    private Category category;

    private String classStartDate; // 문자열로 날짜를 저장할 필드

    private String classEndDate;


    private ItemSellStatus itemSellStatus;

    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();

    private List<Long> itemImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

    public Item createItem(){
        return modelMapper.map(this, Item.class);
    }

    public static ItemFormDto of(Item item){
        return modelMapper.map(item,ItemFormDto.class);
    }

    public void setClassStartDate(String classStartDate) {
        this.classStartDate = classStartDate;
    }

    // 종료 날짜를 문자열로 설정
    public void setClassEndDate(String classEndDate) {
        this.classEndDate = classEndDate;
    }

    // 필요한 경우 문자열에서 LocalDate로 변환하여 반환
    public LocalDate getClassStartDateAsLocalDate() {
        if (classStartDate != null) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            return LocalDate.parse(classStartDate, dateFormatter);
        }
        return null;
    }

    // 필요한 경우 문자열에서 LocalDate로 변환하여 반환
    public LocalDate getClassEndDateAsLocalDate() {
        if (classEndDate != null) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            return LocalDate.parse(classEndDate, dateFormatter);
        }
        return null;
    }


}