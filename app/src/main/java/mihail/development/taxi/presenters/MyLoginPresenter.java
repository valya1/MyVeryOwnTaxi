package mihail.development.taxi.presenters;

import mihail.development.taxi.UseCases.LoginUseCase;
import mihail.development.taxi.data.User;
import mihail.development.taxi.presenters.contracts.LoginContract;
import io.reactivex.observers.DisposableObserver;


public class MyLoginPresenter implements LoginContract.LoginPresenter {

    final LoginContract.View view;

    public MyLoginPresenter(LoginContract.View view) {
        this.view = view;
    }


// обновление интерфейса

    private DisposableObserver<User> createObserver()
    {
       return new DisposableObserver<User>() {
           @Override
           public void onNext(User user) {
               view.toMapActivity(user);
           }
           @Override
           public void onError(Throwable e) {
               view.showErrorMessage();
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
