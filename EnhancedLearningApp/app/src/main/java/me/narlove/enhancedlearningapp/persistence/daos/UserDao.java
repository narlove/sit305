package me.narlove.enhancedlearningapp.persistence.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.TypeConverters;

import java.util.List;

import me.narlove.enhancedlearningapp.datatypes.Interest;
import me.narlove.enhancedlearningapp.datatypes.User;
import me.narlove.enhancedlearningapp.utilities.InterestConversionHandler;

@Dao
public interface UserDao {
    // probably not necessary?
    @Query("SELECT * FROM user")
    LiveData<List<User>> getAllUsers();

    @Query("SELECT * FROM user WHERE userId LIKE :uid")
    User getUserById(long uid);

    @Query("SELECT * FROM user WHERE username LIKE :username")
    User getUserByUsername(String username);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insert(User user);

    @Query("UPDATE user SET interests = :newInterests WHERE userId LIKE :userId")
    void overrideInterests(long userId,
                           String newInterests);

    @Query("SELECT EXISTS(SELECT * FROM user WHERE username LIKE :username)")
    boolean doesUsernameExist(String username);

    // not live data because i need to access the exact object and i dont intend to observe
    // for changes -> this method is likely going to be used in addition to overrideInterests.
    // user not allowed to update their interests
//    @Query("SELECT interests FROM user WHERE userId LIKE :userId")
//    List<Interest> getInterestsById(long userId);
}
