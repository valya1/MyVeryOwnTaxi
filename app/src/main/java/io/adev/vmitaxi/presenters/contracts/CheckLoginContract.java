package io.adev.vmitaxi.presenters.contracts;


public interface CheckLoginContract {

    interface CheckLoginPresenter
    {
        void onCheckLogin(String login);
    }

    interface View
    {
        void onInAccessibleLogin();
        void onAccessibleLogin();
    }

}
