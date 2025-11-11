package com.cybersoft.shop.messaging.event;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpSuccessEvent {
    private String userId;
    private String email;
    private String userName;
    private String code;
}
