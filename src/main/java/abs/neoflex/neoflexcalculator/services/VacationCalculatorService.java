package abs.neoflex.neoflexcalculator.services;

import abs.neoflex.neoflexcalculator.exceptions.VacationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class VacationCalculatorService {
    private final float AVERAGE_DAYS_IN_MONTH = 29.3f;
    private final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM-dd");

    @Value("${app.holidays.fixed}")
    private List<String> holidaysList;

    public double calculateVacationPay(double averageSalary, int vacationDays, LocalDate startDate) {
        validateInputs(averageSalary, vacationDays);

        return startDate == null ?
                averageSalary / AVERAGE_DAYS_IN_MONTH * vacationDays
                : averageSalary / AVERAGE_DAYS_IN_MONTH * calculateVacationDays(vacationDays, startDate);
    }

    private int calculateVacationDays(int vacationDays, LocalDate startDate) {
        LocalDate endDate = startDate.plusDays(vacationDays);

        int countWeekend = (int) startDate.datesUntil(endDate)
                    .filter ( day -> day.getDayOfWeek() == DayOfWeek.SATURDAY
                            || day.getDayOfWeek() == DayOfWeek.SUNDAY )
                    .count();

        int countHolidays = (int) startDate.datesUntil(endDate)
                    .filter(day -> holidaysList.contains(day.format(DATE_FORMATTER)))
                    .count();

        return vacationDays - countWeekend - countHolidays;
    }

    private void validateInputs(Double averageSalary, Integer vacationDays) {
        if(averageSalary < 0)
            throw new VacationException("Average salary cannot be negative");
        if(vacationDays < 0)
            throw new VacationException("Vacation days cannot be negative");
    }
}
