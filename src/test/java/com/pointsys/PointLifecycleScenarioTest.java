package com.pointsys;

import org.junit.jupiter.api.*;
import com.pointsys.controller.admin.dto.AdminDto;
import com.pointsys.controller.user.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 테스트 시나리오
 *
 * 1. 1000원 적립한다 (총 잔액 0 -> 1000 원)
 *     1. pointKey : A 로 할당
 * 2. 500원 적립한다 (총 잔액 1000 -> 1500 원)
 *     1. pointKey : B 로 할당
 * 3. 주문번호 A1234 에서 1200원 사용한다 (총 잔액 1500 -> 300 원)
 *     1. pointKey : C 로 할당
 *     2. A 적립에서 1000원 사용함 -> A의 사용가능 잔액은 1000 -> 0
 *     3. B 적립에서 200원 사용함 -> B의 사용가능 잔액은 500 -> 300
 * 4. A의 적립이 만료되었다
 * 5. C의 사용금액 1200원 중 1100원을 부분 사용취소 한다 (총 잔액 300 -> 1400 원)
 *     1. pointKey : D 로 할당
 *     2. 1200원은 A와 B에서 사용되었다.
 *     3. 그래서, 사용취소 하면 A의 사용가능 잔액이 0 -> 1000원 되어야 하지만, A는 이미 만료일이 지났기 때문에 pointKey E 로 1000원이 신규적립 되어야 한다.
 *     4. B는 만료되지 않았기 때문에 사용가능 잔액은 300 -> 400원이 된다.
 *     5. C는 이제 1200원 사용금액중 100원을 부분취소 할 수 있다.
 */

@Tag(PointSysApplicationTests.INTEGRATION_TEST)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PointLifecycleScenarioTest {
    @Autowired
    private TestRestTemplate restTemplate; // 실제 API 호출을 위한 HTTP 클라이언트

    @Autowired
    private JdbcTemplate jdbcTemplate; // 날짜 이동을 위한 DB 업데이트 실행

    @BeforeAll
    void setup() {
        // 테스트 전 초기 데이터 설정
    }

    @Test
    @Order(1)
    @DisplayName("관리자는 고객에게 1000원을 적립한다.")
    void savePoints1() {
        // given
        var request = new AdminDto.SavePoint(1L, 1000, null);

        // when
        ResponseEntity<Void> response = restTemplate.postForEntity("/api/admin/points/save", request, Void.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @Order(2)
    @DisplayName("관리자는 고객에게 500원을 적립한다.")
    void savePoints2() {
        // given
        var request = new AdminDto.SavePoint(1L, 500, null);

        // when
        ResponseEntity<Void> response = restTemplate.postForEntity("/api/admin/points/save", request, Void.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @Order(3)
    @DisplayName("고객은 주문번호 A1234 에서 1200원을 사용한다.")
    void usePoints() {
        // given
        var request = new UserDto.UsePoint(1L, "A1234", 1200);

        // when
        ResponseEntity<Void> response = restTemplate.postForEntity("/api/user/points/use", request, Void.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @Order(4)
    @DisplayName("고객의 1000원 적립포인트 만료일이 지났다. (=관리자는 만료일을 임의로 변경한다.)")
    void updatePoints() {
        // given
        var request = new AdminDto.UpdatePoint(1L, LocalDateTime.now());

        // when
        ResponseEntity<Void> response = restTemplate.postForEntity("/api/admin/points/update", request, Void.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @Order(5)
    @DisplayName("고객은 주문번호 A1234 에서 1100원 사용에 대해 부분 취소한다.")
    void cancelPoints() {
        // given
        var request = new UserDto.UsePoint(1L, "A1234", 1100);

        // when
        ResponseEntity<Void> response = restTemplate.postForEntity("/api/user/points/cancel", request, Void.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
