package ru.practicum.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.service.RequestService;


@RestController
@RequestMapping("/users/{userId}")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;



}
