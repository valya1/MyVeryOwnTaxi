package mihail.development.taxi.presenters.contracts;

import mihail.development.taxi.data.User;


public interface RegistrationContract {

    interface RegistrationPresenter
    {
        void onRegistration(User user);
    }

    interface View {

        void onSuccessRegistration();

    }

}
