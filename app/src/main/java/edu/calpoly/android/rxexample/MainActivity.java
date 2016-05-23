package edu.calpoly.android.rxexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import edu.calpoly.android.rxexample.model.Repo;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private RepoObserver mObserver;

    private RepoAdapter mRepoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        assert recyclerView != null;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mObserver = (RepoObserver) getLastCustomNonConfigurationInstance();

        if (mObserver == null) {
            mObserver = new RepoObserver();
            mRepoAdapter = new RepoAdapter(mObserver.getRepos());
            mObserver.bind(this);

            GitHub.get()
                    .listRepos("square")
                    .flatMap(new Func1<List<Repo>, Observable<Repo>>() {
                        @Override
                        public Observable<Repo> call(List<Repo> repos) {
                            return Observable.from(repos);
                        }
                    })
                    .zipWith(Observable.interval(1, TimeUnit.SECONDS, Schedulers.io()), new Func2<Repo, Long, Repo>() {
                        @Override
                        public Repo call(Repo repo, Long aLong) {
                            return repo;
                        }
                    })
                    .map(new Func1<Repo, Repo>() {
                        @Override
                        public Repo call(Repo repo) {
                            repo.setName(repo.getName().toUpperCase());
                            return repo;
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(mObserver);
        } else {
            mRepoAdapter = new RepoAdapter(mObserver.getRepos());
            mObserver.bind(this);
        }

        recyclerView.setAdapter(mRepoAdapter);
    }


    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        mObserver.unbind();
        return mObserver;
    }

    private static class RepoObserver implements Observer<Repo> {
        private MainActivity mActivity;

        private ArrayList<Repo> mRepos = new ArrayList<>();

        private void bind(MainActivity activity) {
            mActivity = activity;
        }

        private void unbind() {
            mActivity = null;
        }

        public ArrayList<Repo> getRepos() {
            return mRepos;
        }

        @Override
        public void onCompleted() {
            // ALL DONE
        }

        @Override
        public void onError(Throwable e) {
            Log.e("ERROR", "A terrible error has occurred", e);
        }

        @Override
        public void onNext(Repo repo) {
            int index = mRepos.size();
            mRepos.add(repo);
            mActivity.mRepoAdapter.notifyItemInserted(index);
        }
    }


}
