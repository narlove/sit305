package me.narlove.enhancedlearningapp.persistence.datatypes;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import me.narlove.enhancedlearningapp.Interest;
import me.narlove.enhancedlearningapp.utilities.InterestConversionHandler;

@Entity(tableName = "user",
indices = {@Index(value = {"username"}, unique = true)})
@TypeConverters(InterestConversionHandler.class)
public class User {
    @PrimaryKey(autoGenerate = true)
    private long userId;
    // no duplicates -> used for lookup in authentication flow
    private String username;
    private String name;
    private String password; // hashing beyond the scope of this proof of concept project.
    private String email;
    private String phoneNumber;
    private List<Interest> interests;

    // interests cannot be null, can be empty
    public User(String username, String name, String password, String email, String phoneNumber,
                @NotNull List<Interest> interests) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.interests = interests;
    }

    // second constructor for converting a usermodel to user in apidatatypemapper
    // this one will be ignored by room so that if we decide we want to swap dbs back over
    // from mongo to room, nothing should break and no additional changes should be required
    // bar what is in the DatabaseViewModel.
    public User(long id, String username, String name, String password, String email, String phoneNumber,
                @NotNull List<Interest> interests) {
        this.userId = id;
        this.username = username;
        this.name = name;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.interests = interests;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Interest> getInterests() {
        return interests;
    }

    public void setInterests(List<Interest> interests) {
        this.interests = interests;
    }
}
