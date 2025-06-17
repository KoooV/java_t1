package ru.t1.java.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientStatusRequest {
    private UUID clientId;
    private UUID accountId;
} 