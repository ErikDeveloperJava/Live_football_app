package net.livefootball.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {

    private UserDto user;

    private String comment;

    private String sendDate;
}