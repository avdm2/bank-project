package ru.tinkoff.hse.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.hse.dto.requests.UpdateFeeRequest;
import ru.tinkoff.hse.services.FeeService;

@RestController
public class AdminController {

    private final FeeService feeService;

    public AdminController(FeeService feeService) {
        this.feeService = feeService;
    }

    @PostMapping("/admin/configs")
    public ResponseEntity<?> updateFee(@RequestBody UpdateFeeRequest request) {
        feeService.updateFee(request);
        return ResponseEntity.ok().build();
    }
}
