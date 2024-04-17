package org.example.springbatchproject.adapter;

import org.springframework.stereotype.Service;

public interface SendService {
    void send(String email, String message);
}
