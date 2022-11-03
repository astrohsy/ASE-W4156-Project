package io.zetch.app.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.zetch.app.domain.review.ReviewEntity;
import io.zetch.app.domain.review.ReviewGetDto;
import io.zetch.app.domain.review.ReviewPostDto;
import io.zetch.app.service.ReviewService;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/reviews")
@Tag(name = "Reviews")
@CrossOrigin(origins = "*") // NOSONAR
public class ReviewController {
  private final ReviewService reviewService;
  private final ObjectMapper mapper =
      new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

  @Autowired
  public ReviewController(ReviewService reviewService) {
    this.reviewService = reviewService;
  }

  @PostMapping(path = "/")
  @Operation(summary = "Create a new review")
  @SecurityRequirement(name = "OAuth2")
  @ResponseBody
  ReviewGetDto addNewReview(@RequestBody ReviewPostDto newReviewDto)
      throws JsonProcessingException {
    ReviewEntity r =
        reviewService.createNew(
            newReviewDto.getComment(),
            newReviewDto.getRating(),
            newReviewDto.getUserId(),
            newReviewDto.getLocationId());
    String serialized = mapper.writeValueAsString(r);
    return mapper.readValue(serialized, ReviewGetDto.class);
  }

  /**
   * @return A list of all reviews
   */
  @GetMapping(path = "/")
  @Operation(summary = "Retrieve all reviews")
  @SecurityRequirement(name = "OAuth2")
  @ResponseBody
  Iterable<ReviewGetDto> getAllReviews() throws JsonProcessingException {
    var result = new ArrayList<ReviewGetDto>();
    for (var x : reviewService.getAll().stream().toList()) {
      result.add(mapper.readValue(mapper.writeValueAsString(x), ReviewGetDto.class));
    }
    return result;
  }

  /**
   * @param reviewId Review's id
   * @return A review with id
   */
  @GetMapping("/{reviewId}")
  @Operation(summary = "Retrieve a review with reviewId")
  @SecurityRequirement(name = "OAuth2")
  ReviewGetDto getOneReview(@PathVariable Long reviewId) throws JsonProcessingException {
    ReviewEntity review = reviewService.getOne(reviewId);
    String serialized = mapper.writeValueAsString(review);
    return mapper.readValue(serialized, ReviewGetDto.class);
  }

  /**
   * @param reviewId Review's id
   */
  @DeleteMapping("/{reviewId}")
  @Operation(summary = "Delete a review with reviewId")
  @SecurityRequirement(name = "OAuth2")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void deleteOneReview(@PathVariable Long reviewId) {
    reviewService.deleteOne(reviewId);
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(NoSuchElementException.class)
  String return404(NoSuchElementException ex) {
    return ex.getMessage();
  }
}
