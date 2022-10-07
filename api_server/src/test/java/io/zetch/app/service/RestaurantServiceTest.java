package io.zetch.app.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.zetch.app.domain.restaurant.RestaurantDto;
import io.zetch.app.domain.restaurant.RestaurantEntity;
import io.zetch.app.repo.RestaurantRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@ExtendWith(MockitoExtension.class)
public class RestaurantServiceTest {

  private static final Long ID = 1L;
  private static final String NAME = "Bob's";
  private static final String CUISINE = "Italian";
  private static final String ADDRESS = "1234 Broadway";

  @Mock private RestaurantRepository restaurantRepositoryMock;
  @InjectMocks private RestaurantService restaurantService;
  @Mock private RestaurantEntity restaurantMock;

  // VERIFY SERVICE RETURN VALUE

  @Test
  public void getOne() {
    when(restaurantRepositoryMock.findById(ID)).thenReturn(Optional.of(restaurantMock));
    assertThat(restaurantService.getOne(ID), is(restaurantMock));
  }

  @Test
  public void getAll() {
    when(restaurantRepositoryMock.findAll())
        .thenReturn(List.of(restaurantMock, restaurantMock, restaurantMock));
    assertThat(restaurantService.getAll().size(), is(3));
    assertThat(restaurantService.getAll().get(0), is(restaurantMock));
  }

  // VERIFY INVOCATION OF DEPS + PARAMETERS

  @Test
  public void createNew() {
    // Prepare to capture a Restaurant object
    ArgumentCaptor<RestaurantEntity> restaurantCaptor =
        ArgumentCaptor.forClass(RestaurantEntity.class);

    restaurantService.createNew(NAME, CUISINE, ADDRESS);

    // Verify save() invoked
    verify(restaurantRepositoryMock).save(restaurantCaptor.capture());

    // Verify the attributes of the Restaurant object
    RestaurantEntity value = restaurantCaptor.getValue();
    assertThat(value.getName(), is(NAME));
    assertThat(value.getCuisine(), is(CUISINE));
    assertThat(value.getAddress(), is(ADDRESS));
    assertThat(value.getOwners().isEmpty(), is(true));
  }

  @Test
  public void updateRestaurantName() throws Exception {
    RestaurantEntity old =
            RestaurantEntity.builder()
                    .owners(new ArrayList<>())
                    .name(NAME)
                    .cuisine(CUISINE)
                    .address(ADDRESS)
                    .build();

    RestaurantEntity updated =
            RestaurantEntity.builder()
                    .owners(new ArrayList<>())
                    .name("New Bob's")
                    .cuisine(CUISINE)
                    .address(ADDRESS)
                    .build();

    when(restaurantRepositoryMock.findById(ID)).thenReturn(Optional.of(old));
    restaurantService.update(ID, updated.getName(), updated.getCuisine(), updated.getAddress());

    ArgumentCaptor<RestaurantEntity> restaurantCaptor =
            ArgumentCaptor.forClass(RestaurantEntity.class);
    verify(restaurantRepositoryMock).save(restaurantCaptor.capture());

    RestaurantEntity value = restaurantCaptor.getValue();
    assertThat(value.getName(), is(updated.getName()));
    assertThat(value.getCuisine(), is(updated.getCuisine()));
    assertThat(value.getAddress(), is(updated.getAddress()));
    assertThat(value.getOwners().isEmpty(), is(true));
  }
}
