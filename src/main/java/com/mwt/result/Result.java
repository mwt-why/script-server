package com.mwt.result;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@ToString
public class Result<T> {

    private byte flag;

    private String code;

    private String message;

    private T data;

}
