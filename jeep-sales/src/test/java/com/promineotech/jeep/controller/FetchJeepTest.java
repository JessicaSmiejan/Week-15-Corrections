package com.promineotech.jeep.controller;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import com.promineotech.jeep.controller.support.FetchJeepTestSupport;
import com.promineotech.jeep.entity.Jeep;
import com.promineotech.jeep.entity.JeepModel;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql(scripts= {"classpath:flyway/migrations/V1.0_Jeep_Schema.sql",
                "classpath:flyway/migrations/V1.1_Jeep_Data.sql" },
          config =  @SqlConfig(encoding = "utf-8"))
class FetchJeepTest extends FetchJeepTestSupport{
  

  @Test
  void testThatJeepsAreReturnedWhenAVaildModleAndTrimAreSupplied() {
    // Given: a valid model, trim and  URI
    JeepModel model = JeepModel.WRANGLER;
     String trim =  "Sport";
      String uri = String.format("%s?model=%s&trim=%s", getBaseUri(), model, trim); 
    
      System.out.println(uri);
        //When : a connection is made to the URI
          ResponseEntity<List <Jeep>>  response = 
              getRestTemplate().exchange(uri, HttpMethod.GET, null, 
                  new ParameterizedTypeReference<>() {});
          
    // Then:  a success ( OK - 200) status code is  returned
          assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
     // And the  actual List is the  same as  expected list
          List<Jeep> actual = response.getBody();
          List<Jeep> expected = buildExpected();
          
          actual.forEach(jeep-> jeep.setModelPK(null));
          
           assertThat (actual).isEqualTo(expected);
          
          }
 
}
