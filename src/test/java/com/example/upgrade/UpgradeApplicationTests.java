package com.example.upgrade;

import com.example.upgrade.controller.ReservationController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest
class UpgradeApplicationTests {

	@Autowired
	private ReservationController reservationController;

	@Test
	void contextLoads() {
		assertThat(reservationController, notNullValue());
	}

}
