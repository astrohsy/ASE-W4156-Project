package io.zetch.app.seed;

import io.zetch.app.domain.user.Affiliation;
import io.zetch.app.domain.user.UserEntity;
import java.util.List;

/** Sample users. */
public class SeedUsers {

  private SeedUsers() {
    throw new IllegalStateException("Utility class");
  }

  public static final UserEntity USER_1 =
      UserEntity.builder()
          .username("admin")
          .affiliation(Affiliation.ADMIN)
          .displayName("Admin.")
          .email(null)
          .build();
  public static final UserEntity USER_2 =
      UserEntity.builder()
          .username("bob")
          .affiliation(Affiliation.OTHER)
          .displayName("Bob R.")
          .email("bob@me.com")
          .build();
  public static final UserEntity USER_3 =
      UserEntity.builder()
          .username("cat")
          .affiliation(Affiliation.OTHER)
          .displayName("Kat F.")
          .email("kat@kat.com")
          .build();
  public static final UserEntity USER_4 =
      UserEntity.builder()
          .username("amy")
          .affiliation(Affiliation.OTHER)
          .displayName("Amy T.")
          .email("amy@dog.com")
          .build();
  public static final UserEntity USER_5 =
      UserEntity.builder()
          .username("sam")
          .affiliation(Affiliation.OTHER)
          .displayName("Sam 2.")
          .email("sam@from.com")
          .build();

  public static final List<UserEntity> USERS = List.of(USER_1, USER_2, USER_3, USER_4, USER_5);
}
