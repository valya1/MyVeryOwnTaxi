package io.adev.vmitaxi.presenters.contracts;

/**
 * Created by mihail on 11.04.2017.
 */

public interface LoginContract {

    interface LoginPresenter {
        void onLogin(String login, String password);
    }

    interface View{
        void toMapActivity();
    }
}