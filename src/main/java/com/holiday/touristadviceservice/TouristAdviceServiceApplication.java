package com.holiday.touristadviceservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;


@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class TouristAdviceServiceApplication {

    public static void main(String[] args) throws TelegramApiRequestException {
        ApiContextInitializer.init();
        SpringApplication.run(TouristAdviceServiceApplication.class, args);

        Map<Long, BigDecimal> accountsMap = new HashMap<>();
        accountsMap.put(1L, new BigDecimal("123"));
        accountsMap.put(2L, new BigDecimal("11111111111111"));
        accountsMap.put(3L, new BigDecimal("333333333333333333333333"));

        BigDecimal amount = new BigDecimal("120");
        Long from = 1L;
        Long to = 2L;
        BigDecimal accountFrom = accountsMap.get(from);

        accountsMap.put(from, BigDecimal.valueOf(2L));//кто-то положил данные в мапу на наш айди после чтения
        System.out.println("кто-то положил данные в мапу на наш айди после чтения " + accountsMap.get(from));
        accountsMap.remove(from);
        System.out.println("кто-то удалил аккаунт" + accountsMap.get(from));

        BigDecimal accountTo = accountsMap.get(to);

        BigDecimal resultForFrom = accountFrom.subtract(amount);
        int compareResult = resultForFrom.compareTo(BigDecimal.valueOf(0L));
        if (compareResult == -1) {
            System.out.println("Недостаточно средств");
        } else {
            accountsMap.put(from, resultForFrom);
            BigDecimal resultForTo = accountTo.add(amount);
            accountsMap.put(to, resultForTo);
        }
        System.out.println("from " + accountsMap.get(from));
        System.out.println("to " + accountsMap.get(to));


        int[] array = new int[]{9, 4, 2, 5, 3};
        System.out.println(result(array));

    }

	public static int resultNonClassic(int[] array){
		int min1 = array[0];
		int min2 = array[1];

		if (min2 < min1){
			min1 = array[1];
			min2 = array[0];
		}

		for (int i=0; i<array.length; i++){
// если оба числа попадают в промежуток между мин1 и мин2 то они меньше
			if (array[i] < min1){
				min2 = min1;
				min1 = array[i];
			}else if (array[i] < min2){
				min2 = array[i];
			}
		}
		return min1 * min2;
	}

    public static int result(int[] array) {
    	int min1 = array[0];
    	int min2 = array[1];

    	if (min2 < min1){
    		min1 = array[1];
    		min2 = array[0];
		}

        for (int i=2; i<array.length; i++){
			if (array[i] < min1){
				min2 = min1;
				min1 = array[i];
			}else if (array[i] < min2){
				min2 = array[i];
			}
		}
        return min1 * min2;
    }
}
