package com.yelog.request;

import com.yelog.exception.InvalidRequest;
import lombok.*;

import jakarta.validation.constraints.NotBlank;


@ToString
@Getter
@Setter
@NoArgsConstructor
public class PostCreate {

    @NotBlank(message = "타이틀을 입력해주세요.")
    private String title;

    @NotBlank(message = "콘텐츠를 입력해주세을")
    private String content;

    //빌더의 장점
    // - 가독성이 좋다.(값 생성에 대한 유연함)
    // - 필요한 값만 받을 수 있다.
    // - 객체의 불변성
    @Builder
    public PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void validate() {
        if( title.contains("바보")) {
            throw new InvalidRequest("title", "제목에 바보를 포함할 수 없습니다.");
        }
    }
}
