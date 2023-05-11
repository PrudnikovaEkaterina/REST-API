package ru.prudnikova.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CallbackPhonesBD {
    private String phone;
    private String userId;
    private String link;
}
