package com.unicam.cs.progettoweb.marketplace.service.profile;

import com.unicam.cs.progettoweb.marketplace.model.account.Profile;

public interface ProfileService {

    Profile findProfileById(Long profileId);

    Profile updateProfile(Long profileId, Profile updatedProfile);

    Profile createProfile(Profile profile);

    void deleteProfile(Long profileId);

    String getProfileAddress(Long profileId);
}
