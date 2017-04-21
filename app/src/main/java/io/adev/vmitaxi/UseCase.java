package io.adev.vmitaxi;

import android.location.Criteria;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;


public abstract class UseCase<T, C> {

   private Scheduler scheduler;
   private DisposableObserver<T> observer;

    public UseCase(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void execute(DisposableObserver<T> observer, C criteria){

        this.observer = createObservable(criteria)
                .subscribeOn(scheduler)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer);
    }

    public void dispose() {
        if (!observer.isDisposed()) {
            observer.dispose();
        }
    }

    protected abstract Observable<T> createObservable(C criteria);

}
