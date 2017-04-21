package io.adev.vmitaxi.presenters;

import io.adev.vmitaxi.RegisterActivity;
import io.adev.vmitaxi.UseCases.CheckLoginUseCase;
import io.adev.vmitaxi.presenters.contracts.CheckLoginContract;
import io.reactivex.observers.DisposableObserver;


public class MyCheckLoginPresenter implements CheckLoginContract.CheckLoginPresenter {

    final CheckLoginContract.View view;

    public MyCheckLoginPresenter(CheckLoginContract.View view) {
        this.view = view;
    }

    private DisposableObserver<Boolean> createRegisteredUserObserver()
    {
        return new DisposableObserver<Boolean>() {
            @Override
            public void onNext(Boolean aBoolean) {
                if(aBoolean)
                    view.onAccessibleLogin();
                else
                    view.onInAccessibleLogin();
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onComplete() {
            }
        };
    }

    CheckLoginUseCase checkLoginUseCase = new CheckLoginUseCase("");

    @Override
    public void onCheckLogin(String login) {
        checkLoginUseCase.execute(createRegisteredUserObserver(), login);
    }
}
