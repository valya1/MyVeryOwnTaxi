package mihail.development.taxi.presenters;

import mihail.development.taxi.UseCases.CheckLoginUseCase;
import mihail.development.taxi.presenters.contracts.CheckLoginContract;
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
