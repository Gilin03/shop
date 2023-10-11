package com.shop.constant;

public enum Category {
    CRAFT("공예"),
    ART("미술"),
    MUSIC("악기"),
    DANCE("댄스"),
    PHOTO_VIDEO("사진 / 영상"),
    COOKING("요리"),
    BAKING("베이킹"),
    SPORTS("스포츠"),
    PROGRAMMING("프로그래밍"),
    DESIGN("디자인"),
    CERTIFICATION("시험 / 자격증");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
