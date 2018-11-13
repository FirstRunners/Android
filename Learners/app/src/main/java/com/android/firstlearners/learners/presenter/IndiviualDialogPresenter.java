package com.android.firstlearners.learners.presenter;

import com.android.firstlearners.learners.contract.IndiviualDialogContract;
import com.android.firstlearners.learners.model.Repository;
import com.android.firstlearners.learners.model.data.Study;

import io.realm.Realm;

public class IndiviualDialogPresenter implements IndiviualDialogContract.Action{
    private Realm realm;
    private IndiviualDialogContract.View view;

    public IndiviualDialogPresenter(Repository repository, IndiviualDialogContract.View view) {
        this.realm = repository.getRealm();
        this.view = view;
    }

    @Override
    public void getData() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Study study = realm.where(Study.class).findFirst();
                if(study != null){
                    view.setData(study);
                }
            }
        });
    }
}
