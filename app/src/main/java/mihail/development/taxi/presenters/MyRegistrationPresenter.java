package mihail.development.taxi.presenters;

import mihail.development.taxi.UseCases.RegistrationUseCase;
import mihail.development.taxi.data.User;
import mihail.development.taxi.presenters.contracts.RegistrationContract;
import io.reactivex.observers.DisposableObserver;


public class MyRegistrationPresenter implements RegistrationContract.RegistrationPresenter {

    RegistrationContract.View view;
    public MyRegistrationPresenter(RegistrationContract.View view)
    {
        this.view = view;
    }

    // обновление интерфейса


    private DisposableObserver<Boolean> createRegistrationObserver()
    {
        return new DisposableObserver<Boolean>() {
            @Override
            public void onNext(Boolean aBoolean) {
                view.onSuccessRegistration();
            }

            @Override
            public void onError(Throwable e) {

            }
            @Override
            public void onComplete() {

            }
        };
    }


    // работа с логикой
    RegistrationUseCase registrationUseCase = new RegistrationUseCase();
    @Override
    public void onRegistration(User user) {
        registrationUseCase.execute(createRegistrationObserver(), new RegistrationUseCase.Criteria(user));

    }

}
