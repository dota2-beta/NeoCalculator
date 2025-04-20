package abs.neoflex.neoflexcalculator.controllers;

import abs.neoflex.neoflexcalculator.services.VacationCalculatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
public class VacationCalculatorController {
    private final VacationCalculatorService calculatorService;

    @GetMapping("/calculate")
    public ResponseEntity<Double> calculate(
            @RequestParam("average_salary") Double averageSalary,
            @RequestParam("vacation_days") Integer vacationDays,
            @RequestParam(name = "start_date", required = false)
            @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate startDate
    ) {
        return ResponseEntity.ok(calculatorService.calculateVacationPay(averageSalary, vacationDays, startDate));
    }
}
