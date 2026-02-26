package com.nhiennhatt.bookstoreapi.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder(builderMethodName = "builder")
public class CreateUserResponse {
    private UUID id;
}
