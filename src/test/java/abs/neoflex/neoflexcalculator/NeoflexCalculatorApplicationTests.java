package abs.neoflex.neoflexcalculator;

import abs.neoflex.neoflexcalculator.exceptions.VacationException;
import abs.neoflex.neoflexcalculator.services.VacationCalculatorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.stream.Stream;

@SpringBootTest
class NeoflexCalculatorApplicationTests {
    @Autowired
    private VacationCalculatorService vacationService;
    private static final double DELTA = 0.01;

    @Test
    @DisplayName("Исключение при отрицательной средней зарплате")
    void calculateVacationPay_shouldThrowException_whenAverageSalaryIsNegative() {
        Assertions.assertThrows(VacationException.class, () ->
                vacationService.calculateVacationPay(-1000, 10, LocalDate.now()));
    }

    @Test
    @DisplayName("Исключение при отрицательном количестве дней отпуска")
    void calculateVacationPay_shouldThrowException_whenVacationDaysCountIsNegative() {
        Assertions.assertThrows(VacationException.class, () ->
                vacationService.calculateVacationPay(1000, -10, LocalDate.now()));
    }

    @ParameterizedTest()
    @MethodSource("calculateVacationPay_testCases")
    void calculateVacationPay_shouldCalculateCorrectly(double averageSalary, int vacationDays,
                                                       LocalDate startDate, double expectedResult) throws VacationException {
        double result = vacationService.calculateVacationPay(averageSalary, vacationDays, startDate);
        Assertions.assertEquals(expectedResult, result, DELTA);
    }

    private static Stream<Arguments> calculateVacationPay_testCases() {
        return Stream.of(
                // Случаи без даты
                Arguments.of(1000.0, 10, null, 341.30), // 1000 / 29.3 * 10
                Arguments.of(60000.0, 14, null, 28668.94), // 60000 / 29.3 * 14

                // Случаи с датами (учет выходных и праздников)
                // С 7 мая 2025 по 16 мая 2025
                Arguments.of(1000.0, 10, LocalDate.of(2025, 5, 7), 238.90),
                // С 12 мая 2025 по 31 мая 2025
                Arguments.of(2000.0, 20, LocalDate.of(2025, 5, 12), 1023.89)
        );
    }

}
