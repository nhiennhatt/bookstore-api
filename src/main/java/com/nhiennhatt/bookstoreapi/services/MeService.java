package com.nhiennhatt.bookstoreapi.services;

import com.nhiennhatt.bookstoreapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MeService {
    @Autowired
    private UserRepository userRepository;


}
