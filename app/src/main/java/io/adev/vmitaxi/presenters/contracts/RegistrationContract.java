package io.adev.vmitaxi.presenters.contracts;

import io.adev.vmitaxi.data.User;
import io.reactivex.Scheduler;


public interface RegistrationContract {

    interface RegistrationPresenter
    {
        void onRegistration(User user);
    }

    interface View {

        void onSuccessRegistration();

    }

}
