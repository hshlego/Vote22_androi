package org.techtown.push.buttonnaviation;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Registered {
    private Long id;
    private String regid;

    @Builder
    public Registered(Long id, String regid) {
        this.id = id;
        this.regid = regid;
    }
}
