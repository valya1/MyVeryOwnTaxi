package io.adev.vmitaxi.presenters;

import io.adev.vmitaxi.UseCases.LoginUseCase;
import io.adev.vmitaxi.presenters.contracts.LoginContract;
import io.reactivex.observers.DisposableObserver;


public class MyLoginPresenter implements LoginContract.LoginPresenter {

    final LoginContract.View view;

    public MyLoginPresenter(LoginContract.View view) {
        this.view = view;
    }


// обновление интерфейса

    private DisposableObserver<Boolean> createObserver()
    {
       return new DisposableObserver<Boolean>() {
           @Override
           public void onNext(Boolean aBoolean) {
               view.toMapActivity();
           }
           @Override
           public void onError(Throwable e) {

           }
           @Override
           public void onComplete() {

           }
       };
    }

    // действия с моделью

    private LoginUseCase loginUseCase = new LoginUseCase();

    @Override
    public void onLogin(String login, String password) {
        loginUseCase.execute(createObserver(), new LoginUseCase.Criteria(login,password));
    }
}
